package dev.dubhe.torchikoma.resource;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.file.AnimationFileLoader;
import software.bernie.geckolib3.file.GeoModelLoader;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.molang.MolangRegistrar;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TorchikomaCache {
	private static TorchikomaCache INSTANCE;

	private final AnimationFileLoader animationLoader;
	private final GeoModelLoader modelLoader;

	public final MolangParser parser = new MolangParser();

	private Map<ResourceLocation, GeoModel> geoModels = Collections.emptyMap();

	protected TorchikomaCache() {
		this.animationLoader = new AnimationFileLoader();
		this.modelLoader = new GeoModelLoader();
		MolangRegistrar.registerVars(parser);
	}

	public static TorchikomaCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TorchikomaCache();
			return INSTANCE;
		}
		return INSTANCE;
	}

	public @Nonnull CompletableFuture<Void> reload(PreparationBarrier stage, ResourceManager resourceManager,
												   ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor,
												   Executor gameExecutor) {
		Map<ResourceLocation, AnimationFile> animations = new HashMap<>();
		Map<ResourceLocation, GeoModel> geoModels = new HashMap<>();
		return CompletableFuture.allOf(loadResources(backgroundExecutor, resourceManager, "animations",
				animation -> animationLoader.loadAllAnimations(parser, animation, resourceManager), animations::put),
				loadResources(backgroundExecutor, resourceManager, "geo",
						resource -> modelLoader.loadModel(resourceManager, resource), geoModels::put))
				.thenCompose(stage::wait).thenAcceptAsync(empty -> this.geoModels = geoModels, gameExecutor);
	}

	private static <T> CompletableFuture<Void> loadResources(Executor executor, ResourceManager resourceManager,
			String type, Function<ResourceLocation, T> loader, BiConsumer<ResourceLocation, T> map) {
		return CompletableFuture.supplyAsync(
				() -> resourceManager.listResources(type, fileName -> fileName.endsWith(".json")), executor)
				.thenApplyAsync(resources -> {
					Map<ResourceLocation, CompletableFuture<T>> tasks = new HashMap<>();

					for (ResourceLocation resource : resources) {
						CompletableFuture<T> existing = tasks.put(resource,
								CompletableFuture.supplyAsync(() -> loader.apply(resource), executor));

						if (existing != null) {// Possibly if this matters, the last one will win
							System.err.println("Duplicate resource for " + resource);
							existing.cancel(false);
						}
					}

					return tasks;
				}, executor).thenAcceptAsync(tasks -> {
					for (Entry<ResourceLocation, CompletableFuture<T>> entry : tasks.entrySet()) {
						// Shouldn't be any duplicates as they are caught above
						map.accept(entry.getKey(), entry.getValue().join());
					}
				}, executor);
	}
}

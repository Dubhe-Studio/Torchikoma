package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MyBlocks {
    private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Torchikoma.ID);

    public static final Block PRISMARINE_TORCH = register("prismarine_torch", new Block(defaultProperties(Material.STONE)));
    public static final Block GLOWSTONE_TORCH = register("glowstone_torch", new Block(defaultProperties(Material.STONE)));

    private static BlockBehaviour.Properties defaultProperties() {
        return defaultProperties(Material.METAL);
    }

    private static BlockBehaviour.Properties defaultProperties(Material material) {
        return BlockBehaviour.Properties.of(material).sound(SoundType.METAL).strength(5.0F).requiresCorrectToolForDrops();
    }

    private static <T extends Block> T register(String name, T block) {
        REGISTRY.register(name, () -> block);
        return block;
    }

    public static void register() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

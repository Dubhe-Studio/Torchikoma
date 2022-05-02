package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MyItems {
    private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Torchikoma.ID);
    private static final CreativeModeTab TAB = new CreativeModeTab(Torchikoma.ID + ".tab") {
        @NotNull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TORCH_LAUNCHER.get());
        }
    };



    public static final RegistryObject<Item> PRISMARINE_TORCH = REGISTRY.register("prismarine_torch", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GLOWSTONE_TORCH = REGISTRY.register("glowstone_torch", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> TORCH_LAUNCHER = REGISTRY.register("torch_launcher", () -> new Item(defaultProperties()));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(TAB);
    }

    public static void register() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}

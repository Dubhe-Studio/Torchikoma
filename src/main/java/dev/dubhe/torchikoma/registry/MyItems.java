package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MyItems {
    private static final CreativeModeTab TAB = new CreativeModeTab(Torchikoma.ID + ".tab") {
        @NotNull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TORCH_LAUNCHER);
        }
    };




    public static final Item PRISMARINE_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Item GLOWSTONE_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("glowstone_torch"));
    public static final Item TORCH_LAUNCHER = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("torch_launcher"));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(TAB);
    }
}

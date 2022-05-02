package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MyItems { //TODO 不稳定, 必须在方块注册事件之后初始化, 否则NPE异常
    private static final CreativeModeTab TAB = new CreativeModeTab(Torchikoma.ID + ".tab") {
        @NotNull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TORCH_LAUNCHER);
        }
    };

    public static final Item PRISMARINE_TORCH = new BlockItem(MyBlocks.PRISMARINE_TORCH, defaultProperties()).setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Item GLOWSTONE_TORCH = new BlockItem(MyBlocks.GLOWSTONE_TORCH, defaultProperties()).setRegistryName(Torchikoma.getId("glowstone_torch"));
    public static final Item TORCH_LAUNCHER = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("torch_launcher"));
    public static final Item CLUSTERED_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("clustered_torch"));
    public static final Item CLUSTERED_REDSTONE_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("clustered_redstone_torch"));
    public static final Item CLUSTERED_SOUL_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("clustered_soul_torch"));
    public static final Item CLUSTERED_GLOWSTONE_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("clustered_glowstone_torch"));
    public static final Item CLUSTERED_PRISMARINE_TORCH = new Item(defaultProperties()).setRegistryName(Torchikoma.getId("clustered_prismarine_torch"));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(TAB);
    }
}

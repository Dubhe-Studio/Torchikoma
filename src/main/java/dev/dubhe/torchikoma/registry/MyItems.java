package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.item.TorchLauncher;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import org.jetbrains.annotations.NotNull;

public class MyItems { //TODO 不稳定, 必须在方块注册事件之后初始化, 否则NPE异常
    private static final CreativeModeTab TAB = new CreativeModeTab(Torchikoma.ID + ".tab") {
        @NotNull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TORCH_LAUNCHER);
        }
    };

    public static final Item PRISMARINE_TORCH = new StandingAndWallBlockItem(MyBlocks.PRISMARINE_TORCH, MyBlocks.WALL_PRISMARINE_TORCH, defaultProperties())
            .setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Item GLOWSTONE_TORCH = new StandingAndWallBlockItem(MyBlocks.GLOWSTONE_TORCH, MyBlocks.WALL_GLOWSTONE_TORCH, defaultProperties())
            .setRegistryName(Torchikoma.getId("glowstone_torch"));
    public static final Item TORCH_LAUNCHER = new TorchLauncher(defaultProperties())
            .setRegistryName(Torchikoma.getId("torch_launcher"));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(TAB);
    }
}

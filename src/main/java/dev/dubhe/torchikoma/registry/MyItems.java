package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.item.TorchCannon;
import dev.dubhe.torchikoma.item.TorchGatling;
import dev.dubhe.torchikoma.item.TorchLauncher;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;

import javax.annotation.Nonnull;

public class MyItems { //TODO 不稳定, 必须在方块注册事件之后初始化, 否则NPE异常
    private static final CreativeModeTab TAB = new CreativeModeTab(Torchikoma.ID + ".tab") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TORCH_LAUNCHER);
        }
    };

    public static final Item PRISMARINE_TORCH = new StandingAndWallBlockItem(MyBlocks.PRISMARINE_TORCH, MyBlocks.PRISMARINE_WALL_TORCH, defaultProperties())
            .setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Item GLOWSTONE_TORCH = new StandingAndWallBlockItem(MyBlocks.GLOWSTONE_TORCH, MyBlocks.GLOWSTONE_WALL_TORCH, defaultProperties())
            .setRegistryName(Torchikoma.getId("glowstone_torch"));
    public static final Item TORCH_LAUNCHER = new TorchLauncher(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON))
            .setRegistryName(Torchikoma.getId("torch_launcher"));
    public static final Item TORCH_GATLING = new TorchGatling(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON))
            .setRegistryName(Torchikoma.getId("torch_gatling"));
    public static final Item TORCH_CANNON = new TorchCannon(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON))
            .setRegistryName(Torchikoma.getId("torch_cannon"));
    public static final Item CLUSTERED_TORCH = new Item(defaultProperties())
            .setRegistryName(Torchikoma.getId("clustered_torch"));
    public static final Item CLUSTERED_SOUL_TORCH = new Item(defaultProperties())
            .setRegistryName(Torchikoma.getId("clustered_soul_torch"));
    public static final Item CLUSTERED_REDSTONE_TORCH = new Item(defaultProperties())
            .setRegistryName(Torchikoma.getId("clustered_redstone_torch"));
    public static final Item CLUSTERED_PRISMARINE_TORCH = new Item(defaultProperties())
            .setRegistryName(Torchikoma.getId("clustered_prismarine_torch"));
    public static final Item CLUSTERED_GLOWSTONE_TORCH = new Item(defaultProperties())
            .setRegistryName(Torchikoma.getId("clustered_glowstone_torch"));
    public static final Item TORCHIKOMA = new ItemNameBlockItem(MyBlocks.TORCHIKOMA, defaultProperties())
            .setRegistryName(Torchikoma.getId("torchikoma"));
    public static final Item MECHANICAL_COMPONENT = new ItemNameBlockItem(MyBlocks.MECHANICAL_COMPONENT, defaultProperties())
            .setRegistryName(Torchikoma.getId("mechanical_component"));
    public static final Item ELECTRONIC_COMPONENT = new ItemNameBlockItem(MyBlocks.ELECTRONIC_COMPONENT, defaultProperties())
            .setRegistryName(Torchikoma.getId("electronic_component"));
    public static final Item CLAY_EXPLOSIVES = new ItemNameBlockItem(MyBlocks.CLAY_EXPLOSIVES, defaultProperties())
            .setRegistryName(Torchikoma.getId("clay_explosives"));
    public static final Item BLOCKLIGHT_DETECTOR = new ItemNameBlockItem(MyBlocks.BLOCKLIGHT_DETECTOR, defaultProperties())
            .setRegistryName(Torchikoma.getId("blocklight_detector"));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(TAB);
    }
}

package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.item.TorchCannonItem;
import dev.dubhe.torchikoma.item.TorchGatlingItem;
import dev.dubhe.torchikoma.item.TorchLauncherItem;
import dev.dubhe.torchikoma.item.TorchikomaItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class MyItems { //TODO 不稳定, 必须在方块注册事件之后初始化, 否则NPE异常
    private static final CreativeModeTab TAB = new CreativeModeTab(Torchikoma.ID + ".tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(TORCH_LAUNCHER);
        }
    };
    public static final Item PRISMARINE_TORCH = create("prismarine_torch", new StandingAndWallBlockItem(MyBlocks.PRISMARINE_TORCH, MyBlocks.PRISMARINE_WALL_TORCH, defaultProperties()));
    public static final Item GLOWSTONE_TORCH = create("glowstone_torch", new StandingAndWallBlockItem(MyBlocks.GLOWSTONE_TORCH, MyBlocks.GLOWSTONE_WALL_TORCH, defaultProperties()));
    public static final Item CLUSTERED_TORCH = create("clustered_torch", new Item(defaultProperties()));
    public static final Item CLUSTERED_SOUL_TORCH = create("clustered_soul_torch", new Item(defaultProperties()));
    public static final Item CLUSTERED_REDSTONE_TORCH = create("clustered_redstone_torch", new Item(defaultProperties()));
    public static final Item CLUSTERED_PRISMARINE_TORCH = create("clustered_prismarine_torch", new Item(defaultProperties()));
    public static final Item CLUSTERED_GLOWSTONE_TORCH = create("clustered_glowstone_torch", new Item(defaultProperties()));
    public static final Item TORCH_LAUNCHER = create("torch_launcher", new TorchLauncherItem(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item TORCH_GATLING = create("torch_gatling", new TorchGatlingItem(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item TORCH_CANNON = create("torch_cannon", new TorchCannonItem(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item REDSTONE_ENERGY_CORE = create("redstone_energy_core", new EnergyCoreItem(MyBlocks.REDSTONE_ENERGY_CORE, 1, defaultProperties().stacksTo(1)));
    public static final Item BEACON_ENERGY_CORE = create("beacon_energy_core", new EnergyCoreItem(MyBlocks.BEACON_ENERGY_CORE,5, defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CREATIVE_ENERGY_CORE = create("creative_energy_core", new EnergyCoreItem(MyBlocks.CREATIVE_ENERGY_CORE, 100, defaultProperties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item TORCHIKOMA = create("torchikoma", new TorchikomaItem(MyBlocks.TORCHIKOMA, defaultProperties()));
    public static final Item MECHANICAL_COMPONENT = create("mechanical_component", new ItemNameBlockItem(MyBlocks.MECHANICAL_COMPONENT, defaultProperties()));
    public static final Item ELECTRONIC_COMPONENT = create("electronic_component", new ItemNameBlockItem(MyBlocks.ELECTRONIC_COMPONENT, defaultProperties()));
    public static final Item CLAY_EXPLOSIVES = create("clay_explosives", new ItemNameBlockItem(MyBlocks.CLAY_EXPLOSIVES, defaultProperties()));
    public static final Item BLOCKLIGHT_DETECTOR = create("blocklight_detector", new ItemNameBlockItem(MyBlocks.BLOCKLIGHT_DETECTOR, defaultProperties()));

    public static <T extends ForgeRegistryEntry<T>> T create(String id, T entry) {
        return entry.setRegistryName(Torchikoma.getId(id));
    }
    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(TAB);
    }
}

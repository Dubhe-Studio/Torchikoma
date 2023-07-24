package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.item.ClusteredTorchItem;
import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.item.TorchCannonItem;
import dev.dubhe.torchikoma.item.TorchGatlingItem;
import dev.dubhe.torchikoma.item.TorchLauncherItem;
import dev.dubhe.torchikoma.item.TorchikomaItem;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;

import java.util.HashMap;
import java.util.Map;

public class MyItems { //TODO 不稳定, 必须在方块注册事件之后初始化, 否则NPE异常
    public static final Map<String, Item> ITEM_MAP = new HashMap<>();
    public static final CreativeModeTab TAB = CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + Torchikoma.ID + ".tab"))
        .icon(() -> new ItemStack(MyItems.TORCH_LAUNCHER))
        .displayItems((parameters, output) -> {
            output.accept(MyItems.PRISMARINE_TORCH);
            output.accept(MyItems.GLOWSTONE_TORCH);
            output.accept(MyItems.CLUSTERED_TORCH);
            output.accept(MyItems.CLUSTERED_SOUL_TORCH);
            output.accept(MyItems.CLUSTERED_REDSTONE_TORCH);
            output.accept(MyItems.CLUSTERED_PRISMARINE_TORCH);
            output.accept(MyItems.CLUSTERED_GLOWSTONE_TORCH);
            output.accept(MyItems.TORCH_LAUNCHER);
            output.accept(MyItems.TORCH_GATLING);
            output.accept(MyItems.TORCH_CANNON);
            output.accept(MyItems.REDSTONE_ENERGY_CORE);
            output.accept(MyItems.BEACON_ENERGY_CORE);
            output.accept(MyItems.CREATIVE_ENERGY_CORE);
            output.accept(MyItems.TORCHIKOMA);
            output.accept(MyItems.MECHANICAL_COMPONENT);
            output.accept(MyItems.ELECTRONIC_COMPONENT);
            output.accept(MyItems.CLAY_EXPLOSIVES);
            output.accept(MyItems.BLOCKLIGHT_DETECTOR);
        })
        .build();

    public static final Item PRISMARINE_TORCH = create("prismarine_torch", new StandingAndWallBlockItem(MyBlocks.PRISMARINE_TORCH, MyBlocks.PRISMARINE_WALL_TORCH, defaultProperties(), Direction.DOWN));
    public static final Item GLOWSTONE_TORCH = create("glowstone_torch", new StandingAndWallBlockItem(MyBlocks.GLOWSTONE_TORCH, MyBlocks.GLOWSTONE_WALL_TORCH, defaultProperties(), Direction.DOWN));
    public static final Item CLUSTERED_TORCH = create("clustered_torch", new ClusteredTorchItem(defaultProperties(), Items.TORCH));
    public static final Item CLUSTERED_SOUL_TORCH = create("clustered_soul_torch", new ClusteredTorchItem(defaultProperties(), Items.SOUL_TORCH));
    public static final Item CLUSTERED_REDSTONE_TORCH = create("clustered_redstone_torch", new ClusteredTorchItem(defaultProperties(), Items.REDSTONE_TORCH));
    public static final Item CLUSTERED_PRISMARINE_TORCH = create("clustered_prismarine_torch", new ClusteredTorchItem(defaultProperties(), PRISMARINE_TORCH));
    public static final Item CLUSTERED_GLOWSTONE_TORCH = create("clustered_glowstone_torch", new ClusteredTorchItem(defaultProperties(), GLOWSTONE_TORCH));
    public static final Item TORCH_LAUNCHER = create("torch_launcher", new TorchLauncherItem(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item TORCH_GATLING = create("torch_gatling", new TorchGatlingItem(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item TORCH_CANNON = create("torch_cannon", new TorchCannonItem(defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item REDSTONE_ENERGY_CORE = create("redstone_energy_core", new EnergyCoreItem(MyBlocks.REDSTONE_ENERGY_CORE, 1, defaultProperties().stacksTo(1)));
    public static final Item BEACON_ENERGY_CORE = create("beacon_energy_core", new EnergyCoreItem(MyBlocks.BEACON_ENERGY_CORE,5, defaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CREATIVE_ENERGY_CORE = create("creative_energy_core", new EnergyCoreItem(MyBlocks.CREATIVE_ENERGY_CORE, 100, defaultProperties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item TORCHIKOMA = create("torchikoma", new TorchikomaItem(MyBlocks.TORCHIKOMA, defaultProperties()));
    public static final Item MECHANICAL_COMPONENT = create("mechanical_component", new BlockItem(MyBlocks.MECHANICAL_COMPONENT, defaultProperties()));
    public static final Item ELECTRONIC_COMPONENT = create("electronic_component", new BlockItem(MyBlocks.ELECTRONIC_COMPONENT, defaultProperties()));
    public static final Item CLAY_EXPLOSIVES = create("clay_explosives", new BlockItem(MyBlocks.CLAY_EXPLOSIVES, defaultProperties()));
    public static final Item BLOCKLIGHT_DETECTOR = create("blocklight_detector", new BlockItem(MyBlocks.BLOCKLIGHT_DETECTOR, defaultProperties()));

    public static <T extends Item> T create(String id, T entry) {
        MyItems.ITEM_MAP.put(id, entry);
        return entry;
    }
    private static Item.Properties defaultProperties() {
        return new Item.Properties();
    }
}

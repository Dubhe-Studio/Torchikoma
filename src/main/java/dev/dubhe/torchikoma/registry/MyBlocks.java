package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;
import java.util.Map;

public class MyBlocks {
    public static final Map<String, Block> BLOCK_MAP = new HashMap<>();

    public static final Block PRISMARINE_TORCH = create("prismarine_torch", new ColdFireTorchBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).noCollission().lightLevel(state -> 15)));
    public static final Block PRISMARINE_WALL_TORCH = create("prismarine_wall_torch", new WallColdFireTorchBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).noCollission().lightLevel(state -> 15).lootFrom(() -> PRISMARINE_TORCH)));
    public static final Block GLOWSTONE_TORCH = create("glowstone_torch", new ColdFireTorchBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).noCollission().lightLevel(state -> 15)));
    public static final Block GLOWSTONE_WALL_TORCH = create("glowstone_wall_torch", new WallColdFireTorchBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).noCollission().lightLevel(state -> 15).lootFrom(() -> GLOWSTONE_TORCH)));
    public static final Block TORCHIKOMA = create("torchikoma", new TorchikomaBlock(defaultProperties().noOcclusion()));
    public static final Block MECHANICAL_COMPONENT = create("mechanical_component", new ComponentBlock(defaultProperties().noOcclusion(), 1));
    public static final Block ELECTRONIC_COMPONENT = create("electronic_component", new ComponentBlock(defaultProperties().noOcclusion().lightLevel((p_50763_) -> p_50763_.getValue(BlockStateProperties.LIT) ? 2 : 0), 0));
    public static final Block CLAY_EXPLOSIVES = create("clay_explosives", new ClayExplosivesBlock(defaultProperties()));
    public static final Block BLOCKLIGHT_DETECTOR = create("blocklight_detector", new BlcoklightDetectorBlock(defaultProperties().noOcclusion()));

    public static final Block REDSTONE_ENERGY_CORE = create("redstone_energy_core", new EnergyCoreBlock(defaultProperties().noOcclusion().lightLevel(state -> 15)));

    public static final Block BEACON_ENERGY_CORE = create("beacon_energy_core", new EnergyCoreBlock(defaultProperties().noOcclusion().lightLevel(state -> 15)));

    public static final Block CREATIVE_ENERGY_CORE = create("creative_energy_core", new EnergyCoreBlock(defaultProperties().noOcclusion().lightLevel(state -> 15)));

    public static <T extends Block> T create(String name, T item) {
        BLOCK_MAP.put(name, item);
        return item;
    }

    private static BlockBehaviour.Properties defaultProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL);
    }
}

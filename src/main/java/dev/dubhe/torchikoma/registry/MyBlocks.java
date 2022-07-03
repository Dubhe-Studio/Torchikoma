package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.block.*;
import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class MyBlocks {
    public static final Block PRISMARINE_TORCH = create("prismarine_torch", new ColdFireTorchBlock(properties(Material.DECORATION, SoundType.WOOD).noCollission().lightLevel(state -> 15)));
    public static final Block PRISMARINE_WALL_TORCH = create("prismarine_wall_torch", new WallColdFireTorchBlock(properties(Material.DECORATION, SoundType.WOOD).noCollission().lightLevel(state -> 15).lootFrom(() -> PRISMARINE_TORCH)));
    public static final Block GLOWSTONE_TORCH = create("glowstone_torch", new ColdFireTorchBlock(properties(Material.DECORATION, SoundType.WOOD).noCollission().lightLevel(state -> 15)));
    public static final Block GLOWSTONE_WALL_TORCH = create("glowstone_wall_torch", new WallColdFireTorchBlock(properties(Material.DECORATION, SoundType.WOOD).noCollission().lightLevel(state -> 15).lootFrom(() -> GLOWSTONE_TORCH)));
    public static final Block TORCHIKOMA = create("torchikoma", new TorchikomaBlock(defaultProperties().noOcclusion()));
    public static final Block MECHANICAL_COMPONENT = create("mechanical_component", new ComponentBlock(defaultProperties().noOcclusion(), 1));
    public static final Block ELECTRONIC_COMPONENT = create("electronic_component", new ComponentBlock(defaultProperties().noOcclusion().lightLevel((p_50763_) -> p_50763_.getValue(BlockStateProperties.LIT) ? 2 : 0), 0));
    public static final Block CLAY_EXPLOSIVES = create("clay_explosives", new ClayExplosivesBlock(defaultProperties()));
    public static final Block BLOCKLIGHT_DETECTOR = create("blocklight_detector", new BlcoklightDetectorBlock(defaultProperties().noOcclusion()));

    public static <T extends ForgeRegistryEntry<T>> T create(String id, T entry) {
        return entry.setRegistryName(Torchikoma.getId(id));
    }

    private static BlockBehaviour.Properties defaultProperties() {
        return properties(Material.METAL, SoundType.METAL);
    }

    private static BlockBehaviour.Properties properties(Material material, SoundType sound) {
        return BlockBehaviour.Properties.of(material).sound(sound);
    }
}

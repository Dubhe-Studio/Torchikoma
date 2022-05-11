package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.block.*;
import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class MyBlocks {
    public static final Block PRISMARINE_TORCH = new ColdFireTorchBlock(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Block PRISMARINE_WALL_TORCH = new WallColdFireTorchBlock(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lootFrom(() -> PRISMARINE_TORCH).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("prismarine_wall_torch"));
    public static final Block GLOWSTONE_TORCH = new ColdFireTorchBlock(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("glowstone_torch"));
    public static final Block GLOWSTONE_WALL_TORCH = new WallColdFireTorchBlock(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lootFrom(() -> GLOWSTONE_TORCH).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("glowstone_wall_torch"));

    public static final Block TORCHIKOMA = new TorchikomaBlock(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL).noOcclusion())
            .setRegistryName(Torchikoma.getId("torchikoma"));
    public static final Block MECHANICAL_COMPONENT = new ComponentBlock(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL).noOcclusion())
            .setRegistryName(Torchikoma.getId("mechanical_component"));
    public static final Block ELECTRONIC_COMPONENT = new ComponentBlock(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL).noOcclusion())
            .setRegistryName(Torchikoma.getId("electronic_component"));
    public static final Block CLAY_EXPLOSIVES = new ClayExplosivesBlock(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL))
            .setRegistryName(Torchikoma.getId("clay_explosives"));

    public static final Block BLOCKLIGHT_DETECTOR = new BlcoklightDetectorBlock(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL).noOcclusion())
            .setRegistryName(Torchikoma.getId("blocklight_detector"));

    private static BlockBehaviour.Properties defaultProperties(Material material) {
        return BlockBehaviour.Properties.of(material).sound(SoundType.METAL).strength(5.0F);
    }
}

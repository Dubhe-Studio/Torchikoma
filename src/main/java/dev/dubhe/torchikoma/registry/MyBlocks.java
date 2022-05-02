package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.block.ColdFireTorch;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.block.WallColdFireTorch;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class MyBlocks {
    public static final Block PRISMARINE_TORCH = new ColdFireTorch(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Block PRISMARINE_WALL_TORCH = new WallColdFireTorch(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lootFrom(() -> PRISMARINE_TORCH).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("prismarine_wall_torch"));
    public static final Block GLOWSTONE_TORCH = new ColdFireTorch(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("glowstone_torch"));
    public static final Block GLOWSTONE_WALL_TORCH = new WallColdFireTorch(defaultProperties(Material.DECORATION)
            .noCollission().instabreak().sound(SoundType.WOOD).lootFrom(() -> GLOWSTONE_TORCH).lightLevel(state -> 15))
            .setRegistryName(Torchikoma.getId("glowstone_wall_torch"));

    private static BlockBehaviour.Properties defaultProperties(Material material) {
        return BlockBehaviour.Properties.of(material).sound(SoundType.METAL).strength(5.0F).requiresCorrectToolForDrops();
    }
}

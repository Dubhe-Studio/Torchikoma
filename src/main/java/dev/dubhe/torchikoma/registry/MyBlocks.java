package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.block.ColdFireTorchBlock;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.block.TorchikomaBlock;
import dev.dubhe.torchikoma.block.WallColdFireTorchBlock;
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
            .instabreak().sound(SoundType.METAL))
            .setRegistryName(Torchikoma.getId("torchikoma"));
    public static final Block MECHANICAL_COMPONENT = new Block(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL))
            .setRegistryName(Torchikoma.getId("mechanical_component"));
    public static final Block ELECTRONIC_COMPONENT = new Block(defaultProperties(Material.METAL)
            .instabreak().sound(SoundType.METAL))
            .setRegistryName(Torchikoma.getId("electronic_component"));

    private static BlockBehaviour.Properties defaultProperties(Material material) {
        return BlockBehaviour.Properties.of(material).sound(SoundType.METAL).strength(5.0F).requiresCorrectToolForDrops();
    }
}

package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class MyBlocks {

    public static final Block PRISMARINE_TORCH = new Block(defaultProperties(Material.STONE)).setRegistryName(Torchikoma.getId("prismarine_torch"));
    public static final Block GLOWSTONE_TORCH = new Block(defaultProperties()).setRegistryName(Torchikoma.getId("glowstone_torch"));

    private static BlockBehaviour.Properties defaultProperties() {
        return defaultProperties(Material.METAL);
    }

    private static BlockBehaviour.Properties defaultProperties(Material material) {
        return BlockBehaviour.Properties.of(material).sound(SoundType.METAL).strength(5.0F).requiresCorrectToolForDrops();
    }
}

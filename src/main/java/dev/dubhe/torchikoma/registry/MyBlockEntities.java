package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.block.entity.BlocklightDetectorBlockEntity;
import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MyBlockEntities {
    public static final BlockEntityType<BlocklightDetectorBlockEntity> BLOCKLIGHT_DETECTOR = register("blocklight_detector", BlocklightDetectorBlockEntity::new, MyBlocks.BLOCKLIGHT_DETECTOR);
    public static final BlockEntityType<TorchikomaBlockEntity> TORCHIKOMA = register("torchikoma", TorchikomaBlockEntity::new, MyBlocks.TORCHIKOMA);

    @SuppressWarnings("ConstantConditions")
    private static <T extends BlockEntity> BlockEntityType<T> register(String pKey, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks) {
        BlockEntityType<T> blockEntity = BlockEntityType.Builder.of(pFactory, pValidBlocks).build(null);
        blockEntity.setRegistryName(Torchikoma.getId(pKey));
        return blockEntity;
    }
}

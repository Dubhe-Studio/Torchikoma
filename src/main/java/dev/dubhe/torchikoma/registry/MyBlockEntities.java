package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.block.entity.BlocklightDetectorBlockEntity;
import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.Map;

public class MyBlockEntities {
    public static final Map<String, BlockEntityType<?>> BLOCK_ENTITY_MAP = new HashMap<>();

    public static final BlockEntityType<BlocklightDetectorBlockEntity> BLOCKLIGHT_DETECTOR = create("blocklight_detector", BlocklightDetectorBlockEntity::new, MyBlocks.BLOCKLIGHT_DETECTOR);
    public static final BlockEntityType<TorchikomaBlockEntity> TORCHIKOMA = create("torchikoma", TorchikomaBlockEntity::new, MyBlocks.TORCHIKOMA);

    @SuppressWarnings("ConstantConditions")
    private static <T extends BlockEntity> BlockEntityType<T> create(String pKey, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks) {
        BlockEntityType<T> blockEntity = BlockEntityType.Builder.of(pFactory, pValidBlocks).build(null);
        BLOCK_ENTITY_MAP.put(pKey, blockEntity);
        return blockEntity;
    }
}

package dev.dubhe.torchikoma.block.entity;

import dev.dubhe.torchikoma.registry.MyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlocklightDetectorBlockEntity extends BlockEntity {

    public BlocklightDetectorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(MyBlockEntities.BLOCKLIGHT_DETECTOR, pWorldPosition, pBlockState);
    }

    public void tick() {

    }
}

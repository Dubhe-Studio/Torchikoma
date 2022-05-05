package dev.dubhe.torchikoma.registry;

import com.mojang.datafixers.types.Type;
import dev.dubhe.torchikoma.block.entity.BlocklightDetectorBlockEntity;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MyBlockEntities {
    public static final BlockEntityType<BlocklightDetectorBlockEntity> BLOCKLIGHT_DETECTOR = register("blocklight_detector",
            BlockEntityType.Builder.of(BlocklightDetectorBlockEntity::new, MyBlocks.BLOCKLIGHT_DETECTOR));

    private static <T extends BlockEntity> BlockEntityType<T> register(String pKey, BlockEntityType.Builder<T> pBuilder) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, pKey);
        BlockEntityType<T> blockEntityType = pBuilder.build(type);
        blockEntityType.setRegistryName(pKey);
        return blockEntityType;
    }
}

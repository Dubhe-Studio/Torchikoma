package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TorchikomaItem extends ItemNameBlockItem {
    public TorchikomaItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        boolean result = super.updateCustomBlockEntityTag(pPos, pLevel, pPlayer, pStack, pState);
        if (pLevel.getBlockEntity(pPos) instanceof TorchikomaBlockEntity blockEntity) {
            blockEntity.setOwner(pPlayer == null ? null : pPlayer.getUUID());
            return true;
        } else return result;
    }
}

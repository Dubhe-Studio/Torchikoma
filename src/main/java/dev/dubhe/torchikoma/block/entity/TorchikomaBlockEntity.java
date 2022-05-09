package dev.dubhe.torchikoma.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class TorchikomaBlockEntity extends RandomizableContainerBlockEntity{
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    public TorchikomaBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntityType.BARREL, pWorldPosition, pBlockState);
    }

    @Nonnull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> pItemStacks) {
        this.items = pItemStacks;
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return new TextComponent("Torchikoma");
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, @Nonnull Inventory pInventory) {
        return ChestMenu.threeRows(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}

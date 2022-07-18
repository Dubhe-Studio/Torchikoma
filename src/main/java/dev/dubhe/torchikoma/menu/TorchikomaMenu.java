package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.item.TorchLauncherItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


public abstract class TorchikomaMenu<T> extends AbstractContainerMenu {

    protected final T supplier;

    protected TorchikomaMenu(MenuType<?> menuType, int pContainerId, Inventory inventory, @Nonnull T entity) {
        super(menuType, pContainerId);
        this.supplier = entity;
        this.getInventory().startOpen(inventory.player);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.addSlot(new Slot(this.getInventory(), j + i * 4, 99 + j * 18, 16 + i * 18));
            }
        }

        this.addSlot(new Slot(this.getInventory(), 12, 9, 16) {
            @Override
            public boolean mayPlace(ItemStack pStack) { // 三种枪
                return pStack.getItem() instanceof TorchLauncherItem;
            }
        });
        this.addSlot(new Slot(this.getInventory(), 13, 9, 34) {
            @Override
            public boolean mayPlace(ItemStack pStack) { // 能量
                return pStack.getItem() instanceof EnergyCoreItem;
            }
        });

        this.addSlot(new Slot(this.getInventory(), 14, 9, 52));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 9 + j * 18, 132 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 9 + i * 18, 190));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 15) {
                if (!this.moveItemStackTo(itemstack1, 15, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.slots.get(12).mayPlace(itemstack) && !this.slots.get(12).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 12, 13, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.slots.get(13).mayPlace(itemstack) && !this.slots.get(13).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 13, 14, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 12, false)) {
                if (pIndex < 42) {
                    if (!this.moveItemStackTo(itemstack1, 42, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < this.slots.size() && !this.moveItemStackTo(itemstack1, 15, 42, false)) {
                    return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            } else if (pIndex < 42) {
                if (!this.moveItemStackTo(itemstack1, 42, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex < this.slots.size() && !this.moveItemStackTo(itemstack1, 15, 42, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return itemstack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.getInventory().stopOpen(pPlayer);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmpty(int pIndex) {
        return this.getInventory().getItem(pIndex).isEmpty();
    }

    protected abstract Container getInventory();

    @OnlyIn(Dist.CLIENT)
    public abstract TorchikomaEntity getEntity();

    @OnlyIn(Dist.CLIENT)
    public abstract byte getStatus();

    @OnlyIn(Dist.CLIENT)
    public abstract float getHealth();

    @OnlyIn(Dist.CLIENT)
    public abstract int getEnergy();

    @OnlyIn(Dist.CLIENT)
    public abstract boolean canStandby();

    @OnlyIn(Dist.CLIENT)
    public abstract void setStatus(byte status);
}

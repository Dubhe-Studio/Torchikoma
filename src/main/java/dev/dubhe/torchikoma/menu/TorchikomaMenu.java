package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.item.EnergyCore;
import dev.dubhe.torchikoma.item.TorchLauncher;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


public class TorchikomaMenu extends AbstractContainerMenu {

    public static int t1;

    private final TorchikomaEntity entity;

    public TorchikomaMenu(int pContainerId, Inventory inventory, @Nonnull TorchikomaEntity entity) {
        super(MyMenuTypes.TORCHIKOMA, pContainerId);
        this.entity = entity;
        this.entity.getInventory().startOpen(inventory.player);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.addSlot(new Slot(this.entity.getInventory(), j + i * 2, 99 + j * 18, 16 + i * 18));
            }
        }

        this.addSlot(new Slot(this.entity.getInventory(), 12, 9, 16) {
            @Override
            public boolean mayPlace(ItemStack pStack) { // 三种枪
                return pStack.getItem() instanceof TorchLauncher;
            }
        });
        this.addSlot(new Slot(this.entity.getInventory(), 13, 9, 34) {
            @Override
            public boolean mayPlace(ItemStack pStack) { // 能量
                return pStack.getItem() instanceof EnergyCore;
            }
        });
        this.addSlot(new Slot(this.entity.getInventory(), 14, 9, 52) {
            @Override
            public boolean mayPlace(ItemStack pStack) { // 染料
                return pStack.getItem() instanceof DyeItem;
            }
        });

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
                if (!this.moveItemStackTo(itemstack1, 15, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack.getItem() instanceof TorchLauncher) {
                    if (!this.moveItemStackTo(itemstack1, 12, 13, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack.getItem() instanceof EnergyCore) {
                    if (!this.moveItemStackTo(itemstack1, 13, 14, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack.getItem() instanceof DyeItem) {
                    if (!this.moveItemStackTo(itemstack1, 14, 15, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < 42) {
                    if (!this.moveItemStackTo(itemstack1, 42, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < this.slots.size() && !this.moveItemStackTo(itemstack1, 15, 42, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(pPlayer, itemstack1);
        }
        return itemstack;
    }

    public TorchikomaEntity getEntity() {
        return this.entity;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.entity.getInventory().stopOpen(pPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmpty(int pIndex) {
        return this.entity.getInventory().getItem(pIndex).isEmpty();
    }
}

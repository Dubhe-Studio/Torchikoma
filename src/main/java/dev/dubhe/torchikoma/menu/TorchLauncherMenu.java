package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.item.TorchLauncher;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TorchLauncherMenu extends AbstractContainerMenu {
    private final ItemInventory itemInventory;

    private final float itemSize;

    public static TorchLauncherMenu createLauncher(int pContainerId, Inventory inventory, ItemStack itemStack) {
        return new TorchLauncherMenu(MyMenuTypes.TORCH_LAUNCHER, pContainerId, inventory, itemStack, 48.0F);
    }

    public static TorchLauncherMenu createGatling(int pContainerId, Inventory inventory, ItemStack itemStack) {
        return new TorchLauncherMenu(MyMenuTypes.TORCH_GATLING, pContainerId, inventory, itemStack, 36.0F);
    }

    private TorchLauncherMenu(MenuType<? extends TorchLauncherMenu> pMenuType, int pContainerId, Inventory inventory, ItemStack itemStack, float itemSize) {
        super(pMenuType, pContainerId);
        this.itemInventory = new ItemInventory(itemStack);
        this.itemInventory.startOpen(inventory.player);
        this.itemSize = itemSize;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new Slot(this.itemInventory, j + i * 2, 134 + j * 18, 16 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return TorchLauncher.isTorchItem(pStack);
                    }
                });
            }
        }
        this.addSlot(new Slot(this.itemInventory, 4, 51, 25) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.getItem() == Items.GUNPOWDER;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getItemSize() {
        return itemSize;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmpty(int pIndex) {
        return this.itemInventory.getItem(pIndex).isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public int getShoots() {
        return Math.min(this.itemInventory.shoots, 100);
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItemStack() {
        return this.itemInventory.itemStack;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.itemInventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.itemInventory.getContainerSize(), this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack.getItem() == Items.GUNPOWDER) {
                    if (!this.moveItemStackTo(itemstack1, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TorchLauncher.isTorchItem(itemstack)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 5 && pIndex < 32) {
                    if (!this.moveItemStackTo(itemstack1, 32, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= 32 && pIndex < this.slots.size() && !this.moveItemStackTo(itemstack1, 5, 32, false)) {
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

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.itemInventory.stopOpen(pPlayer);
    }

    static class ItemInventory extends SimpleContainer {
        private final ItemStack itemStack;
        private int shoots = 0;

        private ItemInventory(ItemStack itemStack) {
            super(5);
            this.itemStack = itemStack;
            this.loadData();
            this.addListener(container -> {
                if (container instanceof ItemInventory inv) {
                    inv.saveData();
                }
            });
        }

        private void loadData() {
            if (itemStack.isEmpty()) return;
            CompoundTag nbt = itemStack.getOrCreateTag();
            if (!itemStack.hasTag() || !nbt.contains("Torches") || !nbt.contains("Shoots")) {
                nbt.put("Torches", new ListTag());
                nbt.putInt("Shoots", 0);
                this.shoots = 0;
            } else {
                ListTag torches = nbt.getList("Torches", 10);
                CompoundTag subNbt;
                int j;
                for (int i = 0; i < torches.size(); i++) {
                    subNbt = torches.getCompound(i);
                    j = subNbt.getByte("Slot") & 255;
                    if (j < this.items.size()) this.items.set(j, ItemStack.of(subNbt));
                }
                if (nbt.contains("Gunpowder")) this.items.set(4, ItemStack.of(nbt.getCompound("Gunpowder")));
                this.shoots = nbt.getInt("Shoots");
            }
        }

        private void saveData() {
            if (this.itemStack.isEmpty()) return;
            ListTag torches = new ListTag();
            CompoundTag nbt;
            ItemStack item;
            for (int i = 0; i < this.items.size() - 1; i++) {
                item = this.items.get(i);
                if (!item.isEmpty()) {
                    nbt = item.save(new CompoundTag());
                    nbt.putByte("Slot", (byte) i);
                    torches.add(nbt);
                }
            }
            nbt = this.itemStack.getOrCreateTag();
            nbt.put("Torches", torches);
            nbt.putInt("Shoots", this.shoots);
            item = this.items.get(4);
            if (!item.isEmpty()) {
                nbt.put("Gunpowder", item.save(new CompoundTag()));
            } else if (nbt.contains("Gunpowder")) {
                nbt.remove("Gunpowder");
            }
        }

        @Override
        public void setItem(int pIndex, ItemStack pStack) {
            super.setItem(pIndex, pStack);
            if (pIndex == 4) {
                ItemStack item = this.items.get(pIndex);
                if (this.shoots <= 84 && !item.isEmpty()) {
                    int count = Math.min((100 - this.shoots) / 16, item.getCount());
                    item.shrink(count);
                    this.shoots += count * 16;
                }
            }
        }
    }

}

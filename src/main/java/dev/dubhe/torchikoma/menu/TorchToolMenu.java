package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.item.TorchLauncher;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TorchToolMenu extends AbstractItemMenu<TorchToolMenu.ItemInventory> {

    public TorchToolMenu(int pContainerId, Inventory inventory, ItemStack itemStack) {
        super(MyMenuTypes.TORCH_TOOL_MENU, pContainerId, inventory, itemStack);
    }

    @Override
    protected ItemInventory genItemInventory(ItemStack itemStack) {
        return new ItemInventory(itemStack);
    }

    @Override
    protected void initSlot() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new Slot(this.itemInventory, j + i * 2, 135 + j * 18, 16 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return TorchLauncher.isTorchItem(pStack);
                    }
                });
            }
        }
        this.addSlot(new Slot(this.itemInventory, 4, 52, 25) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.getItem() == Items.GUNPOWDER;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public TorchLauncherScreen.RenderSize getItemSize() {
        return itemInventory.itemStack.getItem() instanceof TorchLauncher item ? item.getRenderSize() : TorchLauncherScreen.RenderSize.DEFAULT;
    }

    @OnlyIn(Dist.CLIENT)
    public int getShoots() {
        return Math.min(this.itemInventory.shoots, 100);
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

    static class ItemInventory extends AbstractMenuInventory {
        private int shoots;

        private ItemInventory(ItemStack itemStack) {
            super(5, itemStack);
        }

        @Override
        protected void loadData() {
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

        @Override
        protected void saveData() {
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

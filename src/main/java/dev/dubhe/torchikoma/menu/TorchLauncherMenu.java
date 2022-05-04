package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.block.ColdFireTorchBlock;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TorchLauncherMenu extends AbstractContainerMenu implements Container {
    public static int temp1 = 0;
    public static int temp2 = 0;
    public static int temp3 = 0;
    public static int temp4 = 0;
    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private int gunpowder = 0;
    private final ItemStack itemStack;

    public TorchLauncherMenu(int pContainerId, Inventory inventory, ItemStack itemStack) {
        super(MyMenuTypes.TORCH_LAUNCHER, pContainerId);
        this.itemStack = itemStack;
        this.startOpen(inventory.player);
        this.loadData();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new Slot(this, j + i * 2, 134 + j * 18, 16 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.getItem() instanceof BlockItem item &&
                                (item.getBlock() instanceof TorchBlock || item.getBlock() instanceof ColdFireTorchBlock);
                    }
                });
            }
        }
        this.addSlot(new Slot(this, 4, 51, 25) {
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

    private void loadData() {
        if (itemStack.isEmpty()) return;
        CompoundTag nbt = itemStack.getOrCreateTag();
        if (!itemStack.hasTag() || !nbt.contains("Torches") || !nbt.contains("Gunpowder")) {
            nbt.put("Torches", new ListTag());
            nbt.putInt("Gunpowder", 0);
            this.items.clear();
            this.gunpowder = 0;
        } else {
            ListTag torches = nbt.getList("Torches", 10);
            CompoundTag subNbt;
            int j;
            for (int i = 0; i < torches.size(); i++) {
                subNbt = torches.getCompound(i);
                j = subNbt.getByte("Slot") & 255;
                if (j < this.items.size()) this.items.set(j, ItemStack.of(subNbt));
            }
            this.gunpowder = nbt.getInt("Gunpowder");
        }
    }

    private void saveData() {
        if (this.itemStack.isEmpty()) return;
        ListTag torches = new ListTag();
        CompoundTag nbt;
        ItemStack item;
        for (int i = 0; i < this.items.size(); i++) {
            item = this.items.get(i);
            if (!item.isEmpty()) {
                nbt = item.save(new CompoundTag());
                nbt.putByte("Slot", (byte) i);
                torches.add(nbt);
            }
        }
        this.itemStack.getOrCreateTag().put("Torches", torches);
        this.itemStack.getOrCreateTag().putInt("Gunpowder", this.gunpowder);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public int getContainerSize() {
        loadData();
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        loadData();
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        loadData();
        return this.items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        loadData();
        ItemStack stack = !this.items.get(pIndex).isEmpty() ? ContainerHelper.removeItem(this.items, pIndex, pCount) : ItemStack.EMPTY;
        saveData();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        loadData();
        ItemStack itemstack = this.items.get(pIndex);
        this.items.set(pIndex, ItemStack.EMPTY);
        saveData();
        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmpty(int pIndex) {
        loadData();
        return this.items.get(pIndex).isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public int getGunpowder() {
        return Math.min(this.gunpowder, 100);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        loadData();
        this.items.set(pIndex, pStack);
        if (pIndex == 4) {
            ItemStack item = this.items.get(pIndex);
            if (this.gunpowder <= 84 && !item.isEmpty()) {
                int count = Math.min((100 - this.gunpowder) / 16, item.getCount());
                item.shrink(count);
                this.gunpowder += count * 16;
            }
        }
        saveData();
    }

    @Override
    public void setChanged() {
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.stopOpen(pPlayer);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

}

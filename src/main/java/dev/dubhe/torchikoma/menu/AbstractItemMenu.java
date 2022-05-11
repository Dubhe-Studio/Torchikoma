package dev.dubhe.torchikoma.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class AbstractItemMenu<T extends AbstractMenuInventory> extends AbstractContainerMenu {

    protected final T itemInventory;

    protected AbstractItemMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, ItemStack itemStack) {
        super(pMenuType, pContainerId);
        this.itemInventory = genItemInventory(itemStack);
        this.itemInventory.startOpen(inventory.player);
    }

    protected abstract T genItemInventory(ItemStack itemStack);

    @OnlyIn(Dist.CLIENT)
    public boolean isEmpty(int pIndex) {
        return this.itemInventory.getItem(pIndex).isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItemStack() {
        return this.itemInventory.itemStack;
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
}

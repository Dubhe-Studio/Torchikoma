package dev.dubhe.torchikoma.menu;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractMenuInventory extends SimpleContainer {
    protected final ItemStack itemStack;

    protected AbstractMenuInventory(int size, ItemStack itemStack) {
        super(size);
        this.itemStack = itemStack;
        this.loadData();
        this.addListener(container -> {
            if (container instanceof AbstractMenuInventory inv) {
                inv.saveData();
            }
        });
    }

    protected abstract void loadData();
    protected abstract void saveData();

}


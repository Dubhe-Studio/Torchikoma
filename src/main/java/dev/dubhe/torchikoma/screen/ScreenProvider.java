package dev.dubhe.torchikoma.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface ScreenProvider {

    void openGUI(Player pPlayer, ItemStack item);

    default MenuProvider getMenuProvider(Component displayName, MenuFunction inventory) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return displayName;
            }
            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                return inventory.run(pContainerId, pInventory, pPlayer);
            }
        };
    }

    interface MenuFunction {
        AbstractContainerMenu run(int containerId, Inventory inventory, Player player);
    }
}

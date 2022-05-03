package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class TorchLauncherMenu extends AbstractContainerMenu {

    public TorchLauncherMenu(int pContainerId, Inventory inventory) {
        super(MyMenuTypes.TORCH_LAUNCHER, pContainerId);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + i));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 140));
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}

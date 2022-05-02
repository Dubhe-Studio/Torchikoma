package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class TorchLauncherMenu extends AbstractContainerMenu {

    public TorchLauncherMenu(int pContainerId, Inventory inventory) {
        super(MyMenuTypes.TORCH_LAUNCHER, pContainerId);
    }
    public TorchLauncherMenu(int pContainerId, Inventory inventory, Container container) {
        super(MyMenuTypes.TORCH_LAUNCHER, pContainerId);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
}

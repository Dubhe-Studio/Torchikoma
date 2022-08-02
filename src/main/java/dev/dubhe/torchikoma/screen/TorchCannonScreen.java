package dev.dubhe.torchikoma.screen;

import dev.dubhe.torchikoma.menu.TorchToolMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TorchCannonScreen extends TorchLauncherScreen {
    public TorchCannonScreen(TorchToolMenu pMenu,
            Inventory inventory, Component pTitle) {
        super(pMenu, inventory, pTitle);
    }
}

package dev.dubhe.torchikoma.screen;

import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import dev.dubhe.torchikoma.registry.MyItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TorchGatlingScreen extends TorchLauncherScreen{
    public TorchGatlingScreen(TorchLauncherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        setLauncher(new ItemStack(MyItems.TORCH_GATLING));
    }
}

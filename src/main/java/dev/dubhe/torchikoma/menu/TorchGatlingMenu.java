package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TorchGatlingMenu extends TorchLauncherMenu{
    public TorchGatlingMenu(int pContainerId, Inventory inventory, ItemStack itemStack) {
        super(MyMenuTypes.TORCH_GATLING, pContainerId, inventory, itemStack);
    }
}

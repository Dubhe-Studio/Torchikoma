package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.item.ClusteredTorchItem;
import dev.dubhe.torchikoma.item.TorchCannonItem;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import dev.dubhe.torchikoma.screen.TorchCannonScreen;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClusteredTorchToolMenu extends TorchToolMenu {

    public ClusteredTorchToolMenu(int pContainerId, Inventory inventory,
            ItemStack itemStack) {
        super(MyMenuTypes.CLUSTERED_TORCH_TOOL, pContainerId, inventory, itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TorchLauncherScreen.RenderSize getItemSize() {
        return itemInventory.itemStack.getItem() instanceof TorchCannonItem item ? item.getRenderSize()
                : TorchCannonScreen.RenderSize.DEFAULT;
    }

    @Override
    public boolean isTorch(ItemStack pStack) {
        return pStack.getItem() instanceof ClusteredTorchItem;
    }
}

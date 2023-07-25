package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.menu.TorchToolMenu;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public class TorchGatlingItem extends TorchLauncherItem {
    public TorchGatlingItem(Properties pProperties) {
        super(pProperties, 5);
    }

    @Override
    public void openGUI(Player pPlayer, ItemStack item){
        NetworkHooks.openScreen((ServerPlayer) pPlayer, getMenu(
                this.getDescription(),
                (id, inv, player) -> new TorchToolMenu(id, inv, item)
        ), buffer -> buffer.writeItem(item));
    }

    @Override
    public TorchLauncherScreen.RenderSize getRenderSize() {
        return new TorchLauncherScreen.RenderSize(26, 34, 22.0F);
    }
}

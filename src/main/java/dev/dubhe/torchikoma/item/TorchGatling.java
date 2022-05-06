package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.menu.TorchToolMenu;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public class TorchGatling extends TorchLauncher {
    public TorchGatling(Properties pProperties) {
        super(pProperties);
        this.setCooldown(5);
    }

    @Override
    public void openGUI(Player pPlayer, ItemStack item){
        NetworkHooks.openGui((ServerPlayer) pPlayer, getMenuProvider(
                this.getDescription(),
                (id, inv, player) -> new TorchToolMenu(id, inv, item)
        ), buffer -> buffer.writeItem(item));
    }

    @Override
    public TorchLauncherScreen.RenderSize getRenderSize() {
        return new TorchLauncherScreen.RenderSize(26, 34, 22.0F);
    }
}

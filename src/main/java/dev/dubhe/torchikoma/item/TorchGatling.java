package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
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
                (id, inv, player) -> TorchLauncherMenu.createGatling(id, inv, item)
        ), buffer -> buffer.writeItem(item));
    }
}

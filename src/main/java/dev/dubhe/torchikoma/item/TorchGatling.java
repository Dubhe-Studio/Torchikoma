package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.menu.TorchGatlingMenu;
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
                (id, inv, player) -> new TorchGatlingMenu(id, inv, item)
        ), buffer -> buffer.writeItem(item));
    }
}

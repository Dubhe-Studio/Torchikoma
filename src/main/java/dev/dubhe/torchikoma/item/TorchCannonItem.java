package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.entity.ClusteredTorchEntity;
import dev.dubhe.torchikoma.menu.ClusteredTorchToolMenu;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class TorchCannonItem extends TorchLauncherItem {

    public TorchCannonItem(Properties pProperties) {
        super(pProperties, 20);
    }

    @Override
    public void openGUI(Player pPlayer, ItemStack item) {
        NetworkHooks.openScreen((ServerPlayer) pPlayer, getMenu(
                this.getDescription(),
                (id, inv, player) -> new ClusteredTorchToolMenu(id, inv, item)
        ), buffer -> buffer.writeItem(item));
    }

    @Override
    protected void shoot(Level pLevel, Player pPlayer, ItemStack pStack) {
        ClusteredTorchEntity entity = new ClusteredTorchEntity(pPlayer, pStack, ((ClusteredTorchItem) pStack.getItem()).getTorch());
        entity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2.0F, 1.0F);
        pLevel.addFreshEntity(entity);
    }

    @Override
    public boolean isTorch(ItemStack pStack) {
        return pStack.getItem() instanceof ClusteredTorchItem;
    }

    @Override
    public TorchLauncherScreen.RenderSize getRenderSize() {
        return new TorchLauncherScreen.RenderSize(26, 34, 32.0F);
    }
}

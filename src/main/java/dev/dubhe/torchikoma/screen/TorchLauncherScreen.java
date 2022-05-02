package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TorchLauncherScreen extends AbstractContainerScreen<TorchLauncherMenu> {
    public TorchLauncherScreen(TorchLauncherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

    }
}

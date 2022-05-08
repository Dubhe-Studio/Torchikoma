package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.AbstractItemMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractItemScreen<T extends AbstractItemMenu<?>> extends AbstractContainerScreen<T> {

    private static final ResourceLocation PLAYER_BACKGROUND = Torchikoma.getId("textures/gui/player.png");
    protected static final int PLAYER_WIDTH = 176;
    protected static final int PLAYER_HEIGHT = 100;
    protected final int menuWidth;
    protected final int menuHeight;

    protected AbstractItemScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, int width, int height) {
        super(pMenu, pPlayerInventory, pTitle);
        this.menuWidth = width;
        this.menuHeight = height;
        this.imageWidth = Math.max(width, PLAYER_WIDTH);
        this.imageHeight = PLAYER_HEIGHT + height + 8;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLAYER_BACKGROUND);
        this.blit(pPoseStack, (this.width - PLAYER_WIDTH) / 2, (this.height - PLAYER_HEIGHT + this.menuHeight) / 2 + 4, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    protected void renderItemBg(PoseStack pPoseStack, int x, int y, int u, int v, int index) {
        if (this.menu.isEmpty(index)) {
            this.blit(pPoseStack, x, y, u, v, 16, 16);
        }
    }
}

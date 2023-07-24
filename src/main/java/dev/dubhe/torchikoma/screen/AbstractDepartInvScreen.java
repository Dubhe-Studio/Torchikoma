package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDepartInvScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private static final ResourceLocation PLAYER_BACKGROUND = Torchikoma.of("textures/gui/player.png");
    protected static final int PLAYER_WIDTH = 176;
    protected static final int PLAYER_HEIGHT = 100;
    protected final int menuWidth;
    protected final int menuHeight;

    protected AbstractDepartInvScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, int width, int height) {
        super(pMenu, pPlayerInventory, pTitle);
        this.menuWidth = width;
        this.menuHeight = height;
        this.imageWidth = Math.max(width, PLAYER_WIDTH);
        this.imageHeight = PLAYER_HEIGHT + height + 8;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(graphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLAYER_BACKGROUND);
        graphics.blit(PLAYER_BACKGROUND, (this.width - PLAYER_WIDTH) / 2, (this.height - PLAYER_HEIGHT + this.menuHeight) / 2 + 4, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
    }
}

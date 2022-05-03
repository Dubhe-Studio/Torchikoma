package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TorchLauncherScreen extends AbstractContainerScreen<TorchLauncherMenu> {
    public static int temp = 0;

    private static final ResourceLocation GUN_BACKGROUND = Torchikoma.getId("textures/gui/gun.png");
    private static final ResourceLocation PLAYER_BACKGROUND = Torchikoma.getId("textures/gui/player.png");
    private static final int GUN_WIDTH = 178;
    private static final int GUN_HEIGHT = 58;
    private static final int PLAYER_WIDTH = 176;
    private static final int PLAYER_HEIGHT = 90;
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");

    public TorchLauncherScreen(TorchLauncherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.titleLabelX = 50;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLAYER_BACKGROUND);
        this.blit(pPoseStack, (this.width - PLAYER_WIDTH) / 2, (this.height - PLAYER_HEIGHT) / 2 + 38, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        RenderSystem.setShaderTexture(0, GUN_BACKGROUND);
        this.blit(pPoseStack, (this.width - GUN_WIDTH) / 2, (this.height - GUN_HEIGHT) / 2 - 61, 0, 0, GUN_WIDTH, GUN_HEIGHT);
    }
}

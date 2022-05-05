package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class TorchLauncherScreen extends AbstractContainerScreen<TorchLauncherMenu> {
    public static int temp1 = 0;
    public static int temp2 = 0;
    public static int temp3 = 0;
    public static int temp4 = 0;

    private static final ResourceLocation GUN_BACKGROUND = Torchikoma.getId("textures/gui/gun.png");
    private static final ResourceLocation PLAYER_BACKGROUND = Torchikoma.getId("textures/gui/player.png");
    private static final int GUN_WIDTH = 178;
    private static final int GUN_HEIGHT = 58;
    private static final int PLAYER_WIDTH = 176;
    private static final int PLAYER_HEIGHT = 100;

    private final ItemRenderer itemRenderer;

    public TorchLauncherScreen(TorchLauncherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.titleLabelX = 50;
        this.titleLabelY += 7;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        super.renderTooltip(pPoseStack, pX, pY);
        int x = (this.width - GUN_WIDTH) / 2 + 76;
        int y = (this.height - GUN_HEIGHT - PLAYER_HEIGHT - 8) / 2 + 31;
        if (pX >= x && pX <= x + 50 && pY >= y && pY <= y + 4) {
            this.renderTooltip(pPoseStack, List.of(new TranslatableComponent("gui.torchikoma.gunpowder", this.menu.getShoots())), Optional.empty(), pX, pY);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int gunX = (this.width - GUN_WIDTH) / 2;
        int gunY = (this.height - GUN_HEIGHT - PLAYER_HEIGHT - 8) / 2;
        RenderSystem.setShaderTexture(0, PLAYER_BACKGROUND);
        this.blit(pPoseStack, (this.width - PLAYER_WIDTH) / 2, gunY + GUN_HEIGHT + 8, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        RenderSystem.setShaderTexture(0, GUN_BACKGROUND);
        this.blit(pPoseStack, gunX, gunY, 0, 0, GUN_WIDTH, GUN_HEIGHT);
        this.blit(pPoseStack, gunX + 76, gunY + 31, GUN_WIDTH, 0, (int)(0.5D * this.menu.getShoots()), 4);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.GUNPOWDER), gunX + 76, gunY + 38);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.TORCH), gunX + 100, gunY + 36);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.SOUL_TORCH), gunX + 104, gunY + 36);
    }
}

package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
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
    private static final ResourceLocation GUN_BACKGROUND = Torchikoma.getId("textures/gui/gun.png");
    private static final ResourceLocation PLAYER_BACKGROUND = Torchikoma.getId("textures/gui/player.png");
    private static final int GUN_WIDTH = 178;
    private static final int GUN_HEIGHT = 58;
    private static final int PLAYER_WIDTH = 176;
    private static final int PLAYER_HEIGHT = 100;
    private final ItemRenderer itemRenderer;
    private int rotateAngle = 0;

    public TorchLauncherScreen(TorchLauncherMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.titleLabelX = 50;
        this.titleLabelY += 7;
    }

    @Override
    protected void containerTick() {
        rotateAngle = (rotateAngle + 5) % 360;
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
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.renderItemBg(pPoseStack, gunX + j * 18 + 135, gunY + i * 18 + 16, GUN_WIDTH + 16, 4, j + i * 2);
            }
        }
        this.renderItemBg(pPoseStack, gunX + 52, gunY + 25, GUN_WIDTH, 4, 4);
        this.renderBigItem(this.menu.getItemStack(), gunX + 26, gunY + 25 + 6, 48.0F);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.GUNPOWDER), gunX + 76, gunY + 38);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.TORCH), gunX + 100, gunY + 36);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.SOUL_TORCH), gunX + 104, gunY + 36);
    }

    @SuppressWarnings("SameParameterValue")
    private void renderItemBg(PoseStack pPoseStack, int x, int y, int u, int v, int index) {
        if (this.menu.isEmpty(index)) {
            this.blit(pPoseStack, x, y, u, v, 16, 16);
        }
    }

    @SuppressWarnings({"deprecation", "SameParameterValue"})
    private void renderBigItem(ItemStack pStack, int x, int y, float size) {
        if (!pStack.isEmpty()) {
            BakedModel bakedmodel = this.itemRenderer.getModel(pStack, null, Minecraft.getInstance().player, 0);
            this.itemRenderer.textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            PoseStack posestack = RenderSystem.getModelViewStack();
            Quaternion quaternion = Vector3f.YP.rotationDegrees(this.rotateAngle);
            double incremental = -Math.cos(Math.toRadians(this.rotateAngle + 60)); // 模型中心点与渲染中心点不一致的坐标补偿
            posestack.pushPose();
            posestack.translate(x + incremental, y, 100.0F);
            posestack.scale(1.0F, -1.0F, 1.0F);
            posestack.scale(size, size, size);
            posestack.mulPose(quaternion);
            RenderSystem.applyModelViewMatrix();
            PoseStack poseStack = new PoseStack();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean flag = !bakedmodel.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }
            this.itemRenderer.render(pStack, ItemTransforms.TransformType.GROUND, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (flag) {
                Lighting.setupFor3DItems();
            }
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }
}

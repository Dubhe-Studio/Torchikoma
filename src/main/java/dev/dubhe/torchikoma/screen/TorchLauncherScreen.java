package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchToolMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class TorchLauncherScreen extends AbstractDepartInvScreen<TorchToolMenu> {
    private static final ResourceLocation BACKGROUND = Torchikoma.of("textures/gui/gun.png");
    private int rotateAngle = 0;

    public TorchLauncherScreen(TorchToolMenu pMenu, Inventory inventory, Component pTitle) {
        super(pMenu, inventory, pTitle, 178, 58);
        this.titleLabelX = 50;
        this.titleLabelY += 7;
    }

    @Override
    protected void containerTick() {
        rotateAngle = (rotateAngle + 5) % 360;
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int pX, int pY) {
        super.renderTooltip(graphics, pX, pY);
        if (pX >= this.leftPos + 76 && pX <= this.leftPos + 126 && pY >= this.topPos + 31 && pY <= this.topPos + 35) {
            graphics.renderTooltip(this.font, Component.translatable("gui.torchikoma.gunpowder", this.menu.getShoots()), pX, pY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.menuWidth, this.menuHeight);
        graphics.blit(BACKGROUND, this.leftPos + 76, this.topPos + 31, this.menuWidth, 0, (int) (0.5D * this.menu.getShoots()), 4);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.renderItemBg(graphics, this.leftPos + j * 18 + 135, this.topPos + i * 18 + 16, this.menuWidth + 16, 4, j + i * 2);
            }
        }
        this.renderItemBg(graphics, this.leftPos + 52, this.topPos + 25, this.menuWidth, 4, 4);
        RenderSize renderSize = this.menu.getItemSize();
        this.renderBigItem(this.menu.getItemStack(), this.leftPos + renderSize.pX, this.topPos + renderSize.pY, renderSize.size);
        graphics.renderItemDecorations(this.font, new ItemStack(Items.GUNPOWDER), this.leftPos + 76, this.topPos + 38);
        graphics.renderItemDecorations(this.font, new ItemStack(Items.TORCH), this.leftPos + 100, this.topPos + 36);
        graphics.renderItemDecorations(this.font, new ItemStack(Items.SOUL_TORCH), this.leftPos + 104, this.topPos + 36);
    }

    @SuppressWarnings({"deprecation", "SameParameterValue"})
    private void renderBigItem(ItemStack pStack, int x, int y, float size) {
        if (!pStack.isEmpty() && this.minecraft != null) {
            BakedModel bakedmodel = this.minecraft.getItemRenderer().getModel(pStack, null, Minecraft.getInstance().player, 0);
            this.minecraft.getItemRenderer().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            PoseStack posestack = RenderSystem.getModelViewStack();
            double incremental = -Math.cos(Math.toRadians(this.rotateAngle + 60)); // 模型中心点与渲染中心点不一致的坐标补偿
            posestack.pushPose();
            posestack.translate(x + incremental, y, 100.0F);
            posestack.scale(1.0F, -1.0F, 1.0F);
            posestack.scale(size, size, size);
            posestack.mulPose(Axis.YP.rotationDegrees(this.rotateAngle));
            RenderSystem.applyModelViewMatrix();
            PoseStack poseStack = new PoseStack();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean flag = !bakedmodel.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }
            this.minecraft.getItemRenderer().render(pStack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (flag) {
                Lighting.setupFor3DItems();
            }
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void renderItemBg(GuiGraphics graphics, int x, int y, int u, int v, int index) {
        if (this.menu.isEmpty(index)) {
            graphics.blit(BACKGROUND, x, y, u, v, 16, 16);
        }
    }

    public record RenderSize(int pX, int pY, float size) {
        public static final RenderSize DEFAULT = new RenderSize(26, 31, 48.0F);
    }
}

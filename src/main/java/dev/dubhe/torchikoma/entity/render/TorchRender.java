package dev.dubhe.torchikoma.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.entity.TorchEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;

@OnlyIn(Dist.CLIENT)
public class TorchRender extends EntityRenderer<TorchEntity> {
    private final ItemRenderer itemRenderer;
//    private final ModelManager modelManager;

    public TorchRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
//        this.modelManager = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager();
    }

    @Override
    public void render(TorchEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
//        Item item = pEntity.getItemStack().getItem();
//        ResourceLocation location = Registry.ITEM.getKey(item);
//        BakedModel bakedmodel = modelManager.getModel(location);
        ItemStack itemstack = pEntity.getItemStack();
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, pEntity.level, null, pEntity.getId());
        pMatrixStack.pushPose();
        this.itemRenderer.render(itemstack, ItemTransforms.TransformType.GROUND, false, pMatrixStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        if (!bakedmodel.isGui3d()) {
            pMatrixStack.translate(0.0, 0.0, 0.09375F);
        }
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TorchEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

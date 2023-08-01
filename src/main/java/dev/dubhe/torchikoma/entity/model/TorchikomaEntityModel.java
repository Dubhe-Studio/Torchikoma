package dev.dubhe.torchikoma.entity.model;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.resource.ResourceListener;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import java.io.IOException;

public class TorchikomaEntityModel extends GeoModel<TorchikomaEntity> {
    private final TorchikomaCustomTexture customTexture = new TorchikomaCustomTexture();

    public TorchikomaEntityModel() {
        try {
            this.customTexture.reload(new ResourceListener().registerReloadListener());
        } catch (IOException e) {
            Torchikoma.LOGGER.warn("Fail load torchikoma entity custom texture");
        }
    }

    @Override
    public ResourceLocation getModelResource(TorchikomaEntity entity) {
        return new ResourceLocation(Torchikoma.ID, "geo/torchikoma.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TorchikomaEntity entity) {
        String texture = this.customTexture.get(entity.getPainting());
        texture = texture == null ? "torchikoma" : "torchikoma/" + texture;
        return new ResourceLocation(Torchikoma.ID, "textures/entity/" + texture + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(TorchikomaEntity entity) {
        return new ResourceLocation(Torchikoma.ID, "animations/entity/torchikoma.animation.json");
    }
}

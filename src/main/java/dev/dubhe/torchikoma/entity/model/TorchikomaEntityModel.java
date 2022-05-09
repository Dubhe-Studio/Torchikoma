package dev.dubhe.torchikoma.entity.model;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.resource.ResourceListener;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import java.io.IOException;

public class TorchikomaEntityModel extends AnimatedTickingGeoModel<TorchikomaEntity> {
    private String texture = "torchikoma";
    private TorchikomaCustomTexture customTexture = new TorchikomaCustomTexture();

    public TorchikomaEntityModel() {
        try {
            this.customTexture.reload(new ResourceListener().registerReloadListener());
        } catch (IOException e) {
            Torchikoma.LOGGER.warn("Fail load torchikoma entity model");
        }
    }

    @Override
    public ResourceLocation getModelLocation(TorchikomaEntity entity) {
        return new ResourceLocation(Torchikoma.ID, "geo/torchikoma.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TorchikomaEntity entity) {
        String painting = entity.getPainting();
        if (painting != null && !painting.equals("minecraft:air")) {
            this.setPaiting(painting);
        }
        return new ResourceLocation(Torchikoma.ID, "textures/entity/" + texture + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TorchikomaEntity entity) {
        return new ResourceLocation(Torchikoma.ID, "animations/entity/torchikoma.animation.json");
    }

    @SuppressWarnings({"unused"})
    @Override
    public void setLivingAnimations(TorchikomaEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }

    public void setPaiting(String itmeID) {
        if (this.customTexture.has(itmeID)) {
            this.texture = this.customTexture.getItemMap().get(itmeID);
        }
    }
}

package dev.dubhe.torchikoma.entity.model;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.entity.render.CustomTorchikomaTexture;
import dev.dubhe.torchikoma.resource.ResourceListener;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

import java.io.IOException;
import java.util.Arrays;

public class TorchikomaEntityModel extends AnimatedTickingGeoModel<TorchikomaEntity> {
    private String texture = "torchikoma";
    private CustomTorchikomaTexture customTexture = new CustomTorchikomaTexture();

    public TorchikomaEntityModel() {
        try {
            this.customTexture.reload(new ResourceListener().registerReloadListener());
        } catch (IOException e) {
            Torchikoma.LOGGER.warn("Fail load torchikoma entity model");
        }
    }

    @Override
    public ResourceLocation getModelLocation(TorchikomaEntity object) {
        return new ResourceLocation(Torchikoma.ID, "geo/torchikoma.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TorchikomaEntity object) {
        return new ResourceLocation(Torchikoma.ID, "textures/entity/" + texture + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TorchikomaEntity animatable) {
        return new ResourceLocation(Torchikoma.ID, "animations/entity/torchikoma.animation.json");
    }

    @SuppressWarnings({"unused"})
    @Override
    public void setLivingAnimations(TorchikomaEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        String painting = entity.getPainting();
        if (painting != null && !painting.equals("minecraft:air")) {
            this.setPaiting(ResourceLocation.tryParse(painting));
        }
    }

    public void setPaiting(ResourceLocation location) {
        if (Arrays.stream(this.customTexture.getItemList()).toList().contains(location)) {
            this.texture = this.customTexture.getItemMap().get(location).toString();
        }
    }
}

package dev.dubhe.torchikoma.entity.model;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class TorchikomaEntityModel extends AnimatedTickingGeoModel<TorchikomaEntity>{
    private String texture = "torchikoma";
    @Override
    public ResourceLocation getModelLocation(TorchikomaEntity object) {
        return new ResourceLocation(Torchikoma.ID,"geo/torchikoma.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TorchikomaEntity object) {
        return new ResourceLocation(Torchikoma.ID,"textures/entity/"+texture+".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TorchikomaEntity animatable) {
        return new ResourceLocation(Torchikoma.ID,"animations/entity/torchikoma.animation.json");
    }

    @SuppressWarnings({"unused"})
    @Override
    public void setLivingAnimations(TorchikomaEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }

    public void setTexture(String texture){
        this.texture = texture;
    }
}

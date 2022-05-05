package dev.dubhe.torchikoma.entity.model;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.item.JackInTheBoxItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class TorchikomaEntityModel extends AnimatedTickingGeoModel<TorchikomaEntity> {
    @Override
    public ResourceLocation getModelLocation(TorchikomaEntity object) {
        return new ResourceLocation(Torchikoma.ID,"models/entity/torchikoma.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TorchikomaEntity object) {
        return new ResourceLocation(Torchikoma.ID,"textures/entity/torchikoma.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TorchikomaEntity animatable) {
        return new ResourceLocation(Torchikoma.ID,"animations/entity/torchikoma.animation.json");
    }
}

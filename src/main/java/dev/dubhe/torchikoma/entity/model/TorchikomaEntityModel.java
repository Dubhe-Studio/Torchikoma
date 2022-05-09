package dev.dubhe.torchikoma.entity.model;

import com.mojang.brigadier.StringReader;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.resource.ResourceListener;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
            Torchikoma.LOGGER.warn("Fail load torchikoma entity custom texture");
        }
    }

    @Override
    public ResourceLocation getModelLocation(TorchikomaEntity entity) {
        return new ResourceLocation(Torchikoma.ID, "geo/torchikoma.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TorchikomaEntity entity) {
        this.setPaiting(entity.getPainting());
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
        String texture = this.customTexture.get(itmeID);
        if (texture != null) {
            this.texture = "torchikoma/" + texture;
        } else {
            this.texture = "torchikoma";
        }
    }
}

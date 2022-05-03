package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MyEntities {
    public static final EntityType<TorchEntity> TORCH = register("torch", EntityType.Builder.<TorchEntity>of(TorchEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        EntityType<T> entityType = builder.build(name);
        entityType.setRegistryName(Torchikoma.getId(name));
        return entityType;
    }
}

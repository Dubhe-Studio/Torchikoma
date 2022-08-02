package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.ClusteredTorchEntity;
import dev.dubhe.torchikoma.entity.TorchEntity;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MyEntities {

    public static final EntityType<TorchEntity> TORCH = create("torch",
            EntityType.Builder.<TorchEntity>of(TorchEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    public static final EntityType<ClusteredTorchEntity> CLUSTERED_TORCH = create("clustered_torch",
            EntityType.Builder.<ClusteredTorchEntity>of(ClusteredTorchEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    public static final EntityType<TorchikomaEntity> TORCHIKOMA = create("torchikoma",
            EntityType.Builder.of(TorchikomaEntity::new, MobCategory.MISC)
                    .sized(1F, 1F).clientTrackingRange(4).updateInterval(1));

    public static <T extends Entity> EntityType<T> create(String name, EntityType.Builder<T> builder) {
        EntityType<T> entityType = builder.build(name);
        entityType.setRegistryName(Torchikoma.of(name));
        return entityType;
    }
}

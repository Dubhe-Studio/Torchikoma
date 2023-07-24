package dev.dubhe.torchikoma.entity;

import dev.dubhe.torchikoma.registry.MyEntities;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;

public class ClusteredTorchEntity extends TorchEntity{
    private Item torchItem;

    public ClusteredTorchEntity(EntityType<? extends ClusteredTorchEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ClusteredTorchEntity(LivingEntity entity, ItemStack stack, Item item) {
        super(MyEntities.CLUSTERED_TORCH, entity, stack);
        this.torchItem = item;
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        BlockPos pos = result.getBlockPos().offset(0,1,0);

        TorchEntity entity1 = new TorchEntity((LivingEntity) Objects.requireNonNull(this.getOwner()), new ItemStack(this.torchItem, 1));
        TorchEntity entity2 = new TorchEntity((LivingEntity) Objects.requireNonNull(this.getOwner()), new ItemStack(this.torchItem, 1));
        TorchEntity entity3 = new TorchEntity((LivingEntity) Objects.requireNonNull(this.getOwner()), new ItemStack(this.torchItem, 1));
        TorchEntity entity4 = new TorchEntity((LivingEntity) Objects.requireNonNull(this.getOwner()), new ItemStack(this.torchItem, 1));

        entity1.setPos(pos.getX(),pos.getY(),pos.getZ());
        entity2.setPos(pos.getX(),pos.getY(),pos.getZ());
        entity3.setPos(pos.getX(),pos.getY(),pos.getZ());
        entity4.setPos(pos.getX(),pos.getY(),pos.getZ());

        double i = 0.5d;

        entity1.setDeltaMovement(i,i,0);
        entity2.setDeltaMovement(-i,i,0);
        entity3.setDeltaMovement(0,i,i);
        entity4.setDeltaMovement(0,i,-i);

        level().addFreshEntity(entity1);
        level().addFreshEntity(entity2);
        level().addFreshEntity(entity3);
        level().addFreshEntity(entity4);

        this.level().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.discard();
    }
}

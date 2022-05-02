package dev.dubhe.torchikoma.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class TorchEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.ITEM_STACK);

    public TorchEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public TorchEntity(Level level, LivingEntity entity, ItemStack stack) {
        super(EntityType.TRIDENT, entity, level);
        this.entityData.set(ITEM_STACK, stack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ITEM_STACK, new ItemStack(Items.TORCH));
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.entityData.get(ITEM_STACK);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
    }
}

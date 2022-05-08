package dev.dubhe.torchikoma.entity;

import dev.dubhe.torchikoma.registry.MyEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class TorchEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.ITEM_STACK);

    public TorchEntity(EntityType<? extends AbstractArrow> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public TorchEntity(Level level, LivingEntity entity, ItemStack stack) {
        super(MyEntities.TORCH, entity, level);
        this.entityData.set(ITEM_STACK, stack);
    }

    public ItemStack getItemStack() {
        return this.entityData.get(ITEM_STACK);
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
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.getOwner() instanceof Player player && this.entityData.get(ITEM_STACK).getItem() instanceof BlockItem blockItem) {
            InteractionResult interactionResult = blockItem.place(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(blockItem), result));
            if (interactionResult == InteractionResult.CONSUME) this.discard();
        } else this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.WOOD_BREAK;
    }
}

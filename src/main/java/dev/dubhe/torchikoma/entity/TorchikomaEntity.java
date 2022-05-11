package dev.dubhe.torchikoma.entity;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import dev.dubhe.torchikoma.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TorchikomaEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {
    AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<String> PAINTING_ITEM = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.BYTE);

    private final SimpleContainer inventory;
    private boolean following;
    private boolean inSitu;
    private boolean sleeping;
    private int energy;

    public TorchikomaEntity(EntityType<? extends PathfinderMob> type, Level inLevel) {
        super(type, inLevel);
        this.noCulling = true;
        this.inventory = new SimpleContainer(15);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getOwnerUUID() != null) pCompound.putUUID("Owner", this.getOwnerUUID());
        pCompound.putString("PaintingItem", this.getPainting());
        ListTag list = new ListTag();
        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                itemstack.save(compoundtag);
                list.add(compoundtag);
            }
        }
        pCompound.put("Items", list);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setOwnerUUID(pCompound.hasUUID("Owner") ? pCompound.getUUID("Owner") : null);
        this.setPainting(pCompound.contains("PaintingItem") ? pCompound.getString("PaintingItem") : "minecraft:air");
        ListTag listtag = pCompound.getList("Items", 10);
        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 2 && j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundtag));
            }
        }
    }

    public void setPainting(String itemName){
        this.entityData.set(PAINTING_ITEM, itemName);
    }

    public String getPainting(){
        return this.entityData.get(PAINTING_ITEM);
    }

    public boolean isOwnedBy(LivingEntity pEntity) {
        return pEntity == this.getOwner();
    }

    @Override
    public boolean isAlliedTo(@Nonnull Entity pEntity) {
        LivingEntity livingentity = this.getOwner();
        if (pEntity == livingentity) {
            return true;
        }

        if (livingentity != null) {
            return livingentity.isAlliedTo(pEntity);
        }


        return super.isAlliedTo(pEntity);
    }

    @Nonnull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }


    public void tick() {
        super.tick();
        if (this.getOwner() instanceof Player player) {
            float f = getOwnerDistance();
            if (f > 10.0F) {
                double d0 = (player.getX() - this.getX()) / (double) f;
                double d1 = (player.getY() - this.getY()) / (double) f;
                double d2 = (player.getZ() - this.getZ()) / (double) f;
                this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
            } else {
                this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                Vec3 vec3 = (new Vec3(player.getX() - this.getX(), player.getY() - this.getY(), player.getZ() - this.getZ())).normalize().scale(Math.max(f - 2.0F, 0.0F));
                this.getNavigation().moveTo(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z, this.followLeashSpeed());
            }
        }
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER).orElse(null);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER, Optional.empty());
        this.entityData.define(PAINTING_ITEM, "minecraft:blaze_powder");
    }

    public void setOwnerUUID(@Nullable UUID Owner) {
        this.entityData.set(OWNER, Optional.ofNullable(Owner));
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    protected float getOwnerDistance() {
        Player player = (Player) this.getOwner();
        if (player != null) {
            return this.distanceTo(player);
        } else {
            return 0;
        }
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torchikoma.unpacking", false));
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torchikoma.walk", true));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torchikoma.packing", false));
        }
        return PlayState.CONTINUE;
    }

    public void die(@Nonnull DamageSource pCause) {
        net.minecraft.network.chat.Component deathMessage = this.getCombatTracker().getDeathMessage();
        super.die(pCause);

        if (this.dead)
            if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
                this.getOwner().sendMessage(deathMessage, Util.NIL_UUID);
            }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<TorchikomaEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.registerCustomInstructionListener(this::customListener);
        data.addAnimationController(controller);
    }

    private <ENTITY extends IAnimatable> void customListener(CustomInstructionKeyframeEvent<ENTITY> event) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(new TextComponent("KeyFraming"), true);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        super.registerGoals();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isInSitu() {
        return inSitu;
    }

    @Override
    public boolean isSleeping() {
        return sleeping;
    }

    public int getEnergy() {
        return energy;
    }

    public SimpleContainer getInventory() {
        return inventory;
    }
}

package dev.dubhe.torchikoma.entity;

import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.menu.TorchikomaMenu;
import dev.dubhe.torchikoma.registry.MyItems;
import dev.dubhe.torchikoma.screen.ScreenProvider;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import dev.dubhe.torchikoma.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TorchikomaEntity extends PathfinderMob implements IAnimatable, IAnimationTickable, ScreenProvider, ContainerListener, PlayerRideableJumping {
    protected float playerJumpPendingScale;
    private boolean allowStandSliding;
    protected int gallopSoundCounter;
    protected boolean isJumping;
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENERGY_DATA = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> STATUS_FLAG = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.BYTE);
    private static final DecimalFormat TRANS_FORMAT = new DecimalFormat("0.00#");
    private final AnimationFactory factory = new AnimationFactory(this);
    private final SimpleContainer inventory = new SimpleContainer(15);

    public TorchikomaEntity(EntityType<? extends PathfinderMob> type, Level inLevel) {
        super(type, inLevel);
        this.noCulling = true;
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER, Optional.empty());
        this.entityData.define(ENERGY_DATA, 0);
        this.entityData.define(STATUS_FLAG, (byte) 1); // 默认原地警戒
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double)this.getBbHeight() * 0.7D;
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public void tick() {
        super.tick();

        ItemStack itemStack = this.inventory.getItem(13);
        if (itemStack.getItem() instanceof EnergyCoreItem item) {
            this.addEnergy(item.getRecovery());
        }

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
    protected double followLeashSpeed() {
        return 0.25D;
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level.isClientSide && pPlayer.isSecondaryUseActive()) {
            this.openGUI(pPlayer, ItemStack.EMPTY);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            this.doPlayerRide(pPlayer);
            return super.mobInteract(pPlayer, pHand);
        }
    }

    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
    }

    @Override
    public void openGUI(Player pPlayer, ItemStack item) {
        NetworkHooks.openGui((ServerPlayer) pPlayer, this.getMenuProvider(
                this.getDisplayName(),
                (id, inv, player) -> new TorchikomaMenu(id, inv, this)
        ), buffer -> buffer.writeInt(this.getId()));
    }

    @Override
    public void containerChanged(Container pInvBasic) {

    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
        pCompound.putString("PaintingItem", this.getPainting());
        pCompound.putInt("Energy", this.getEnergy());
        pCompound.putByte("Status", this.getStatus());
        ListTag list = new ListTag();
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
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
        if (pCompound.contains("Energy")) {
            this.setEnergy(pCompound.getInt("Energy"));
        }
        if (pCompound.contains("Status")) {
            this.setStatus(pCompound.getByte("Status"));
        }
        ListTag listtag = pCompound.getList("Items", 10);
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 2 && j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundtag));
            }
        }
    }

    public boolean canSitu() {
        return new BlockPlaceContext(
                this.level ,
                null, InteractionHand.MAIN_HAND,
                new ItemStack(MyItems.TORCHIKOMA),
                new BlockHitResult(
                        this.getDeltaMovement(),
                        Direction.UP,
                        this.getBlockPosBelowThatAffectsMyMovement(),
                        false
                )
        ).canPlace();
    }

    @Nonnull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public int tickTimer() {
        return this.tickCount;
    }

    @Override
    public void die(@Nonnull DamageSource pCause) {
        super.die(pCause);
        if (this.dead && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        UUID uuid = this.getOwnerUUID();
        return uuid == null ? null : this.level.getPlayerByUUID(uuid);
    }

    protected float getOwnerDistance() {
        LivingEntity entity = this.getOwner();
        return entity == null ? -1 : this.distanceTo(entity);
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<TorchikomaEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.registerCustomInstructionListener(this::customListener);
        data.addAnimationController(controller);
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torchikoma.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torchikoma.packing", false));
        }
        if (allowStandSliding){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torchikoma.beforejump", false));
        }
        return PlayState.CONTINUE;
    }

    private <T extends IAnimatable> void customListener(CustomInstructionKeyframeEvent<T> event) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(new TextComponent("KeyFraming"), true);
        }
    }

    @Override
    public void positionRider(@Nonnull Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
            pPassenger.setPos(this.getX() + (double)(0.3F * f1), this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset(), this.getZ() - (double)(0.3F * f));
        }
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

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID Owner) {
        this.entityData.set(OWNER, Optional.ofNullable(Owner));
    }

    public String getPainting() {
        return inventory.getItem(14).getItem().toString();
    }

    public int getEnergy() {
        return Math.min(this.entityData.get(ENERGY_DATA), 20000);
    }

    public String getFormatEnergy() {
        return TRANS_FORMAT.format(getEnergy() / 100F);
    }

    public void setEnergy(int energy) {
        this.entityData.set(ENERGY_DATA, energy);
    }

    public void addEnergy(int energy) {
        int now = this.getEnergy();
        if (now != 20000) {
            this.setEnergy(Math.min(now + energy, 20000));
        }
    }

    public boolean decEnergy(int energy) {
        int now = this.getEnergy();
        if (now >= energy) {
            this.setEnergy(Math.max(now - energy, 0));
            return true;
        }
        return false;
    }

    public byte getStatus() {
        return this.entityData.get(STATUS_FLAG);
    }

    public void setStatus(byte status) {
        this.entityData.set(STATUS_FLAG, status);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    @Override
    public void onPlayerJump(int pJumpPower) {
        if (pJumpPower < 0) {
            pJumpPower = 0;
        }

        if (pJumpPower >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float) pJumpPower / 90.0F;
        }
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                this.setYRot(livingentity.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                    this.gallopSoundCounter = 0;
                }

                if (this.onGround && this.playerJumpPendingScale == 0.0F && !this.allowStandSliding) {
                    f = 0.0F;
                    f1 = 0.0F;
                }

                if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
                    double d0 = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
                    double d1 = d0 + this.getJumpBoostPower();
                    Vec3 vec3 = this.getDeltaMovement();
                    this.setDeltaMovement(vec3.x, d1, vec3.z);
                    this.setIsJumping(true);
                    this.hasImpulse = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F));
                        float f3 = Mth.cos(this.getYRot() * ((float)Math.PI / 180F));
                        this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f2 * this.playerJumpPendingScale, 0.0D, 0.4F * f3 * this.playerJumpPendingScale));
                    }

                    this.playerJumpPendingScale = 0.0F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(f, pTravelVector.y, f1));
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                if (this.onGround) {
                    this.playerJumpPendingScale = 0.0F;
                    this.setIsJumping(false);
                }

                this.calculateEntityAnimation(this, false);
                this.tryCheckInsideBlocks();
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    public void setIsJumping(boolean pJumping) {
        this.isJumping = pJumping;
    }

    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    @Override
    public void handleStartJump(int pJumpPower) {
        this.allowStandSliding = true;
    }

    @Override
    public void handleStopJump() {

    }
}

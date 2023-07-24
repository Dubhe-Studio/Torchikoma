package dev.dubhe.torchikoma.entity;

import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.menu.TorchikomaEntityMenu;
import dev.dubhe.torchikoma.registry.MyBlocks;
import dev.dubhe.torchikoma.registry.MyEntities;
import dev.dubhe.torchikoma.registry.MyItems;
import dev.dubhe.torchikoma.screen.ScreenProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
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
import dev.dubhe.torchikoma.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.goal.FloatGoal;
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
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation.LoopType;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TorchikomaEntity extends PathfinderMob implements GeoEntity, ScreenProvider<ItemStack>, ContainerListener, PlayerRideableJumping {
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENERGY_DATA = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> STATUS_FLAG = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<String> PAINTING = SynchedEntityData.defineId(TorchikomaEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Container inventory = new TorchikomaContainer(this);
    protected float playerJumpPendingScale;
    private boolean allowStandSliding;
    protected int gallopSoundCounter;
    protected boolean isJumping;

    public TorchikomaEntity(EntityType<? extends PathfinderMob> type, Level inLevel) {
        super(type, inLevel);
        this.noCulling = true;
    }

    public static TorchikomaEntity of(Level level, BlockPos pos, NonNullList<ItemStack> items, @Nullable UUID uuid, float health, int energy) {
        TorchikomaEntity entity = new TorchikomaEntity(MyEntities.TORCHIKOMA, level);
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        for (int i = 0; i < items.size(); i++) {
            entity.inventory.setItem(i, items.get(i));
        }
        entity.setOwnerUUID(uuid);
        entity.setHealth(health);
        entity.setEnergy(energy);
        return entity;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 0.4D));
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
        this.entityData.define(PAINTING, "minecraft:air");
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.entityData.get(STATUS_FLAG) == 2 && this.canStandby()) {
            Vec3 position = this.getPosition(1.0F);
            BlockPos pos = BlockPos.containing(position.x, position.y, position.z);
            this.level().setBlockAndUpdate(pos, MyBlocks.TORCHIKOMA.defaultBlockState());
            if (this.level().getBlockEntity(pos) instanceof TorchikomaBlockEntity blockEntity) {
                blockEntity.initFromEntity(this);
                this.remove(RemovalReason.DISCARDED);
            }
        }

        ItemStack itemStack = this.inventory.getItem(13);
        if (itemStack.getItem() instanceof EnergyCoreItem item) {
            this.addEnergy(item.getRecovery());
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
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

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.7D;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof LivingEntity) {
            return null;
        }
        return (LivingEntity) this.getFirstPassenger();
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    protected double followLeashSpeed() {
        return 0.25D;
    }

    @Override
    protected @Nonnull InteractionResult mobInteract(@Nonnull Player pPlayer, @Nonnull InteractionHand pHand) {
        if (!this.level().isClientSide) {
            if (pPlayer.isSecondaryUseActive()) {
                this.openGUI(pPlayer, null);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else this.doPlayerRide(pPlayer);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    protected void doPlayerRide(Player pPlayer) {
        if (!this.level().isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
    }

    @Override
    public void openGUI(Player pPlayer, ItemStack ignore) {
        NetworkHooks.openScreen((ServerPlayer) pPlayer, this.getMenu(
                this.getDisplayName(),
                (id, inv, player) -> new TorchikomaEntityMenu(id, inv, this)
        ), buffer -> buffer.writeInt(this.getId()));
    }

    @Override
    public void containerChanged(@Nonnull Container pInvBasic) {

    }

    public boolean canStandby() {
        return new BlockPlaceContext(
                this.level(),
                null, InteractionHand.MAIN_HAND,
                new ItemStack(MyItems.TORCHIKOMA),
                new BlockHitResult(
                        this.getDeltaMovement(),
                        Direction.UP,
                        this.getOnPos(),
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
    public void die(@Nonnull DamageSource pCause) {
        super.die(pCause);
        if (this.dead && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        UUID uuid = this.getOwnerUUID();
        return uuid == null ? null : this.level().getPlayerByUUID(uuid);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<TorchikomaEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.setCustomInstructionKeyframeHandler(this::customListener);
        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.torchikoma.walk", LoopType.LOOP));
        } else {
            event.getController().setAnimation(RawAnimation.begin().then("animation.torchikoma.packing", LoopType.PLAY_ONCE));
        }
        if (allowStandSliding) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.torchikoma.beforejump", LoopType.PLAY_ONCE));
        }
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> void customListener(CustomInstructionKeyframeEvent<T> event) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(Component.literal("KeyFraming"), true);
        }
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback)  {
        if (this.hasPassenger(pPassenger)) {
            float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
            pPassenger.setPos(this.getX() + (double) (0.3F * f1), this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset(), this.getZ() - (double) (0.3F * f));
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

    public void updatePainting() {
        if (!this.level().isClientSide) {
            String old = this.entityData.get(PAINTING);
            String now = ForgeRegistries.ITEMS.getKey(inventory.getItem(14).getItem()).toString();
            if (!now.equals(old)) this.entityData.set(PAINTING, now);
        }
    }

    public String getPainting() {
        return this.entityData.get(PAINTING);
    }

    public int getEnergy() {
        return Math.min(this.entityData.get(ENERGY_DATA), 20000);
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public Container getInventory() {
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void travel(@Nonnull Vec3 pTravelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.isControlledByLocalInstance()) {
                LivingEntity livingentity = this.getControllingPassenger();
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

                if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround()) {
                    double d0 = this.getCustomJump() * (double) this.playerJumpPendingScale * (double) this.getBlockJumpFactor();
                    double d1 = d0 + this.getJumpBoostPower();
                    Vec3 vec3 = this.getDeltaMovement();
                    this.setDeltaMovement(vec3.x, d1, vec3.z);
                    this.setIsJumping(true);
                    this.hasImpulse = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
                        float f3 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
                        this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f2 * this.playerJumpPendingScale, 0.0D, 0.4F * f3 * this.playerJumpPendingScale));
                    }

                    this.playerJumpPendingScale = 0.0F;
                }

                if (this.isControlledByLocalInstance()) {
                    this.setSpeed(0.4f);
                    super.travel(new Vec3(f, pTravelVector.y, f1));
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                if (this.onGround()) {
                    this.playerJumpPendingScale = 0.0F;
                    this.setIsJumping(false);
                }

                this.calculateEntityAnimation(false);
                this.tryCheckInsideBlocks();
            } else {
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
        return 0.65D;
    }

    @Override
    public void handleStartJump(int pJumpPower) {
        this.allowStandSliding = true;
    }

    @Override
    public void handleStopJump() {
    }

    static class TorchikomaContainer extends SimpleContainer {
        private final TorchikomaEntity entity;

        private TorchikomaContainer(TorchikomaEntity entity) {
            super(15);
            this.entity = entity;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            this.entity.updatePainting();
        }
    }

}

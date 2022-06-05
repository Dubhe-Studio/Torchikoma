package dev.dubhe.torchikoma.entity;

import dev.dubhe.torchikoma.registry.MyEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TorchEntity extends Projectile {
    /**
     * 1: 是否可燃烧
     * 2: 是否在水中减速
     * 4: 是否是暴击箭
     * 8: 是否有物理碰撞
     */
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.ITEM_STACK);
    @Nullable
    private BlockState lastState;
    protected boolean inGround;
    protected int inGroundTime;
    public Pickup pickup = Pickup.DISALLOWED;
    public int shakeTime;
    private int life;
    private double baseDamage = 2.0D;
    private int knockback;
    @Nullable
    private List<Entity> piercedAndKilledEntities;


    public TorchEntity(EntityType<? extends TorchEntity> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public TorchEntity(LivingEntity entity, ItemStack stack, boolean allowBurn, boolean fastInWater) {
        super(MyEntities.TORCH, entity.level);
        this.setOwner(entity);
        this.entityData.set(ITEM_STACK, stack);
        this.setAllowBurn(allowBurn);
        this.setFastInWater(fastInWater);
        if (entity instanceof Player) {
            this.pickup = Pickup.ALLOWED;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ID_FLAGS, (byte)0);
        this.entityData.define(ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putShort("life", (short)this.life);
        if (this.lastState != null) {
            pCompound.put("inBlockState", NbtUtils.writeBlockState(this.lastState));
        }
        pCompound.putByte("shake", (byte)this.shakeTime);
        pCompound.putBoolean("inGround", this.inGround);
        pCompound.putByte("pickup", (byte)this.pickup.ordinal());
        pCompound.putDouble("damage", this.baseDamage);
        pCompound.putBoolean("crit", this.isCritArrow());
        pCompound.putBoolean("allowBurn", this.allowBurn());
        pCompound.putBoolean("fastInWater", this.fastInWater());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.life = pCompound.getShort("life");
        if (pCompound.contains("inBlockState", 10)) {
            this.lastState = NbtUtils.readBlockState(pCompound.getCompound("inBlockState"));
        }
        this.shakeTime = pCompound.getByte("shake") & 255;
        this.inGround = pCompound.getBoolean("inGround");
        if (pCompound.contains("damage", 99)) {
            this.baseDamage = pCompound.getDouble("damage");
        }
        this.pickup = Pickup.byOrdinal(pCompound.getByte("pickup"));
        this.setCritArrow(pCompound.getBoolean("crit"));
        this.setAllowBurn(pCompound.getBoolean("allowBurn"));
        this.setFastInWater(pCompound.getBoolean("fastInWater"));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void tick() {
        super.tick();
        boolean noPhysics = this.isNoPhysics();
        Vec3 moveResult = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = moveResult.horizontalDistance();
            this.setYRot((float)(Mth.atan2(moveResult.x, moveResult.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(moveResult.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);
        if (!blockstate.isAir() && !noPhysics) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 pos = this.position();
                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(pos)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.shakeTime > 0) --this.shakeTime;
        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW)) this.clearFire();
        if (this.inGround && !noPhysics) {
            if (this.lastState != blockstate && this.shouldFall()) this.startFalling();
            else if (!this.level.isClientSide && ++this.life >= 1200) this.discard();
            ++this.inGroundTime;
        } else {
            this.inGroundTime = 0;
            Vec3 oPos = this.position();
            Vec3 nPos = oPos.add(moveResult);
            HitResult hit = this.level.clip(new ClipContext(oPos, nPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hit.getType() != HitResult.Type.MISS) nPos = hit.getLocation();
            while(!this.isRemoved()) {
                EntityHitResult entityHit = this.findHitEntity(oPos, nPos);
                if (entityHit != null) hit = entityHit;
                if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                    assert hit instanceof EntityHitResult;
                    Entity hitEntity = ((EntityHitResult)hit).getEntity();
                    Entity owner = this.getOwner();
                    if (hitEntity instanceof Player && owner instanceof Player && !((Player)owner).canHarmPlayer((Player)hitEntity)) {
                        hit = null;
                        entityHit = null;
                    }
                }
                if (hit != null && hit.getType() != HitResult.Type.MISS && !noPhysics && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hit)) {
                    this.onHit(hit);
                    this.hasImpulse = true;
                }
                if (entityHit == null) break;
                hit = null;
            }
            moveResult = this.getDeltaMovement();
            double oX = moveResult.x;
            double oY = moveResult.y;
            double oZ = moveResult.z;
            if (this.isCritArrow()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.CRIT, this.getX() + oX * (double)i / 4.0D, this.getY() + oY * (double)i / 4.0D, this.getZ() + oZ * (double)i / 4.0D, -oX, -oY + 0.2D, -oZ);
                }
            }
            double nX = this.getX() + oX;
            double nY = this.getY() + oY;
            double nZ = this.getZ() + oZ;
            double distance = moveResult.horizontalDistance();
            if (noPhysics) this.setYRot((float)(Mth.atan2(-oX, -oZ) * (double)(180F / (float)Math.PI)));
            else this.setYRot((float)(Mth.atan2(oX, oZ) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(oY, distance) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    this.level.addParticle(ParticleTypes.BUBBLE, nX - oX * 0.25D, nY - oY * 0.25D, nZ - oZ * 0.25D, oX, oY, oZ);
                }
                f = this.getWaterInertia();
            }
            this.setDeltaMovement(moveResult.scale(f));
            if (!this.isNoGravity() && !noPhysics) {
                moveResult = this.getDeltaMovement();
                this.setDeltaMovement(moveResult.x, moveResult.y - (double)0.05F, moveResult.z);
            }
            this.setPos(nX, nY, nZ);
            this.checkInsideBlocks();
        }
    }

    private boolean shouldFall() {
        return this.inGround && this.level.noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.multiply(this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F));
        this.life = 0;
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        if (pType != MoverType.SELF && this.shouldFall()) {
            this.startFalling();
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity hitEntity = pResult.getEntity();
        float length = (float)this.getDeltaMovement().length();
        int damage = Mth.ceil(Mth.clamp((double)length * this.baseDamage, 0.0D, 2.147483647E9D));
        if (this.isCritArrow()) {
            long j = this.random.nextInt(damage / 2 + 2);
            damage = (int)Math.min(j + (long)damage, 2147483647L);
        }
        Entity owner = this.getOwner();
        DamageSource damagesource;
        if (owner == null) {
            damagesource = this.getDamageSource(this);
        } else {
            damagesource = this.getDamageSource(owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(hitEntity);
            }
        }
        boolean hitEnderman = hitEntity.getType() == EntityType.ENDERMAN;
        int k = hitEntity.getRemainingFireTicks();
        if (this.isOnFire() && !hitEnderman) hitEntity.setSecondsOnFire(5);
        if (hitEntity.hurt(damagesource, (float)damage)) {
            if (hitEnderman) return;
            if (hitEntity instanceof LivingEntity livingentity) {
                if (this.knockback > 0) {
                    Vec3 pos = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D);
                    if (pos.lengthSqr() > 0.0D) {
                        livingentity.push(pos.x, 0.1D, pos.z);
                    }
                }
                if (!this.level.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingentity);
                }
                if (livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer serverPlayer && !this.isSilent()) {
                    serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!hitEntity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!this.level.isClientSide && owner instanceof ServerPlayer serverplayer) {
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, this.piercedAndKilledEntities);
                    } else if (!hitEntity.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, List.of(hitEntity));
                    }
                }
            }

            this.playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            hitEntity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }
    }

    private DamageSource getDamageSource(@Nullable Entity entity) {
        return (new IndirectEntityDamageSource("arrow", this, entity)).setProjectile();
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        this.lastState = this.level.getBlockState(p_36755_.getBlockPos());
        super.onHitBlock(p_36755_);
        Vec3 vec3 = p_36755_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        this.setCritArrow(false);
        this.setPierceLevel((byte)0);
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        this.setShotFromCrossbow(false);
        this.resetPiercedEntities();
        if (this.getOwner() instanceof Player player && this.getItemStack().getItem() instanceof BlockItem blockItem) {
            InteractionResult interactionResult = blockItem.place(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(blockItem), result));
            if (interactionResult == InteractionResult.CONSUME) this.discard();
        } else this.discard();
    }

    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level, this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_36743_.getId()));
    }

    @Override
    public void setOwner(@Nullable Entity pEntity) {
        super.setOwner(pEntity);
        if (pEntity instanceof Player) {
            this.pickup = ((Player)pEntity).getAbilities().instabuild ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
        }

    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!this.level.isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
            if (this.tryPickup(pEntity)) {
                pEntity.take(this, 1);
                this.discard();
            }

        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 *= 64.0D * getViewScale();
        return pDistance < d0 * d0;
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        this.life = 0;
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.setPos(pX, pY, pZ);
        this.setRot(pYaw, pPitch);
    }

    @Override
    public void lerpMotion(double pX, double pY, double pZ) {
        super.lerpMotion(pX, pY, pZ);
        this.life = 0;
    }

    protected boolean tryPickup(Player p_150121_) {
        return switch (this.pickup) {
            case ALLOWED -> p_150121_.getInventory().add(this.getPickupItem());
            case CREATIVE_ONLY -> p_150121_.getAbilities().instabuild;
            default -> false;
        };
    }

    public void setBaseDamage(double pDamage) {
        this.baseDamage = pDamage;
    }

    public double getBaseDamage() {
        return this.baseDamage;
    }

    public void setKnockback(int pKnockbackStrength) {
        this.knockback = pKnockbackStrength;
    }

    public int getKnockback() {
        return this.knockback;
    }

    public boolean isAttackable() {
        return false;
    }

    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.13F;
    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    public ItemStack getItemStack() {
        return this.entityData.get(ITEM_STACK);
    }

    public boolean allowBurn() {
        byte b0 = this.entityData.get(ID_FLAGS);
        return (b0 & 1) != 0;
    }

    public boolean fastInWater() {
        byte b0 = this.entityData.get(ID_FLAGS);
        return (b0 & 2) != 0;
    }

    public boolean isCritArrow() {
        byte b0 = this.entityData.get(ID_FLAGS);
        return (b0 & 4) != 0;
    }

    public boolean isNoPhysics() {
        if (!this.level.isClientSide) {
            return this.noPhysics;
        } else {
            return (this.entityData.get(ID_FLAGS) & 4) != 0;
        }
    }

    private void setFlag(int p_36738_, boolean p_36739_) {
        byte b0 = this.entityData.get(ID_FLAGS);
        if (p_36739_) {
            this.entityData.set(ID_FLAGS, (byte)(b0 | p_36738_));
        } else {
            this.entityData.set(ID_FLAGS, (byte)(b0 & ~p_36738_));
        }

    }

    public void setAllowBurn(boolean pCritical) {
        this.setFlag(1, pCritical);
    }

    public void setFastInWater(boolean pCritical) {
        this.setFlag(2, pCritical);
    }

    public void setCritArrow(boolean pCritical) {
        this.setFlag(4, pCritical);
    }

    public void setNoPhysics(boolean pNoClip) {
        this.noPhysics = pNoClip;
        this.setFlag(8, pNoClip);
    }

    public enum Pickup {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static Pickup byOrdinal(int pOrdinal) {
            if (pOrdinal < 0 || pOrdinal > values().length) {
                pOrdinal = 0;
            }

            return values()[pOrdinal];
        }
    }

}

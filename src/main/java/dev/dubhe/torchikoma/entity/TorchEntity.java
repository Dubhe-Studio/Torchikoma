package dev.dubhe.torchikoma.entity;

import dev.dubhe.torchikoma.registry.MyEntities;
import dev.dubhe.torchikoma.registry.MyItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import java.util.HashMap;
import java.util.Map;

public class TorchEntity extends Projectile {
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final TorchData DEFAULT_DATA = new TorchData(3, true, true);
    private static final Map<Item, TorchData> TORCH = new HashMap<>();
    @Nullable
    private BlockState lastState;
    protected boolean inGround;
    protected int inGroundTime;
    public Pickup pickup = Pickup.DISALLOWED;
    public int shakeTime;
    private int life;
    private double baseDamage;

    public TorchEntity(EntityType<? extends TorchEntity> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    public TorchEntity(LivingEntity entity, ItemStack stack) {
        super(MyEntities.TORCH, entity.level);
        this.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
        this.setOwner(entity);
        this.entityData.set(ITEM_STACK, stack);
        TorchData data = TORCH.getOrDefault(stack.getItem(), DEFAULT_DATA);
        this.baseDamage = data.damage();
        this.setAllowBurn(data.allowBurn());
        this.setSlowInWater(data.slowInWater());
        if (entity instanceof Player) this.pickup = Pickup.ALLOWED;
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
        pCompound.putBoolean("allowBurn", this.allowBurn());
        pCompound.putBoolean("slowInWater", this.slowInWater());
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
        this.setAllowBurn(pCompound.getBoolean("allowBurn"));
        this.setSlowInWater(pCompound.getBoolean("slowInWater"));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void tick() {
        super.tick();
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
        if (!blockstate.isAir()) {
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
        if (this.inGround) {
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
                if (hit != null && hit.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hit)) {
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
            double nX = this.getX() + oX;
            double nY = this.getY() + oY;
            double nZ = this.getZ() + oZ;
            double distance = moveResult.horizontalDistance();
            this.setYRot((float)(Mth.atan2(oX, oZ) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(oY, distance) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    this.level.addParticle(ParticleTypes.BUBBLE, nX - oX * 0.25D, nY - oY * 0.25D, nZ - oZ * 0.25D, oX, oY, oZ);
                }
                if (this.slowInWater()) f = 0.6F;
            }
            this.setDeltaMovement(moveResult.scale(f));
            if (!this.isNoGravity()) {
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
        System.out.println(this.baseDamage);
        System.out.println(this.getDeltaMovement());
        System.out.println(length);
        System.out.println(damage);
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
        int fireTicks = hitEntity.getRemainingFireTicks();
        if (this.isOnFire() && !hitEnderman) hitEntity.setSecondsOnFire(5);
        if (hitEntity.hurt(damagesource, (float)damage)) {
            if (hitEnderman) return;
            if (hitEntity instanceof LivingEntity livingentity) {
                if (!this.level.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingentity);
                }
                if (livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer serverPlayer && !this.isSilent()) {
                    serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
            this.playSound(SoundEvents.ARROW_HIT, 0.2F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        } else {
            hitEntity.setRemainingFireTicks(fireTicks);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            double sqr = this.getDeltaMovement().lengthSqr();
            if (this.level.isClientSide) {
                if (sqr == 0.0D) this.discard();
            } else if (sqr < 1.0E-7D) {
                if (this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getItemStack(), 0.1F);
                }
                this.discard();
            }
        }
    }

    @Override
    public void setSecondsOnFire(int pSeconds) {
        if (this.allowBurn()) super.setSecondsOnFire(pSeconds);
    }

    @Override
    public void setRemainingFireTicks(int pTicks) {
        if (this.allowBurn()) super.setRemainingFireTicks(pTicks);
    }

    private DamageSource getDamageSource(@Nullable Entity entity) {
        return (new IndirectEntityDamageSource("torch", this, entity)).setProjectile();
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        this.lastState = this.level.getBlockState(result.getBlockPos());
        super.onHitBlock(result);
        Vec3 offset = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(offset);
        offset = offset.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - offset.x, this.getY() - offset.y, this.getZ() - offset.z);
        if (
                this.getOwner() instanceof Player player &&
                this.getItemStack().getItem() instanceof BlockItem blockItem &&
                blockItem.place(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(blockItem), result)) == InteractionResult.CONSUME
        ) this.discard();
        this.playSound(SoundEvents.WOOD_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
    }

    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level, this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    @Override
    public void setOwner(@Nullable Entity pEntity) {
        super.setOwner(pEntity);
        if (pEntity instanceof Player player) {
            this.pickup = player.getAbilities().instabuild ? Pickup.CREATIVE_ONLY : Pickup.ALLOWED;
        }
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!this.level.isClientSide && this.inGround && this.shakeTime <= 0 && this.tryPickup(pEntity)) {
            if (!this.isRemoved()) ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundTakeItemEntityPacket(this.getId(), pEntity.getId(), 1));
            this.discard();
        }
    }

    protected boolean tryPickup(Player p_150121_) {
        return switch (this.pickup) {
            case ALLOWED -> p_150121_.getInventory().add(this.getItemStack());
            case CREATIVE_ONLY -> p_150121_.getAbilities().instabuild;
            default -> false;
        };
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double size = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(size)) size = 1.0D;
        size *= 64.0D * getViewScale();
        return pDistance < size * size;
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

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.13F;
    }

    public ItemStack getItemStack() {
        return this.entityData.get(ITEM_STACK);
    }

    public boolean allowBurn() {
        byte b0 = this.entityData.get(ID_FLAGS);
        return (b0 & 1) != 0;
    }

    public boolean slowInWater() {
        byte b0 = this.entityData.get(ID_FLAGS);
        return (b0 & 2) != 0;
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

    public void setSlowInWater(boolean pCritical) {
        this.setFlag(2, pCritical);
    }

    static {
        TORCH.put(Items.SOUL_TORCH, new TorchData(5, true, true));
        TORCH.put(Items.REDSTONE_TORCH, new TorchData(7, false, true));
        TORCH.put(MyItems.GLOWSTONE_TORCH, new TorchData(7, false, true));
        TORCH.put(MyItems.PRISMARINE_TORCH, new TorchData(7, false, false));
    }

    private record TorchData(double damage, boolean allowBurn, boolean slowInWater) { }

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

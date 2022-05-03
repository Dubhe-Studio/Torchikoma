package dev.dubhe.torchikoma.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ClayExplosives extends DirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape Y_AXIS_AABB = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 3.0D, 12.0D);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 3.0D);
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 4.0D, 4.0D, 3.0D, 12.0D, 12.0D);
    protected static final VoxelShape BY_AXIS_AABB = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape BZ_AXIS_AABB = Block.box(4.0D, 4.0D, 13.0D, 12.0D, 12.0D, 16.0D);
    protected static final VoxelShape BX_AXIS_AABB = Block.box(13.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);

    public ClayExplosives(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace()).setValue(WATERLOGGED, flag);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState p_154346_, @NotNull BlockGetter p_154347_, @NotNull BlockPos p_154348_, @NotNull CollisionContext p_154349_) {
        return switch (p_154346_.getValue(FACING)) {
            case EAST -> X_AXIS_AABB;
            case WEST -> BX_AXIS_AABB;
            case SOUTH -> Z_AXIS_AABB;
            case NORTH -> BZ_AXIS_AABB;
            case UP -> Y_AXIS_AABB;
            case DOWN -> BY_AXIS_AABB;
        };
    }

    @NotNull
    @Override
    public BlockState rotate(BlockState p_154354_, Rotation p_154355_) {
        return p_154354_.setValue(FACING, p_154355_.rotate(p_154354_.getValue(FACING)));
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState p_154341_, @NotNull BlockGetter p_154342_, @NotNull BlockPos p_154343_, @NotNull PathComputationType p_154344_) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED);
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        } else {
            onCaughtFire(pState, pLevel, pPos, pHit.getDirection(), pPlayer);
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemstack.getItem();
            if (!pPlayer.isCreative()) {
                if (itemstack.is(Items.FLINT_AND_STEEL)) {
                    itemstack.hurtAndBreak(1, pPlayer, (p_57425_) -> p_57425_.broadcastBreakEvent(pHand));
                } else {
                    itemstack.shrink(1);
                }
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
    }

    private static void explode(Level pLevel, BlockPos pPos, @Nullable LivingEntity pEntity) {
        pLevel.gameEvent(GameEvent.EXPLODE, pPos);
        if (!pLevel.isClientSide) {
            pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
            BlockPos behindBlock = behindBlock(pLevel, pPos);
            pLevel.destroyBlock(pPos, false);
            BlockState blockstate = pLevel.getBlockState(behindBlock);
            if (!blockstate.isAir()) {
                if (blockstate.getBlock().getExplosionResistance() < 9 && !(blockstate.hasProperty(WATERLOGGED) && blockstate.getValue(WATERLOGGED))) {
                    pLevel.destroyBlock(behindBlock, true);
                }
            }
        }
    }

    private static BlockPos behindBlock(Level pLevel, BlockPos pPos) {
        return pPos.relative(pLevel.getBlockState(pPos).getValue(FACING).getOpposite());
    }

    @Override
    public void onProjectileHit(Level pLevel, @NotNull BlockState pState, @NotNull BlockHitResult pHit, @NotNull Projectile pProjectile) {
        if (!pLevel.isClientSide) {
            BlockPos blockpos = pHit.getBlockPos();
            Entity entity = pProjectile.getOwner();
            if (pProjectile.isOnFire() && pProjectile.mayInteract(pLevel, blockpos)) {
                onCaughtFire(pState, pLevel, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                pLevel.removeBlock(blockpos, false);
            }
        }

    }
}

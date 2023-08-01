package dev.dubhe.torchikoma.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ComponentBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public final int damage;
    protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.99D, 16.0D);

    public ComponentBlock(Properties pProperties,int pDamage) {
        super(pProperties);
        this.damage = pDamage;
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockstate = this.defaultBlockState();
        if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
            return blockstate.setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos())).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(FACING, context.getHorizontalDirection().getOpposite());
        }
        return blockstate.setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, LIT);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState pState, @Nonnull BlockGetter pLevel, @Nonnull BlockPos pPos, @Nonnull CollisionContext pContext) {
        return AABB;
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState pState, @Nonnull Direction pFacing, @Nonnull BlockState pFacingState, @Nonnull LevelAccessor pLevel, @Nonnull BlockPos pCurrentPos, @Nonnull BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        return pFacing == Direction.DOWN && !this.canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState pState, @Nonnull LevelReader pLevel, @Nonnull BlockPos pPos) {
        return true;
    }

    @Override
    public void animateTick(BlockState pState, @Nonnull Level pLevel, @Nonnull BlockPos pPos, @Nonnull RandomSource pRand) {
        if (pState.getValue(LIT) && !pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.cycle(LIT), 2);
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState pState, Level pLevel, @Nonnull BlockPos pPos, @Nonnull Block pBlock, @Nonnull BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean flag = pState.getValue(LIT);
            if (flag != pLevel.hasNeighborSignal(pPos)) {
                if (flag) {
                    pLevel.scheduleTick(pPos, this, 4);
                } else {
                    pLevel.setBlock(pPos, pState.cycle(LIT), 2);
                }
            }
        }
    }

    @Override
    public void stepOn(@Nonnull Level pLevel, @Nonnull BlockPos pPos,BlockState pState, @Nonnull Entity pEntity) {
        if (pState.getValue(LIT) && pEntity instanceof LivingEntity) {
            pEntity.hurt(new DamageSources(pLevel.registryAccess()).generic(), (float)this.damage);
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}

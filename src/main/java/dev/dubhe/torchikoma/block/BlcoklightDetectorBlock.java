package dev.dubhe.torchikoma.block;

import dev.dubhe.torchikoma.block.entity.BlocklightDetectorBlockEntity;
import dev.dubhe.torchikoma.registry.MyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlcoklightDetectorBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public BlcoklightDetectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0));
    }
    @Override
    public @Nonnull VoxelShape getShape(@Nonnull BlockState pState, @Nonnull BlockGetter pLevel, @Nonnull BlockPos pPos, @Nonnull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(@Nonnull BlockState pState) {
        return false;
    }

    @Override
    public @Nonnull RenderShape getRenderShape(@Nonnull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pPos, @Nonnull BlockState pState) {
        return new BlocklightDetectorBlockEntity(pPos, pState);
    }

    @Override
    public int getSignal(BlockState pBlockState, @Nonnull BlockGetter pBlockAccess, @Nonnull BlockPos pPos, @Nonnull Direction pSide) {
        return pBlockState.getValue(POWER);
    }

    private static void updateSignalStrength(BlockState pState, Level pLevel, BlockPos pPos) {
        int i = pLevel.getBrightness(LightLayer.BLOCK, pPos);
        if (pState.getValue(POWER) != i) {
            pLevel.setBlock(pPos, pState.setValue(POWER, i), 3);
        }
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState pState) {
        return true;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, MyBlockEntities.BLOCKLIGHT_DETECTOR, BlcoklightDetectorBlock::tickEntity) : null;
    }

    public static <E extends BlockEntity> void tickEntity(Level level, BlockPos blockPos, BlockState blockState, E e) {
        updateSignalStrength(blockState,level,blockPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWER);
    }
}

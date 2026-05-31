package signplates.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import signplates.PlateTint;

public class PlateBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty GLOW = BooleanProperty.create("glow");
    public static final EnumProperty<PlateTint> TINT = EnumProperty.create("tint", PlateTint.class);

    private static final VoxelShape FLOOR_SHAPE = Block.box(0, 0, 0, 16, 1, 16);
    private static final VoxelShape CEILING_SHAPE = Block.box(0, 15, 0, 16, 16, 16);
    private static final VoxelShape NORTH_WALL_SHAPE = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape SOUTH_WALL_SHAPE = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape EAST_WALL_SHAPE = Block.box(0, 0, 0, 1, 16, 16);
    private static final VoxelShape WEST_WALL_SHAPE = Block.box(15, 0, 0, 16, 16, 16);

    public PlateBlock(Properties properties) {
        super(properties.lightLevel(state -> state.getValue(GLOW) ? 13 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false).setValue(GLOW, false).setValue(TINT, PlateTint.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, WATERLOGGED, GLOW, TINT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState state;
            if (direction.getAxis() == Direction.Axis.Y) {
                state = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection().getOpposite());
            } else {
                state = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
            }
            state = state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
            if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof DyeItem dyeItem) {
            PlateTint tint = PlateTint.fromDyeColor(dyeItem.getDyeColor());
            if (state.getValue(TINT) != tint) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state.setValue(TINT, tint), 3);
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        if (stack.is(Items.GLOW_INK_SAC) && !state.getValue(GLOW)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(GLOW, true), 3);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return state.canSurvive(level, pos) ? state : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACE) == AttachFace.FLOOR) return FLOOR_SHAPE;
        if (state.getValue(FACE) == AttachFace.CEILING) return CEILING_SHAPE;
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_WALL_SHAPE;
            case SOUTH -> SOUTH_WALL_SHAPE;
            case EAST -> EAST_WALL_SHAPE;
            case WEST -> WEST_WALL_SHAPE;
            default -> Shapes.block();
        };
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}

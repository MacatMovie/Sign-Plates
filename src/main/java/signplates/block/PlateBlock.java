package signplates.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import signplates.PlateTint;

public class PlateBlock extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<PlateBlock> CODEC = simpleCodec(PlateBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty GLOW = BooleanProperty.create("glow");
    public static final EnumProperty<PlateTint> TINT = EnumProperty.create("tint", PlateTint.class);

    public PlateBlock() {
        this(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(0.5f, 6f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
    }

    public PlateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(FACE, AttachFace.WALL)
            .setValue(WATERLOGGED, false)
            .setValue(GLOW, false)
            .setValue(TINT, PlateTint.NONE));
    }

    @Override
    protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, WATERLOGGED, GLOW, TINT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> switch (state.getValue(FACE)) {
                case FLOOR -> box(1, 0, 1, 15, 1, 15);
                case WALL -> box(1, 1, 0, 15, 15, 1);
                case CEILING -> box(1, 15, 1, 15, 16, 15);
            };
            case NORTH -> switch (state.getValue(FACE)) {
                case FLOOR -> box(1, 0, 1, 15, 1, 15);
                case WALL -> box(1, 1, 15, 15, 15, 16);
                case CEILING -> box(1, 15, 1, 15, 16, 15);
            };
            case EAST -> switch (state.getValue(FACE)) {
                case FLOOR -> box(1, 0, 1, 15, 1, 15);
                case WALL -> box(0, 1, 1, 1, 15, 15);
                case CEILING -> box(1, 15, 1, 15, 16, 15);
            };
            case WEST -> switch (state.getValue(FACE)) {
                case FLOOR -> box(1, 0, 1, 15, 1, 15);
                case WALL -> box(15, 1, 1, 16, 15, 15);
                case CEILING -> box(1, 15, 1, 15, 16, 15);
            };
        };
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }

    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        if (context.getClickedFace().getAxis() == Direction.Axis.Y) {
            return this.defaultBlockState()
                .setValue(FACE, context.getClickedFace().getOpposite() == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                .setValue(FACING, context.getClickedFace() == Direction.DOWN ? context.getHorizontalDirection() : context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }
        return this.defaultBlockState()
            .setValue(FACE, AttachFace.WALL)
            .setValue(FACING, context.getClickedFace())
            .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTickAccess, currentPos, direction, neighborPos, neighborState, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.GLOW_INK_SAC)) {
            if (!state.getValue(GLOW)) {
                if (!level.isClientSide()) {
                    level.setBlock(pos, state.setValue(GLOW, true), 3);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        if (stack.is(Items.INK_SAC)) {
            if (state.getValue(GLOW)) {
                if (!level.isClientSide()) {
                    level.setBlock(pos, state.setValue(GLOW, false), 3);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        DyeColor dyeColor = getDyeColor(stack);
        if (dyeColor != null) {
            PlateTint tint = PlateTint.fromDyeColor(dyeColor);
            if (state.getValue(TINT) != tint) {
                if (!level.isClientSide()) {
                    level.setBlock(pos, state.setValue(TINT, tint), 3);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
    private static DyeColor getDyeColor(ItemStack stack) {
        if (stack.is(Items.WHITE_DYE)) return DyeColor.WHITE;
        if (stack.is(Items.ORANGE_DYE)) return DyeColor.ORANGE;
        if (stack.is(Items.MAGENTA_DYE)) return DyeColor.MAGENTA;
        if (stack.is(Items.LIGHT_BLUE_DYE)) return DyeColor.LIGHT_BLUE;
        if (stack.is(Items.YELLOW_DYE)) return DyeColor.YELLOW;
        if (stack.is(Items.LIME_DYE)) return DyeColor.LIME;
        if (stack.is(Items.PINK_DYE)) return DyeColor.PINK;
        if (stack.is(Items.GRAY_DYE)) return DyeColor.GRAY;
        if (stack.is(Items.LIGHT_GRAY_DYE)) return DyeColor.LIGHT_GRAY;
        if (stack.is(Items.CYAN_DYE)) return DyeColor.CYAN;
        if (stack.is(Items.PURPLE_DYE)) return DyeColor.PURPLE;
        if (stack.is(Items.BLUE_DYE)) return DyeColor.BLUE;
        if (stack.is(Items.BROWN_DYE)) return DyeColor.BROWN;
        if (stack.is(Items.GREEN_DYE)) return DyeColor.GREEN;
        if (stack.is(Items.RED_DYE)) return DyeColor.RED;
        if (stack.is(Items.BLACK_DYE)) return DyeColor.BLACK;
        return null;
    }
}

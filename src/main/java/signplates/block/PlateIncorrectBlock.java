package signplates.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class PlateIncorrectBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = FaceAttachedHorizontalDirectionalBlock.FACE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PlateIncorrectBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.METAL).strength(0.5f, 6f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false));
	}


	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, FACE, WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		if (context.getClickedFace().getAxis() == Direction.Axis.Y) {
			AttachFace attachFace = context.getClickedFace() == Direction.DOWN ? AttachFace.CEILING : AttachFace.FLOOR;
			Direction facing = attachFace == AttachFace.FLOOR ? context.getHorizontalDirection().getOpposite() : context.getHorizontalDirection();
			return super.getStateForPlacement(context).setValue(FACE, attachFace).setValue(FACING, facing).setValue(WATERLOGGED, flag);
		}
		return super.getStateForPlacement(context).setValue(FACE, AttachFace.WALL).setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, flag);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, world, scheduledTickAccess, currentPos, facing, facingPos, facingState, random);
	}
}
package signplates.block;

import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.fluid.Fluids;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.StateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.block.Block;
import net.minecraft.world.IWorld;
import net.minecraft.world.IBlockReader;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class PlateABlock extends Block implements IWaterLoggable {
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = HorizontalFaceBlock.FACE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PlateABlock() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(0.5f, 6f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public int getLightBlock(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		Direction facing = state.getValue(FACING);
		AttachFace face = state.getValue(FACE);

		if (facing == Direction.EAST) {
			switch (face) {
				case FLOOR:
					return box(1, 0, 1, 15, 1, 15);
				case WALL:
					return box(0, 1, 1, 1, 15, 15);
				case CEILING:
				default:
					return box(1, 15, 1, 15, 16, 15);
			}
		}

		if (facing == Direction.WEST) {
			switch (face) {
				case FLOOR:
					return box(1, 0, 1, 15, 1, 15);
				case WALL:
					return box(15, 1, 1, 16, 15, 15);
				case CEILING:
				default:
					return box(1, 15, 1, 15, 16, 15);
			}
		}

		if (facing == Direction.NORTH) {
			switch (face) {
				case FLOOR:
					return box(1, 0, 1, 15, 1, 15);
				case WALL:
					return box(1, 1, 15, 15, 15, 16);
				case CEILING:
				default:
					return box(1, 15, 1, 15, 16, 15);
			}
		}

		switch (face) {
			case FLOOR:
				return box(1, 0, 1, 15, 1, 15);
			case WALL:
				return box(1, 1, 0, 15, 15, 1);
			case CEILING:
			default:
				return box(1, 15, 1, 15, 16, 15);
		}
	}


	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, FACE, WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
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
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}
}
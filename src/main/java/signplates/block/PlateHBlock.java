package signplates.block;

import signplates.PlateTint;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlateHBlock extends Block implements SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = FaceAttachedHorizontalDirectionalBlock.FACE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<PlateTint> TINT = EnumProperty.create("tint", PlateTint.class);
	public static final BooleanProperty GLOW = BooleanProperty.create("glow");

	public PlateHBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(0.5f, 6f).noOcclusion().lightLevel(state -> state.getValue(GLOW) ? 13 : 0).isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, false).setValue(TINT, PlateTint.NONE).setValue(GLOW, false));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
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
		builder.add(FACING, FACE, WATERLOGGED, TINT, GLOW);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(Items.GLOW_INK_SAC)) {
			if (!state.getValue(GLOW)) {
				if (!level.isClientSide()) {
					level.setBlock(pos, state.setValue(GLOW, true), 3);
					if (!player.getAbilities().instabuild) itemstack.shrink(1);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
			return InteractionResult.CONSUME;
		}
		if (itemstack.is(Items.INK_SAC)) {
			if (state.getValue(GLOW)) {
				if (!level.isClientSide()) {
					level.setBlock(pos, state.setValue(GLOW, false), 3);
					if (!player.getAbilities().instabuild) itemstack.shrink(1);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
			return InteractionResult.CONSUME;
		}
		if (itemstack.getItem() instanceof DyeItem dyeItem) {
			PlateTint newTint = PlateTint.fromDyeColor(dyeItem.getDyeColor());
			if (state.getValue(TINT) != newTint) {
				if (!level.isClientSide()) {
					level.setBlock(pos, state.setValue(TINT, newTint), 3);
					if (!player.getAbilities().instabuild) itemstack.shrink(1);
				}
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return super.use(state, level, pos, player, hand, hitResult);
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
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}
}
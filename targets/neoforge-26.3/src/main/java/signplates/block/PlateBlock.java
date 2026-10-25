package signplates.block;

import signplates.PlateTint;
import net.minecraft.world.InteractionResult;

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

public class PlateBlock extends Block implements SimpleWaterloggedBlock, net.minecraft.world.level.block.EntityBlock {
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = FaceAttachedHorizontalDirectionalBlock.FACE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final net.minecraft.world.level.block.state.properties.IntegerProperty LIGHT=net.minecraft.world.level.block.state.properties.IntegerProperty.create("light",0,signplates.PlateAppearance.MAX_LIGHT);
	public static final BooleanProperty GLOW = BooleanProperty.create("glow");

    public PlateBlock(BlockBehaviour.Properties properties) {
        // Emissive symbol pixels and actual block light are independent settings.
        super(properties.sound(SoundType.METAL).strength(0.5f,6f).noOcclusion().lightLevel(state->state.getValue(LIGHT)).isRedstoneConductor((s,w,p)->false));
        registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(FACE,AttachFace.WALL).setValue(WATERLOGGED,false).setValue(GLOW,false).setValue(LIGHT,0));
    }
    @Override public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos,BlockState state) {return new PlateBlockEntity(pos,state);}
    @Override public ItemStack getCloneItemStack(net.minecraft.world.level.LevelReader level,BlockPos pos,BlockState state,boolean includeData) {
        return signplates.PlateStyle.stack(level.getBlockEntity(pos) instanceof PlateBlockEntity plate?plate.appearance():signplates.PlateAppearance.DEFAULT);
    }

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public int getLightDampening(BlockState state) {
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
		builder.add(FACING, FACE, WATERLOGGED, GLOW, LIGHT);
	}

    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,net.minecraft.world.phys.BlockHitResult hit) {
        if(!player.mayBuild() || !level.mayInteract(player,pos)) return InteractionResult.FAIL;
        if(!(level.getBlockEntity(pos) instanceof PlateBlockEntity plate)) return InteractionResult.FAIL;
        var a=plate.appearance();
        boolean sponge=stack.is(Items.SPONGE)||stack.is(Items.WET_SPONGE);
        PlateTint tint=PlateTint.fromItem(stack);
        if(tint==PlateTint.NONE && stack.has(net.minecraft.core.component.DataComponents.DYE))tint=PlateTint.fromDyeColor(stack.get(net.minecraft.core.component.DataComponents.DYE));
        if(!sponge && !stack.is(Items.GLOW_INK_SAC) && tint==PlateTint.NONE) return useWithoutItem(state,level,pos,player,hit);
        var next=new signplates.PlateAppearance(a.symbol(),a.frame(),a.background(),sponge?PlateTint.NONE:tint==PlateTint.NONE?a.tint():tint,sponge?false:stack.is(Items.GLOW_INK_SAC)||a.glow(),sponge?0:a.light());
        if(a.equals(next))return InteractionResult.CONSUME;
        if(!level.isClientSide() && plate.update(next)) {
            if(!sponge && !player.getAbilities().instabuild) stack.shrink(1);
            if(sponge && level instanceof net.minecraft.server.level.ServerLevel server) {
                var point=hit.getLocation().add(hit.getDirection().getUnitVec3().scale(0.04));
                server.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH,point.x,point.y,point.z,8,0.12,0.12,0.12,0.02);
                level.playSound(null,pos,net.minecraft.sounds.SoundEvents.GENERIC_SPLASH,net.minecraft.sounds.SoundSource.BLOCKS,0.45f,1.2f);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
            Player player, net.minecraft.world.phys.BlockHitResult hit) {
        if (player.isShiftKeyDown()) return net.minecraft.world.InteractionResult.PASS;
        if (!player.mayBuild() || !level.mayInteract(player, pos)) return net.minecraft.world.InteractionResult.FAIL;
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)
            signplates.menu.PlateEditorMenu.openBlock(serverPlayer, pos, hit.getDirection());
        return net.minecraft.world.InteractionResult.SUCCESS;
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
	public BlockState updateShape(BlockState state, net.minecraft.world.level.LevelReader level,
            net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction,
            BlockPos neighborPos,BlockState neighbor,net.minecraft.util.RandomSource random) {
        if(state.getValue(WATERLOGGED)) ticks.scheduleTick(pos,Fluids.WATER,Fluids.WATER.getTickDelay(level));
        return super.updateShape(state,level,ticks,pos,direction,neighborPos,neighbor,random);
    }
}

package signplates.block;

import signplates.PlateTint;
import net.minecraft.world.InteractionResult;

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

public class PlateBlock extends Block implements SimpleWaterloggedBlock, net.minecraft.world.level.block.EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = FaceAttachedHorizontalDirectionalBlock.FACE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final net.minecraft.world.level.block.state.properties.IntegerProperty LIGHT=net.minecraft.world.level.block.state.properties.IntegerProperty.create("light",0,signplates.PlateAppearance.MAX_LIGHT);
	public static final BooleanProperty GLOW = BooleanProperty.create("glow");
 public static final BooleanProperty WAXED = BooleanProperty.create("waxed");

    public PlateBlock() {
        // Emissive symbol pixels and actual block light are independent settings.
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(0.5f,6f).noOcclusion().lightLevel(state->state.getValue(LIGHT)).isRedstoneConductor((s,w,p)->false));
        registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(FACE,AttachFace.WALL).setValue(WATERLOGGED,false).setValue(WAXED,false).setValue(GLOW,false).setValue(LIGHT,0));
    }
    @Override public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos,BlockState state) {return new PlateBlockEntity(pos,state);}
    @Override public ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level,BlockPos pos,BlockState state) {
        ItemStack result=signplates.PlateStyle.stack(level.getBlockEntity(pos) instanceof PlateBlockEntity plate?plate.appearance():signplates.PlateAppearance.DEFAULT);
        signplates.PlateWax.set(result,state.getValue(WAXED));
        return result;
    }


    @Override public java.util.List<ItemStack> getDrops(BlockState state,net.minecraft.world.level.storage.loot.LootParams.Builder params) {
        var tool=params.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
        if(tool!=null && params.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY) instanceof PlateBlockEntity plate) {
            
            if(net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH,tool)>0) {
                ItemStack drop=signplates.PlateStyle.stack(plate.appearance());
                signplates.PlateWax.set(drop,state.getValue(WAXED));
                return java.util.List.of(drop);
            }
        }
        return super.getDrops(state,params);
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
		builder.add(FACING, FACE, WATERLOGGED, WAXED, GLOW, LIGHT);
	}

    @Override public InteractionResult use(BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,net.minecraft.world.phys.BlockHitResult hit) {
        ItemStack stack=player.getItemInHand(hand);
        if(!player.mayBuild() || !level.mayInteract(player,pos)) return InteractionResult.FAIL;
        if(!(level.getBlockEntity(pos) instanceof PlateBlockEntity plate)) return InteractionResult.FAIL;
        if(stack.is(Items.HONEYCOMB) && !state.getValue(WAXED)) {
            if(!level.isClientSide()) {
                level.setBlock(pos,state.setValue(WAXED,true),3);
                level.playSound(null,pos,net.minecraft.sounds.SoundEvents.HONEYCOMB_WAX_ON,net.minecraft.sounds.SoundSource.BLOCKS,1f,1f);
                if(!player.getAbilities().instabuild)stack.shrink(1);
                if(level instanceof net.minecraft.server.level.ServerLevel server)server.sendParticles(net.minecraft.core.particles.ParticleTypes.WAX_ON,pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5,5,.25,.25,.25,0);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        if(stack.is(net.minecraft.tags.ItemTags.AXES) && state.getValue(WAXED)) {
            if(!level.isClientSide()) {
                level.setBlock(pos,state.setValue(WAXED,false),3);
                level.playSound(null,pos,net.minecraft.sounds.SoundEvents.AXE_WAX_OFF,net.minecraft.sounds.SoundSource.BLOCKS,1f,1f);
                stack.hurtAndBreak(1,player,p->p.broadcastBreakEvent(hand));
                if(level instanceof net.minecraft.server.level.ServerLevel server)server.sendParticles(net.minecraft.core.particles.ParticleTypes.WAX_OFF,pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5,5,.25,.25,.25,0);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        if(state.getValue(WAXED))return InteractionResult.CONSUME;
        var a=plate.appearance();
        boolean sponge=stack.is(Items.SPONGE)||stack.is(Items.WET_SPONGE);
        PlateTint tint=PlateTint.fromItem(stack);
        if(tint==PlateTint.NONE && stack.getItem() instanceof DyeItem dye)tint=PlateTint.fromDyeColor(dye.getDyeColor());
        if(!sponge && !stack.is(Items.GLOW_INK_SAC) && tint==PlateTint.NONE) return useWithoutItem(state,level,pos,player,hit);
        var next=new signplates.PlateAppearance(a.symbol(),a.frame(),a.background(),sponge?PlateTint.NONE:tint==PlateTint.NONE?a.tint():tint,sponge?false:stack.is(Items.GLOW_INK_SAC)||a.glow(),sponge?0:a.light());
        if(a.equals(next))return InteractionResult.CONSUME;
        if(!level.isClientSide() && plate.update(next)) {
            if(!sponge && !player.getAbilities().instabuild) stack.shrink(1);
            if(sponge && level instanceof net.minecraft.server.level.ServerLevel server) {
                var point=hit.getLocation().add(net.minecraft.world.phys.Vec3.atLowerCornerOf(hit.getDirection().getNormal()).scale(0.04));
                server.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH,point.x,point.y,point.z,8,0.12,0.12,0.12,0.02);
                level.playSound(null,pos,net.minecraft.sounds.SoundEvents.GENERIC_SPLASH,net.minecraft.sounds.SoundSource.BLOCKS,0.45f,1.2f);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
            Player player, net.minecraft.world.phys.BlockHitResult hit) {
        if(state.getValue(WAXED))return InteractionResult.CONSUME;
        if (player.isShiftKeyDown()) return net.minecraft.world.InteractionResult.PASS;
        if (!player.mayBuild() || !level.mayInteract(player, pos)) return net.minecraft.world.InteractionResult.FAIL;
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)
            signplates.menu.PlateEditorMenu.openBlock(serverPlayer, pos, hit.getDirection());
        return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide());
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

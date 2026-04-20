package signplates.init;

import signplates.block.*;
import signplates.SignPlatesMod;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
public class SignPlatesModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(SignPlatesMod.MODID);

	public static final DeferredBlock<Block> PLATE_A = REGISTRY.register("plate_a", id -> new PlateABlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_B = REGISTRY.register("plate_b", id -> new PlateBBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_C = REGISTRY.register("plate_c", id -> new PlateCBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_D = REGISTRY.register("plate_d", id -> new PlateDBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_E = REGISTRY.register("plate_e", id -> new PlateEBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_F = REGISTRY.register("plate_f", id -> new PlateFBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_G = REGISTRY.register("plate_g", id -> new PlateGBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_H = REGISTRY.register("plate_h", id -> new PlateHBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_I = REGISTRY.register("plate_i", id -> new PlateIBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_J = REGISTRY.register("plate_j", id -> new PlateJBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_K = REGISTRY.register("plate_k", id -> new PlateKBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_L = REGISTRY.register("plate_l", id -> new PlateLBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_M = REGISTRY.register("plate_m", id -> new PlateMBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_N = REGISTRY.register("plate_n", id -> new PlateNBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_O = REGISTRY.register("plate_o", id -> new PlateOBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_P = REGISTRY.register("plate_p", id -> new PlatePBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_R = REGISTRY.register("plate_r", id -> new PlateRBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_S = REGISTRY.register("plate_s", id -> new PlateSBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_T = REGISTRY.register("plate_t", id -> new PlateTBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_Q = REGISTRY.register("plate_q", id -> new PlateQBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_U = REGISTRY.register("plate_u", id -> new PlateUBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_V = REGISTRY.register("plate_v", id -> new PlateVBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_W = REGISTRY.register("plate_w", id -> new PlateWBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_X = REGISTRY.register("plate_x", id -> new PlateXBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_Y = REGISTRY.register("plate_y", id -> new PlateYBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_Z = REGISTRY.register("plate_z", id -> new PlateZBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_EMPTY = REGISTRY.register("plate_empty", id -> new PlateEmptyBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_0 = REGISTRY.register("plate_0", id -> new Plate0Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_1 = REGISTRY.register("plate_1", id -> new Plate1Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_2 = REGISTRY.register("plate_2", id -> new Plate2Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_3 = REGISTRY.register("plate_3", id -> new Plate3Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_4 = REGISTRY.register("plate_4", id -> new Plate4Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_5 = REGISTRY.register("plate_5", id -> new Plate5Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_6 = REGISTRY.register("plate_6", id -> new Plate6Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_7 = REGISTRY.register("plate_7", id -> new Plate7Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_8 = REGISTRY.register("plate_8", id -> new Plate8Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_9 = REGISTRY.register("plate_9", id -> new Plate9Block(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_PLUS = REGISTRY.register("plate_plus", id -> new PlatePlusBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_MINUS = REGISTRY.register("plate_minus", id -> new PlateMinusBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_ARROWUP = REGISTRY.register("plate_arrowup", id -> new PlateArrowupBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_ARROWDOWN = REGISTRY.register("plate_arrowdown", id -> new PlateArrowdownBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_ARROWLEFT = REGISTRY.register("plate_arrowleft", id -> new PlateArrowleftBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_ARROWRIGHT = REGISTRY.register("plate_arrowright", id -> new PlateArrowrightBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_MONEY = REGISTRY.register("plate_money", id -> new PlateMoneyBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_FACEHAPPY = REGISTRY.register("plate_facehappy", id -> new PlateFacehappyBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_FACESAD = REGISTRY.register("plate_facesad", id -> new PlateFacesadBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_FACEMAD = REGISTRY.register("plate_facemad", id -> new PlateFacemadBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_FACENEUTRAL = REGISTRY.register("plate_faceneutral", id -> new PlateFaceneutralBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_FACECAT = REGISTRY.register("plate_facecat", id -> new PlateFacecatBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_INCORRECT = REGISTRY.register("plate_incorrect", id -> new PlateIncorrectBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_CORRECT = REGISTRY.register("plate_correct", id -> new PlateCorrectBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_DANGER = REGISTRY.register("plate_danger", id -> new PlateDangerBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_STOP = REGISTRY.register("plate_stop", id -> new PlateStopBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_BONUS = REGISTRY.register("plate_bonus", id -> new PlateBonusBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_FIRE = REGISTRY.register("plate_fire", id -> new PlateFireBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_WATER = REGISTRY.register("plate_water", id -> new PlateWaterBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_ELECTRICITY = REGISTRY.register("plate_electricity", id -> new PlateElectricityBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_HEART = REGISTRY.register("plate_heart", id -> new PlateHeartBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredBlock<Block> PLATE_LIGHT = REGISTRY.register("plate_light", id -> new PlateLightBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))));
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
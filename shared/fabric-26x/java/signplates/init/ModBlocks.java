package signplates.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import signplates.SignPlatesMod;
import signplates.block.PlateBlock;
import signplates.SignPlateBlockItem;

public final class ModBlocks {
public static final Block PLATE_0 = register("plate_0");
public static final Block PLATE_1 = register("plate_1");
public static final Block PLATE_2 = register("plate_2");
public static final Block PLATE_3 = register("plate_3");
public static final Block PLATE_4 = register("plate_4");
public static final Block PLATE_5 = register("plate_5");
public static final Block PLATE_6 = register("plate_6");
public static final Block PLATE_7 = register("plate_7");
public static final Block PLATE_8 = register("plate_8");
public static final Block PLATE_9 = register("plate_9");
public static final Block PLATE_A = register("plate_a");
public static final Block PLATE_ARROWDOWN = register("plate_arrowdown");
public static final Block PLATE_ARROWLEFT = register("plate_arrowleft");
public static final Block PLATE_ARROWRIGHT = register("plate_arrowright");
public static final Block PLATE_ARROWUP = register("plate_arrowup");
public static final Block PLATE_B = register("plate_b");
public static final Block PLATE_BONUS = register("plate_bonus");
public static final Block PLATE_C = register("plate_c");
public static final Block PLATE_CORRECT = register("plate_correct");
public static final Block PLATE_D = register("plate_d");
public static final Block PLATE_DANGER = register("plate_danger");
public static final Block PLATE_E = register("plate_e");
public static final Block PLATE_ELECTRICITY = register("plate_electricity");
public static final Block PLATE_EMPTY = register("plate_empty");
public static final Block PLATE_F = register("plate_f");
public static final Block PLATE_FACECAT = register("plate_facecat");
public static final Block PLATE_FACEHAPPY = register("plate_facehappy");
public static final Block PLATE_FACEMAD = register("plate_facemad");
public static final Block PLATE_FACENEUTRAL = register("plate_faceneutral");
public static final Block PLATE_FACESAD = register("plate_facesad");
public static final Block PLATE_FIRE = register("plate_fire");
public static final Block PLATE_G = register("plate_g");
public static final Block PLATE_H = register("plate_h");
public static final Block PLATE_HEART = register("plate_heart");
public static final Block PLATE_I = register("plate_i");
public static final Block PLATE_INCORRECT = register("plate_incorrect");
public static final Block PLATE_J = register("plate_j");
public static final Block PLATE_K = register("plate_k");
public static final Block PLATE_L = register("plate_l");
public static final Block PLATE_LIGHT = register("plate_light");
public static final Block PLATE_M = register("plate_m");
public static final Block PLATE_MINUS = register("plate_minus");
public static final Block PLATE_MONEY = register("plate_money");
public static final Block PLATE_N = register("plate_n");
public static final Block PLATE_O = register("plate_o");
public static final Block PLATE_P = register("plate_p");
public static final Block PLATE_PLUS = register("plate_plus");
public static final Block PLATE_Q = register("plate_q");
public static final Block PLATE_R = register("plate_r");
public static final Block PLATE_S = register("plate_s");
public static final Block PLATE_STOP = register("plate_stop");
public static final Block PLATE_T = register("plate_t");
public static final Block PLATE_U = register("plate_u");
public static final Block PLATE_V = register("plate_v");
public static final Block PLATE_W = register("plate_w");
public static final Block PLATE_WATER = register("plate_water");
public static final Block PLATE_X = register("plate_x");
public static final Block PLATE_Y = register("plate_y");
public static final Block PLATE_Z = register("plate_z");

public static final Block[] PLATES = new Block[] {
    PLATE_0, PLATE_1, PLATE_2, PLATE_3, PLATE_4, PLATE_5, PLATE_6, PLATE_7, PLATE_8, PLATE_9, PLATE_A, PLATE_ARROWDOWN, PLATE_ARROWLEFT, PLATE_ARROWRIGHT, PLATE_ARROWUP, PLATE_B, PLATE_BONUS, PLATE_C, PLATE_CORRECT, PLATE_D, PLATE_DANGER, PLATE_E, PLATE_ELECTRICITY, PLATE_EMPTY, PLATE_F, PLATE_FACECAT, PLATE_FACEHAPPY, PLATE_FACEMAD, PLATE_FACENEUTRAL, PLATE_FACESAD, PLATE_FIRE, PLATE_G, PLATE_H, PLATE_HEART, PLATE_I, PLATE_INCORRECT, PLATE_J, PLATE_K, PLATE_L, PLATE_LIGHT, PLATE_M, PLATE_MINUS, PLATE_MONEY, PLATE_N, PLATE_O, PLATE_P, PLATE_PLUS, PLATE_Q, PLATE_R, PLATE_S, PLATE_STOP, PLATE_T, PLATE_U, PLATE_V, PLATE_W, PLATE_WATER, PLATE_X, PLATE_Y, PLATE_Z
};

    private ModBlocks() {
    }

    private static Block register(String name) {
        Identifier id = SignPlatesMod.id(name);
        Block block = Registry.register(BuiltInRegistries.BLOCK, id,
            new PlateBlock(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, id))
                .strength(1f, 10f)
                .sound(SoundType.STONE)
                .noOcclusion()
                .lightLevel(state -> state.getValue(PlateBlock.GLOW) ? 13 : 0)));
        Registry.register(BuiltInRegistries.ITEM, id, new SignPlateBlockItem(block, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id))));
        return block;
    }

    public static void init() {
    }
}

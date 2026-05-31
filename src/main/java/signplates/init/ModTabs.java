package signplates.init;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import signplates.SignPlatesMod;

public final class ModTabs {
    public static final CreativeModeTab SIGN_PLATES_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
        SignPlatesMod.id("sign_plates_tab"),
        FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group.sign_plates.sign_plates_tab"))
            .icon(() -> new ItemStack(ModBlocks.PLATE_FACEHAPPY))
            .displayItems((parameters, entries) -> {
    entries.accept(ModBlocks.PLATE_0.asItem());
    entries.accept(ModBlocks.PLATE_1.asItem());
    entries.accept(ModBlocks.PLATE_2.asItem());
    entries.accept(ModBlocks.PLATE_3.asItem());
    entries.accept(ModBlocks.PLATE_4.asItem());
    entries.accept(ModBlocks.PLATE_5.asItem());
    entries.accept(ModBlocks.PLATE_6.asItem());
    entries.accept(ModBlocks.PLATE_7.asItem());
    entries.accept(ModBlocks.PLATE_8.asItem());
    entries.accept(ModBlocks.PLATE_9.asItem());
    entries.accept(ModBlocks.PLATE_A.asItem());
    entries.accept(ModBlocks.PLATE_ARROWDOWN.asItem());
    entries.accept(ModBlocks.PLATE_ARROWLEFT.asItem());
    entries.accept(ModBlocks.PLATE_ARROWRIGHT.asItem());
    entries.accept(ModBlocks.PLATE_ARROWUP.asItem());
    entries.accept(ModBlocks.PLATE_B.asItem());
    entries.accept(ModBlocks.PLATE_BONUS.asItem());
    entries.accept(ModBlocks.PLATE_C.asItem());
    entries.accept(ModBlocks.PLATE_CORRECT.asItem());
    entries.accept(ModBlocks.PLATE_D.asItem());
    entries.accept(ModBlocks.PLATE_DANGER.asItem());
    entries.accept(ModBlocks.PLATE_E.asItem());
    entries.accept(ModBlocks.PLATE_ELECTRICITY.asItem());
    entries.accept(ModBlocks.PLATE_EMPTY.asItem());
    entries.accept(ModBlocks.PLATE_F.asItem());
    entries.accept(ModBlocks.PLATE_FACECAT.asItem());
    entries.accept(ModBlocks.PLATE_FACEHAPPY.asItem());
    entries.accept(ModBlocks.PLATE_FACEMAD.asItem());
    entries.accept(ModBlocks.PLATE_FACENEUTRAL.asItem());
    entries.accept(ModBlocks.PLATE_FACESAD.asItem());
    entries.accept(ModBlocks.PLATE_FIRE.asItem());
    entries.accept(ModBlocks.PLATE_G.asItem());
    entries.accept(ModBlocks.PLATE_H.asItem());
    entries.accept(ModBlocks.PLATE_HEART.asItem());
    entries.accept(ModBlocks.PLATE_I.asItem());
    entries.accept(ModBlocks.PLATE_INCORRECT.asItem());
    entries.accept(ModBlocks.PLATE_J.asItem());
    entries.accept(ModBlocks.PLATE_K.asItem());
    entries.accept(ModBlocks.PLATE_L.asItem());
    entries.accept(ModBlocks.PLATE_LIGHT.asItem());
    entries.accept(ModBlocks.PLATE_M.asItem());
    entries.accept(ModBlocks.PLATE_MINUS.asItem());
    entries.accept(ModBlocks.PLATE_MONEY.asItem());
    entries.accept(ModBlocks.PLATE_N.asItem());
    entries.accept(ModBlocks.PLATE_O.asItem());
    entries.accept(ModBlocks.PLATE_P.asItem());
    entries.accept(ModBlocks.PLATE_PLUS.asItem());
    entries.accept(ModBlocks.PLATE_Q.asItem());
    entries.accept(ModBlocks.PLATE_R.asItem());
    entries.accept(ModBlocks.PLATE_S.asItem());
    entries.accept(ModBlocks.PLATE_STOP.asItem());
    entries.accept(ModBlocks.PLATE_T.asItem());
    entries.accept(ModBlocks.PLATE_U.asItem());
    entries.accept(ModBlocks.PLATE_V.asItem());
    entries.accept(ModBlocks.PLATE_W.asItem());
    entries.accept(ModBlocks.PLATE_WATER.asItem());
    entries.accept(ModBlocks.PLATE_X.asItem());
    entries.accept(ModBlocks.PLATE_Y.asItem());
    entries.accept(ModBlocks.PLATE_Z.asItem());
            })
            .build());

    private ModTabs() {
    }

    public static void init() {
    }
}

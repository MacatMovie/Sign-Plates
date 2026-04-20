/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package signplates.init;

import signplates.SignPlatesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class SignPlatesModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SignPlatesMod.MODID);
	public static final RegistryObject<CreativeModeTab> SIGN_PLATES_TAB = REGISTRY.register("sign_plates_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.sign_plates.sign_plates_tab")).icon(() -> new ItemStack(SignPlatesModBlocks.PLATE_FACEHAPPY.get())).displayItems((parameters, tabData) -> {
				tabData.accept(SignPlatesModBlocks.PLATE_A.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_B.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_C.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_D.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_E.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_F.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_G.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_H.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_I.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_J.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_K.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_L.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_M.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_N.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_O.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_P.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_R.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_S.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_T.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_Q.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_U.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_V.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_W.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_X.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_Y.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_Z.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_EMPTY.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_0.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_1.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_2.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_3.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_4.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_5.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_6.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_7.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_8.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_9.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_PLUS.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_MINUS.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_ARROWUP.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_ARROWDOWN.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_ARROWLEFT.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_ARROWRIGHT.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_MONEY.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_FACEHAPPY.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_FACESAD.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_FACEMAD.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_FACENEUTRAL.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_FACECAT.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_INCORRECT.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_CORRECT.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_DANGER.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_STOP.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_BONUS.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_FIRE.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_WATER.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_ELECTRICITY.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_HEART.get().asItem());
				tabData.accept(SignPlatesModBlocks.PLATE_LIGHT.get().asItem());
			}).build());
}
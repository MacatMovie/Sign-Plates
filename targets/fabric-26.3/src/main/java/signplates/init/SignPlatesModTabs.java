package signplates.init;

import signplates.PlateCatalog;
import signplates.SignPlatesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class SignPlatesModTabs {
    public static void init(){}
    public static final CreativeModeTab SIGN_PLATES_TAB=net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.CREATIVE_MODE_TAB,SignPlatesMod.id("sign_plates_tab"),
        net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab.builder().title(Component.translatable("item_group.sign_plates.sign_plates_tab"))
            .icon(() -> signplates.PlateStyle.stack(signplates.PlateAppearance.DEFAULT))
            .displayItems((parameters, output) -> {
                for (int s = 0; s < PlateCatalog.SYMBOLS.size(); s++) output.accept(signplates.PlateStyle.stack(new signplates.PlateAppearance(s,1,1,signplates.PlateTint.NONE,false)));
            }).build());
}

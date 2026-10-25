package signplates.init;

import signplates.PlateCatalog;
import signplates.SignPlatesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class SignPlatesModTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SignPlatesMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SIGN_PLATES_TAB = REGISTRY.register("sign_plates_tab",
        () -> CreativeModeTab.builder().title(Component.translatable("item_group.sign_plates.sign_plates_tab"))
            .icon(() -> signplates.PlateStyle.stack(signplates.PlateAppearance.DEFAULT))
            .displayItems((parameters, output) -> {
                for (int s = 0; s < PlateCatalog.SYMBOLS.size(); s++) output.accept(signplates.PlateStyle.stack(new signplates.PlateAppearance(s,1,1,signplates.PlateTint.NONE,false)));
            }).build());
}

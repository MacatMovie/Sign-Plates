package signplates.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class SignPlatesModTabs {
    public static final ItemGroup SIGN_PLATES_TAB = new ItemGroup("sign_plates_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SignPlatesModBlocks.PLATE_FACEHAPPY.get());
        }
    };
}

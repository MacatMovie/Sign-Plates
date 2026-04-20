package signplates.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SignPlatesModTabs {
	public static final CreativeModeTab SIGN_PLATES_TAB = new CreativeModeTab("sign_plates_tab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(SignPlatesModBlocks.PLATE_FACEHAPPY.get());
		}
	};
}

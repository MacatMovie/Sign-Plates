package signplates;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

/** Wax is independent of the appearance component, so old saved designs remain compatible. */
public final class PlateWax {
    private PlateWax() {}
    public static boolean isWaxed(ItemStack stack) {
        CustomData data=stack.get(DataComponents.CUSTOM_DATA);
        return data!=null && data.copyTag().getBooleanOr("sign_plates:waxed",false);
    }
    public static void set(ItemStack stack,boolean waxed) {
        // A clean, unsealed plate should not gain an empty custom-data component.
        if(!waxed && !stack.has(DataComponents.CUSTOM_DATA))return;
        CustomData.update(DataComponents.CUSTOM_DATA,stack,tag->{if(waxed)tag.putBoolean("sign_plates:waxed",true);else tag.remove("sign_plates:waxed");});
    }
}

package signplates;
import net.minecraft.world.item.ItemStack;
/** Custom wax marker, kept separate from appearance to preserve stack compatibility. */
public final class PlateWax {
 private PlateWax() {}
 public static boolean isWaxed(ItemStack stack) {
  var tag=stack.getTag();return tag!=null && tag.getBoolean("sign_plates:waxed");
 }
 public static void set(ItemStack stack,boolean waxed) {
  if(waxed)stack.getOrCreateTag().putBoolean("sign_plates:waxed",true);
  else if(stack.getTag()!=null)stack.getTag().remove("sign_plates:waxed");
 }
}

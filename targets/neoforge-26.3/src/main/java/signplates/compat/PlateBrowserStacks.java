package signplates.compat;
import java.util.*;
import net.minecraft.world.item.ItemStack;
import signplates.*;
public final class PlateBrowserStacks {
 public static List<ItemStack> all() {
  var stacks=new ArrayList<ItemStack>();
  for(int i=0;i<PlateCatalog.SYMBOLS.size();i++)stacks.add(PlateStyle.stack(new PlateAppearance(i,1,1,PlateTint.NONE,false)));
  return List.copyOf(stacks);
 }
 private PlateBrowserStacks(){}
}

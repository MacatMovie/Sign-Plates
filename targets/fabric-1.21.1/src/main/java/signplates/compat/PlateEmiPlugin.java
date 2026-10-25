package signplates.compat;
import dev.emi.emi.api.*;
import dev.emi.emi.api.stack.*;
import signplates.PlateStyle;
import signplates.PlateAppearance;
import signplates.init.SignPlatesModItems;
@EmiEntrypoint
public final class PlateEmiPlugin implements EmiPlugin {
 @Override public void register(EmiRegistry registry) {
  var item=SignPlatesModItems.PLATE.get();
  registry.setDefaultComparison(item,Comparison.compareData(stack->PlateStyle.appearance(stack.getItemStack())));
  // EMI applies removal predicates to future additions too. Keep the index's
  // existing variants and fill its gaps for either registered or creative mode.
  var present=new java.util.HashSet<PlateAppearance>();
  for(var stack:EmiApi.getIndexStacks())if(item.equals(stack.getKey()))present.add(PlateStyle.appearance(stack.getItemStack()));
  for(var stack:PlateBrowserStacks.all())if(present.add(PlateStyle.appearance(stack)))registry.addEmiStack(EmiStack.of(stack));
 }
}

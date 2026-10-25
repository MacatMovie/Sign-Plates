package signplates.compat;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.subtypes.*;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import signplates.*;
import signplates.init.SignPlatesModItems;
@JeiPlugin
public final class PlateJeiPlugin implements IModPlugin {
 @Override public ResourceLocation getPluginUid(){return new ResourceLocation("sign_plates","designer");}
 @Override public void registerItemSubtypes(ISubtypeRegistration registration) {
  registration.registerSubtypeInterpreter(SignPlatesModItems.PLATE.get(),new IIngredientSubtypeInterpreter<ItemStack>() {
   @Override public String apply(ItemStack stack,UidContext context) {
    var a=PlateStyle.appearance(stack);return PlateCatalog.SYMBOLS.get(a.symbol())+"/"+PlateLayers.FRAMES.get(a.frame()).id()+"/"+PlateLayers.BACKGROUNDS.get(a.background()).id()+"/"+a.tint().getSerializedName()+"/"+a.glow();
   }
  });
 }
 @Override public void registerExtraIngredients(IExtraIngredientRegistration registration){registration.addExtraItemStacks(PlateBrowserStacks.all());}
}

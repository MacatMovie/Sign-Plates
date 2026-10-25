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
 @Override public ResourceLocation getPluginUid(){return ResourceLocation.fromNamespaceAndPath("sign_plates","designer");}
 @Override public void registerItemSubtypes(ISubtypeRegistration registration) {
  registration.registerSubtypeInterpreter(SignPlatesModItems.PLATE.get(),new ISubtypeInterpreter<ItemStack>() {
   @Override public Object getSubtypeData(ItemStack stack,UidContext context){return PlateStyle.appearance(stack);}
   @Override public String getLegacyStringSubtypeInfo(ItemStack stack,UidContext context) {
    var a=PlateStyle.appearance(stack);return PlateCatalog.SYMBOLS.get(a.symbol())+"/"+PlateLayers.FRAMES.get(a.frame()).id()+"/"+PlateLayers.BACKGROUNDS.get(a.background()).id()+"/"+a.tint().getSerializedName()+"/"+a.glow()+"/"+a.light();
   }
  });
 }
 @Override public void registerExtraIngredients(IExtraIngredientRegistration registration){registration.addExtraItemStacks(PlateBrowserStacks.all());}
}

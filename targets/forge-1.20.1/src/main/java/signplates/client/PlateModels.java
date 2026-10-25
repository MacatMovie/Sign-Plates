package signplates.client;
import java.util.*;
import signplates.SignPlatesMod;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.client.event.ModelEvent;
@EventBusSubscriber(modid=SignPlatesMod.MODID,bus=EventBusSubscriber.Bus.MOD,value=Dist.CLIENT)
public final class PlateModels {
 @SubscribeEvent public static void register(ModelEvent.RegisterAdditional event) {for(var id:PlateBakedModel.componentIds())event.register(id);}
 @SubscribeEvent public static void bake(ModelEvent.ModifyBakingResult event) {
  Map<net.minecraft.resources.ResourceLocation,BakedModel> parts=new HashMap<>();
  for(var id:PlateBakedModel.componentIds()) {var key=id;var model=event.getModels().get(key);if(model!=null)parts.put(key,model);}
  var caches=new PlateBakedModel.Caches();
  var immutableParts=Map.copyOf(parts);
  event.getModels().replaceAll((id,model)->id.getNamespace().equals("sign_plates") && id.getPath().equals("sign_plate")?new PlateBakedModel(model,immutableParts,caches):model);
 }
}

package signplates.client;
import java.util.*;import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;import net.minecraft.client.resources.model.*;import net.minecraft.resources.ResourceLocation;
public final class PlateModels {
 public static void init(){ModelLoadingPlugin.register(context->{
  context.addModels(PlateBakedModel.componentIds());
  var caches=new PlateBakedModel.Caches();
  context.modifyModelAfterBake().register((model,ctx)->{
   var id=ctx.id();
   // 1.20.1 invokes this hook for direct JSON model paths, not just state/item IDs.
   if(model==null || model instanceof PlateBakedModel || id==null || !id.getNamespace().equals("sign_plates") ||
      !Set.of("sign_plate","block/sign_plate","item/sign_plate").contains(id.getPath()))return model;
   var parts=new HashMap<ResourceLocation,BakedModel>();
   for(var partId:PlateBakedModel.componentIds()){
    var part=ctx.baker().bake(partId,BlockModelRotation.X0_Y0);
    if(part!=null)parts.put(partId,part);
   }
   return new PlateBakedModel(model,Map.copyOf(parts),caches);
  });
 });}
}

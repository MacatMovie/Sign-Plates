package signplates.client;
import net.neoforged.api.distmarker.Dist;import net.neoforged.bus.api.SubscribeEvent;import net.neoforged.fml.common.EventBusSubscriber;import net.neoforged.neoforge.client.event.*;
@EventBusSubscriber(modid="sign_plates",value=Dist.CLIENT) public final class PlateModels {
 @SubscribeEvent public static void preview(RegisterPictureInPictureRenderersEvent event) {
  event.register(PlatePreviewState.class, PlatePreviewRenderer::new);
 }

 @SubscribeEvent public static void items(RegisterItemModelsEvent event){event.register(PlateModelCache.id("plate"),PlateItemModel.Unbaked.CODEC);}
 @SubscribeEvent public static void blocks(ModelEvent.ModifyBakingResult event){event.getBakingResult().blockStateModels().replaceAll((state,model)->state.getBlock() instanceof signplates.block.PlateBlock?new PlateBlockModel(state,model):model);}
}

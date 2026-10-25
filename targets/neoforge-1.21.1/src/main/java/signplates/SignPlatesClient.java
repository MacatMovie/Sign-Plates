package signplates;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
@EventBusSubscriber(modid=SignPlatesMod.MODID,bus=EventBusSubscriber.Bus.MOD,value=Dist.CLIENT)
public final class SignPlatesClient {
 @SubscribeEvent public static void screens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {event.register(signplates.menu.PlateEditorMenu.TYPE.get(),signplates.client.PlateEditorScreen::new);}
}

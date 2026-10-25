package signplates;
import net.minecraftforge.api.distmarker.Dist;import net.minecraftforge.eventbus.api.SubscribeEvent;import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid="sign_plates",bus=Mod.EventBusSubscriber.Bus.MOD,value=Dist.CLIENT) public final class SignPlatesClient {@SubscribeEvent public static void setup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event){event.enqueueWork(()->net.minecraft.client.gui.screens.MenuScreens.register(signplates.menu.PlateEditorMenu.TYPE.get(),signplates.client.PlateEditorScreen::new));}}

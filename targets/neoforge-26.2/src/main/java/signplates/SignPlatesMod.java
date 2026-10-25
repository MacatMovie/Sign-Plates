package signplates;
import signplates.init.*;
import signplates.menu.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
@Mod("sign_plates")
public final class SignPlatesMod {
 public static final String MODID="sign_plates";
 public static net.minecraft.resources.Identifier id(String path){return net.minecraft.resources.Identifier.fromNamespaceAndPath(MODID,path);}
 public static final Logger LOGGER=LogManager.getLogger(SignPlatesMod.class);
 public SignPlatesMod(IEventBus bus) {
  SignPlatesConfig.load();SignPlatesModBlocks.REGISTRY.register(bus);SignPlatesModItems.REGISTRY.register(bus);
  SignPlatesModData.COMPONENTS.register(bus);SignPlatesModData.BLOCK_ENTITIES.register(bus);
  SignPlatesModTabs.REGISTRY.register(bus);PlateEditorMenu.REGISTRY.register(bus);
  // More background choices move the following style command IDs. Require the
  // matching protocol so older clients cannot send a different action by mistake.
  bus.addListener((net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event)->event.registrar("designer5").playToServer(PlateEditPayload.TYPE,PlateEditPayload.CODEC,(payload,context)->context.enqueueWork(()->{
   var player=context.player();
   if(player.containerMenu instanceof PlateEditorMenu menu && menu.containerId==payload.containerId())menu.applyVisuals(player,payload.appearance());
  })));
 }
}

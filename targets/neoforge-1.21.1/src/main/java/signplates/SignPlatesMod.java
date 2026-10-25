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
 public static final Logger LOGGER=LogManager.getLogger(SignPlatesMod.class);
 public SignPlatesMod(IEventBus bus) {
  SignPlatesConfig.load();SignPlatesModBlocks.REGISTRY.register(bus);SignPlatesModItems.REGISTRY.register(bus);
  SignPlatesModData.COMPONENTS.register(bus);SignPlatesModData.BLOCK_ENTITIES.register(bus);
  SignPlatesModTabs.REGISTRY.register(bus);PlateEditorMenu.REGISTRY.register(bus);
  // Complete visual drafts include independent block light. Require matching
  // clients so an old editing protocol cannot be interpreted as a new draft.
  bus.addListener((net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event)->event.registrar("designer5").playToServer(PlateEditPayload.TYPE,PlateEditPayload.CODEC,(payload,context)->context.enqueueWork(()->{
   var player=context.player();
   if(player.containerMenu instanceof PlateEditorMenu menu && menu.containerId==payload.containerId())menu.applyVisuals(player,payload.appearance());
  })));
 }
}

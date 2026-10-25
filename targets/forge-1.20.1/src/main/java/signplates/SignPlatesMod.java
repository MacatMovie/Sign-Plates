package signplates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import signplates.init.*;
@Mod("sign_plates")
public final class SignPlatesMod {
 public static final String MODID="sign_plates";
 public static final Logger LOGGER=LogManager.getLogger(SignPlatesMod.class);
 public SignPlatesMod(FMLJavaModLoadingContext context) {
  SignPlatesConfig.load();var bus=context.getModEventBus();SignPlatesModData.BLOCK_ENTITIES.register(bus);signplates.menu.DesignerNetworking.init();
  SignPlatesModBlocks.REGISTRY.register(bus);SignPlatesModItems.REGISTRY.register(bus);SignPlatesModTabs.REGISTRY.register(bus);
  signplates.menu.PlateEditorMenu.REGISTRY.register(bus);
 }
}

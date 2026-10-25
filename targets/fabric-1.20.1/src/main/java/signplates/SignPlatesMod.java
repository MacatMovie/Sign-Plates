package signplates;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import signplates.init.SignPlatesModBlocks;
import signplates.init.SignPlatesModTabs;
public final class SignPlatesMod implements ModInitializer {
 public static final String MODID="sign_plates", MOD_ID=MODID;
 public static final Logger LOGGER=LoggerFactory.getLogger(MODID);
 public static ResourceLocation id(String path) {return new ResourceLocation(MODID,path);}
 @Override public void onInitialize(){SignPlatesConfig.load();signplates.init.SignPlatesModData.init();SignPlatesModBlocks.init();signplates.init.SignPlatesModItems.init();SignPlatesModTabs.init();signplates.menu.PlateEditorMenu.init();signplates.menu.DesignerNetworking.init();}
}

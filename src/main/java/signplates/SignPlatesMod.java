package signplates;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import signplates.init.ModBlocks;
import signplates.init.ModTabs;

public class SignPlatesMod implements ModInitializer {
    public static final String MOD_ID = "sign_plates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        SignPlatesConfig.load();
        ModBlocks.init();
        ModTabs.init();
        LOGGER.info("Sign Plates initialized.");
    }
}

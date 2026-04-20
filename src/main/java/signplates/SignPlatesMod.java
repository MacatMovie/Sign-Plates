package signplates;

import signplates.init.SignPlatesModBlocks;
import signplates.init.SignPlatesModItems;
import signplates.init.SignPlatesModTabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SignPlatesMod.MODID)
public class SignPlatesMod {
	public static final Logger LOGGER = LogManager.getLogger(SignPlatesMod.class);
	public static final String MODID = "sign_plates";

	public SignPlatesMod(IEventBus modEventBus) {
		SignPlatesModBlocks.REGISTRY.register(modEventBus);
		SignPlatesModItems.REGISTRY.register(modEventBus);
		SignPlatesModTabs.REGISTRY.register(modEventBus);
	}
}

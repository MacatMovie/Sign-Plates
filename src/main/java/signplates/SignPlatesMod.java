package signplates;

import signplates.init.SignPlatesModTabs;
import signplates.init.SignPlatesModItems;
import signplates.init.SignPlatesModBlocks;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod("sign_plates")
public class SignPlatesMod {
	public static final Logger LOGGER = LogManager.getLogger(SignPlatesMod.class);
	public static final String MODID = "sign_plates";

	public SignPlatesMod(IEventBus modEventBus) {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		SignPlatesModBlocks.REGISTRY.register(modEventBus);

		SignPlatesModItems.REGISTRY.register(modEventBus);

		SignPlatesModTabs.REGISTRY.register(modEventBus);

		// Start of user code block mod init
		// End of user code block mod init
	}

	// Start of user code block mod methods
	// End of user code block mod methods
}

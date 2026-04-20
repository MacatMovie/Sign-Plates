package signplates;

import signplates.init.SignPlatesModBlocks;
import signplates.init.SignPlatesModItems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SignPlatesMod.MODID)
public class SignPlatesMod {
    public static final Logger LOGGER = LogManager.getLogger(SignPlatesMod.class);
    public static final String MODID = "sign_plates";

    public SignPlatesMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        SignPlatesModBlocks.REGISTRY.register(bus);
        SignPlatesModItems.REGISTRY.register(bus);
    }
}

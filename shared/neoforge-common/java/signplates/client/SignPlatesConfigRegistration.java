package signplates.client;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
@Mod(value = "sign_plates", dist = Dist.CLIENT)
public final class SignPlatesConfigRegistration {
    public SignPlatesConfigRegistration(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class,
            (mc, parent) -> new SignPlatesConfigScreen(parent));
    }
}

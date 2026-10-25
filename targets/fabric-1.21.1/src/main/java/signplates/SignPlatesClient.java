package signplates;
import net.fabricmc.api.ClientModInitializer;import net.minecraft.client.gui.screens.MenuScreens;
public final class SignPlatesClient implements ClientModInitializer {public void onInitializeClient(){MenuScreens.register(signplates.menu.PlateEditorMenu.TYPE,signplates.client.PlateEditorScreen::new);net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE.putBlock(signplates.init.SignPlatesModBlocks.PLATE.get(),net.minecraft.client.renderer.RenderType.cutout());signplates.client.PlateModels.init();}}

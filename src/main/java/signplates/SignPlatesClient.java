package signplates;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.state.BlockState;
import signplates.block.PlateBlock;
import signplates.init.ModBlocks;

public class SignPlatesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> getPlateColor(state), ModBlocks.PLATES);
    }

    private static int getPlateColor(BlockState state) {
        if (!state.hasProperty(PlateBlock.TINT)) {
            return 0xFFFFFF;
        }
        PlateTint tint = state.getValue(PlateBlock.TINT);
        return state.getValue(PlateBlock.GLOW) ? tint.getGlowTintColor() : tint.getTintColor();
    }
}

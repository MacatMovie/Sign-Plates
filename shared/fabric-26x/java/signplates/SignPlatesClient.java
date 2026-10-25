package signplates;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import signplates.block.PlateBlock;
import signplates.init.ModBlocks;

public class SignPlatesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockColorRegistry.register(List.of(new BlockTintSource() {
            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return getPlateColor(state);
            }

            @Override
            public int color(BlockState state) {
                return getPlateColor(state);
            }
        }), ModBlocks.PLATES);
    }

    private static int getPlateColor(BlockState state) {
        if (!state.hasProperty(PlateBlock.TINT)) {
            return 0xFFFFFFFF;
        }
        PlateTint tint = state.getValue(PlateBlock.TINT);
        int rgb = state.getValue(PlateBlock.GLOW) ? tint.getGlowTintColor() : tint.getTintColor();
        return 0xFF000000 | rgb;
    }
}

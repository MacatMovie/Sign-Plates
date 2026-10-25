package signplates;

import signplates.init.SignPlatesModBlocks;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

@EventBusSubscriber(modid = SignPlatesMod.MODID, value = Dist.CLIENT)
public class SignPlatesClient {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new BlockTintSource() {
            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return getTintColor(state);
            }

            @Override
            public int color(BlockState state) {
                return getTintColor(state);
            }
        }), allBlocks().toArray(Block[]::new));
    }

    private static int getTintColor(BlockState state) {
        PlateTint tint = PlateTint.NONE;
        boolean glow = false;
        for (Property<?> property : state.getProperties()) {
            if ("tint".equals(property.getName())) {
                Object value = state.getValue(property);
                if (value instanceof PlateTint plateTint) {
                    tint = plateTint;
                }
            } else if ("glow".equals(property.getName())) {
                Object value = state.getValue(property);
                if (value instanceof Boolean b) {
                    glow = b;
                }
            }
        }
        return 0xFF000000 | (glow ? tint.getGlowTintColor() : tint.getTintColor());
    }

    private static List<? extends Block> allBlocks() {
        return SignPlatesModBlocks.REGISTRY.getEntries().stream().map(holder -> holder.get()).toList();
    }
}

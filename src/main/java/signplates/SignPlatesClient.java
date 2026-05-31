package signplates;

import signplates.init.SignPlatesModBlocks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mod.EventBusSubscriber(modid = SignPlatesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SignPlatesClient {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) return 0xFFFFFF;
            return getTintColor(state);
        }, allBlocks());
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
        return glow ? tint.getGlowTintColor() : tint.getTintColor();
    }

    private static Block[] allBlocks() {
        return SignPlatesModBlocks.REGISTRY.getEntries().stream().map(holder -> holder.get()).toArray(Block[]::new);
    }
}

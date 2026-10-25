package signplates.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Access to the native PiP submission queue on both loaders. */
@Mixin(value=GuiGraphicsExtractor.class, remap=false)
public interface PlateGuiAccessor {
    @Accessor("guiRenderState")
    GuiRenderState signplates$guiRenderState();
}

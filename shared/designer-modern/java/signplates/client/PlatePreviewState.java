package signplates.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Extracted item geometry for a preview-sized render target, bypassing the item atlas. */
public record PlatePreviewState(TrackingItemStackRenderState item, int x0, int y0, int x1, int y1)
        implements PictureInPictureRenderState {
    public static void submit(GuiGraphicsExtractor graphics, ItemStack stack, int x0, int y0, int x1, int y1) {
        ((signplates.mixin.PlateGuiAccessor)graphics).signplates$guiRenderState()
                .addPicturesInPictureState(extract(stack, x0, y0, x1, y1));
    }

    public static PlatePreviewState extract(ItemStack stack, int x0, int y0, int x1, int y1) {
        var minecraft = Minecraft.getInstance();
        var item = new TrackingItemStackRenderState();
        minecraft.getItemModelResolver().updateForTopItem(item, stack, ItemDisplayContext.GUI,
                minecraft.level, minecraft.player, 0);
        return new PlatePreviewState(item, x0, y0, x1, y1);
    }

    @Override public float scale() { return 90; }
    @Override public ScreenRectangle scissorArea() { return bounds(); }
    @Override public ScreenRectangle bounds() { return new ScreenRectangle(x0, y0, x1-x0, y1-y0); }
}

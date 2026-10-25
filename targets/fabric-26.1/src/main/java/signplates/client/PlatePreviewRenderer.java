package signplates.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Renders at the panel's GUI-scaled resolution instead of enlarging a 16px item icon. */
public final class PlatePreviewRenderer extends PictureInPictureRenderer<PlatePreviewState> {
    private Object modelOnTexture;
    public PlatePreviewRenderer(net.minecraft.client.renderer.MultiBufferSource.BufferSource buffers) { super(buffers); }
    @Override public Class<PlatePreviewState> getRenderStateClass() { return PlatePreviewState.class; }
    @Override protected void renderToTexture(PlatePreviewState state, PoseStack pose) {
        var minecraft = Minecraft.getInstance();
        pose.scale(1, -1, -1);
        minecraft.gameRenderer.getLighting().setupFor(state.item().usesBlockLight()
                ? Lighting.Entry.ITEMS_3D : Lighting.Entry.ITEMS_FLAT);
        var dispatcher = minecraft.gameRenderer.getFeatureRenderDispatcher();
        var collector = dispatcher.getSubmitNodeStorage();
        state.item().submit(pose, collector, 0xF000F0, OverlayTexture.NO_OVERLAY, 0);
        dispatcher.renderAllFeatures();
        modelOnTexture = state.item().getModelIdentity();
    }
    @Override protected boolean textureIsReadyToBlit(PlatePreviewState state) {
        return !state.item().isAnimated() && state.item().getModelIdentity().equals(modelOnTexture);
    }
    @Override protected float getTranslateY(int height, int guiScale) { return height / 2f; }
    @Override protected String getTextureLabel() { return "sign_plates_designer_preview"; }
}

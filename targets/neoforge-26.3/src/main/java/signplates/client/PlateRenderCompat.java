package signplates.client;
import java.util.List;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
final class PlateRenderCompat {
 // Keep the baked item pipeline and its lighting/material metadata on 26.3.
 static BakedQuad.MaterialInfo material(BakedQuad.MaterialInfo material){return material;}
 static void quads(ItemStackRenderState.LayerRenderState layer,List<BakedQuad> quads){
  layer.setQuads(net.minecraft.client.resources.model.geometry.ItemQuads.split(quads));
 }
}

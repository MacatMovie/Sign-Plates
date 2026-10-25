package signplates.client;
import java.util.*;import com.mojang.serialization.*;import org.joml.*;
import net.minecraft.client.renderer.item.*;import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;import net.minecraft.client.resources.model.*;import net.minecraft.client.resources.model.geometry.BakedQuad;import net.minecraft.resources.Identifier;import net.minecraft.client.multiplayer.ClientLevel;import net.minecraft.world.entity.ItemOwner;import net.minecraft.world.item.*;import signplates.*;
/** Selects independent, front-facing item layers from the appearance component. */
public final class PlateItemModel implements ItemModel {
 private record Layer(List<BakedQuad> quads,ModelRenderProperties properties,Vector3fc[] extents){}
 private final Map<String,Layer> layers;private final Matrix4fc transform;
 private PlateItemModel(ModelBaker baker,boolean angled,Matrix4fc transform){this.transform=new Matrix4f(transform);var layers=new HashMap<String,Layer>();for(String name:PlateModelCache.names()){
  Identifier id=PlateModelCache.id("item/"+(angled?"preview/":"")+"layers/"+name);var model=baker.getModel(id);var slots=model.getTopTextureSlots();var geometry=model.bakeTopGeometry(slots,baker,BlockModelRotation.IDENTITY);var quads=new ArrayList<BakedQuad>();var extents=new ArrayList<Vector3fc>();
  for(var q:geometry.getAll()){
   var material=PlateRenderCompat.material(q.materialInfo());
   // NeoForge 26.3 attaches baked normals and colors to the original quad.
   // Retain it whenever no material adaptation is required.
   quads.add(material==q.materialInfo()?q:new BakedQuad(q.position0(),q.position1(),q.position2(),q.position3(),q.packedUV0(),q.packedUV1(),q.packedUV2(),q.packedUV3(),q.direction(),material));
   for(int i=0;i<4;i++)extents.add(new Vector3f(q.position(i)));
  }
  layers.put(name,new Layer(List.copyOf(quads),ModelRenderProperties.fromResolvedModel(baker,model,slots),extents.toArray(Vector3fc[]::new)));
 }this.layers=Map.copyOf(layers);}
 @Override public void update(ItemStackRenderState state,ItemStack stack,ItemModelResolver resolver,ItemDisplayContext context,ClientLevel level,ItemOwner owner,int seed){var a=PlateStyle.appearance(stack);state.appendModelIdentityElement(this);state.appendModelIdentityElement(a);for(String name:PlateModelCache.selected(a)){var source=layers.get(name);if(source==null)continue;var layer=state.newLayer();source.properties().applyToLayer(layer,context);layer.setLocalTransform(transform);PlateRenderCompat.quads(layer,source.quads());layer.setExtents(()->source.extents());layer.tintLayers().add(0xFF000000|(a.glow()?a.tint().getGlowTintColor():a.tint().getTintColor()));for(var q:source.quads())if((q.materialInfo().flags()&BakedQuad.FLAG_ANIMATED)!=0){state.setAnimated();break;}}}
 public record Unbaked(boolean angled) implements ItemModel.Unbaked {
  public static final MapCodec<Unbaked> CODEC=Codec.BOOL.optionalFieldOf("angled",false).xmap(Unbaked::new,Unbaked::angled);
  @Override public MapCodec<? extends ItemModel.Unbaked> type(){return CODEC;}
  @Override public void resolveDependencies(ResolvableModel.Resolver resolver){for(String name:PlateModelCache.names()){resolver.markDependency(PlateModelCache.id("block/layers/"+name));resolver.markDependency(PlateModelCache.id("item/"+(angled?"preview/":"")+"layers/"+name));}}
  @Override public ItemModel bake(ItemModel.BakingContext context,Matrix4fc transform){if(!angled)PlateModelCache.bake(context.blockModelBaker());return new PlateItemModel(context.blockModelBaker(),angled,transform);}
 }
}

package signplates.client;
import java.util.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import signplates.*;

/** Tests the actual assembler with synthetic baked layers; no GPU required. */
public class ModelCullingChecks {
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 static BakedQuad quad(Direction direction) {
  int[] vertices=new int[32];
  for(int vertex=0;vertex<4;vertex++) {
   vertices[vertex*8]=Float.floatToRawIntBits((vertex%2==0?1:15)/16f);
   vertices[vertex*8+1]=Float.floatToRawIntBits(direction==Direction.DOWN?0:1/16f);
   vertices[vertex*8+2]=Float.floatToRawIntBits((vertex<2?1:15)/16f);
   vertices[vertex*8+3]=0xFFFFFFFF;vertices[vertex*8+6]=0x00F000F0;
  }
  return new BakedQuad(vertices,-1,direction,null,false,false);
 }
 static final class Layer extends BakedModelWrapper<BakedModel> {
  final BakedQuad front=quad(Direction.UP),back=quad(Direction.DOWN);
  Layer(){super(null);}
  @Override public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random){return side==null?List.of(front):side==Direction.DOWN?List.of(back):List.of();}
  @Override public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random,net.neoforged.neoforge.client.model.data.ModelData data,net.minecraft.client.renderer.RenderType type){return getQuads(state,side,random);}
 }
 public static void main(String[] args)throws Exception {
  var source=new Layer();var parts=new HashMap<ModelResourceLocation,BakedModel>();
  for(String prefix:List.of("block","item"))parts.put(ModelResourceLocation.standalone(PlateBakedModel.id(prefix+"/layers/background_iron")),source);
  var model=new PlateBakedModel(source,parts,new PlateBakedModel.Caches());
  var geometry=PlateBakedModel.class.getDeclaredMethod("geometry",PlateAppearance.class,int.class,boolean.class);geometry.setAccessible(true);
  var a=new PlateAppearance(0,0,1,PlateTint.NONE,false);
  for(int orientation=0;orientation<12;orientation++) {
   Object g=geometry.invoke(model,a,orientation,false);
   var side=g.getClass().getDeclaredMethod("side",Direction.class);side.setAccessible(true);
   var general=(List<?>)side.invoke(g,(Object)null);check(general.size()==1,"backing face leaked into unconditional geometry");
   int pitch=orientation/4,yaw=(orientation%4+(pitch==2?2:0))%4;
   var n=PlateBakedModel.rotate(0,-1,0,pitch,yaw);var back=Direction.getNearest(n[0],n[1],n[2]);
   for(Direction d:Direction.values()) {
    var quads=(List<?>)side.invoke(g,d);check(quads.size()==(d==back?1:0),"rotated cull direction");
    if(!quads.isEmpty()){var q=(BakedQuad)quads.get(0);check(q.getDirection()==back,"quad normal");check(q.getVertices()[6]==0x00F000F0,"fullbright vertex data lost");}
   }
   check(geometry.invoke(model,a,orientation,false)==g,"cache reuse");
  }
  Object item=geometry.invoke(model,a,0,true);var general=item.getClass().getDeclaredMethod("unculled");general.setAccessible(true);
  check(((List<?>)general.invoke(item)).size()==2,"item must retain its actual background front and back");
  check(source.front.getDirection()==Direction.UP&&source.back.getDirection()==Direction.DOWN,"source quads mutated");
  System.out.println("PASS: backing-face culling for all 12 orientations, cache reuse, item background faces and vertex data.");
 }
}

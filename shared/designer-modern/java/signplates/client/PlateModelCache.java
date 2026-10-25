package signplates.client;
import java.util.*;import org.joml.Quaternionf;import com.mojang.math.Transformation;
import net.minecraft.resources.Identifier;import net.minecraft.client.resources.model.*;import net.minecraft.client.renderer.block.dispatch.*;
import signplates.*;
/** Immutable geometry baked once per resource reload. No ticking or block entity renderer. */
public final class PlateModelCache {
 private record Cache(Map<String,List<BlockStateModelPart>> parts,int flags){}
 private static volatile Cache cache=new Cache(Map.of(),0);
 public static Identifier id(String path){return Identifier.fromNamespaceAndPath("sign_plates",path);}
 public static List<String> names(){var names=new ArrayList<String>();for(var b:PlateLayers.BACKGROUNDS)if(!b.texture().isEmpty())names.add("background_"+b.id());for(var f:PlateLayers.FRAMES)if(!f.texture().isEmpty())names.add("frame_"+f.id());for(String s:PlateCatalog.SYMBOLS)if(!s.equals("empty"))for(String v:List.of("original","tint","glow"))names.add(s+"_"+v);return List.copyOf(names);}
 public static void bake(ModelBaker baker){
  var parts=new HashMap<String,List<BlockStateModelPart>>();int flags=0;
  for(String name:names()){var rotations=new ArrayList<BlockStateModelPart>();for(int o=0;o<12;o++){
   int pitch=o/4,yaw=(o%4+(pitch==2?2:0))%4;
   var rotation=new Quaternionf().rotationYXZ(-yaw*(float)Math.PI/2,-pitch*(float)Math.PI/2,0);
   var transform=new Transformation(null,rotation,null,null);
   ModelState pose=new ModelState(){@Override public Transformation transformation(){return transform;}};
   var part=SimpleModelWrapper.bake(baker,id("block/layers/"+name),pose);rotations.add(part);flags|=part.materialFlags();
  }parts.put(name,List.copyOf(rotations));}cache=new Cache(Map.copyOf(parts),flags);
 }
 public static int flags(){return cache.flags();}
 public static List<String> selected(PlateAppearance a){var names=new ArrayList<String>(3);if(a.background()>0)names.add("background_"+PlateLayers.BACKGROUNDS.get(a.background()).id());if(a.frame()>0)names.add("frame_"+PlateLayers.FRAMES.get(a.frame()).id());if(a.symbol()>0)names.add(PlateCatalog.SYMBOLS.get(a.symbol())+"_"+(a.glow()?"glow":a.tint()!=PlateTint.NONE?"tint":"original"));return names;}
 public static void collect(PlateAppearance a,int orientation,List<BlockStateModelPart> output){var snapshot=cache;for(String name:selected(a)){var parts=snapshot.parts().get(name);if(parts!=null)output.add(parts.get(orientation));}}
}

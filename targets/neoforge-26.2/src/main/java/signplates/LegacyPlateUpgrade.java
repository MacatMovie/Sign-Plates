package signplates;
import java.util.*;
import net.minecraft.nbt.*;
/** Pure-NBT migration before registry lookups: old IDs never need registration.
 * Does not load chunks, access a level, or mutate other mods' data. */
public final class LegacyPlateUpgrade {
 private static final String CANONICAL="sign_plates:sign_plate",PREVIOUS="sign_plates:plate_empty",COMPONENT="sign_plates:appearance";
 private LegacyPlateUpgrade(){}
 private static PlateAppearance old(String id,CompoundTag properties) {
  if(!id.startsWith("sign_plates:"))return null;
  String path=id.substring(12),symbol;int frame,bg;
  if(path.startsWith("spruce_plate_")){symbol=path.substring(13);frame=0;bg=2;}
  else if(path.startsWith("clear_plate_")){symbol=path.substring(12);frame=0;bg=0;}
  else if(path.startsWith("plate_")){symbol=path.substring(6);frame=1;bg=1;}
  else return null;
  if(!PlateCatalog.SYMBOLS.contains(symbol))return null;
  PlateTint tint=PlateTint.NONE;
  for(var t:PlateTint.values())if(t.getSerializedName().equals(properties.getStringOr("tint",""))){tint=t;break;}
  return new PlateAppearance(PlateCatalog.SYMBOLS.indexOf(symbol),frame,bg,tint,"true".equals(properties.getStringOr("glow","")));
 }
 public static void item(CompoundTag item) {
  String id=item.getStringOr("id","");var components=item.getCompoundOrEmpty("components");
  if(id.equals(CANONICAL))return;
  var properties=components.getCompoundOrEmpty("minecraft:block_state");
  var appearance=old(id,properties);if(appearance==null)return;
  // The previous single-item prototype already stores the complete appearance.
  // Rename its ID without replacing that data or any unrelated components.
  if(components.contains(COMPONENT)){item.putString("id",CANONICAL);return;}
  item.putString("id",CANONICAL);components=components.copy();components.remove("minecraft:block_state");components.put(COMPONENT,appearance.save());item.put("components",components);
 }
 public static void chunk(CompoundTag chunk) {
  int chunkX=chunk.getIntOr("xPos",0),chunkZ=chunk.getIntOr("zPos",0);
  var entities=chunk.getListOrEmpty("block_entities");var occupied=new HashSet<String>();
  for(int i=0;i<entities.size();i++){var n=entities.getCompoundOrEmpty(i);occupied.add(n.getIntOr("x",0)+","+n.getIntOr("y",0)+","+n.getIntOr("z",0));}
  boolean changed=false;var sections=chunk.getListOrEmpty("sections");
  for(int section=0;section<sections.size();section++) {
   var part=sections.getCompoundOrEmpty(section);var states=part.getCompoundOrEmpty("block_states");var palette=states.getListOrEmpty("palette");
   var legacy=new HashMap<Integer,PlateAppearance>();
   for(int i=0;i<palette.size();i++) {
    var entry=palette.getCompoundOrEmpty(i);String id=entry.getStringOr("Name","");var props=entry.getCompoundOrEmpty("Properties");
    if(id.equals(CANONICAL))continue;
    if(id.equals(PREVIOUS) && !props.contains("tint")) {
     // Existing block entities are retained by the occupied-position check.
     // Supply a default only if a previous plate is missing its block entity.
     legacy.put(i,new PlateAppearance(0,1,1,PlateTint.NONE,"true".equals(props.getStringOr("glow",""))));continue;
    }
    var a=old(id,props);if(a==null)continue;
    legacy.put(i,a);
   }
   if(legacy.isEmpty())continue;
   int bits=Math.max(4,32-Integer.numberOfLeadingZeros(palette.size()-1)),perLong=64/bits;long mask=(1L<<bits)-1;long[] packed=states.getLongArray("data").orElse(new long[0]);
   if(palette.size()>1 && packed.length<(4096+perLong-1)/perLong)continue; // Do not rewrite malformed sections.
   for(int cell=0;cell<4096;cell++) {
    int index=palette.size()==1?0:(int)((packed[cell/perLong]>>>((cell%perLong)*bits))&mask);var a=legacy.get(index);if(a==null)continue;
    int x=(chunkX<<4)+(cell&15),y=(part.getIntOr("Y",0)<<4)+((cell>>8)&15),z=(chunkZ<<4)+((cell>>4)&15);
    if(!occupied.add(x+","+y+","+z))continue;
    var be=new CompoundTag();be.putString("id","sign_plates:plate");be.putInt("x",x);be.putInt("y",y);be.putInt("z",z);be.put("appearance",a.save());entities.add(be);
   }
   for(int index:legacy.keySet()) {
    var entry=palette.getCompoundOrEmpty(index);entry.putString("Name",CANONICAL);var props=entry.getCompoundOrEmpty("Properties").copy();props.remove("tint");entry.put("Properties",props);
   }
   changed=true;
  }
  if(changed) {
   chunk.put("block_entities",entities);
   // Old glowing plates emitted block light. Rebuild cached lighting after
   // migration so their removed light sources do not linger in saved chunks.
   chunk.putBoolean("isLightOn",false);
  }
 }
}

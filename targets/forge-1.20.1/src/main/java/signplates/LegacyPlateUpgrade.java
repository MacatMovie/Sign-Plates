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
  for(var t:PlateTint.values())if(t.getSerializedName().equals(properties.getString("tint"))){tint=t;break;}
  return new PlateAppearance(PlateCatalog.SYMBOLS.indexOf(symbol),frame,bg,tint,"true".equals(properties.getString("glow")));
 }
 public static void item(CompoundTag item) {
  String id=item.getString("id");var components=item.getCompound("tag");
  if(id.equals(CANONICAL))return;
  var properties=components.getCompound("BlockStateTag");
  var appearance=old(id,properties);if(appearance==null)return;
  // The previous single-item prototype already stores the complete appearance.
  // Rename its ID without replacing that data or any unrelated components.
  if(components.contains(COMPONENT)){item.putString("id",CANONICAL);return;}
  item.putString("id",CANONICAL);components=components.copy();components.remove("BlockStateTag");components.put(COMPONENT,appearance.save());item.put("tag",components);
 }
 public static void chunk(CompoundTag chunk) {
  int chunkX=chunk.getInt("xPos"),chunkZ=chunk.getInt("zPos");
  var entities=chunk.getList("block_entities",Tag.TAG_COMPOUND);var occupied=new HashSet<String>();
  for(int i=0;i<entities.size();i++){var n=entities.getCompound(i);occupied.add(n.getInt("x")+","+n.getInt("y")+","+n.getInt("z"));}
  boolean changed=false;var sections=chunk.getList("sections",Tag.TAG_COMPOUND);
  for(int section=0;section<sections.size();section++) {
   var part=sections.getCompound(section);var states=part.getCompound("block_states");var palette=states.getList("palette",Tag.TAG_COMPOUND);
   var legacy=new HashMap<Integer,PlateAppearance>();
   for(int i=0;i<palette.size();i++) {
    var entry=palette.getCompound(i);String id=entry.getString("Name");var props=entry.getCompound("Properties");
    if(id.equals(CANONICAL))continue;
    if(id.equals(PREVIOUS) && !props.contains("tint")) {
     // Existing block entities are retained by the occupied-position check.
     // Supply a default only if a previous plate is missing its block entity.
     legacy.put(i,new PlateAppearance(0,1,1,PlateTint.NONE,"true".equals(props.getString("glow"))));continue;
    }
    var a=old(id,props);if(a==null)continue;
    legacy.put(i,a);
   }
   if(legacy.isEmpty())continue;
   int bits=Math.max(4,32-Integer.numberOfLeadingZeros(palette.size()-1)),perLong=64/bits;long mask=(1L<<bits)-1;long[] packed=states.getLongArray("data");
   if(palette.size()>1 && packed.length<(4096+perLong-1)/perLong)continue; // Do not rewrite malformed sections.
   for(int cell=0;cell<4096;cell++) {
    int index=palette.size()==1?0:(int)((packed[cell/perLong]>>>((cell%perLong)*bits))&mask);var a=legacy.get(index);if(a==null)continue;
    int x=(chunkX<<4)+(cell&15),y=(part.getInt("Y")<<4)+((cell>>8)&15),z=(chunkZ<<4)+((cell>>4)&15);
    if(!occupied.add(x+","+y+","+z))continue;
    var be=new CompoundTag();be.putString("id","sign_plates:plate");be.putInt("x",x);be.putInt("y",y);be.putInt("z",z);be.put("appearance",a.save());entities.add(be);
   }
   for(int index:legacy.keySet()) {
    var entry=palette.getCompound(index);entry.putString("Name",CANONICAL);var props=entry.getCompound("Properties").copy();props.remove("tint");entry.put("Properties",props);
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

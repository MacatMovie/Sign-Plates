import signplates.*;
import net.minecraft.nbt.*;
/** Run against the target's Minecraft/NeoForge development classpath. */
public class AppearanceDataChecks {
 private static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 private static CompoundTag properties(){var p=new CompoundTag();p.putString("facing","east");p.putString("face","wall");p.putString("waterlogged","true");p.putString("tint","red");p.putString("glow","true");return p;}
 private static CompoundTag state(String id){var p=new CompoundTag();p.putString("Name",id);p.put("Properties",properties());return p;}
 public static void main(String[] args) {
  int roundtrips=0;
  for(int s=0;s<PlateCatalog.SYMBOLS.size();s++)for(int f=0;f<PlateLayers.FRAMES.size();f++)for(int bg=0;bg<PlateLayers.BACKGROUNDS.size();bg++)for(var tint:PlateTint.values())for(boolean glow:new boolean[]{false,true}) {
   var a=new PlateAppearance(s,f,bg,tint,glow);
   check(a.equals(PlateAppearance.load(a.save())),"NBT roundtrip");
   var encoded=PlateAppearance.CODEC.encodeStart(NbtOps.INSTANCE,a).getOrThrow();
   check(a.equals(PlateAppearance.CODEC.parse(NbtOps.INSTANCE,encoded).getOrThrow()),"component codec roundtrip");roundtrips++;
  }
  check(new PlateAppearance(-1,999,-2,null,false).equals(new PlateAppearance(0,3,0,PlateTint.NONE,false)),"invalid network values clamped");
  check(PlateAppearance.CODEC.parse(NbtOps.INSTANCE,new CompoundTag()).getOrThrow().equals(PlateAppearance.DEFAULT),"missing fields default");
  var buffer=new net.minecraft.network.RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(),net.minecraft.core.RegistryAccess.EMPTY);
  try {
   for(int command:new int[]{0,85,86,90,93,94,95,127,128,129,1000}) {
    buffer.clear();var payload=new signplates.menu.PlateEditPayload(300,command);
    signplates.menu.PlateEditPayload.CODEC.encode(buffer,payload);
    check(payload.equals(signplates.menu.PlateEditPayload.CODEC.decode(buffer)),"menu command VarInt roundtrip");
   }
   for(var tint:PlateTint.values()) {
    buffer.clear();var a=new PlateAppearance(85,3,2,tint,true);PlateAppearance.STREAM_CODEC.encode(buffer,a);
    check(a.equals(PlateAppearance.STREAM_CODEC.decode(buffer)),"appearance network roundtrip");
   }
  } finally {buffer.release();}
  for(String prefix:new String[]{"plate_","spruce_plate_","clear_plate_"})for(String symbol:PlateCatalog.SYMBOLS) {
   var item=new CompoundTag();item.putString("id","sign_plates:"+prefix+symbol);item.putInt("count",12);var components=new CompoundTag();components.put("minecraft:block_state",properties());components.putString("minecraft:custom_name","preserve");item.put("components",components);
   LegacyPlateUpgrade.item(item);var upgraded=item.copy();LegacyPlateUpgrade.item(item);
   check(item.getString("id").equals("sign_plates:sign_plate"),"canonical item ID");
   check(item.equals(upgraded),"item upgrade idempotent");check(item.getInt("count")==12,"stack count preserved");
   var a=PlateAppearance.load(item.getCompound("components").getCompound("sign_plates:appearance"));
   check(a.symbol()==PlateCatalog.SYMBOLS.indexOf(symbol)&&a.tint()==PlateTint.RED&&a.glow(),"legacy item appearance");
   check(item.getCompound("components").getString("minecraft:custom_name").equals("preserve"),"unrelated component preserved");
  }
  // Exercise packed palettes across the padded-long boundary, negative chunks,
  // negative section Y, and 4/5/6-bit local palette widths.
  for(int size:new int[]{1,2,16,17,32,33,64}) {
   var chunk=new CompoundTag();chunk.putInt("xPos",-3);chunk.putInt("zPos",2);
   var section=new CompoundTag();section.putByte("Y",(byte)-2);var states=new CompoundTag();var palette=new ListTag();
   for(int i=0;i<size;i++)palette.add(i==size-1?state("sign_plates:spruce_plate_a"):state("minecraft:stone"));states.put("palette",palette);
   int bits=Math.max(4,32-Integer.numberOfLeadingZeros(size-1)),perLong=64/bits;long[] data=new long[(4096+perLong-1)/perLong];int expected=0;
   for(int cell=0;cell<4096;cell++){int entry=size==1?0:(cell%7==0?size-1:0);if(entry==size-1)expected++;data[cell/perLong]|=(long)entry<<((cell%perLong)*bits);}
   if(size>1)states.putLongArray("data",data);section.put("block_states",states);var sections=new ListTag();sections.add(section);chunk.put("sections",sections);
   LegacyPlateUpgrade.chunk(chunk);var upgraded=chunk.copy();LegacyPlateUpgrade.chunk(chunk);check(chunk.equals(upgraded),"chunk upgrade idempotent");
   var entities=chunk.getList("block_entities",Tag.TAG_COMPOUND);check(entities.size()==expected,"palette width "+bits+" count");
   for(int i=0;i<entities.size();i++){var be=entities.getCompound(i);check(be.getInt("x")>=-48&&be.getInt("x")<-32&&be.getInt("y")>=-32&&be.getInt("y")<-16&&be.getInt("z")>=32&&be.getInt("z")<48,"coordinates");var a=PlateAppearance.load(be.getCompound("appearance"));check(a.symbol()==1&&a.frame()==0&&a.background()==2&&a.tint()==PlateTint.RED&&a.glow(),"legacy chunk appearance");}
   var props=palette.getCompound(size-1).getCompound("Properties");check(!props.contains("tint")&&props.getString("facing").equals("east")&&props.getString("waterlogged").equals("true"),"placement preserved");
  }
  var unrelated=state("minecraft:stone");var original=unrelated.copy();LegacyPlateUpgrade.item(unrelated);check(unrelated.equals(original),"other data untouched");
  System.out.println("PASS: "+roundtrips+" appearance NBT/component roundtrips; network appearance/menu commands; 258 legacy item variants; packed chunk migration and idempotence.");
 }
}

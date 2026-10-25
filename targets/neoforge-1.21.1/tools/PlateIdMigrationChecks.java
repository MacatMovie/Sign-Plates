import signplates.*;
import net.minecraft.nbt.*;
/** Exercises the already-customized single-block prototype's ID migration. */
public class PlateIdMigrationChecks {
 static final String OLD="sign_plates:plate_empty",NEW="sign_plates:sign_plate";
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 static CompoundTag item(String id){var n=new CompoundTag();n.putString("id",id);n.putInt("count",17);return n;}
 static CompoundTag chunk(boolean existingEntity) {
  var n=new CompoundTag();n.putInt("xPos",-1);n.putInt("zPos",2);n.putBoolean("isLightOn",true);
  var air=new CompoundTag();air.putString("Name","minecraft:air");var plate=new CompoundTag();plate.putString("Name",OLD);
  var props=new CompoundTag();props.putString("face","ceiling");props.putString("facing","west");props.putString("waterlogged","true");props.putString("glow","true");plate.put("Properties",props);
  var palette=new ListTag();palette.add(air);palette.add(plate);var states=new CompoundTag();states.put("palette",palette);long[] packed=new long[256];packed[0]=1;states.putLongArray("data",packed);
  var section=new CompoundTag();section.putByte("Y",(byte)-1);section.put("block_states",states);var sections=new ListTag();sections.add(section);n.put("sections",sections);
  var entities=new ListTag();if(existingEntity) {
   var be=new CompoundTag();be.putString("id","sign_plates:plate");be.putInt("x",-16);be.putInt("y",-16);be.putInt("z",32);be.putString("custom_field","preserve");
   be.put("appearance",new PlateAppearance(1,3,0,PlateTint.RED,true).save());entities.add(be);
  }
  n.put("block_entities",entities);return n;
 }
 public static void main(String[] args) {
  int count=0;
  for(int s=0;s<PlateCatalog.SYMBOLS.size();s++)for(int f=0;f<4;f++)for(int b=0;b<3;b++)for(boolean glow:new boolean[]{false,true}) {
   var stack=item(OLD);var components=new CompoundTag();var a=new PlateAppearance(s,f,b,PlateTint.RED,glow);
   components.put("sign_plates:appearance",a.save());components.putString("minecraft:custom_name","keep");stack.put("components",components);
   LegacyPlateUpgrade.item(stack);check(stack.getString("id").equals(NEW),"item rename");check(stack.getInt("count")==17,"stack count");
   check(stack.getCompound("components").equals(components),"all appearance and unrelated components preserved");
   var upgraded=stack.copy();LegacyPlateUpgrade.item(stack);check(stack.equals(upgraded),"item idempotence");count++;
  }
  var blank=item(OLD);LegacyPlateUpgrade.item(blank);check(blank.getString("id").equals(NEW),"default prototype stack");check(PlateAppearance.load(blank.getCompound("components").getCompound("sign_plates:appearance")).equals(PlateAppearance.DEFAULT),"default appearance");
  for(boolean existing:new boolean[]{false,true}) {
   var n=chunk(existing);var originalEntities=n.getList("block_entities",Tag.TAG_COMPOUND).copy();LegacyPlateUpgrade.chunk(n);
   var state=n.getList("sections",Tag.TAG_COMPOUND).getCompound(0).getCompound("block_states").getList("palette",Tag.TAG_COMPOUND).getCompound(1);
   check(state.getString("Name").equals(NEW),"chunk palette ID");check(state.getCompound("Properties").getString("facing").equals("west"),"orientation retained");
   var entities=n.getList("block_entities",Tag.TAG_COMPOUND);check(entities.size()==1,"no duplicate block entity");
   if(existing)check(entities.equals(originalEntities),"customized block entity untouched");
   else check(PlateAppearance.load(entities.getCompound(0).getCompound("appearance")).glow(),"missing entity recovered from old state");
   check(!n.getBoolean("isLightOn"),"old emitted block light scheduled for rebuilding");
   var upgraded=n.copy();LegacyPlateUpgrade.chunk(n);check(n.equals(upgraded),"chunk idempotence");
  }
  var other=item("another_mod:plate_empty");var components=new CompoundTag();components.put("sign_plates:appearance",PlateAppearance.DEFAULT.save());other.put("components",components);var copy=other.copy();LegacyPlateUpgrade.item(other);check(other.equals(copy),"unrelated item unchanged");
  System.out.println("PASS: "+count+" customized item migrations, default stacks, existing/missing block entities, chunk relighting and idempotence.");
 }
}

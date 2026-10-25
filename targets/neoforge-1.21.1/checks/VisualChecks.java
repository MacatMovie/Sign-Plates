import java.util.*;import signplates.*;import signplates.menu.PlateEditPayload;import net.minecraft.SharedConstants;import net.minecraft.server.Bootstrap;import net.minecraft.nbt.*;import net.minecraft.core.RegistryAccess;import net.minecraft.network.RegistryFriendlyByteBuf;import net.minecraft.world.item.*;import io.netty.buffer.Unpooled;
public class VisualChecks {
 static int checks;
 static void check(boolean b,String label){checks++;if(!b)throw new AssertionError(label);}
 static Map<Item,Integer> costs(PlateAppearance a,PlateAppearance b){var result=new HashMap<Item,Integer>();for(var c:VisualCost.between(a,b))result.put(c.item(),c.amount());return result;}
 public static void main(String[] args){
  SharedConstants.tryDetectVersion();Bootstrap.bootStrap();
  var base=PlateAppearance.DEFAULT;
  check(PlateAppearance.MAX_LIGHT==15,"old 13 + 2 maximum");
  check(PlateAppearance.LIGHT_LEVELS.equals(List.of(0,8,12,15)),"four actual light levels");
  for(int i=0;i<16;i++)for(int option=0;option<4;option++){
   int level=PlateAppearance.LIGHT_LEVELS.get(option);
   check(VisualCost.lightCost(i,level)==(i==level?0:option),"selected option cost "+i+" "+option);
  }
  for(int level=0;level<16;level++)check(PlateAppearance.isLightOption(level)==List.of(0,8,12,15).contains(level),"new designer choices restricted to presets");
  check(costs(base,base).isEmpty(),"unchanged is free");
  for(int s=0;s<86;s++)check(costs(base,new PlateAppearance(s,1,1,PlateTint.NONE,false)).isEmpty(),"free symbol "+s);
  for(int f=0;f<5;f++){
   var a=new PlateAppearance(0,f,1,PlateTint.NONE,false);var c=costs(base,a);
   check(c.size()==(f==0||f==1?0:1),"frame cost size "+f);
   if(f>=2)check(c.get(VisualCost.frame(f))==1,"one frame material "+f);
  }
  check(VisualCost.frame(1)==Items.IRON_INGOT&&VisualCost.frame(2)==Items.COPPER_INGOT&&VisualCost.frame(3)==Items.GOLD_INGOT&&VisualCost.frame(4)==Items.COAL,"frame material mapping");
  for(int b=0;b<36;b++){
   var a=new PlateAppearance(0,1,b,PlateTint.NONE,false);var c=costs(base,a);
   check(c.size()==(b<2?0:1),"background cost size "+b);
   if(b>1){check(VisualCost.background(b)!=Items.AIR,"background item exists "+b);check(c.get(VisualCost.background(b))==1,"one background block "+b);}
  }
  check(VisualCost.background(2)==Items.SPRUCE_LOG,"spruce log");check(VisualCost.background(10)==Items.BAMBOO_BLOCK,"bamboo block");check(VisualCost.background(11)==Items.CRIMSON_STEM,"crimson stem");check(VisualCost.background(12)==Items.WARPED_STEM,"warped stem");
  var draft=new PlateAppearance(12,4,13,PlateTint.RED,true,15);var c=costs(base,draft);
  check(c.size()==5,"all five cost groups");check(c.get(Items.COAL)==1&&c.get(Items.WHITE_CONCRETE)==1&&c.get(Items.RED_DYE)==1&&c.get(Items.GLOW_INK_SAC)==1&&c.get(Items.GLOWSTONE_DUST)==3,"combined materials");
  check(costs(draft,new PlateAppearance(12,4,13,PlateTint.NONE,false,0)).isEmpty(),"effect clearing free");
  check(costs(draft,new PlateAppearance(13,4,13,PlateTint.RED,true,15)).isEmpty(),"symbol-only change free");
  check(costs(new PlateAppearance(0,0,0,PlateTint.NONE,false,15),new PlateAppearance(0,0,0,PlateTint.NONE,false,8)).get(Items.GLOWSTONE_DUST)==1,"lower option uses selected option price");
  check(costs(draft,draft).isEmpty(),"repeated Apply charges nothing");
  check(PlateAppearance.LIGHT_LEVELS.get(PlateAppearance.lightOption(13))==15,"legacy slider clipboard selects next available preset");
  for(int symbol=0;symbol<86;symbol++)for(int frame=0;frame<5;frame++)for(int light=0;light<16;light++){
   var a=new PlateAppearance(symbol,frame,(symbol+frame)%36,PlateTint.RED,symbol%2==0,light);
   check(PlateAppearance.load(a.save()).equals(a),"NBT retains light/coal");
   var tag=PlateAppearance.CODEC.encodeStart(NbtOps.INSTANCE,a).getOrThrow();check(PlateAppearance.CODEC.parse(NbtOps.INSTANCE,tag).getOrThrow().equals(a),"codec retains light");
   var b=new RegistryFriendlyByteBuf(Unpooled.buffer(),RegistryAccess.EMPTY);var packet=new PlateEditPayload(17,a);PlateEditPayload.CODEC.encode(b,packet);check(PlateEditPayload.CODEC.decode(b).equals(packet)&&b.readableBytes()==0,"atomic wire round trip");b.release();
  }
  var old=base.save();old.remove("light");old.putBoolean("glow",true);check(PlateAppearance.load(old).light()==0&&PlateAppearance.load(old).glow(),"old emission retained without light");
  check(new PlateAppearance(0,0,0,PlateTint.NONE,false,99).light()==15,"clamp max");check(new PlateAppearance(0,0,0,PlateTint.NONE,false,-1).light()==0,"clamp min");
  System.out.println("PASS "+checks+" visual cost, material mapping, appearance, migration default and atomic packet checks");
 }
}

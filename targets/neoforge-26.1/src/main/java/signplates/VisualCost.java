package signplates;
import java.util.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
/** Shared client/server cost ledger, including the selected Glow option's dust cost. */
public final class VisualCost {
 public record Material(Item item,int amount){}
 public static Item frame(int index){return switch(PlateLayers.FRAMES.get(index).id()){case "iron"->Items.IRON_INGOT;case "copper"->Items.COPPER_INGOT;case "gold"->Items.GOLD_INGOT;case "coal"->Items.COAL;default->Items.AIR;};}
 public static Item background(int index){
  var layer=PlateLayers.BACKGROUNDS.get(index);if(layer.id().equals("none")||layer.id().equals("iron"))return Items.AIR;
  String id=layer.texture().substring("minecraft:block/".length());
  if(id.endsWith("_top"))id=id.substring(0,id.length()-4);
  return BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(id));
 }
 public static List<Material> between(PlateAppearance applied,PlateAppearance draft){
  var cost=new LinkedHashMap<Item,Integer>();
  if(applied.background()!=draft.background())add(cost,background(draft.background()),1);
  if(applied.frame()!=draft.frame())add(cost,frame(draft.frame()),1);
  if(applied.tint()!=draft.tint()&&draft.tint()!=PlateTint.NONE)add(cost,PlateStyle.dye(draft.tint()),1);
  if(draft.glow()&&!applied.glow())add(cost,Items.GLOW_INK_SAC,1);
  add(cost,Items.GLOWSTONE_DUST,lightCost(applied.light(),draft.light()));
  return cost.entrySet().stream().map(e->new Material(e.getKey(),e.getValue())).toList();
 }
 public static int lightCost(int applied,int draft){return applied==draft?0:Math.max(0,PlateAppearance.LIGHT_LEVELS.indexOf(draft));}
 private static void add(Map<Item,Integer> cost,Item item,int count){if(item!=Items.AIR&&count>0)cost.merge(item,count,Integer::sum);}
 private VisualCost(){}
}

package signplates;
import java.util.List;
/** Appearance choices are data, never blockstate properties. Keep IDs stable. */
public final class PlateLayers {
 public record Layer(String id,String texture) {}
 public static final List<Layer> FRAMES=List.of(new Layer("none",""),new Layer("iron","sign_plates:block/frames/iron"),new Layer("copper","sign_plates:block/frames/copper"),new Layer("gold","sign_plates:block/frames/gold"),new Layer("coal","sign_plates:block/frames/coal"),new Layer("dyed_white","sign_plates:block/frames/dyed_white"), new Layer("dyed_orange","sign_plates:block/frames/dyed_orange"), new Layer("dyed_magenta","sign_plates:block/frames/dyed_magenta"), new Layer("dyed_light_blue","sign_plates:block/frames/dyed_light_blue"), new Layer("dyed_yellow","sign_plates:block/frames/dyed_yellow"), new Layer("dyed_lime","sign_plates:block/frames/dyed_lime"), new Layer("dyed_pink","sign_plates:block/frames/dyed_pink"), new Layer("dyed_gray","sign_plates:block/frames/dyed_gray"), new Layer("dyed_light_gray","sign_plates:block/frames/dyed_light_gray"), new Layer("dyed_cyan","sign_plates:block/frames/dyed_cyan"), new Layer("dyed_purple","sign_plates:block/frames/dyed_purple"), new Layer("dyed_blue","sign_plates:block/frames/dyed_blue"), new Layer("dyed_brown","sign_plates:block/frames/dyed_brown"), new Layer("dyed_green","sign_plates:block/frames/dyed_green"), new Layer("dyed_red","sign_plates:block/frames/dyed_red"), new Layer("dyed_black","sign_plates:block/frames/dyed_black"), new Layer("dyed_maroon","sign_plates:block/frames/dyed_maroon"), new Layer("dyed_rose","sign_plates:block/frames/dyed_rose"), new Layer("dyed_coral","sign_plates:block/frames/dyed_coral"), new Layer("dyed_indigo","sign_plates:block/frames/dyed_indigo"), new Layer("dyed_navy","sign_plates:block/frames/dyed_navy"), new Layer("dyed_slate","sign_plates:block/frames/dyed_slate"), new Layer("dyed_olive","sign_plates:block/frames/dyed_olive"), new Layer("dyed_amber","sign_plates:block/frames/dyed_amber"), new Layer("dyed_beige","sign_plates:block/frames/dyed_beige"), new Layer("dyed_teal","sign_plates:block/frames/dyed_teal"), new Layer("dyed_mint","sign_plates:block/frames/dyed_mint"), new Layer("dyed_aqua","sign_plates:block/frames/dyed_aqua"), new Layer("dyed_verdant","sign_plates:block/frames/dyed_verdant"), new Layer("dyed_forest","sign_plates:block/frames/dyed_forest"), new Layer("dyed_ginger","sign_plates:block/frames/dyed_ginger"), new Layer("dyed_tan","sign_plates:block/frames/dyed_tan"));
 public static final List<Layer> BACKGROUNDS=List.of(
  new Layer("none",""),
  new Layer("iron","sign_plates:block/backgrounds/iron"),
  new Layer("spruce","minecraft:block/spruce_log_top"),
  new Layer("oak","minecraft:block/oak_log_top"),
  new Layer("birch","minecraft:block/birch_log_top"),
  new Layer("jungle","minecraft:block/jungle_log_top"),
  new Layer("acacia","minecraft:block/acacia_log_top"),
  new Layer("dark_oak","minecraft:block/dark_oak_log_top"),
  new Layer("mangrove","minecraft:block/mangrove_log_top"),
  new Layer("cherry","minecraft:block/cherry_log_top"),
  new Layer("bamboo","minecraft:block/bamboo_block_top"),
  new Layer("crimson","minecraft:block/crimson_stem_top"),
  new Layer("warped","minecraft:block/warped_stem_top"),
  new Layer("white_concrete","minecraft:block/white_concrete"),
  new Layer("orange_concrete","minecraft:block/orange_concrete"),
  new Layer("magenta_concrete","minecraft:block/magenta_concrete"),
  new Layer("light_blue_concrete","minecraft:block/light_blue_concrete"),
  new Layer("yellow_concrete","minecraft:block/yellow_concrete"),
  new Layer("lime_concrete","minecraft:block/lime_concrete"),
  new Layer("pink_concrete","minecraft:block/pink_concrete"),
  new Layer("gray_concrete","minecraft:block/gray_concrete"),
  new Layer("light_gray_concrete","minecraft:block/light_gray_concrete"),
  new Layer("cyan_concrete","minecraft:block/cyan_concrete"),
  new Layer("purple_concrete","minecraft:block/purple_concrete"),
  new Layer("blue_concrete","minecraft:block/blue_concrete"),
  new Layer("brown_concrete","minecraft:block/brown_concrete"),
  new Layer("green_concrete","minecraft:block/green_concrete"),
  new Layer("red_concrete","minecraft:block/red_concrete"),
  new Layer("black_concrete","minecraft:block/black_concrete"),
  new Layer("nether_bricks","minecraft:block/nether_bricks"),
  new Layer("stone_bricks","minecraft:block/stone_bricks"),
  new Layer("deepslate_bricks","minecraft:block/deepslate_bricks"),
  new Layer("mud_bricks","minecraft:block/mud_bricks"),
  new Layer("bricks","minecraft:block/bricks"),
  new Layer("end_stone_bricks","minecraft:block/end_stone_bricks"),
  new Layer("prismarine_bricks","minecraft:block/prismarine_bricks"),
  new Layer("dye_depot_maroon_concrete","dye_depot:block/maroon_concrete"),
  new Layer("dye_depot_rose_concrete","dye_depot:block/rose_concrete"),
  new Layer("dye_depot_coral_concrete","dye_depot:block/coral_concrete"),
  new Layer("dye_depot_indigo_concrete","dye_depot:block/indigo_concrete"),
  new Layer("dye_depot_navy_concrete","dye_depot:block/navy_concrete"),
  new Layer("dye_depot_slate_concrete","dye_depot:block/slate_concrete"),
  new Layer("dye_depot_olive_concrete","dye_depot:block/olive_concrete"),
  new Layer("dye_depot_amber_concrete","dye_depot:block/amber_concrete"),
  new Layer("dye_depot_beige_concrete","dye_depot:block/beige_concrete"),
  new Layer("dye_depot_teal_concrete","dye_depot:block/teal_concrete"),
  new Layer("dye_depot_mint_concrete","dye_depot:block/mint_concrete"),
  new Layer("dye_depot_aqua_concrete","dye_depot:block/aqua_concrete"),
  new Layer("dye_depot_verdant_concrete","dye_depot:block/verdant_concrete"),
  new Layer("dye_depot_forest_concrete","dye_depot:block/forest_concrete"),
  new Layer("dye_depot_ginger_concrete","dye_depot:block/ginger_concrete"),
  new Layer("dye_depot_tan_concrete","dye_depot:block/tan_concrete")
 );

 /** Extra colors are registered only by Dye Depot. Do not reindex saved appearance IDs. */
 public static PlateTint dyedFrame(int index) {
  String name=FRAMES.get(index).id();
  if(!name.startsWith("dyed_"))return PlateTint.NONE;
  String color=name.substring("dyed_".length());
  for(PlateTint tint:PlateTint.values())if(tint.getSerializedName().equals(color))return tint;
  return PlateTint.NONE;
 }
 public static boolean frameAvailable(int index) {
  PlateTint tint=dyedFrame(index);
  return tint==PlateTint.NONE || PlateStyle.dye(tint)!=net.minecraft.world.item.Items.AIR;
 }
 public static boolean backgroundAvailable(int index) {
  String texture=BACKGROUNDS.get(index).texture();
  if(!texture.startsWith("dye_depot:block/"))return true;
  String color=texture.substring("dye_depot:block/".length()).replace("_concrete","");
  for(PlateTint tint:PlateTint.values())if(tint.getSerializedName().equals(color))return PlateStyle.dye(tint)!=net.minecraft.world.item.Items.AIR;
  return false;
 }
 public static int index(List<Layer> layers,String id) {for(int i=0;i<layers.size();i++) if(layers.get(i).id().equals(id))return i;return 0;}
 private PlateLayers() {}
}

package signplates;
import java.util.List;
/** Appearance choices are data, never blockstate properties. Keep IDs stable. */
public final class PlateLayers {
 public record Layer(String id,String texture) {}
 public static final List<Layer> FRAMES=List.of(new Layer("none",""),new Layer("iron","sign_plates:block/frames/iron"),new Layer("copper","sign_plates:block/frames/copper"),new Layer("gold","sign_plates:block/frames/gold"),new Layer("coal","sign_plates:block/frames/coal"));
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
  new Layer("prismarine_bricks","minecraft:block/prismarine_bricks")
 );
 public static int index(List<Layer> layers,String id) {for(int i=0;i<layers.size();i++) if(layers.get(i).id().equals(id))return i;return 0;}
 private PlateLayers() {}
}

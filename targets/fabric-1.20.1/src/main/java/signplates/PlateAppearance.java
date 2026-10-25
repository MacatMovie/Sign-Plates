package signplates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
/** Immutable snapshot shared by item components, menus and chunk model data. */
public record PlateAppearance(int symbol,int frame,int background,PlateTint tint,boolean glow,int light) {
 public static final int MAX_LIGHT=15;
 public static final java.util.List<Integer> LIGHT_LEVELS=java.util.List.of(0,8,12,15);
 /** Existing saved slider values remain valid; new designer choices use these four presets. */
 public static boolean isLightOption(int level){return LIGHT_LEVELS.contains(level);}
 public static int lightOption(int level){return level<=0?0:level<=8?1:level<=12?2:3;}
 public PlateAppearance(int symbol,int frame,int background,PlateTint tint,boolean glow){this(symbol,frame,background,tint,glow,0);}
 public static final PlateAppearance DEFAULT=new PlateAppearance(0,1,1,PlateTint.NONE,false);
 public PlateAppearance {
  symbol=Math.max(0,Math.min(symbol,PlateCatalog.SYMBOLS.size()-1));
  frame=Math.max(0,Math.min(frame,PlateLayers.FRAMES.size()-1));
  background=Math.max(0,Math.min(background,PlateLayers.BACKGROUNDS.size()-1));
  light=Math.max(0,Math.min(light,MAX_LIGHT));
  if(tint==null)tint=PlateTint.NONE;
 }
 public static final Codec<PlateAppearance> CODEC=RecordCodecBuilder.create(i->i.group(
  Codec.STRING.optionalFieldOf("symbol","empty").forGetter(a->PlateCatalog.SYMBOLS.get(a.symbol())),
  Codec.STRING.optionalFieldOf("frame","iron").forGetter(a->PlateLayers.FRAMES.get(a.frame()).id()),
  Codec.STRING.optionalFieldOf("background","iron").forGetter(a->PlateLayers.BACKGROUNDS.get(a.background()).id()),
  Codec.STRING.optionalFieldOf("tint","none").forGetter(a->a.tint().getSerializedName()),
  Codec.BOOL.optionalFieldOf("glow",false).forGetter(PlateAppearance::glow),
  Codec.INT.optionalFieldOf("light",0).forGetter(PlateAppearance::light)
 ).apply(i,PlateAppearance::fromIds));
 public static PlateAppearance fromIds(String symbol,String frame,String background,String tint,boolean glow,int light) {
  PlateTint colour=PlateTint.NONE;for(var t:PlateTint.values())if(t.getSerializedName().equals(tint)){colour=t;break;}
  return new PlateAppearance(PlateCatalog.SYMBOLS.indexOf(symbol),PlateLayers.index(PlateLayers.FRAMES,frame),PlateLayers.index(PlateLayers.BACKGROUNDS,background),colour,glow,light);
 }
 public static void write(net.minecraft.network.FriendlyByteBuf b,PlateAppearance a){b.writeVarInt(a.symbol());b.writeVarInt(a.frame());b.writeVarInt(a.background());b.writeVarInt(a.tint().ordinal());b.writeBoolean(a.glow());b.writeVarInt(a.light());}
 public static PlateAppearance read(net.minecraft.network.FriendlyByteBuf b){int s=b.readVarInt(),f=b.readVarInt(),bg=b.readVarInt(),t=b.readVarInt();return new PlateAppearance(s,f,bg,PlateTint.values()[Math.max(0,Math.min(t,PlateTint.values().length-1))],b.readBoolean(),b.readVarInt());}
 public CompoundTag save() {var n=new CompoundTag();n.putString("symbol",PlateCatalog.SYMBOLS.get(symbol));n.putString("frame",PlateLayers.FRAMES.get(frame).id());n.putString("background",PlateLayers.BACKGROUNDS.get(background).id());n.putString("tint",tint.getSerializedName());n.putBoolean("glow",glow);n.putInt("light",light);return n;}
 public static PlateAppearance load(CompoundTag n) {return fromIds(n.getString("symbol"),n.contains("frame")?n.getString("frame"):"iron",n.contains("background")?n.getString("background"):"iron",n.getString("tint"),n.getBoolean("glow"),n.getInt("light"));}
}

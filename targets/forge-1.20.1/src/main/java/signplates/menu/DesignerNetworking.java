package signplates.menu;
public final class DesignerNetworking {
 private static final String VERSION="designer5";
 public static final net.minecraftforge.network.simple.SimpleChannel CHANNEL=net.minecraftforge.network.NetworkRegistry.newSimpleChannel(new net.minecraft.resources.ResourceLocation("sign_plates","designer5"),()->VERSION,VERSION::equals,VERSION::equals);
 public record Edit(int id,signplates.PlateAppearance appearance){}
 public static void init(){CHANNEL.registerMessage(0,Edit.class,(m,b)->{b.writeVarInt(m.id());signplates.PlateAppearance.write(b,m.appearance());},b->new Edit(b.readVarInt(),signplates.PlateAppearance.read(b)),(m,c)->{var ctx=c.get();ctx.enqueueWork(()->{var player=ctx.getSender();if(player!=null&&player.containerMenu instanceof PlateEditorMenu menu&&menu.containerId==m.id())menu.applyVisuals(player,m.appearance());});ctx.setPacketHandled(true);},java.util.Optional.of(net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER));}
}

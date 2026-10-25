package signplates.menu;
public final class DesignerNetworking {
 public static final net.minecraft.resources.ResourceLocation EDIT=signplates.SignPlatesMod.id("designer5_visuals");
 public static void init(){net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(EDIT,(server,player,handler,buffer,sender)->{int id=buffer.readVarInt();var appearance=signplates.PlateAppearance.read(buffer);server.execute(()->{if(player.containerMenu instanceof PlateEditorMenu menu&&menu.containerId==id)menu.applyVisuals(player,appearance);});});}
}

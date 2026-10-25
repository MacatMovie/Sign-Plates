package signplates.menu;
public final class DesignerNetworking {
 public static void init(){net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(PlateEditPayload.TYPE,PlateEditPayload.CODEC);net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(PlateEditPayload.TYPE,(payload,context)->context.server().execute(()->{var player=context.player();if(player.containerMenu instanceof PlateEditorMenu menu&&menu.containerId==payload.containerId())menu.applyVisuals(player,payload.appearance());}));}
}

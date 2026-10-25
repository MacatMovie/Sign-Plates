package signplates.client;
public final class DesignerPackets {public static void send(int id,signplates.PlateAppearance appearance){signplates.menu.DesignerNetworking.CHANNEL.sendToServer(new signplates.menu.DesignerNetworking.Edit(id,appearance));}}

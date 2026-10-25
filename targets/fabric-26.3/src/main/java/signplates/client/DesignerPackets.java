package signplates.client;
public final class DesignerPackets {public static void send(int id,signplates.PlateAppearance appearance){net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new signplates.menu.PlateEditPayload(id,appearance));}}

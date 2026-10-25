package signplates.client;
public final class DesignerPackets {public static void send(int id,signplates.PlateAppearance appearance){net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new signplates.menu.PlateEditPayload(id,appearance));}}

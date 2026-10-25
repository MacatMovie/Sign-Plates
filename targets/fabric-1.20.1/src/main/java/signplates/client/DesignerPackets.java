package signplates.client;
public final class DesignerPackets {public static void send(int id,signplates.PlateAppearance appearance){var b=net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();b.writeVarInt(id);signplates.PlateAppearance.write(b,appearance);net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(signplates.menu.DesignerNetworking.EDIT,b);}}

package signplates.menu;
import net.minecraft.resources.Identifier;import net.minecraft.network.RegistryFriendlyByteBuf;import net.minecraft.network.codec.StreamCodec;import net.minecraft.network.protocol.common.custom.CustomPacketPayload;import signplates.PlateAppearance;
/** A complete visual draft commits in one server-authorized transaction. */
public record PlateEditPayload(int containerId,PlateAppearance appearance) implements CustomPacketPayload {
 public static final Type<PlateEditPayload> TYPE=new Type<>(Identifier.fromNamespaceAndPath("sign_plates","designer_visuals_v4"));
 public static final StreamCodec<RegistryFriendlyByteBuf,PlateEditPayload> CODEC=StreamCodec.of((b,p)->{b.writeVarInt(p.containerId());PlateAppearance.STREAM_CODEC.encode(b,p.appearance());},b->new PlateEditPayload(b.readVarInt(),PlateAppearance.STREAM_CODEC.decode(b)));
 @Override public Type<PlateEditPayload> type(){return TYPE;}
}

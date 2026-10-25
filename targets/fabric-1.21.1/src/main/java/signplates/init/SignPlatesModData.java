package signplates.init;
import java.util.function.Supplier;import net.minecraft.core.Registry;import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;import signplates.block.PlateBlockEntity;import signplates.*;
public final class SignPlatesModData {
 private static final net.minecraft.core.component.DataComponentType<PlateAppearance> COMPONENT_TYPE=Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,SignPlatesMod.id("appearance"),net.minecraft.core.component.DataComponentType.<PlateAppearance>builder().persistent(PlateAppearance.CODEC).networkSynchronized(PlateAppearance.STREAM_CODEC).build());
 public static final Supplier<net.minecraft.core.component.DataComponentType<PlateAppearance>> APPEARANCE=()->COMPONENT_TYPE;
 public static final Supplier<BlockEntityType<PlateBlockEntity>> PLATE=()->Holder.TYPE;
 private static class Holder {static final BlockEntityType<PlateBlockEntity> TYPE=Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,SignPlatesMod.id("plate"),net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(PlateBlockEntity::new,SignPlatesModBlocks.PLATE.get()).build());}
 public static void init(){APPEARANCE.get(); PLATE.get();}
}

package signplates.init;
import signplates.PlateAppearance;
import signplates.SignPlatesMod;
import signplates.block.PlateBlockEntity;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
public final class SignPlatesModData {
 public static final DeferredRegister<DataComponentType<?>> COMPONENTS=DeferredRegister.create(Registries.DATA_COMPONENT_TYPE,SignPlatesMod.MODID);
 public static final DeferredHolder<DataComponentType<?>,DataComponentType<PlateAppearance>> APPEARANCE=COMPONENTS.register("appearance",()->DataComponentType.<PlateAppearance>builder().persistent(PlateAppearance.CODEC).networkSynchronized(PlateAppearance.STREAM_CODEC).build());
 public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES=DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,SignPlatesMod.MODID);
 public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<PlateBlockEntity>> PLATE=BLOCK_ENTITIES.register("plate",()->BlockEntityType.Builder.of(PlateBlockEntity::new,SignPlatesModBlocks.PLATE.get()).build(null));
}

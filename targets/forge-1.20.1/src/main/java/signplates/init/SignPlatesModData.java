package signplates.init;
import signplates.PlateAppearance;
import signplates.SignPlatesMod;
import signplates.block.PlateBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
public final class SignPlatesModData {
 public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES=DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,SignPlatesMod.MODID);
 public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE=BLOCK_ENTITIES.register("plate",()->BlockEntityType.Builder.of(PlateBlockEntity::new,SignPlatesModBlocks.PLATE.get()).build(null));
}

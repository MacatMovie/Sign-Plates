package signplates.init;
import signplates.SignPlatesMod;
import signplates.block.PlateBlock;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
public final class SignPlatesModBlocks {
 public static final DeferredRegister<net.minecraft.world.level.block.Block> REGISTRY=DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.BLOCKS,SignPlatesMod.MODID);
 public static final RegistryObject<PlateBlock> PLATE=REGISTRY.register("sign_plate",PlateBlock::new);
}

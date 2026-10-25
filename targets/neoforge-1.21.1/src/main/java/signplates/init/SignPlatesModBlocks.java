package signplates.init;
import signplates.SignPlatesMod;
import signplates.block.PlateBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
public final class SignPlatesModBlocks {
 public static final DeferredRegister.Blocks REGISTRY=DeferredRegister.createBlocks(SignPlatesMod.MODID);
 public static final DeferredBlock<PlateBlock> PLATE=REGISTRY.register("sign_plate",PlateBlock::new);
}

package signplates.init;
import signplates.SignPlatesMod;
import signplates.block.PlateBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
public final class SignPlatesModBlocks {
 public static final DeferredRegister.Blocks REGISTRY=DeferredRegister.createBlocks(SignPlatesMod.MODID);
 public static final DeferredBlock<PlateBlock> PLATE=REGISTRY.register("sign_plate",()->new PlateBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK,SignPlatesMod.id("sign_plate")))));
}

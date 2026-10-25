package signplates.init;
import java.util.function.Supplier;
import signplates.SignPlatesMod;import signplates.block.PlateBlock;
import net.minecraft.core.Registry;import net.minecraft.core.registries.BuiltInRegistries;
public final class SignPlatesModBlocks {
 public static final Supplier<PlateBlock> PLATE=()->Holder.BLOCK;
 private static class Holder {static final PlateBlock BLOCK=Registry.register(BuiltInRegistries.BLOCK,SignPlatesMod.id("sign_plate"),new PlateBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK,SignPlatesMod.id("sign_plate")))));}
 public static void init(){PLATE.get();}
}

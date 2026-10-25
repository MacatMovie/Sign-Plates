package signplates.init;
import signplates.SignPlatesMod;
import signplates.item.SignPlateBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
public final class SignPlatesModItems {
 public static final DeferredRegister<Item> REGISTRY=DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.ITEMS,SignPlatesMod.MODID);
 public static final RegistryObject<SignPlateBlockItem> PLATE=REGISTRY.register("sign_plate",()->new SignPlateBlockItem(SignPlatesModBlocks.PLATE.get(),new Item.Properties()));
}

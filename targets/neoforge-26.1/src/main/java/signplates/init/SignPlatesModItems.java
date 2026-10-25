package signplates.init;
import signplates.SignPlatesMod;
import signplates.item.SignPlateBlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
public final class SignPlatesModItems {
 public static final DeferredRegister.Items REGISTRY=DeferredRegister.createItems(SignPlatesMod.MODID);
 public static final DeferredItem<SignPlateBlockItem> PLATE=REGISTRY.register("sign_plate",()->new SignPlateBlockItem(SignPlatesModBlocks.PLATE.get(),new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM,SignPlatesMod.id("sign_plate"))).useBlockDescriptionPrefix().component(SignPlatesModData.APPEARANCE.get(),signplates.PlateAppearance.DEFAULT)));
}

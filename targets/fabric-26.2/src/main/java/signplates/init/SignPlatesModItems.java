package signplates.init;
import java.util.function.Supplier;import net.minecraft.core.Registry;import net.minecraft.core.registries.BuiltInRegistries;import net.minecraft.world.item.Item;import signplates.*;import signplates.item.SignPlateBlockItem;
public final class SignPlatesModItems {
 private static final SignPlateBlockItem ITEM=Registry.register(BuiltInRegistries.ITEM,SignPlatesMod.id("sign_plate"),new SignPlateBlockItem(SignPlatesModBlocks.PLATE.get(),new Item.Properties().component(SignPlatesModData.APPEARANCE.get(),PlateAppearance.DEFAULT).setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM,SignPlatesMod.id("sign_plate"))).useBlockDescriptionPrefix()));
 public static final Supplier<SignPlateBlockItem> PLATE=()->ITEM;
 public static void init(){}
}

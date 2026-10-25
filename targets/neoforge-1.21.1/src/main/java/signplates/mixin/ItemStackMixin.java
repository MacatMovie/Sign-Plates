package signplates.mixin;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
 @Inject(method="parse(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/nbt/Tag;)Ljava/util/Optional;",at=@At("HEAD"),remap=false)
 private static void signplates$upgrade(HolderLookup.Provider registries,Tag tag,CallbackInfoReturnable<Optional<ItemStack>> callback) {if(tag instanceof CompoundTag item)signplates.LegacyPlateUpgrade.item(item);}
}

package signplates.mixin;
import net.minecraft.nbt.CompoundTag;import net.minecraft.world.item.ItemStack;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(ItemStack.class) public abstract class ItemStackMixin {
 @Inject(method={"of","m_41712_"},at=@At("HEAD"),remap=false) private static void signplates$upgrade(CompoundTag item,CallbackInfoReturnable<ItemStack> callback){signplates.LegacyPlateUpgrade.item(item);}
}

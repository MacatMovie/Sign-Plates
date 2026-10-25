package signplates.mixin;
import com.mojang.serialization.*;import java.util.function.Supplier;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;
@Mixin(net.minecraft.world.item.ItemStack.class)
public abstract class ItemStackMixin {
 @Redirect(method="<clinit>",at=@At(value="INVOKE",target="Lcom/mojang/serialization/Codec;lazyInitialized(Ljava/util/function/Supplier;)Lcom/mojang/serialization/Codec;"),remap=false)
 private static <A> Codec<A> signplates$upgrade(Supplier<Codec<A>> supplier){var original=Codec.lazyInitialized(supplier);return new Codec<>(){
  @Override public <T> DataResult<com.mojang.datafixers.util.Pair<A,T>> decode(DynamicOps<T> ops,T input){if(input instanceof net.minecraft.nbt.CompoundTag item)signplates.LegacyPlateUpgrade.item(item);return original.decode(ops,input);}
  @Override public <T> DataResult<T> encode(A input,DynamicOps<T> ops,T prefix){return original.encode(input,ops,prefix);}
 };}
}

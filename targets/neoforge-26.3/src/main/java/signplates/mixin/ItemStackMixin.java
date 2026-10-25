package signplates.mixin;
import com.mojang.serialization.*;import java.util.function.Supplier;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;
@Mixin(net.minecraft.world.item.ItemStack.class)
public abstract class ItemStackMixin {
 // Modify the supplier instead of redirecting the call: Redirect.at changed
 // annotation shape between the Mixin versions bundled by 26.3 loaders.
 @ModifyArg(method="<clinit>",at=@At(value="INVOKE",target="Lcom/mojang/serialization/Codec;lazyInitialized(Ljava/util/function/Supplier;)Lcom/mojang/serialization/Codec;"),index=0,remap=false)
 private static <A> Supplier<Codec<A>> signplates$upgrade(Supplier<Codec<A>> supplier){return ()->{var original=supplier.get();return new Codec<>(){
  @Override public <T> DataResult<com.mojang.datafixers.util.Pair<A,T>> decode(DynamicOps<T> ops,T input){if(input instanceof net.minecraft.nbt.CompoundTag item)signplates.LegacyPlateUpgrade.item(item);return original.decode(ops,input);}
  @Override public <T> DataResult<T> encode(A input,DynamicOps<T> ops,T prefix){return original.encode(input,ops,prefix);}
 };};}
}

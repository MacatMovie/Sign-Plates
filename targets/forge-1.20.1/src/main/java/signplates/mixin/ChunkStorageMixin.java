package signplates.mixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(ChunkStorage.class)
public abstract class ChunkStorageMixin {
 @Inject(method={"upgradeChunkTag","m_188288_"},at=@At("RETURN"),remap=false)
 private void signplates$upgrade(CallbackInfoReturnable<CompoundTag> callback) {signplates.LegacyPlateUpgrade.chunk(callback.getReturnValue());}
}

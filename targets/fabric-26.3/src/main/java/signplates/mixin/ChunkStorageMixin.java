package signplates.mixin;
import net.minecraft.nbt.CompoundTag;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(net.minecraft.world.level.chunk.storage.SerializableChunkData.class)
public abstract class ChunkStorageMixin {
 @Inject(method="parse",at=@At("HEAD"),remap=false)
 private static void signplates$upgrade(net.minecraft.world.level.LevelHeightAccessor height,net.minecraft.world.level.chunk.PalettedContainerFactory factory,CompoundTag tag,CallbackInfoReturnable<net.minecraft.world.level.chunk.storage.SerializableChunkData> callback){signplates.LegacyPlateUpgrade.chunk(tag);}
}

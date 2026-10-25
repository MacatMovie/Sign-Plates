package signplates.block;
import signplates.PlateAppearance;
import signplates.init.SignPlatesModData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
/** No ticker and no per-frame block entity renderer. */
public final class PlateBlockEntity extends BlockEntity implements net.fabricmc.fabric.api.blockgetter.v2.RenderDataBlockEntity {
 @Override public Object getRenderData(){return appearance;}
 private volatile PlateAppearance appearance=PlateAppearance.DEFAULT;
 private long revision;
 public PlateBlockEntity(BlockPos pos,BlockState state) {super(SignPlatesModData.PLATE.get(),pos,state);}
 public PlateAppearance appearance() {return appearance;}
 public long revision() {return revision;}
 public boolean update(PlateAppearance next) {
  if(appearance.equals(next))return true;
  if(level!=null && !level.isClientSide()) {
   BlockState state=getBlockState();
   if((state.getValue(PlateBlock.GLOW)!=next.glow() || state.getValue(PlateBlock.LIGHT)!=next.light()) && !level.setBlock(worldPosition,state.setValue(PlateBlock.GLOW,next.glow()).setValue(PlateBlock.LIGHT,next.light()),3))return false;
  }
  appearance=next;revision++;setChanged();
  if(level!=null) level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
  return true;
 }
 @Override protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output){super.saveAdditional(output);output.store("appearance",PlateAppearance.CODEC,appearance);}
 @Override protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input){super.loadAdditional(input);appearance=input.read("appearance",PlateAppearance.CODEC).orElse(PlateAppearance.DEFAULT);revision++;if(level!=null&&level.isClientSide()){ level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);}}
 @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries){return saveWithoutMetadata(registries);}
 @Override public ClientboundBlockEntityDataPacket getUpdatePacket() {return ClientboundBlockEntityDataPacket.create(this);}
}

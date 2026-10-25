package signplates.client;
import java.util.*;import net.minecraft.client.renderer.block.dispatch.*;import net.minecraft.client.renderer.block.BlockAndTintGetter;import net.minecraft.client.resources.model.sprite.Material;import net.minecraft.core.*;import net.minecraft.util.RandomSource;import net.minecraft.world.level.block.state.BlockState;import signplates.*;import signplates.block.PlateBlock;
public final class PlateBlockModel implements BlockStateModel,net.neoforged.neoforge.client.extensions.BlockStateModelExtension {
 private record Key(PlateAppearance appearance,int orientation){}
 private final BlockStateModel original;private final int orientation;
 public PlateBlockModel(BlockState state,BlockStateModel original){this.original=original;int face=switch(state.getValue(PlateBlock.FACE)){case FLOOR->0;case WALL->1;case CEILING->2;};int yaw=switch(state.getValue(PlateBlock.FACING)){case EAST->1;case SOUTH->2;case WEST->3;default->0;};orientation=face*4+yaw;}
 public static PlateAppearance appearance(BlockAndTintGetter view,BlockPos pos){var data=((net.neoforged.neoforge.common.extensions.IBlockGetterExtension)view).getModelData(pos);return data.has(signplates.block.PlateBlockEntity.APPEARANCE)?data.get(signplates.block.PlateBlockEntity.APPEARANCE):PlateAppearance.DEFAULT;}
 @Override public void collectParts(RandomSource random,List<BlockStateModelPart> parts){PlateModelCache.collect(PlateAppearance.DEFAULT,orientation,parts);}
 @Override public Material.Baked particleMaterial(){return original.particleMaterial();}
 @Override public int materialFlags(){return PlateModelCache.flags();}
 @Override public Object createGeometryKey(BlockAndTintGetter view,BlockPos pos,BlockState state,RandomSource random){return new Key(appearance(view,pos),orientation);}
 @Override public void collectParts(BlockAndTintGetter view,BlockPos pos,BlockState state,RandomSource random,List<BlockStateModelPart> parts){PlateModelCache.collect(appearance(view,pos),orientation,parts);}
 @Override public int materialFlags(BlockAndTintGetter view,BlockPos pos,BlockState state){return materialFlags();}
}

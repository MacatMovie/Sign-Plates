package signplates.client;

import java.util.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;import net.minecraft.world.level.BlockAndTintGetter;import net.minecraft.core.BlockPos;import java.util.function.Supplier;
import signplates.*;
import signplates.block.PlateBlock;
import signplates.block.PlateBlockEntity;

/** Static chunk geometry, assembled lazily from independently baked layers.
 * Each resource reload creates fresh, bounded caches. No block entity renderer. */
final class PlateBakedModel extends PlateForwardingModel {
    private record Key(PlateAppearance appearance,int orientation,boolean item) {}
    private record Geometry(List<BakedQuad> unculled,Map<Direction,List<BakedQuad>> culled) {
        List<BakedQuad> side(Direction side) {return side==null?unculled:culled.getOrDefault(side,List.of());}
    }
    private final Map<ResourceLocation,BakedModel> parts;
    static final class Caches {
        final Map<Key,Geometry> quads=Collections.synchronizedMap(new LinkedHashMap<>(64,0.75f,true) {
            @Override protected boolean removeEldestEntry(Map.Entry<Key,Geometry> entry){return size()>2048;}
        });
        final Map<PlateAppearance,BakedModel> items=Collections.synchronizedMap(new LinkedHashMap<>(32,0.75f,true) {
            @Override protected boolean removeEldestEntry(Map.Entry<PlateAppearance,BakedModel> entry){return size()>512;}
        });
    }
    private final Map<Key,Geometry> cache;
    private final Map<PlateAppearance,BakedModel> items;
    private final ItemOverrides overrides=new ItemOverrides(null,null,List.of()) {
        @Override public BakedModel resolve(BakedModel root,ItemStack stack,ClientLevel level,LivingEntity entity,int seed) {
            PlateAppearance appearance=PlateStyle.appearance(stack);
            synchronized(items) {return items.computeIfAbsent(appearance,a->new ItemModel(wrapped,geometry(a,0,true).unculled()));}
        }
    };
    PlateBakedModel(BakedModel root,Map<ResourceLocation,BakedModel> parts,Caches caches) {this.wrapped=root;this.parts=parts;this.cache=caches.quads;this.items=caches.items;}
    static ResourceLocation id(String path) {return new ResourceLocation("sign_plates",path);}
    static List<ResourceLocation> componentIds() {
        var ids=new ArrayList<ResourceLocation>();
        for(String context:List.of("block","item")) {
            for(int i=0;i<PlateLayers.FRAMES.size();i++) if(i>0 && PlateLayers.frameAvailable(i))for(String variant:List.of("","_glow"))ids.add(id(context+"/layers/frame_"+PlateLayers.FRAMES.get(i).id()+variant));
            for(int i=0;i<PlateLayers.BACKGROUNDS.size();i++) if(i>0 && PlateLayers.backgroundAvailable(i))ids.add(id(context+"/layers/background_"+PlateLayers.BACKGROUNDS.get(i).id()));
            for(String symbol:PlateCatalog.SYMBOLS) if(!symbol.equals("empty"))for(String style:List.of("original","tint","glow"))ids.add(id(context+"/layers/"+symbol+"_"+style));
        }
        return ids;
    }
    @Override public ItemOverrides getOverrides() {return overrides;}
    @Override public boolean usesBlockLight() {return false;}
    @Override public boolean isCustomRenderer() {return false;}
    @Override public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random) {
        var appearance=PlateAppearance.DEFAULT;
        int face=state==null?0:switch(state.getValue(PlateBlock.FACE)){case FLOOR->0;case WALL->1;case CEILING->2;};
        int yaw=state==null?0:switch(state.getValue(PlateBlock.FACING)){case EAST->1;case SOUTH->2;case WEST->3;default->0;};
        return geometry(appearance,face*4+yaw,false).side(side);
    }

    @Override public boolean isVanillaAdapter(){return false;}
    @Override public boolean useAmbientOcclusion(){return false;}
    private static void emit(Geometry geometry,RenderContext context){
     var renderer=RendererAccess.INSTANCE.getRenderer();if(renderer==null)return;
     var normal=renderer.materialFinder().blendMode(BlendMode.CUTOUT).disableDiffuse(true).ambientOcclusion(net.fabricmc.fabric.api.util.TriState.FALSE).find();
     var bright=renderer.materialFinder().blendMode(BlendMode.CUTOUT).disableDiffuse(true).emissive(true).ambientOcclusion(net.fabricmc.fabric.api.util.TriState.FALSE).find();
     var emitter=context.getEmitter();
     for(var q:geometry.unculled())emitter.fromVanilla(q,(q.getVertices()[6]&0x00F000F0)==0x00F000F0?bright:normal,null).emit();
     geometry.culled().forEach((side,quads)->{for(var q:quads)emitter.fromVanilla(q,(q.getVertices()[6]&0x00F000F0)==0x00F000F0?bright:normal,side).emit();});
    }
    @Override public void emitBlockQuads(BlockAndTintGetter view,BlockState state,BlockPos pos,Supplier<RandomSource> random,RenderContext context){
     Object attachment=((RenderAttachedBlockView)view).getBlockEntityRenderAttachment(pos);var a=attachment instanceof PlateAppearance appearance?appearance:PlateAppearance.DEFAULT;
     int face=switch(state.getValue(PlateBlock.FACE)){case FLOOR->0;case WALL->1;case CEILING->2;};int yaw=switch(state.getValue(PlateBlock.FACING)){case EAST->1;case SOUTH->2;case WEST->3;default->0;};emit(geometry(a,face*4+yaw,false),context);
    }
    @Override public void emitItemQuads(ItemStack stack,Supplier<RandomSource> random,RenderContext context){emit(geometry(PlateStyle.appearance(stack),0,true),context);}
    private Geometry geometry(PlateAppearance a,int orientation,boolean item) {
        Key key=new Key(a,orientation,item);var found=cache.get(key);if(found!=null)return found;
        var result=new ArrayList<BakedQuad>();String prefix=(item?"item":"block")+"/layers/";
        var culled=new EnumMap<Direction,List<BakedQuad>>(Direction.class);
        if(a.background()>0)add(result,culled,prefix+"background_"+PlateLayers.BACKGROUNDS.get(a.background()).id(),orientation,0xFFFFFF,item);
        if(a.frame()>0)add(result,culled,prefix+"frame_"+PlateLayers.FRAMES.get(a.frame()).id()+(a.glow()?"_glow":""),orientation,0xFFFFFF,item);
        if(a.symbol()>0)add(result,culled,prefix+PlateCatalog.SYMBOLS.get(a.symbol())+"_"+(a.glow()?"glow":a.tint()!=PlateTint.NONE?"tint":"original"),orientation,a.glow()?a.tint().getGlowTintColor():a.tint().getTintColor(),item);
        culled.replaceAll((direction,quads)->List.copyOf(quads));
        var geometry=new Geometry(List.copyOf(result),Map.copyOf(culled));cache.put(key,geometry);return geometry;
    }
    private void add(List<BakedQuad> output,Map<Direction,List<BakedQuad>> culled,String name,int orientation,int rgb,boolean item) {
        BakedModel model=parts.get(id(name));
        if(model==null)return;
        var random=RandomSource.create(0);
        int face=orientation/4,yaw=(orientation%4+(face==2?2:0))%4;
        rotateQuads(model.getQuads(null,null,random),output,face,yaw,rgb,name.endsWith("_glow"));
        // Preserve source cullface groups and rotate them with the geometry.
        // Vanilla can then omit the backing face against an opaque neighbour.
        for(Direction side:Direction.values()) {
            var quads=model.getQuads(null,side,random);
            if(quads.isEmpty())continue;
            var normal=rotate(side.getStepX(),side.getStepY(),side.getStepZ(),face,yaw);
            var direction=Direction.getNearest(normal[0],normal[1],normal[2]);
            var target=item?output:culled.computeIfAbsent(direction,d->new ArrayList<>());
            rotateQuads(quads,target,face,yaw,rgb,name.endsWith("_glow"));
        }
    }
    private static void rotateQuads(List<BakedQuad> quads,List<BakedQuad> output,int face,int yaw,int rgb,boolean glow) {
        for(var quad:quads) {
            int[] vertices=quad.getVertices().clone();int stride=vertices.length/4;
            var normal=rotate(quad.getDirection().getStepX(),quad.getDirection().getStepY(),quad.getDirection().getStepZ(),face,yaw);
            Direction direction=Direction.getNearest(normal[0],normal[1],normal[2]);
            for(int v=0;v<4;v++) {
                int index=v*stride;
                float[] point=rotate(Float.intBitsToFloat(vertices[index])-.5f,Float.intBitsToFloat(vertices[index+1])-.5f,Float.intBitsToFloat(vertices[index+2])-.5f,face,yaw);
                for(int axis=0;axis<3;axis++)vertices[index+axis]=Float.floatToRawIntBits(point[axis]+.5f);
                if(quad.isTinted()) {
                    int old=vertices[index+3],r=(old&255)*((rgb>>16)&255)/255,g=((old>>8)&255)*((rgb>>8)&255)/255,b=((old>>16)&255)*(rgb&255)/255;
                    vertices[index+3]=(old&0xFF000000)|r|(g<<8)|(b<<16);
                }
                if(glow && !quad.isShade())vertices[index+6]=0x00F000F0;
                if(stride>=8)vertices[index+7]=(vertices[index+7]&0xFF000000)|((direction.getStepX()*127)&255)|(((direction.getStepY()*127)&255)<<8)|(((direction.getStepZ()*127)&255)<<16);
            }
            output.add(new BakedQuad(vertices,-1,direction,quad.getSprite(),quad.isShade()));
        }
    }
    static float[] rotate(float x,float y,float z,int pitch,int yaw) {
        for(int i=0;i<pitch;i++){float old=y;y=z;z=-old;}
        for(int i=0;i<yaw;i++){float old=x;x=-z;z=old;}
        return new float[]{x,y,z};
    }
    private static final class ItemModel extends PlateForwardingModel {
        private final List<BakedQuad> quads;
        ItemModel(BakedModel root,List<BakedQuad> quads) {this.wrapped=root;this.quads=quads;}
        @Override public List<BakedQuad> getQuads(BlockState state,Direction side,RandomSource random) {return side==null?quads:List.of();}
        @Override public boolean isVanillaAdapter(){return false;}
        @Override public boolean useAmbientOcclusion(){return false;}
        @Override public void emitItemQuads(ItemStack stack,Supplier<RandomSource> random,RenderContext context){emit(new Geometry(quads,Map.of()),context);}
        @Override public ItemOverrides getOverrides() {return ItemOverrides.EMPTY;}
        @Override public boolean usesBlockLight(){return false;}
        @Override public boolean isCustomRenderer(){return false;}
    }
}

abstract class PlateForwardingModel implements BakedModel,net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel {
 protected BakedModel wrapped;
 @Override public boolean useAmbientOcclusion(){return false;}
 @Override public boolean isGui3d(){return wrapped.isGui3d();}
 @Override public boolean usesBlockLight(){return false;}
 @Override public boolean isCustomRenderer(){return false;}
 @Override public net.minecraft.client.renderer.texture.TextureAtlasSprite getParticleIcon(){return wrapped.getParticleIcon();}
 @Override public net.minecraft.client.renderer.block.model.ItemTransforms getTransforms(){return wrapped.getTransforms();}
}

package signplates.menu;
import signplates.*;import signplates.block.*;
import net.minecraft.core.*;import net.minecraft.core.registries.Registries;import net.minecraft.network.FriendlyByteBuf;import net.minecraft.network.chat.Component;import net.minecraft.server.level.ServerPlayer;import net.minecraft.world.*;import net.minecraft.world.entity.player.*;import net.minecraft.world.inventory.*;import net.minecraft.world.item.*;import net.minecraft.world.level.Level;import net.minecraft.world.level.block.state.BlockState;import net.minecraft.world.phys.*;

/** One authoritative, revision-checked transaction for all visual tabs. */
public final class PlateEditorMenu extends AbstractContainerMenu {
 public record Opening(BlockPos pos,PlateAppearance appearance){public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf,Opening> CODEC=net.minecraft.network.codec.StreamCodec.of((b,o)->{b.writeBlockPos(o.pos());PlateAppearance.STREAM_CODEC.encode(b,o.appearance());},b->new Opening(b.readBlockPos(),PlateAppearance.STREAM_CODEC.decode(b)));}
 public static final MenuType<PlateEditorMenu> TYPE=net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.MENU,SignPlatesMod.id("plate_editor_v5"),new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(PlateEditorMenu::new,Opening.CODEC));
 public static void init(){}
 private final SimpleContainerData data=new SimpleContainerData(6);
 private final Player owner;private final Level level;private final BlockPos pos;private final Direction hitFace;
 private final int initialSymbol,initialMaterial;private BlockState expectedState;private PlateBlockEntity expectedEntity;private long expectedRevision;
 public PlateEditorMenu(int id,Inventory inventory,Opening buffer){
  super(TYPE,id);owner=inventory.player;level=owner.level();pos=buffer.pos();hitFace=Direction.UP;
  var a=buffer.appearance();initialSymbol=a.symbol();initialMaterial=a.frame();syncAppearance(a);addInventorySyncSlots(inventory);addDataSlots(data);
 }
 private PlateEditorMenu(int id,Inventory inventory,BlockPos pos,Direction face){
  super(TYPE,id);owner=inventory.player;level=owner.level();this.pos=pos.immutable();hitFace=face;expectedState=level.getBlockState(pos);expectedEntity=(PlateBlockEntity)level.getBlockEntity(pos);expectedRevision=expectedEntity.revision();var a=expectedEntity.appearance();initialSymbol=a.symbol();initialMaterial=a.frame();syncAppearance(a);addInventorySyncSlots(inventory);addDataSlots(data);
 }
 public static void openBlock(ServerPlayer player,BlockPos pos,Direction face){
  if(!player.mayBuild()||player.isSpectator()||!player.level().mayInteract(player,pos)||!(player.level().getBlockEntity(pos) instanceof PlateBlockEntity plate))return;
  player.openMenu(new net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider<Opening>(){
   @Override public AbstractContainerMenu createMenu(int id,Inventory inv,Player p){return new PlateEditorMenu(id,inv,pos,face);}
   @Override public Component getDisplayName(){return Component.translatable("screen.sign_plates.editor");}
   @Override public Opening getScreenOpeningData(ServerPlayer p){return new Opening(pos,plate.appearance());}
  });
 }
 public int initialSymbol(){return initialSymbol;}public int initialMaterial(){return initialMaterial;}
 public int symbol(){return data.get(0);}public int material(){return data.get(1);}public int background(){return data.get(4);}public PlateTint tint(){return PlateTint.values()[Math.max(0,Math.min(data.get(2),PlateTint.values().length-1))];}public boolean glow(){return data.get(3)!=0;}public int light(){return data.get(5);}
 public PlateAppearance appearance(){return new PlateAppearance(symbol(),material(),background(),tint(),glow(),light());}
 private void syncAppearance(PlateAppearance a){data.set(0,a.symbol());data.set(1,a.frame());data.set(2,a.tint().ordinal());data.set(3,a.glow()?1:0);data.set(4,a.background());data.set(5,a.light());}
 public int count(Item item){return PlateStyle.count(owner,item);}public int dyeCount(PlateTint tint){return count(PlateStyle.dye(tint));}public int glowCount(){return count(Items.GLOW_INK_SAC);}
 private void addInventorySyncSlots(Inventory inventory){for(int i=0;i<inventory.getContainerSize();i++)addSlot(new Slot(inventory,i,-1000,-1000){@Override public boolean isActive(){return false;}@Override public boolean mayPlace(ItemStack stack){return false;}@Override public boolean mayPickup(Player player){return false;}});}
 @Override public void clicked(int slot,int button,ContainerInput type,Player player){}
 @Override public boolean clickMenuButton(Player player,int button){return false;}
 @Override public ItemStack quickMoveStack(Player player,int slot){return ItemStack.EMPTY;}
 @Override public boolean stillValid(Player player){if(level.isClientSide())return true;return player==owner&&player.level()==level&&player.isAlive()&&!player.isSpectator()&&player.mayBuild()&&level.hasChunkAt(pos)&&level.getBlockState(pos)==expectedState&&level.getBlockEntity(pos)==expectedEntity&&expectedEntity.revision()==expectedRevision&&level.mayInteract(player,pos)&&player.distanceToSqr(Vec3.atCenterOf(pos))<=64;}
 private boolean affordable(Player player,java.util.List<VisualCost.Material> costs){return player.getAbilities().instabuild||costs.stream().allMatch(c->count(c.item())>=c.amount());}
 public boolean applyVisuals(Player player,PlateAppearance next){
  if(!(player instanceof ServerPlayer server)||player.containerMenu!=this||!stillValid(player))return false;
  var current=expectedEntity.appearance();if(current.equals(next))return true;
  if(next.light()!=current.light()&&!PlateAppearance.isLightOption(next.light()))return false;
  if(next.tint()!=current.tint()&&next.tint()!=PlateTint.NONE&&PlateStyle.dye(next.tint())==Items.AIR)return false;
  var costs=VisualCost.between(current,next);if(!affordable(player,costs))return false;
  var permission=net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.invoker().interact(player,level,InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(pos),hitFace,pos,false));
  if(permission==net.minecraft.world.InteractionResult.FAIL){player.sendOverlayMessage(Component.translatable("screen.sign_plates.denied"));server.closeContainer();return false;}
  if(!stillValid(player)||!affordable(player,costs)||!expectedEntity.update(next))return false;
  expectedState=level.getBlockState(pos);expectedRevision=expectedEntity.revision();
  if(!player.getAbilities().instabuild)for(var cost:costs)for(int i=0;i<cost.amount();i++)PlateStyle.consume(player,cost.item());
  syncAppearance(next);broadcastChanges();player.inventoryMenu.broadcastChanges();
  level.playSound(null,pos,net.minecraft.sounds.SoundEvents.UI_STONECUTTER_TAKE_RESULT,net.minecraft.sounds.SoundSource.BLOCKS,1,1);return true;
 }
}

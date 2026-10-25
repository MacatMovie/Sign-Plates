package signplates;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Appearance components and material costs shared by items and the designer. */
public final class PlateStyle {
    private PlateStyle() {}
    public static PlateAppearance appearance(ItemStack stack) {return stack.getOrDefault(signplates.init.SignPlatesModData.APPEARANCE.get(),PlateAppearance.DEFAULT);}
    public static void set(ItemStack stack,PlateAppearance appearance) {stack.set(signplates.init.SignPlatesModData.APPEARANCE.get(),appearance);}
    public static ItemStack stack(PlateAppearance appearance) {ItemStack stack=new ItemStack(signplates.init.SignPlatesModItems.PLATE.get());set(stack,appearance);return stack;}
    public static PlateTint tint(ItemStack stack) {return appearance(stack).tint();}
    public static boolean glow(ItemStack stack) {return appearance(stack).glow();}
    public static void apply(ItemStack stack,PlateTint tint,boolean glow) {var a=appearance(stack);set(stack,new PlateAppearance(a.symbol(),a.frame(),a.background(),tint,glow,a.light()));}
    public static Item dye(PlateTint tint) {
        if (tint == PlateTint.NONE) return Items.AIR;
        String namespace = tint.ordinal() <= PlateTint.BLACK.ordinal() ? "minecraft" : "dye_depot";
        var id = ResourceLocation.fromNamespaceAndPath(namespace, tint.getSerializedName() + "_dye");
        return BuiltInRegistries.ITEM.containsKey(id) ? BuiltInRegistries.ITEM.get(id) : Items.AIR;
    }
    public static int count(Player player, Item item) {
        if (item == Items.AIR) return 0;
        int amount = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) amount += stack.getCount();
        }
        return amount;
    }
    public static boolean consume(Player player, Item item) {
        if (player.getAbilities().instabuild) return true;
        if (item == Items.AIR) return false;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item) && !stack.isEmpty()) {
                stack.shrink(1);
                player.getInventory().setChanged();
                return true;
            }
        }
        return false;
    }
}

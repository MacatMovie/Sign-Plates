package signplates.item;

import java.util.List;
import signplates.SignPlatesConfig;
import signplates.block.PlateBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class SignPlateBlockItem extends BlockItem {
    public SignPlateBlockItem(Block block, Item.Properties properties) {super(block,properties);}

    @Override public Component getName(ItemStack stack) {
        int symbol=signplates.PlateStyle.appearance(stack).symbol();
        return Component.translatable(symbol==0?"block.sign_plates.sign_plate":"block.sign_plates.plate_"+signplates.PlateCatalog.SYMBOLS.get(symbol));
    }
    @Override public net.minecraft.world.InteractionResult place(net.minecraft.world.item.context.BlockPlaceContext context) {
        var appearance=signplates.PlateStyle.appearance(context.getItemInHand());
        var result=super.place(context);
        if(result.consumesAction() && !context.getLevel().isClientSide() && context.getLevel().getBlockEntity(context.getClickedPos()) instanceof signplates.block.PlateBlockEntity plate) plate.update(appearance);
        return result;
    }
    @Override public void appendHoverText(ItemStack stack,Item.TooltipContext context,net.minecraft.world.item.component.TooltipDisplay tooltipDisplay,java.util.function.Consumer<Component> tooltip,TooltipFlag flag) {
        super.appendHoverText(stack,context,tooltipDisplay,tooltip,flag);
        if(!SignPlatesConfig.showTooltips()) return;
        if(!net.minecraft.client.Minecraft.getInstance().hasShiftDown()) {
            tooltip.accept(Component.translatable("tooltip.sign_plates.hold_shift").withStyle(ChatFormatting.GOLD));
            return;
        }
        boolean blank=signplates.PlateStyle.appearance(stack).symbol()==0;
        if(blank) {
            tooltip.accept(Component.translatable("tooltip.sign_plates.blank_edit").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("tooltip.sign_plates.blank_style").withStyle(ChatFormatting.GRAY));
        } else tooltip.accept(Component.translatable("tooltip.sign_plates.symbol_origin").withStyle(ChatFormatting.GRAY));
    }
}

package signplates.item;

import signplates.SignPlatesConfig;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class SignPlateBlockItem extends BlockItem {
	public SignPlateBlockItem(Block block, Item.Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
		if (!SignPlatesConfig.showTooltips()) return;
		tooltip.accept(Component.translatable("tooltip.sign_plates.interactions").withStyle(ChatFormatting.GRAY));
	}
}

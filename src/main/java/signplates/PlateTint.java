package signplates;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum PlateTint implements StringRepresentable {
	NONE("none", null, 0xFFFFFF, 0xFFFFFF),
	WHITE("white", DyeColor.WHITE, 0xEEF4FF, 0xFFFFFF),
	ORANGE("orange", DyeColor.ORANGE, 0xE08D2F, 0xFFB546),
	MAGENTA("magenta", DyeColor.MAGENTA, 0xC85EE6, 0xFF8AFF),
	LIGHT_BLUE("light_blue", DyeColor.LIGHT_BLUE, 0x81D9FF, 0xB8EEFF),
	YELLOW("yellow", DyeColor.YELLOW, 0xE6DC56, 0xF4DD64),
	LIME("lime", DyeColor.LIME, 0x8AE03C, 0xBEFF67),
	PINK("pink", DyeColor.PINK, 0xEE8FD0, 0xFFB0E6),
	GRAY("gray", DyeColor.GRAY, 0x67707A, 0x9AA5B1),
	LIGHT_GRAY("light_gray", DyeColor.LIGHT_GRAY, 0xB4BEC8, 0xD8E0E8),
	CYAN("cyan", DyeColor.CYAN, 0x40CAD6, 0x7DFAFF),
	PURPLE("purple", DyeColor.PURPLE, 0x8E63E0, 0xC49CFF),
	BLUE("blue", DyeColor.BLUE, 0x568BF4, 0x8BB8FF),
	BROWN("brown", DyeColor.BROWN, 0xA1633D, 0xCC865B),
	GREEN("green", DyeColor.GREEN, 0x63B73A, 0x97EA62),
	RED("red", DyeColor.RED, 0xF05A4E, 0xFF8375),
	BLACK("black", DyeColor.BLACK, 0x32373D, 0x626A75);

	private final String name;
	private final DyeColor dyeColor;
	private final int baseTintColor;
	private final int glowTintColor;

	PlateTint(String name, DyeColor dyeColor, int baseTintColor, int glowTintColor) {
		this.name = name;
		this.dyeColor = dyeColor;
		this.baseTintColor = baseTintColor;
		this.glowTintColor = glowTintColor;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public int getTintColor() {
		return this.baseTintColor;
	}

	public int getGlowTintColor() {
		return this.glowTintColor;
	}

	public static boolean isDyeItemStack(ItemStack stack) {
		return fromItemStack(stack) != NONE;
	}

	public static PlateTint fromItemStack(ItemStack stack) {
		if (stack.is(Items.WHITE_DYE)) return WHITE;
		if (stack.is(Items.ORANGE_DYE)) return ORANGE;
		if (stack.is(Items.MAGENTA_DYE)) return MAGENTA;
		if (stack.is(Items.LIGHT_BLUE_DYE)) return LIGHT_BLUE;
		if (stack.is(Items.YELLOW_DYE)) return YELLOW;
		if (stack.is(Items.LIME_DYE)) return LIME;
		if (stack.is(Items.PINK_DYE)) return PINK;
		if (stack.is(Items.GRAY_DYE)) return GRAY;
		if (stack.is(Items.LIGHT_GRAY_DYE)) return LIGHT_GRAY;
		if (stack.is(Items.CYAN_DYE)) return CYAN;
		if (stack.is(Items.PURPLE_DYE)) return PURPLE;
		if (stack.is(Items.BLUE_DYE)) return BLUE;
		if (stack.is(Items.BROWN_DYE)) return BROWN;
		if (stack.is(Items.GREEN_DYE)) return GREEN;
		if (stack.is(Items.RED_DYE)) return RED;
		if (stack.is(Items.BLACK_DYE)) return BLACK;
		return NONE;
	}

	public static PlateTint fromDyeColor(DyeColor dyeColor) {
		for (PlateTint tint : values()) {
			if (tint.dyeColor == dyeColor)
				return tint;
		}
		return NONE;
	}
}

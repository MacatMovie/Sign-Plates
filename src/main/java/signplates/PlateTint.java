package signplates;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;

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
	BLACK("black", DyeColor.BLACK, 0x32373D, 0x626A75),
	MAROON("maroon", null, 0x7B2713, 0xAA695A),
	ROSE("rose", null, 0xFF5E64, 0xFF969B),
	CORAL("coral", null, 0xDF7758, 0xE9A08A),
	INDIGO("indigo", null, 0x331E57, 0x706283),
	NAVY("navy", null, 0x153D64, 0x5B7793),
	SLATE("slate", null, 0x4C5E86, 0x8592AA),
	OLIVE("olive", null, 0x8C8F2A, 0xB5B761),
	AMBER("amber", null, 0xD7AF00, 0xE4C84C),
	BEIGE("beige", null, 0xE1D5A3, 0xE9E2C0),
	TEAL("teal", null, 0x2F7B67, 0x6DAA99),
	MINT("mint", null, 0x38CE7D, 0x74DD9F),
	AQUA("aqua", null, 0x5EF0CC, 0x96F5DD),
	VERDANT("verdant", null, 0x255714, 0x677F5A),
	FOREST("forest", null, 0x32A326, 0x70C267),
	GINGER("ginger", null, 0xCF6121, 0xDD9063),
	TAN("tan", null, 0xF49C5D, 0xF7BA8D);

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

	public static PlateTint fromDyeColor(DyeColor dyeColor) {
		if (dyeColor == null)
			return NONE;
		String dyeName = dyeColor.getSerializedName();
		for (PlateTint tint : values()) {
			if (tint.dyeColor == dyeColor || tint.name.equals(dyeName))
				return tint;
		}
		return NONE;
	}
}

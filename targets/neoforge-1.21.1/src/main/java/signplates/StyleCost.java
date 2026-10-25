package signplates;

/** One shared cost calculation for the client preview and server commit. */
public record StyleCost(int dyeOrdinal, boolean glowInk) {
    public static StyleCost between(int appliedTint, boolean appliedGlow, int draftTint, boolean draftGlow) {
        return new StyleCost(draftTint != 0 && draftTint != appliedTint ? draftTint : -1,
            draftGlow && !appliedGlow);
    }
    public boolean canAfford(int dyes, int inkSacs, boolean creative) {
        return creative || ((dyeOrdinal < 0 || dyes > 0) && (!glowInk || inkSacs > 0));
    }
}

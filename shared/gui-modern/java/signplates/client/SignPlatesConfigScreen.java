package signplates.client;

import signplates.SignPlatesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SignPlatesConfigScreen extends Screen {
    private final Screen parent;
    private Button tooltips;
    public SignPlatesConfigScreen(Screen parent) {
        super(Component.literal("Sign Plates Configuration"));
        this.parent = parent;
    }
    @Override protected void init() {
        int x = width / 2 - 110;
        int y = Math.max(55, height / 2 - 20);
        tooltips = addRenderableWidget(Button.builder(label(), b -> {
            SignPlatesConfig.setShowTooltips(!SignPlatesConfig.showTooltips());
            b.setMessage(label());
        }).bounds(x, y, 220, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Reset to Defaults"), b -> {
            SignPlatesConfig.setShowTooltips(true);
            tooltips.setMessage(label());
        }).bounds(x, y + 30, 108, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
            .bounds(x + 112, y + 30, 108, 20).build());
    }
    private Component label() {
        return Component.literal("Item Tooltips: " + (SignPlatesConfig.showTooltips() ? "ON" : "OFF"));
    }
    @Override public void onClose() {
        SignPlatesConfig.save();
        minecraft.setScreen(parent);
    }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // Apply vanilla background blur before drawing any config content.
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(font, title, width / 2, Math.max(20, height / 2 - 68), 0xFFFFFFFF);
        graphics.drawCenteredString(font, "Show dye and glow instructions on plate items.",
            width / 2, Math.max(36, height / 2 - 48), 0xFFB0B0B0);
    }
}

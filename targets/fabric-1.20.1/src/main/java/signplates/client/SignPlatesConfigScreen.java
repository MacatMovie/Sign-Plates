package signplates.client;

import signplates.SignPlatesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SignPlatesConfigScreen extends Screen {
    private final Screen parent;
    private Button tooltips,theme;
    public SignPlatesConfigScreen(Screen parent) {
        super(Component.literal("Sign Plates Configuration"));this.parent=parent;
    }
    @Override protected void init() {
        int x=width/2-110,y=Math.max(55,height/2-30);
        tooltips=addRenderableWidget(Button.builder(tooltipLabel(),b->{
            SignPlatesConfig.setShowTooltips(!SignPlatesConfig.showTooltips());b.setMessage(tooltipLabel());
        }).bounds(x,y,220,20).build());
        tooltips.setTooltip(Tooltip.create(Component.literal("Show the Shift hint and extra plate instructions.")));
        theme=addRenderableWidget(Button.builder(themeLabel(),b->{
            SignPlatesConfig.setVanillaGui(!SignPlatesConfig.vanillaGui());b.setMessage(themeLabel());
        }).bounds(x,y+26,220,20).build());
        theme.setTooltip(Tooltip.create(Component.literal("Vanilla uses classic Minecraft panels. Dark keeps the original designer appearance. This setting only affects your client.")));
        addRenderableWidget(Button.builder(Component.literal("Reset to Defaults"),b->{
            SignPlatesConfig.setShowTooltips(true);SignPlatesConfig.setVanillaGui(true);
            tooltips.setMessage(tooltipLabel());theme.setMessage(themeLabel());
        }).bounds(x,y+56,108,20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"),b->onClose()).bounds(x+112,y+56,108,20).build());
    }
    private Component tooltipLabel() {return Component.literal("Item Tooltips: "+(SignPlatesConfig.showTooltips()?"ON":"OFF"));}
    private Component themeLabel() {return Component.literal("Designer Theme: "+(SignPlatesConfig.vanillaGui()?"Vanilla":"Dark"));}
    @Override public void onClose() {SignPlatesConfig.save();minecraft.setScreen(parent);}
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float delta) {
        renderBackground(g);super.render(g,mouseX,mouseY,delta);
        g.drawCenteredString(font,title,width/2,Math.max(20,height/2-77),0xFFFFFFFF);
        g.drawCenteredString(font,"Client-side settings",width/2,Math.max(36,height/2-57),0xFFB0B0B0);
    }
}

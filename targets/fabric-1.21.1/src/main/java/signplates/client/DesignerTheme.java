package signplates.client;

import signplates.SignPlatesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/** Texture-backed vanilla panels; the original dark theme remains available. */
final class DesignerTheme {
    private static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath("sign_plates","textures/gui/designer/vanilla/"+name+".png");
    }
    private static final ResourceLocation PANEL=texture("panel"),INSET=texture("inset"),SLOT=texture("slot"),SCROLL=texture("scrollbar");
    private DesignerTheme() {}
    static boolean vanilla() {return SignPlatesConfig.vanillaGui();}
    static int label() {return vanilla()?0xFF404040:0xFFD8E1EB;}
    static int title() {return vanilla()?0xFF404040:0xFFF0F4F8;}
    static int secondary() {return vanilla()?0xFF606060:0xFFB5BCC3;}
    static int required(boolean missing) {return missing?(vanilla()?0xFFAA0000:0xFFFF6868):(vanilla()?0xFF404040:0xFFE0E6ED);}
    static void panel(GuiGraphics g,int x,int y,int w,int h) {
        if(vanilla()) nine(g,PANEL,x-1,y-1,w+2,h+2,16,16,4);
        else {g.fill(x-1,y-1,x+w+1,y+h+1,0xFF7C8B9A);g.fill(x,y,x+w,y+h,0xFF252C34);}
    }
    static void inset(GuiGraphics g,int x,int y,int w,int h) {
        if(vanilla()) nine(g,INSET,x,y,w,h,8,8,1);
        else g.fill(x,y,x+w,y+h,0xFF151A20);
    }
    static void scrollbar(GuiGraphics g,int x,int y,int h,boolean dragging) {
        if(vanilla()) nine(g,SCROLL,x,y,6,h,6,15,1);
        else g.fill(x,y,x+6,y+h,dragging?0xFFE2EAF2:0xFF8595A5);
    }
    static void slot(GuiGraphics g,int x,int y,int w,int h,boolean hover,boolean selected,boolean original) {
        if(vanilla()) {
            g.blit(SLOT,x,y,0,0,22,22,22,22);
            if(hover) g.fill(x+1,y+1,x+w-1,y+h-1,0x55FFFFFF);
            if(original || selected) outline(g,x,y,w,h,original?0xFF559EFF:0xFFFFD166);
        } else {
            int border=original?0xFF559EFF:selected?0xFFFFD166:hover?0xFFD3DAE2:0xFF59636E;
            g.fill(x,y,x+w,y+h,border);g.fill(x+1,y+1,x+w-1,y+h-1,0xFF30373F);
        }
        if(original && selected) outline(g,x+2,y+2,w-4,h-4,0xFFFFD166);
    }
    private static void outline(GuiGraphics g,int x,int y,int w,int h,int colour) {
        g.fill(x,y,x+w,y+1,colour);g.fill(x,y+h-1,x+w,y+h,colour);
        g.fill(x,y+1,x+1,y+h-1,colour);g.fill(x+w-1,y+1,x+w,y+h-1,colour);
    }
    private static void nine(GuiGraphics g,ResourceLocation tex,int x,int y,int w,int h,int tw,int th,int b) {
        int[] sx={0,b,tw-b},sy={0,b,th-b},sw={b,tw-2*b,b},sh={b,th-2*b,b};
        int[] dx={x,x+b,x+w-b},dy={y,y+b,y+h-b},dw={b,w-2*b,b},dh={b,h-2*b,b};
        for(int row=0;row<3;row++) for(int col=0;col<3;col++) {
            if(dw[col]<=0 || dh[row]<=0) continue;
            g.pose().pushPose();g.pose().translate(dx[col],dy[row],0);
            g.pose().scale((float)dw[col]/sw[col],(float)dh[row]/sh[row],1);
            g.blit(tex,0,0,(float)sx[col],(float)sy[row],sw[col],sh[row],tw,th);
            g.pose().popPose();
        }
    }
}

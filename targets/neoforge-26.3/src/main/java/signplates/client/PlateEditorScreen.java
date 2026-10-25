package signplates.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import signplates.PlateCatalog;
import signplates.PlateStyle;
import signplates.VisualCost;
import signplates.PlateAppearance;
import signplates.PlateTint;
import signplates.init.SignPlatesModBlocks;
import signplates.menu.PlateEditorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PlateEditorScreen extends AbstractContainerScreen<PlateEditorMenu> {
    private static final int CELL=24, GRID_Y=100, SIDE=112, PREVIEW_HEIGHT=128;
    private static final String[] CATEGORIES={"all","letters","numbers","arrows","faces","icons"};
    
    private final List<Integer> filtered=new ArrayList<>(),backgrounds=new ArrayList<>();
    private final List<ChoiceButton> cells=new ArrayList<>(),backgroundCells=new ArrayList<>();
    private final List<Button> categories=new ArrayList<>(), tabs=new ArrayList<>();
    private EditBox search;
    private Button glowButton, applyButton, leaveButton, returnButton, rotateButton, copyButton, pasteButton;
    private final List<Button> lightButtons=new ArrayList<>();
    private static PlateAppearance clipboard;
    private PlateAppearance sentAppearance;
    private int draftFrame,draftBackground,draftLight;
    private boolean angledPreview;
    private PlateTint draftTint;
    private boolean draftGlow, closePrompt;
    private int applyTicks;
    private int designerWidth,designerHeight;
    private int columns,rows,mainWidth,tab=1,category,selected;
    private double scroll,backgroundScroll;
    private int dragPane;
    private boolean dragging;
    private String query="", applyTooltip="";

    public PlateEditorScreen(PlateEditorMenu menu,Inventory inventory,Component title) {
        super(menu,inventory,title);
        selected=menu.initialSymbol();
        draftTint=menu.tint();draftGlow=menu.glow();draftFrame=menu.material();draftBackground=menu.background();draftLight=menu.light();
    }
    public int getImageWidth() { return designerWidth; }
    public int getImageHeight() { return designerHeight; }
    @Override protected boolean hasClickedOutside(double x,double y,int left,int top) {
        return x<left || y<top || x>=left+designerWidth || y>=top+designerHeight;
    }
    private static Component text(String key) { return Component.translatable("screen.sign_plates."+key); }
    private ItemStack plate(int symbol,int frame) {return signplates.PlateStyle.stack(new signplates.PlateAppearance(symbol,frame,draftBackground,draftTint,draftGlow,draftLight));}
    private ItemStack layer(int choice,int pane) {return signplates.PlateStyle.stack(new signplates.PlateAppearance(0,pane==1?choice:0,pane==0?choice:0,PlateTint.NONE,false));}
    private int sideX() { return leftPos+mainWidth+8; }
    private int contentX() { return leftPos+12; }
    private int gridX() { return contentX()+(tab==1?3:0); }
    private int gridTop() { return topPos+GRID_Y-(tab==0?CELL:0); }
    // The inset starts three pixels above the first cell and has a one-pixel border.
    private int clipTop() { return gridTop()-2; }
    private int trackX() { return gridX()+columns*CELL; }
    private int headerWidth() { return trackX()+7-gridX(); }
    private int gridHeight() { return Math.max(CELL,rows*CELL-footerHeight())+(tab==0?CELL:0); }
    private int viewportHeight() { return gridHeight()-1; }
    private boolean overGrid(double x,double y) { return x>=gridX() && x<trackX() && y>=clipTop() && y<gridTop()+viewportHeight(); }
    private boolean creative() { return minecraft.player!=null && minecraft.player.getAbilities().instabuild; }
    private PlateAppearance draft(){return new PlateAppearance(selected,draftFrame,draftBackground,draftTint,draftGlow,draftLight);}
    private void select(PlateAppearance a){selected=a.symbol();draftFrame=a.frame();draftBackground=a.background();draftTint=a.tint();draftGlow=a.glow();draftLight=PlateAppearance.LIGHT_LEVELS.get(PlateAppearance.lightOption(a.light()));refresh();}
    private int costColumns(){return Math.max(1,(mainWidth-108)/34);}
    private int footerHeight(){return Math.max(32,((5+costColumns()-1)/costColumns())*18+8);}
    private java.util.List<VisualCost.Material> costs(){return VisualCost.between(menu.appearance(),draft());}
    @Override protected void init() {
        // An odd number of whole columns also fits two equal split grids.
        // Fit the panel around the cells instead of stretching their spacing.
        int availableMain=Math.min(444,width-16)-SIDE-8;
        columns=Math.max(3,(availableMain-32)/CELL);
        if(columns%2==0)columns--;
        mainWidth=columns*CELL+32;
        designerWidth=mainWidth+SIDE+8;
        rows=Math.max(1,Math.min(7,(height-124)/CELL));
        designerHeight=Math.max(232,GRID_Y+rows*CELL+12);
        super.init();
        // The vanilla dimensions are final on 26.x; center using our responsive layout.
        leftPos=(width-designerWidth)/2;
        topPos=(height-designerHeight)/2;
        cells.clear();backgroundCells.clear();categories.clear();tabs.clear();lightButtons.clear();
        search=new EditBox(font,contentX(),topPos+29,headerWidth(),18,text("search"));
        search.setMaxLength(64);search.setHint(text("search"));search.setValue(query);
        search.setResponder(value->{query=value;scroll=backgroundScroll=0;filter();});addRenderableWidget(search);
        int categoryWidth=headerWidth();
        for(int i=0;i<CATEGORIES.length;i++) {
            final int choice=i;
            int column=i%3,start=column*(categoryWidth+2)/3,end=(column+1)*(categoryWidth+2)/3-2;
            categories.add(addRenderableWidget(Button.builder(text(CATEGORIES[i]),b->{category=choice;scroll=backgroundScroll=0;filter();})
                .bounds(contentX()+start,topPos+51+(i/3)*22,end-start,20).build()));
        }
        for(int i=0;i<(rows+1)*columns;i++) {cells.add(addRenderableWidget(new ChoiceButton(0)));backgroundCells.add(addRenderableWidget(new ChoiceButton(1)));}
        String[] names={"background_tab","symbols","style"};
        for(int i=0;i<names.length;i++) {
            final int choice=i;
            tabs.add(addRenderableWidget(Button.builder(text(names[i]),b->{tab=choice;query="";search.setValue("");scroll=backgroundScroll=0;dragging=false;filter();})
                .bounds(sideX(),topPos+PREVIEW_HEIGHT+8+i*24,SIDE,20).build()));
        }
        copyButton=addRenderableWidget(Button.builder(text("copy"),b->{clipboard=draft();refresh();}).bounds(sideX(),topPos+PREVIEW_HEIGHT+80,(SIDE-4)/2,20).build());
        copyButton.setTooltip(Tooltip.create(text("copy_tip")));
        pasteButton=addRenderableWidget(Button.builder(text("paste"),b->{if(clipboard!=null)select(clipboard);}).bounds(sideX()+(SIDE+4)/2,topPos+PREVIEW_HEIGHT+80,(SIDE-4)/2,20).build());
        int lightX=contentX()+mainWidth/2,lightWidth=mainWidth/2-24;
        for(int i=0;i<PlateAppearance.LIGHT_LEVELS.size();i++) {
            final int option=i,level=PlateAppearance.LIGHT_LEVELS.get(i);
            int start=i*(lightWidth+2)/4,end=(i+1)*(lightWidth+2)/4-2;
            Button button=addRenderableWidget(Button.builder(Component.literal(Integer.toString(option)),b->{draftLight=level;refresh();})
                .bounds(lightX+start,topPos+44,end-start,20).build());
            lightButtons.add(button);
        }
        rotateButton=addRenderableWidget(new PreviewRotateButton(sideX()+SIDE-24,topPos+4));
        glowButton=addRenderableWidget(Button.builder(Component.empty(),b->{draftGlow=!draftGlow;refresh();})
            .bounds(contentX(),topPos+44,64,20).build());
        applyButton=addRenderableWidget(new VisualApplyButton(leftPos+mainWidth-84,topPos+designerHeight-30));
        int modalWidth=Math.min(300,designerWidth-16),modalX=leftPos+(designerWidth-modalWidth)/2;
        int modalY=topPos+(designerHeight-102)/2;
        leaveButton=Button.builder(text("leave"),b->leaveDesigner())
            .bounds(modalX+8,modalY+72,(modalWidth-24)/2,20).build();
        returnButton=Button.builder(text("return_style"),b->returnToEditing())
            .bounds(modalX+16+(modalWidth-24)/2,modalY+72,(modalWidth-24)/2,20).build();
        applyTooltip="";
        filter();
    }
    private void filter() {
        filtered.clear();backgrounds.clear();String needle=query.strip().toLowerCase(Locale.ROOT);
        if(tab==0) {
            for(int i=0;i<signplates.PlateLayers.BACKGROUNDS.size();i++) if(needle.isEmpty() || backgroundName(i).getString().toLowerCase(Locale.ROOT).contains(needle)) filtered.add(i);
            for(int i=0;i<signplates.PlateLayers.FRAMES.size();i++) if(needle.isEmpty() || frameName(i).getString().toLowerCase(Locale.ROOT).contains(needle)) backgrounds.add(i);
        } else if(tab==1) {
            for(int i=0;i<PlateCatalog.SYMBOLS.size();i++) {
                if(category!=0 && PlateCatalog.category(i)!=category) continue;
                String name=plate(i,0).getHoverName().getString().toLowerCase(Locale.ROOT);
                if(needle.isEmpty() || name.contains(needle) || PlateCatalog.SYMBOLS.get(i).contains(needle)) filtered.add(i);
            }
        } else {
            for(PlateTint tint:PlateTint.values()) if(tint==PlateTint.NONE || PlateStyle.dye(tint)!=Items.AIR) filtered.add(tint.ordinal());
        }
        refresh();
    }
    private static Component frameName(int frame) {return text("frame_"+signplates.PlateLayers.FRAMES.get(frame).id());}
    private static Component backgroundName(int choice) {return text("background_"+signplates.PlateLayers.BACKGROUNDS.get(choice).id());}
    private int paneX(int pane) {return gridX()+pane*((columns+1)/2)*CELL;}
    private int paneTrack(int pane) {return paneX(pane)+paneColumns(pane)*CELL;}
    private int paneColumns(int pane) {return (columns-1)/2;}
    private int columnX(int left,int column) {return left+column*CELL;}
    private boolean overPane(double x,double y,int pane) {return x>=paneX(pane) && x<paneTrack(pane) && y>=clipTop() && y<gridTop()+viewportHeight();}
    private double paneMax(int pane) {int count=pane==0?filtered.size():backgrounds.size(),cols=paneColumns(pane);return Math.max(0,((count+cols-1)/cols)*CELL-viewportHeight());}
    private double maxScroll() { return tab==0?paneMax(0):Math.max(0,((filtered.size()+columns-1)/columns)*CELL-viewportHeight()); }
    private boolean dirtyVisuals(){return !draft().equals(menu.appearance());}
    private boolean canPay(){return (draftTint==menu.tint()||draftTint==PlateTint.NONE||PlateStyle.dye(draftTint)!=Items.AIR)&&(creative()||costs().stream().allMatch(c->menu.count(c.item())>=c.amount()));}
    private void applyVisuals(){
        if(!dirtyVisuals()||!canPay()||applyTicks>0)return;
        sentAppearance=draft();applyTicks=40;
        DesignerPackets.send(menu.containerId,sentAppearance);refresh();
    }
    private void leaveDesigner() {closePrompt=false;super.onClose();}
    private void returnToEditing() {closePrompt=false;dragging=false;refresh();}
    @Override public void onClose() {
        if(dirtyVisuals()) {closePrompt=true;dragging=false;search.setFocused(false);refresh();}
        else super.onClose();
    }
    private void refresh() {
        scroll=Math.max(0,Math.min(scroll,maxScroll()));
        backgroundScroll=Math.max(0,Math.min(backgroundScroll,paneMax(1)));
        search.visible=tab!=2;search.active=tab!=2 && !closePrompt;
        search.setHint(text(tab==0?"search_frames":"search"));
        if(tab==2) search.setFocused(false);
        for(int i=0;i<categories.size();i++) {
            Button b=categories.get(i);b.visible=tab==1;b.active=i!=category && !closePrompt;
        }
        for(int i=0;i<tabs.size();i++) tabs.get(i).active=i!=tab && !closePrompt;
        rotateButton.active=!closePrompt;
        copyButton.active=!closePrompt;pasteButton.active=clipboard!=null&&!closePrompt;
        pasteButton.setTooltip(Tooltip.create(text(clipboard==null?"paste_empty":"paste_tip")));
        for(int i=0;i<lightButtons.size();i++) {
            Button button=lightButtons.get(i);button.visible=tab==2;
            button.active=!closePrompt && draftLight!=PlateAppearance.LIGHT_LEVELS.get(i);
        }
        glowButton.visible=tab==2;
        glowButton.active=!closePrompt;
        glowButton.setMessage(text(draftGlow?"on":"off"));
        applyButton.visible=true;
        applyButton.active=!closePrompt && dirtyVisuals() && canPay() && applyTicks==0;
        String applyTip=!dirtyVisuals()?"no_style_changes":!canPay()?"missing_materials":"apply_style";
        if(!applyTip.equals(applyTooltip)) {applyTooltip=applyTip;applyButton.setTooltip(Tooltip.create(text(applyTip)));}
        positionChoices(cells,filtered,0,scroll);
        positionChoices(backgroundCells,backgrounds,1,backgroundScroll);
    }
    private void positionChoices(List<ChoiceButton> buttons,List<Integer> choices,int pane,double position) {
        int cols=tab==0?paneColumns(pane):columns;
        int first=(int)(position/CELL),offset=(int)(position%CELL);
        for(int i=0;i<buttons.size();i++) {
            ChoiceButton b=buttons.get(i);int index=first*cols+i;
            b.choice=index<choices.size()?choices.get(index):-1;
            b.setX(columnX(tab==0?paneX(pane):gridX(),i%cols));b.setY(gridTop()+(i/cols)*CELL-offset);
            b.visible=(pane==0 || tab==0) && b.choice>=0 && b.getY()<gridTop()+viewportHeight() && b.getY()+22>clipTop();
            b.active=b.visible&&!closePrompt;
            if(b.visible) {
                Component label=tab==0?(pane==0?backgroundName(b.choice):frameName(b.choice)):tab==1?plate(b.choice,draftFrame).getHoverName():colourName(PlateTint.values()[b.choice]);
                b.setMessage(label);
                if(tab==2 && b.choice>0) {b.setTooltip(null);b.tooltipText="";}
                else if(!label.getString().equals(b.tooltipText)){b.tooltipText=label.getString();b.setTooltip(Tooltip.create(label));}
            }
        }
    }

    private Component colourName(PlateTint tint) {
        return tint==PlateTint.NONE?text("no_colour"):new ItemStack(PlateStyle.dye(tint)).getHoverName();
    }
    @Override protected void containerTick() {
        super.containerTick();

        if(applyTicks>0) {
            if(menu.appearance().equals(sentAppearance)) applyTicks=0;
            else applyTicks--;
        }
        if(closePrompt && !dirtyVisuals()) {leaveDesigner();return;}
        refresh();
    }
    private int thumbHeight() { return Math.max(18,(int)(viewportHeight()*viewportHeight()/(viewportHeight()+maxScroll()))); }
    private int thumbY() { return gridTop()+(maxScroll()==0?0:(int)(scroll/maxScroll()*(viewportHeight()-thumbHeight()))); }
    private void dragScroll(double y) {
        double travel=viewportHeight()-thumbHeight();
        scroll=travel<=0?0:(y-gridTop()-thumbHeight()/2.0)/travel*maxScroll();refresh();
    }
    private void dragPane(double y) {
        double max=paneMax(dragPane);int h=Math.max(18,(int)(viewportHeight()*viewportHeight()/(viewportHeight()+max)));
        double travel=viewportHeight()-h,value=travel<=0?0:(y-gridTop()-h/2.0)/travel*max;
        if(dragPane==0)scroll=value;else backgroundScroll=value;refresh();
    }
    private void drawPaneTrack(GuiGraphicsExtractor g,int pane) {
        double max=paneMax(pane);if(max<=0)return;
        int h=Math.max(18,(int)(viewportHeight()*viewportHeight()/(viewportHeight()+max)));
        double position=pane==0?scroll:backgroundScroll;int y=gridTop()+(int)(position/max*(viewportHeight()-h));
        g.fill(paneTrack(pane),gridTop(),paneTrack(pane)+6,gridTop()+viewportHeight(),DesignerTheme.vanilla()?0xFF8B8B8B:0xFF101419);
        DesignerTheme.scrollbar(g,paneTrack(pane),y,h,dragging&&dragPane==pane);
    }
    @Override public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event,boolean doubleClick) {double x=event.x(),y=event.y();int button=event.button();

        if(closePrompt) {leaveButton.mouseClicked(event,doubleClick);returnButton.mouseClicked(event,doubleClick);return true;}
        if(tab==0 && button==0) {
            for(int pane=0;pane<2;pane++) if(x>=paneTrack(pane) && x<paneTrack(pane)+7 && y>=clipTop() && y<gridTop()+viewportHeight() && paneMax(pane)>0) {dragging=true;dragPane=pane;dragPane(y);return true;}
        }
        if(tab!=0 && button==0 && x>=trackX() && x<trackX()+7 && y>=clipTop() && y<gridTop()+viewportHeight() && maxScroll()>0) {
            dragging=true;dragScroll(y);return true;
        }
        return super.mouseClicked(event,doubleClick);
    }
    @Override public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event,double dx,double dy) {double x=event.x(),y=event.y();int button=event.button();
        if(closePrompt) return true;
        if(dragging && button==0) {if(tab==0)dragPane(y);else dragScroll(y);return true;}
        return super.mouseDragged(event,dx,dy);
    }
    @Override public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {double x=event.x(),y=event.y();int button=event.button();
        if(closePrompt) return true;
        if(dragging && button==0) {dragging=false;return true;}
        return super.mouseReleased(event);
    }
    @Override public boolean mouseScrolled(double x,double y,double horizontal,double vertical) {
        if(closePrompt) return true;
        if(tab==0) {
            for(int pane=0;pane<2;pane++) if(x>=paneX(pane) && x<paneTrack(pane)+7 && y>=clipTop() && y<gridTop()+viewportHeight()) {if(pane==0)scroll-=vertical*CELL;else backgroundScroll-=vertical*CELL;refresh();return true;}
        }
        if(tab!=0 && x>=gridX() && x<trackX()+7 && y>=clipTop() && y<gridTop()+viewportHeight()) {
            scroll-=vertical*CELL;refresh();return true;
        }
        return super.mouseScrolled(x,y,horizontal,vertical);
    }
    @Override public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {int key=event.key();
        if(closePrompt) {if(key==256 || key==257 || key==335) returnToEditing();return true;}
        if(key==256) {onClose();return true;}
        if(search.visible && search.isFocused()) return search.keyPressed(event)||search.canConsumeInput();
        return super.keyPressed(event);
    }
    @Override public void extractBackground(GuiGraphicsExtractor g,int mouseX,int mouseY,float delta) {
        panel(g,leftPos,topPos,mainWidth,designerHeight);
        if(tab==0) {for(int pane=0;pane<2;pane++)DesignerTheme.inset(g,paneX(pane)-3,gridTop()-3,paneTrack(pane)+10-paneX(pane),gridHeight()+3);}
        else DesignerTheme.inset(g,gridX()-3,gridTop()-3,trackX()+10-gridX(),gridHeight()+3);
        // Include the border in the sidebar width so it aligns with the tabs.
        panel(g,sideX()+1,topPos,SIDE-2,PREVIEW_HEIGHT);
        g.text(font,text("preview"),sideX()+8,topPos+8,DesignerTheme.label(),false);
        renderPreview(g);
        if(tab==0) {drawPaneTrack(g,0);drawPaneTrack(g,1);}
        if(tab!=0 && maxScroll()>0) {
            g.fill(trackX(),gridTop(),trackX()+6,gridTop()+viewportHeight(),DesignerTheme.vanilla()?0xFF8B8B8B:0xFF101419);
            DesignerTheme.scrollbar(g,trackX(),thumbY(),thumbHeight(),dragging);
        }
        renderRequirements(g);
    }
    private void renderRequirements(GuiGraphicsExtractor g){
        var costs=costs();int columns=costColumns();int rows=(costs.size()+columns-1)/columns;
        for(int i=0;i<costs.size();i++){
            var c=costs.get(i);int x=contentX()+(i%columns)*34,y=topPos+designerHeight-10-Math.max(1,rows)*18+(i/columns)*18;
            g.text(font,c.amount()+"×",x,y+4,DesignerTheme.required(menu.count(c.item())<c.amount()&&!creative()),false);g.item(new ItemStack(c.item()),x+14,y);
        }
    }
    private void requirementTooltips(GuiGraphicsExtractor g,int mouseX,int mouseY){
        var costs=costs();int columns=costColumns(),rows=(costs.size()+columns-1)/columns;
        for(int i=0;i<costs.size();i++){int x=contentX()+(i%columns)*34,y=topPos+designerHeight-10-Math.max(1,rows)*18+(i/columns)*18;
            if(mouseX>=x&&mouseX<x+32&&mouseY>=y&&mouseY<y+16){var c=costs.get(i);g.setTooltipForNextFrame(font,java.util.List.of(new ItemStack(c.item()).getHoverName(),Component.translatable("screen.sign_plates.material_count",c.amount(),menu.count(c.item()))),java.util.Optional.empty(),mouseX,mouseY);}
        }
    }

    private static void panel(GuiGraphicsExtractor g,int x,int y,int w,int h) {
        DesignerTheme.panel(g,x,y,w,h);
    }
    private void renderPreview(GuiGraphicsExtractor g) {
        ItemStack stack=PlateStyle.stack(draft());
        if(angledPreview) stack.set(net.minecraft.core.component.DataComponents.ITEM_MODEL,
            net.minecraft.resources.Identifier.fromNamespaceAndPath("sign_plates","preview/sign_plate"));
        PlatePreviewState.submit(g, stack, sideX()+1, topPos+24,
                sideX()+SIDE-1, topPos+PREVIEW_HEIGHT-1);
    }

    @Override protected void extractLabels(GuiGraphicsExtractor g,int mouseX,int mouseY) {
        g.text(font,title,12,11,DesignerTheme.title(),false);
        if(tab==0) {
            g.text(font,text("background_heading"),12,60,DesignerTheme.label(),false);
            g.text(font,text("frame_heading"),paneX(1)-leftPos,60,DesignerTheme.label(),false);
        }
        if(tab==2) {
            g.text(font,text("colour"),12,76,DesignerTheme.label(),false);
            g.text(font,text("emission"),12,29,DesignerTheme.label(),false);
            g.text(font,text("glow"),12+mainWidth/2,29,DesignerTheme.label(),false);
        }
        if(tab==0){if(filtered.isEmpty())emptyMessage(g,"empty_backgrounds",0);if(backgrounds.isEmpty())emptyMessage(g,"empty_frames",1);}
        else if(filtered.isEmpty())emptyMessage(g,"empty",-1);
    }
    private void emptyMessage(GuiGraphicsExtractor g,String key,int pane){
        int x=pane<0?gridX()-leftPos:paneX(pane)-leftPos,width=pane<0?trackX()-gridX():paneTrack(pane)-paneX(pane);
        var lines=font.split(text(key),Math.max(24,width-8));int y=gridTop()-topPos+Math.max(0,(viewportHeight()-lines.size()*font.lineHeight)/2);for(var line:lines){g.centeredText(font,line,x+width/2,y,0xFFB0B0B0);y+=font.lineHeight;}
    }
    @Override public void extractRenderState(GuiGraphicsExtractor g,int mouseX,int mouseY,float delta) {
        super.extractRenderState(g,closePrompt?-10000:mouseX,closePrompt?-10000:mouseY,delta);
        if(closePrompt) renderClosePrompt(g,mouseX,mouseY,delta);
        else {
            extractTooltip(g,mouseX,mouseY);
            dyeTooltip(g,mouseX,mouseY);
            requirementTooltips(g,mouseX,mouseY);
        }
    }
    private void dyeTooltip(GuiGraphicsExtractor g,int mouseX,int mouseY) {
        if(tab!=2 || !overGrid(mouseX,mouseY))return;
        for(ChoiceButton cell:cells) {
            if(cell.visible && cell.choice>0 && cell.isMouseOver(mouseX,mouseY)) {
                g.setTooltipForNextFrame(font,new ItemStack(PlateStyle.dye(PlateTint.values()[cell.choice])),mouseX,mouseY);
                return;
            }
        }
    }
    private void renderClosePrompt(GuiGraphicsExtractor g,int mouseX,int mouseY,float delta) {
        int w=Math.min(300,designerWidth-16),x=leftPos+(designerWidth-w)/2,y=topPos+(designerHeight-102)/2;
        g.pose().pushMatrix();g.nextStratum();g.pose().translate(0,0);
        g.fill(0,0,width,height,0xB0000000);panel(g,x,y,w,102);
        g.text(font,text("unapplied_title"),x+10,y+10,DesignerTheme.vanilla()?0xFF404040:0xFFFFD166,false);
        g.textWithWordWrap(font,text("unapplied_message"),x+10,y+28,w-20,DesignerTheme.label());
        leaveButton.extractRenderState(g,mouseX,mouseY,delta);returnButton.extractRenderState(g,mouseX,mouseY,delta);
        g.pose().popMatrix();
    }
    @Override public boolean isPauseScreen() {return false;}
    private final class PreviewRotateButton extends Button {
        private static final net.minecraft.resources.Identifier ICON = net.minecraft.resources.Identifier.fromNamespaceAndPath(
            "sign_plates","textures/block/symbols/recycle.png");
        PreviewRotateButton(int x,int y) {
            super(x,y,18,18,text("rotate_preview"),b->{},DEFAULT_NARRATION);
            updateTip();
        }
        private void updateTip() {setTooltip(Tooltip.create(text(angledPreview?"preview_front":"preview_angled")));}
        @Override public void onPress(net.minecraft.client.input.InputWithModifiers input) {angledPreview=!angledPreview;updateTip();}
        @Override protected void extractContents(GuiGraphicsExtractor g,int mouseX,int mouseY,float delta) {
            int tint=DesignerTheme.vanilla()?(isHoveredOrFocused()?0xFF191919:0xFF404040):0xFFFFFFFF;
            g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,ICON,getX()+3,getY()+3,0,0,12,12,12,12,tint);
        }
    }
    private final class VisualApplyButton extends Button {
        VisualApplyButton(int x,int y) {super(x,y,72,20,text("apply"),b->applyVisuals(),DEFAULT_NARRATION);}
        @Override protected void extractContents(GuiGraphicsExtractor g,int mouseX,int mouseY,float delta){extractDefaultSprite(g);g.centeredText(font,getMessage(),getX()+width/2,getY()+6,active?0xFFFFFFFF:0xFFA0A0A0);}
        @Override public void playDownSound(net.minecraft.client.sounds.SoundManager sounds) {
            // The server plays the stonecutter sound only after a successful commit.
        }
    }
    private final class ChoiceButton extends Button {
        private int choice=-1;
        private String tooltipText="";
        private final int pane;
        ChoiceButton(int pane) {super(0,0,22,22,Component.empty(),b->{},DEFAULT_NARRATION);this.pane=pane;}
        @Override public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event,boolean doubleClick) {return (tab==0?overPane(event.x(),event.y(),pane):overGrid(event.x(),event.y())) && super.mouseClicked(event,doubleClick);}
        @Override public void onPress(net.minecraft.client.input.InputWithModifiers input) {
            if(choice<0) return;
            if(tab==1) {selected=choice;refresh();}
            else if(tab==0) {if(pane==0)draftBackground=choice;else draftFrame=choice;refresh();}
            else {draftTint=PlateTint.values()[choice];refresh();}
        }
        @Override protected void extractContents(GuiGraphicsExtractor g,int mouseX,int mouseY,float delta) {
            if(choice<0) return;
            if(!(tab==0?overPane(mouseX,mouseY,pane):overGrid(mouseX,mouseY))) isHovered=false;
            boolean current=tab==0?choice==(pane==0?draftBackground:draftFrame):tab==1?choice==selected:choice==draftTint.ordinal();
            boolean original=tab==1 && choice==menu.initialSymbol();
            g.enableScissor(tab==0?paneX(pane):gridX(),clipTop(),tab==0?paneTrack(pane):trackX(),gridTop()+viewportHeight());
            DesignerTheme.slot(g,getX(),getY(),width,height,isHoveredOrFocused(),current,original);
            if(tab==2) {
                PlateTint tint=PlateTint.values()[choice];
                if(tint==PlateTint.NONE) {
                    checkerboard(g);
                } else {
                    g.fill(getX()+3,getY()+3,getX()+19,getY()+19,0xFF000000|tint.getTintColor());
                    g.item(new ItemStack(PlateStyle.dye(tint)),getX()+3,getY()+2);
                    g.pose().pushMatrix();g.nextStratum();g.pose().translate(getX()+21,getY()+15);g.pose().scale(0.65f,0.65f);
                    String count=Integer.toString(menu.dyeCount(tint));
                    g.text(font,count,-font.width(count),0,menu.dyeCount(tint)==0?0xFFFF6868:0xFFFFFFFF,true);g.pose().popMatrix();
                }
            } else if(tab==0 && choice==0) {
                // A checkerboard makes the invisible empty frame selectable.
                checkerboard(g);

            } else g.item(tab==0?layer(choice,pane):plate(choice,draftFrame),getX()+3,getY()+3);
            g.disableScissor();
        }
        private void checkerboard(GuiGraphicsExtractor g) {
            for(int y=0;y<4;y++) for(int x=0;x<4;x++) g.fill(getX()+3+x*4,getY()+3+y*4,getX()+7+x*4,getY()+7+y*4,((x+y)%2==0)?(DesignerTheme.vanilla()?0xFFB4B4B4:0xFF98A3AE):(DesignerTheme.vanilla()?0xFF888888:0xFF52606E));
        }
    }
}

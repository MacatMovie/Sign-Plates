import java.lang.reflect.*;
import signplates.client.PlateEditorScreen;
/** Headless regression checks against the real screen's geometry methods. */
public class GeometryChecks {
 static int checks;
 static void check(boolean ok,String message){checks++;if(!ok)throw new AssertionError(message);}
 static Method method(String name,Class<?>... types)throws Exception{var m=PlateEditorScreen.class.getDeclaredMethod(name,types);m.setAccessible(true);return m;}
 public static void main(String[] args)throws Exception{
  net.minecraft.SharedConstants.tryDetectVersion();net.minecraft.server.Bootstrap.bootStrap();
  var f=sun.misc.Unsafe.class.getDeclaredField("theUnsafe");f.setAccessible(true);
  var unsafe=(sun.misc.Unsafe)f.get(null);var screen=(PlateEditorScreen)unsafe.allocateInstance(PlateEditorScreen.class);
  var columnX=method("columnX",int.class,int.class);
  var columnsField=PlateEditorScreen.class.getDeclaredField("columns");columnsField.setAccessible(true);
  var main=PlateEditorScreen.class.getDeclaredField("mainWidth");main.setAccessible(true);
  var rows=PlateEditorScreen.class.getDeclaredField("rows");rows.setAccessible(true);
  for(int available=184;available<=324;available++) {
   int cols=Math.max(3,(available-32)/24);if(cols%2==0)cols--;
   int mainWidth=cols*24+32;columnsField.setInt(screen,cols);main.setInt(screen,mainWidth);
   check(mainWidth<=available,"panel fits available width");
   int gridLeft=(int)method("gridX").invoke(screen),track=(int)method("trackX").invoke(screen);
   int header=(int)method("headerWidth").invoke(screen);
   check(gridLeft+header==track+7,"search/categories align to grid's right border");
   int firstWidth=-1;
   for(int pane=-1;pane<2;pane++) {
    int left=pane<0?gridLeft:(int)method("paneX",int.class).invoke(screen,pane);
    int paneTrack=pane<0?track:(int)method("paneTrack",int.class).invoke(screen,pane);
    int count=pane<0?cols:(int)method("paneColumns",int.class).invoke(screen,pane);
    int previous=left-24;
    for(int col=0;col<count;col++) {
     int x=(int)columnX.invoke(screen,left,col);
     check(x-previous==24,"horizontal pitch equals vertical pitch (22 + 2 pixels)");
     check(x>=left&&x+22<=paneTrack-2,"no scrollbar overlap");previous=x;
    }
    check(previous+22==paneTrack-2,"two-pixel gutter before every scrollbar");
    if(pane==0)firstWidth=paneTrack-left;
    if(pane==1){check(paneTrack-left==firstWidth,"Background and Frame have identical widths");check(paneTrack==track,"split and single grids align right");}
   }
   int last=-2;
   for(int button=0;button<3;button++) {
    int start=button*(header+2)/3,end=(button+1)*(header+2)/3-2;
    check(start==last+2&&end>start,"category buttons remain aligned without overlap");last=end;
   }
   check(last==header,"last category button aligns with search and grid border");
   for(int r=1;r<=7;r++) {
    rows.setInt(screen,r);
    int frame=(int)method("gridHeight").invoke(screen),viewport=(int)method("viewportHeight").invoke(screen);
    check(viewport==frame-1,"bottom content clipping remains one pixel above frame");
    int lightWidth=mainWidth/2-24;last=-2;
    for(int option=0;option<4;option++) {
     int start=option*(lightWidth+2)/4,end=(option+1)*(lightWidth+2)/4-2;
     check(start==last+2&&end>start&&end<=lightWidth,"Glow options still fit");last=end;
    }
   }
  }
  var tab=PlateEditorScreen.class.getDeclaredField("tab");tab.setAccessible(true);
  for(int cols=5;cols<=11;cols+=2)for(int r=1;r<=7;r++) {
   columnsField.setInt(screen,cols);main.setInt(screen,cols*24+32);rows.setInt(screen,r);
   int commonBottom=-1,regularHeight=0,regularTop=0;
   for(int page=2;page>=0;page--) {
    tab.setInt(screen,page);
    int left=(int)method("gridX").invoke(screen),content=(int)method("contentX").invoke(screen);
    int top=(int)method("gridTop").invoke(screen),clip=(int)method("clipTop").invoke(screen);
    int frameHeight=(int)method("gridHeight").invoke(screen),visible=(int)method("viewportHeight").invoke(screen);
    check(clip==top-2,"clip begins immediately inside one-pixel top border");
    check(clip==(top-3)+1,"no interior strip is hidden above scrolling entries");
    check(visible==frameHeight-1,"bottom border remains protected");
    if(page==2){commonBottom=top+visible;regularHeight=frameHeight;regularTop=top;}
    check(top+visible==commonBottom,"footer and grid bottom stay fixed across all tabs");
    if(page==0){check(frameHeight==regularHeight+24,"Background/Frame get one extra full row");check(top==regularTop-24,"split grids start one row higher");}
    if(page==1) {
     check(left==content+3,"Symbols entries and scrollbar shift right by exactly three pixels");
     check(left-3==content,"Symbols frame's left border aligns with search/category controls");
    }else check(left==content,"other tabs keep their original left alignment");
    check((int)method("headerWidth").invoke(screen)==cols*24+7,"header width stays independent of selected tab");
   }
  }
  System.out.println("PASS "+checks+" screen geometry checks (equal spacing, pane widths, Symbols alignment, top/bottom clipping, extra Background row and Glow control fit)");
 }
}

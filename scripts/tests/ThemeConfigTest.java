import signplates.SignPlatesConfig;
import java.nio.file.*;
import java.util.*;
public class ThemeConfigTest {
 static final Path FILE=Path.of("config/sign_plates-client.properties");
 static Properties read() throws Exception {var p=new Properties();try(var s=Files.newInputStream(FILE)){p.load(s);}return p;}
 static void write(Properties p) throws Exception {try(var s=Files.newOutputStream(FILE)){p.store(s,"test");}}
 static void check(boolean ok,String reason){if(!ok)throw new AssertionError(reason);}
 public static void main(String[] args) throws Exception {
  check(SignPlatesConfig.vanillaGui(),"Fresh installs must default to Vanilla");
  check(SignPlatesConfig.showTooltips(),"Tooltip default changed");
  check(read().getProperty("gui_theme").equals("vanilla"),"Fresh default not saved");
  var old=new Properties();old.setProperty("show_tooltips","false");old.setProperty("foreign_key","keep");write(old);
  SignPlatesConfig.load();
  check(SignPlatesConfig.vanillaGui()&&!SignPlatesConfig.showTooltips(),"Migration must default Vanilla and preserve tooltip OFF");
  check(read().getProperty("foreign_key").equals("keep"),"Migration lost unrelated property");
  SignPlatesConfig.setVanillaGui(false);SignPlatesConfig.save();
  check(read().getProperty("gui_theme").equals("dark"),"Dark not saved");
  SignPlatesConfig.setVanillaGui(true);SignPlatesConfig.load();
  check(!SignPlatesConfig.vanillaGui(),"Dark did not survive reload");
  var data=read();data.setProperty("gui_theme"," DaRk ");write(data);SignPlatesConfig.load();
  check(!SignPlatesConfig.vanillaGui(),"Theme parsing not case/space tolerant");
  data=read();data.setProperty("gui_theme","unknown");write(data);SignPlatesConfig.load();
  check(SignPlatesConfig.vanillaGui(),"Unknown theme must safely default Vanilla");
  SignPlatesConfig.setShowTooltips(true);SignPlatesConfig.setVanillaGui(true);SignPlatesConfig.save();SignPlatesConfig.load();
  check(SignPlatesConfig.showTooltips()&&SignPlatesConfig.vanillaGui(),"Reset defaults did not persist");
  check(read().getProperty("foreign_key").equals("keep"),"Save lost unrelated property");
  System.out.println("PASS: actual config default, migration, Dark persistence, reset, parsing and unrelated-key preservation.");
 }
}

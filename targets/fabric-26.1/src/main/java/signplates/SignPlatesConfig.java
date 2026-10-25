package signplates;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class SignPlatesConfig {
    private static final Path CONFIG_PATH=Paths.get("config","sign_plates-client.properties");
    private static boolean loaded=false, showTooltips=true, vanillaGui=true;
    private SignPlatesConfig() {}
    public static boolean showTooltips() {if(!loaded) load();return showTooltips;}
    public static boolean vanillaGui() {if(!loaded) load();return vanillaGui;}
    public static void setShowTooltips(boolean value) {showTooltips();showTooltips=value;}
    public static void setVanillaGui(boolean value) {vanillaGui();vanillaGui=value;}
    private static Properties read() throws Exception {
        Properties properties=new Properties();
        Files.createDirectories(CONFIG_PATH.getParent());
        if(Files.exists(CONFIG_PATH)) try(InputStream input=Files.newInputStream(CONFIG_PATH)) {properties.load(input);}
        return properties;
    }
    private static void write(Properties properties) throws Exception {
        try(OutputStream output=Files.newOutputStream(CONFIG_PATH)) {properties.store(output,"Sign Plates client config");}
    }
    public static void load() {
        showTooltips=true;vanillaGui=true;
        try {
            Properties properties=read();
            showTooltips=Boolean.parseBoolean(properties.getProperty("show_tooltips","true").trim());
            vanillaGui=!properties.getProperty("gui_theme","vanilla").trim().equalsIgnoreCase("dark");
            if(!properties.containsKey("show_tooltips") || !properties.containsKey("gui_theme")) {
                properties.setProperty("show_tooltips",Boolean.toString(showTooltips));
                properties.setProperty("gui_theme",vanillaGui?"vanilla":"dark");
                write(properties);
            }
        } catch(Exception exception) {SignPlatesMod.LOGGER.warn("Could not load Sign Plates config, using defaults",exception);}
        loaded=true;
    }
    public static void save() {
        if(!loaded) load();
        try {
            Properties properties=read();
            properties.setProperty("show_tooltips",Boolean.toString(showTooltips));
            properties.setProperty("gui_theme",vanillaGui?"vanilla":"dark");
            write(properties);
        } catch(Exception exception) {SignPlatesMod.LOGGER.warn("Could not save Sign Plates config",exception);}
    }
}

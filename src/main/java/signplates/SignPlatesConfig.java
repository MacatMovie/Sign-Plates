package signplates;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class SignPlatesConfig {
    private static final Path CONFIG_PATH = Paths.get("config", "sign_plates-client.properties");
    private static boolean loaded = false;
    private static boolean showTooltips = true;

    private SignPlatesConfig() {
    }

    public static boolean showTooltips() {
        if (!loaded) {
            load();
        }
        return showTooltips;
    }

    public static void load() {
        Properties properties = new Properties();
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            if (Files.exists(CONFIG_PATH)) {
                try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                    properties.load(input);
                }
            }

            String value = properties.getProperty("show_tooltips");
            if (value == null) {
                properties.setProperty("show_tooltips", Boolean.toString(showTooltips));
                try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
                    properties.store(output, "Sign Plates client config");
                }
            } else {
                showTooltips = Boolean.parseBoolean(value.trim());
            }
        } catch (Exception exception) {
            SignPlatesMod.LOGGER.warn("Could not load Sign Plates config, using defaults", exception);
        }
        loaded = true;
    }
}

package javafxapplication1.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Properties props = new Properties();
    static {
        try(InputStream in = ConfigLoader.class.getResourceAsStream("/config.properties")) {
            props.load(in);
        } catch(Exception e) {
            throw new RuntimeException("No se pudo cargar config.properties", e);
        }
    }
    public static String get(String key) {
        return props.getProperty(key);
    }
}
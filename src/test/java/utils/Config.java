package utils;

public class Config {

    public static String get(String key) {
        String v = System.getenv(key);
        if (v != null && !v.isBlank()) {

            return v;
        }
        v = System.getProperty(key);
        if (v != null && !v.isBlank()) {

            return v;
        }
        return null;
    }

    public static String getOrDefault(String key, String def) {
        String v = get(key);
        return (v == null || v.isBlank()) ? def : v;
    }
}
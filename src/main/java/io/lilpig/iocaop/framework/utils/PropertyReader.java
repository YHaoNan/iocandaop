package io.lilpig.iocaop.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private Properties properties;
    private String source;
    public PropertyReader(String filename) throws IOException {
        this.source = filename;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        properties = new Properties();
        properties.load(inputStream);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getPropertyOrDefault(String key, String defaultValue) {
        String result = getProperty(key);
        return result == null ? defaultValue : result;
    }

    public <T> T getProperty(String key, Class<T> requiredType) {
        try {
            T t = (T) properties.get(key);
            return t;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T> T getPropertyOrDefault(String key, Class<T> requiredType, T defaultValue) {
        T t = getProperty(key, requiredType);
        return t==null ? defaultValue : t;
    }

    public String getSource() {
        return source;
    }
}

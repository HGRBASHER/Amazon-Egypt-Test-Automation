package Reuse;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigHandler {
    Properties properties;
    public ConfigHandler(String path) {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            properties.load(fileInputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getValue(String key) {
        return properties.getProperty(key);
    }
    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }

}

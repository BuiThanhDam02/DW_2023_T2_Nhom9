package PropertiesConfig;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
public class PropertiesConfig {
    Map<String, String> resource  ;

    public PropertiesConfig(String properties_path) {
        readPropertiesSource(properties_path);
    }

    public Map<String, String> getResource() {
        return resource;
    }

    public void setResource(Map<String, String> resource) {
        this.resource = resource;
    }

    public void readPropertiesSource(String properties_path){

            Map<String, String> propertiesMap = new HashMap<>();
            try (InputStream input = PropertiesConfig.class.getClassLoader().getResourceAsStream(properties_path)) {
                Properties properties = new Properties();
                if (input == null) {
                    System.out.println("Sorry, unable to find " + properties_path);
                }

                properties.load(input);

                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    propertiesMap.put(key, value);
                }

            }catch (Exception e) {
                e.printStackTrace();
            }

            this.resource = propertiesMap;
    }

    public static void main(String[] args) {
        PropertiesConfig pc = new PropertiesConfig("path.properties");
        for (Map.Entry<String, String> entry : pc.getResource().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

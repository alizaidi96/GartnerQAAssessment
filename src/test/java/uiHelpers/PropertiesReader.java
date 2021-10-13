package uiHelpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertiesReader {

    private static PropertiesReader envProperties;
    private Properties properties;
    
    public PropertiesReader() {
        properties = loadProperties();
    }

    // To load Properties file

    public Properties loadProperties() {
        File file = new File("./src/main/resources/configuration.properties");
        FileInputStream fileInput = null;
        Properties props = new Properties();

        try {
            fileInput = new FileInputStream(file);
            props.load(fileInput);
            fileInput.close();
            file = null;

        } catch (FileNotFoundException e) {
            System.err.println("config.properties is missing or corrupt : " + e.getMessage());
        } catch (IOException e) {
        	System.err.println("read failed due to: " + e.getMessage());
        }

        return props;
    }

    // Overloaded function to load Properties file

    

    // To get Instance of Properties Reader

    public static PropertiesReader getInstance()
    {
        if (envProperties == null) {
            envProperties = new PropertiesReader();
        }

        return envProperties;
    }

    // To get Property on the basis of key

    public String getProperty(String key)
    {
        return properties.getProperty(key);
    }

}

package hyper.reports.util;

import java.io.*;
import java.util.Properties;

public abstract class SettingsProvider {

    private static Properties internalProperties;
    private static Properties externalProperties;

    public static void initialize(InputStream internalConfigFile, File externalConfigFile) {
        try {
            internalProperties = new Properties();
            internalProperties.load(internalConfigFile);

            externalProperties = new Properties();
            externalProperties.load(new FileInputStream(externalConfigFile));
        } catch (IOException e) {
            System.err.println("Error reading config file!!!");
        }
    }

    public static String getConfig(String key) {
        if (externalProperties != null && externalProperties.getProperty(key) != null) {
            return externalProperties.getProperty(key);
        }
        if (internalProperties != null && internalProperties.getProperty(key) != null) {
            return internalProperties.getProperty(key);
        }

        return null;
    }

    public static Boolean internalConfigIsLoaded() {
        return internalProperties != null;
    }

    public static Boolean externalConfigIsLoaded() {
        return externalProperties != null;
    }
}

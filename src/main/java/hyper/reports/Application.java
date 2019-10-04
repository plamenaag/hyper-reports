package hyper.reports;

import hyper.reports.command.Command;
import hyper.reports.command.CommandConfig;
import hyper.reports.command.CommandProcess;
import hyper.reports.command.CommandReport;
import hyper.reports.util.SettingsProvider;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

public class Application {
    private static String userHomeDir = System.getProperty("user.home");
    private static String configFileName = Paths.get(userHomeDir, Constants.PROJECT_NAME, Constants.CONFIG_FILE_NAME).toString();

    public static void main(String[] args) {

        Command command = null;

        if(args.length==0){
            System.err.println("No command supplied!!!");
            return;
        }

        switch (args[0]) {
            case "config":
                command = new CommandConfig(args, userHomeDir, configFileName);
                break;
            case "process":
                setupConfiguration();
                if(!SettingsProvider.internalConfigIsLoaded()){
                    System.err.println("Error loading internal config!!!");
                }else if(!SettingsProvider.externalConfigIsLoaded()){
                    System.err.println("Error loading external config!!!");
                }else if (SettingsProvider.getConfig(Constants.CONFIG_DATA_DIR) != null
                        && !SettingsProvider.getConfig(Constants.CONFIG_DATA_DIR).isEmpty()) {
                    command = new CommandProcess();
                } else {
                    System.err.println("No data dir is found in the config!!!");
                }
                break;
            case "report":
                setupConfiguration();
                if(!SettingsProvider.internalConfigIsLoaded()){
                    System.err.println("Error loading internal config!!!");
                }else if(!SettingsProvider.externalConfigIsLoaded()){
                    System.err.println("Error loading external config!!!");
                }else if (SettingsProvider.getConfig(Constants.CONFIG_EXPORT_DIR) != null
                        && !SettingsProvider.getConfig(Constants.CONFIG_EXPORT_DIR).isEmpty()) {
                    command =  new CommandReport(args);
                } else {
                    System.err.println("No export dir is found in the config!!!");
                }
                break;
        }

        if (command != null) {
            command.run();
        }
    }

    private static void setupConfiguration() {
        InputStream internalConfigFile = Application.class.getClassLoader().getResourceAsStream("config.properties");
        SettingsProvider.initialize(internalConfigFile, new File(configFileName));
    }
}


package hyper.reports.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import hyper.reports.Constants;
import hyper.reports.util.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class CommandConfig implements Command {
    private String userHomeDir;
    private String configFileName;

    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "--data-dir")
    private String dataDir;

    @Parameter(names = "--export-dir")
    private String exportDir;

    public CommandConfig(String[] args, String userHomeDir, String configFileName) {
        this.userHomeDir = userHomeDir;
        this.configFileName = configFileName;

        JCommander jc = JCommander.newBuilder().addObject(this).build();
        jc.parse(args);
    }

    @Override
    public void run() {
        new File(Paths.get(userHomeDir, Constants.PROJECT_NAME).toString()).mkdir();
        Properties properties = new Properties();

        try {
            String fileStr = FileUtils.readFile(configFileName);
            if (fileStr != null) {
                properties.load(new StringReader(fileStr));
            }
        } catch (IOException e) {
            System.out.println("Config is empty");
        }

        if (dataDir != null) {
            properties.setProperty(Constants.CONFIG_DATA_DIR, dataDir);
            System.out.println("Setting " + Constants.CONFIG_DATA_DIR);
        }
        if (exportDir != null) {
            properties.setProperty(Constants.CONFIG_EXPORT_DIR, exportDir);
            System.out.println("Setting " + Constants.CONFIG_EXPORT_DIR);
        }

        String readyConf = getPropertyAsString(properties);

        try {
            FileUtils.writeFile(readyConf, configFileName);
            System.out.println("Config saved ");
        } catch (IOException e) {
            System.err.println("Config is not saved!");
        }
    }

    private static String getPropertyAsString(Properties properties) {
        Set<String> keys = properties.stringPropertyNames();
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key + "=" + StringEscapeUtils.escapeJava(properties.getProperty(key)));
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}

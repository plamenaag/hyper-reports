package hyper.reports.command;

import hyper.reports.Constants;
import hyper.reports.database.ConnectionManager;
import hyper.reports.model.FileInfo;
import hyper.reports.service.DailyReportService;
import hyper.reports.util.SettingsProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandProcess implements Command {
    @Override
    public void run() {
        String filesDirectory = SettingsProvider.getConfig(Constants.CONFIG_DATA_DIR);
        if (filesDirectory != null) {
            try (Stream<Path> filePaths = Files.walk(Paths.get(filesDirectory))) {
                List<Path> paths = filePaths.filter(Files::isRegularFile).collect(Collectors.toList());
                for (Path path : paths) {
                    DailyReportService dailyReportService = new DailyReportService(ConnectionManager.getConnection());
                    dailyReportService.importFile(new FileInfo(path));
                }
            } catch (IOException e) {
                System.err.println("Couldn't read files from directory!");
            } catch (Exception e) {
                System.err.println("Import failed!");
            }
        }
    }
}

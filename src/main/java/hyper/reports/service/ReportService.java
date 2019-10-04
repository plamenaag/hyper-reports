package hyper.reports.service;

import hyper.reports.Constants;
import hyper.reports.database.ConnectionManager;
import hyper.reports.repository.ReportRepo;
import hyper.reports.repository.error.RepoException;
import hyper.reports.util.OdfExportUtil;
import hyper.reports.util.SettingsProvider;
import java.nio.file.Paths;
import java.util.List;

public class ReportService {

    public void generateReport(String reportType, String companyName, String fromDate, String toDate, String order, Integer top) throws RepoException {
        ReportRepo reportRepo = new ReportRepo(ConnectionManager.getConnection());
        List<Object[]> reportDataByEmployee = reportRepo.getTurnoverReport(reportType, companyName, fromDate, toDate, order, top);

        String fileName = fromDate + "_" + toDate + "_" + companyName + "_" + reportType + ".ods";
        String exportDir = SettingsProvider.getConfig(Constants.CONFIG_EXPORT_DIR);
        if (exportDir != null) {
            String fullFilePath = Paths.get(exportDir, fileName).toString();
            OdfExportUtil.exportToOds(reportDataByEmployee, new String[]{reportType, Constants.REPORT_HEADER_TURNOVER}, fullFilePath);
        } else {
            System.err.println("No export dir specified!!! Can't do export!!!");
        }
    }
}



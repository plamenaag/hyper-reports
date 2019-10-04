package hyper.reports.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import hyper.reports.repository.error.RepoException;
import hyper.reports.service.ReportService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommandReport implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "--company")
    private String company;

    @Parameter(names = "--aggregation")
    private String aggregation;

    @Parameter(names = "--order")
    private String order;

    @Parameter(names = "--year")
    private String year;

    @Parameter(names = "--month")
    private String month;

    @Parameter(names = "--quarter")
    private String quarter;

    @Parameter(names = "--top")
    private Integer top;

    public CommandReport(String[] args) {
        JCommander jc = JCommander.newBuilder().addObject(this).build();
        jc.parse(args);
    }

    @Override
    public void run() {
        Integer monthCount;
        SimpleDateFormat sdf;
        SimpleDateFormat reportSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            if (year != null & month != null) {
                sdf = new SimpleDateFormat("yyyy-MM");
                date = sdf.parse(year + "-" + month);
                monthCount = 1;
            } else if (year != null & quarter != null) {
                sdf = new SimpleDateFormat("yyyy-MM");
                String month;
                switch (quarter.toLowerCase()) {
                    case "q2":
                        month = "04";
                        break;
                    case "q3":
                        month = "07";
                        break;
                    case "q4":
                        month = "10";
                        break;
                    default:
                        month = "01";
                }
                date = sdf.parse(year + "-" + month);
                monthCount = 3;
            } else {
                sdf = new SimpleDateFormat("yyyy");
                date = sdf.parse(year);
                monthCount = 12;
            }

            String fromDate = reportSdf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, monthCount);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            String toDate = reportSdf.format(cal.getTime());

            ReportService reportService = new ReportService();
            reportService.generateReport(aggregation, company, fromDate, toDate, order, top);
        } catch (ParseException e) {
            System.err.println("Invalid date arguments!");
        } catch (RepoException e) {
            System.err.println("Report generation failed!!");
        }
    }
}

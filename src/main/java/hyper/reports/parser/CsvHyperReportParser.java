package hyper.reports.parser;

import hyper.reports.dto.ReportDto;
import hyper.reports.dto.csv.CsvDto;
import hyper.reports.model.FileInfo;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHyperReportParser implements HyperReportParser {

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                new NotNull(), // city
                new NotNull(), // employee
                new ParseDouble(), // turnover
        };
    }

    @Override
    public List<ReportDto> parse(FileInfo fileInfo) throws Exception {
        List<ReportDto> list = new ArrayList<>();
        try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(fileInfo.getAbsolutePath()), CsvPreference.STANDARD_PREFERENCE)) {
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            CsvDto record;
            while ((record = beanReader.read(CsvDto.class, header, processors)) != null) {
                ReportDto dtoObject = new ReportDto();
                dtoObject.setCityName(record.getCity());
                dtoObject.setTurnoverAmount(record.getTurnover());
                dtoObject.setEmployeeName(record.getEmployee());
                list.add(dtoObject);
            }
        }

        return list;
    }
}

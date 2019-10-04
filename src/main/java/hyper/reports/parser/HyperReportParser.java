package hyper.reports.parser;

import hyper.reports.dto.ReportDto;
import hyper.reports.model.FileInfo;

import java.util.List;

public interface HyperReportParser {
    List<ReportDto> parse(FileInfo fileInfo) throws Exception;
}

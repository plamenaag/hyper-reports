package hyper.reports.parser;

import hyper.reports.dto.ReportDto;
import hyper.reports.model.FileInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

public class XmlSaxHyperReportParser implements HyperReportParser {

    @Override
    public List<ReportDto> parse(FileInfo fileInfo) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        List<ReportDto> list = new ArrayList<>();
        XmlFileHandler handler = new XmlFileHandler(list);
        saxParser.parse(fileInfo.getFile(), handler);

        return list;
    }

    private class XmlFileHandler extends DefaultHandler {
        private List<ReportDto> list;
        private String cityName;
        private String departmentName;
        private String employeeName;
        private Double turnover;
        boolean isEmployeeTag = false;
        boolean isTurnoverTag = false;

        public XmlFileHandler(List<ReportDto> list) {
            super();
            this.list = list;
        }

        @Override
        public void startElement(String uri, String localName, String tagName, Attributes attributes) {
            if (tagName.equalsIgnoreCase("city")) {
                cityName = attributes.getValue("name");
            } else if (tagName.equalsIgnoreCase("department")) {
                departmentName = attributes.getValue("name");
            } else if (tagName.equalsIgnoreCase("employee")) {
                isEmployeeTag = true;
            } else if (tagName.equalsIgnoreCase("turnover")) {
                isTurnoverTag = true;
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String tagName) {
            if (tagName.equalsIgnoreCase("department")) {
                ReportDto dtoObject = new ReportDto();
                dtoObject.setDepartmentName(departmentName);
                dtoObject.setEmployeeName(employeeName);
                dtoObject.setTurnoverAmount(turnover);
                dtoObject.setCityName(cityName);
                list.add(dtoObject);

                employeeName = null;
                departmentName = null;
                turnover = null;
            } else if (tagName.equalsIgnoreCase("city")) {
                cityName = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isEmployeeTag) {
                employeeName = new String(ch, start, length);
                isEmployeeTag = false;
            } else if (isTurnoverTag) {
                turnover = Double.parseDouble(new String(ch, start, length));
                isTurnoverTag = false;
            }
        }
    }
}




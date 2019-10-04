package hyper.reports.parser;

import hyper.reports.dto.ReportDto;
import hyper.reports.model.FileInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class XmlDomHyperReportParser implements HyperReportParser {

    @Override
    public List<ReportDto> parse(FileInfo fileInfo) throws Exception {
        List<ReportDto> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fileInfo.getAbsolutePath());

        NodeList cityList = document.getElementsByTagName("city");
        for (int i = 0; i < cityList.getLength(); i++) {
            Node cityNode = cityList.item(i);
            String cityName = null;
            if (cityNode.getNodeType() == Node.ELEMENT_NODE) {
                Element cityElement = (Element) cityNode;
                cityName = cityElement.getAttribute("name");
            }

            NodeList departmentList = document.getElementsByTagName("department");
            for (int j = 0; j < departmentList.getLength(); j++) {
                Node departmentNode = departmentList.item(j);

                if (departmentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element departmentElement = (Element) departmentNode;
                    String departmentName = departmentElement.getAttribute("name");
                    NodeList employeeNodes = departmentElement.getElementsByTagName("employee");

                    String employeeName = null;
                    if (employeeNodes != null && employeeNodes.getLength() >= 1) {
                        employeeName = employeeNodes.item(0).getTextContent();
                    }

                    Double turnover = null;
                    NodeList turnoverNodes = departmentElement.getElementsByTagName("turnover");
                    if (turnoverNodes != null && turnoverNodes.getLength() >= 1) {
                        turnover = Double.parseDouble(turnoverNodes.item(0).getTextContent());
                    }

                    ReportDto dtoObject = new ReportDto();
                    dtoObject.setCityName(cityName);
                    dtoObject.setTurnoverAmount(turnover);
                    dtoObject.setEmployeeName(employeeName);
                    dtoObject.setDepartmentName(departmentName);
                    list.add(dtoObject);
                }
            }
        }

        return list;
    }
}

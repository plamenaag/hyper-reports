package hyper.reports.parser;

import hyper.reports.dto.ReportDto;
import hyper.reports.dto.xml.CityDto;
import hyper.reports.dto.xml.CityListDto;
import hyper.reports.dto.xml.DepartmentDto;
import hyper.reports.model.FileInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.List;

public class XmlJaxbHyperReportParser implements HyperReportParser {

    @Override
    public List<ReportDto> parse(FileInfo fileInfo) throws Exception {
        List<ReportDto> list = new ArrayList<>();
        JAXBContext jaxbContext = JAXBContext.newInstance(CityListDto.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        CityListDto cityListDto = (CityListDto) jaxbUnmarshaller.unmarshal(fileInfo.getFile());
        for (CityDto cityDto : cityListDto.getCities()) {
            for (DepartmentDto departmentDto : cityDto.getDepartmentList().getDepartments()) {
                ReportDto dtoObject = new ReportDto();
                dtoObject.setCityName(cityDto.getCityName());
                dtoObject.setTurnoverAmount(departmentDto.getTurnover());
                dtoObject.setEmployeeName(departmentDto.getEmployee());
                dtoObject.setDepartmentName(departmentDto.getDepartmentName());
                list.add(dtoObject);
            }
        }

        return list;
    }
}

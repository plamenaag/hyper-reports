package hyper.reports.dto.xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "city")
@XmlAccessorType(XmlAccessType.FIELD)
public class CityDto {

    @XmlAttribute(name = "name")
    private String cityName;

    @XmlElement(name = "departments")
    private DepartmentListDto departmentList;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public DepartmentListDto getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(DepartmentListDto departmentList) {
        this.departmentList = departmentList;
    }
}



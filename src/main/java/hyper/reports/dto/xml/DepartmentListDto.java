package hyper.reports.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "departments")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentListDto {

    @XmlElement(name = "department")
    private List<DepartmentDto> departments;

    public List<DepartmentDto> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDto> departments) {
        this.departments = departments;
    }
}

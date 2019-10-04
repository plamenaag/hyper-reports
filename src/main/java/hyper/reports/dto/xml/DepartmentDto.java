package hyper.reports.dto.xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "department")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepartmentDto {

    @XmlAttribute(name = "name")
    private String departmentName;

    @XmlElement(name = "employee")
    private String employee;

    @XmlElement(name = "turnover")
    private Double turnover;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }
}

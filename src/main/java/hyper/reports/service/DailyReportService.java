package hyper.reports.service;

import hyper.reports.Constants;
import hyper.reports.database.ConnectionManager;
import hyper.reports.dto.ReportDto;
import hyper.reports.entity.City;
import hyper.reports.entity.Company;
import hyper.reports.entity.Department;
import hyper.reports.entity.Employee;
import hyper.reports.model.FileInfo;
import hyper.reports.parser.*;
import hyper.reports.repository.*;
import hyper.reports.repository.error.RepoException;
import hyper.reports.util.SettingsProvider;
import java.sql.Connection;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class DailyReportService {

    private CompanyRepo companyRepo;
    private CityRepo cityRepo;
    private DepartmentRepo departmentRepo;
    private TurnoverRepo turnoverRepo;
    private EmployeeRepo employeeRepo;
    private Connection connection;

    public DailyReportService(Connection connection) {
        this.connection = connection;
        companyRepo = new CompanyRepo(connection);
        cityRepo = new CityRepo(connection);
        departmentRepo = new DepartmentRepo(connection);
        turnoverRepo = new TurnoverRepo(connection);
        employeeRepo = new EmployeeRepo(connection);
    }

    public void importFile(FileInfo fileInfo) throws Exception {
        if (fileInfo.getFileExtension() == null) {
            System.out.println(fileInfo.getFileName() + " is not supported. Will not be imported!!!");
            return;
        }

        boolean fileImportedSuccessfully = true;

        try {
            connection = ConnectionManager.getConnection();
            if (connection == null) {
                System.err.println("No connection. Can't import file:" + fileInfo.getFileName());
                return;
            }

            connection.setAutoCommit(false);

            String companyName = fileInfo.getCompanyName();
            Instant date = fileInfo.getDate();

            Company company = companyRepo.getByName(companyName);
            if (company == null) {
                company = new Company();
                company.setName(companyName);
                company.setLastDocDate(date);
                company = companyRepo.save(company);
            } else if (company.getLastDocDate() == null || company.getLastDocDate().isBefore(date)) {
                company.setLastDocDate(date);
                company = companyRepo.save(company);
            } else {
                System.out.println(fileInfo.getFileName() + " is an old one. Will not be imported!!!");
                return;
            }

            HyperReportParser parser = null;

            if (fileInfo.getFileExtension().equals("csv")) {
                parser = new CsvHyperReportParser();
            } else if (fileInfo.getFileExtension().equals("xml")) {
                parser = getXmlHyperReportParser();
            } else {
                System.out.println(fileInfo.getFileExtension() + " is not supported. Will not be imported!!!");
                return;
            }

            persistDataToDb(parser.parse(fileInfo), company, date);

            connection.commit();
        } catch (RepoException ex) {
            fileImportedSuccessfully = false;
            connection.rollback();
            System.err.println(ex.getMessage());
            System.err.printf("Error in import. %s not imported \n", fileInfo.getFileName());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        if (fileImportedSuccessfully) {
            System.out.printf("%s imported successfully!!! \n", fileInfo.getFileName());
        }
    }

    private void persistDataToDb(List<ReportDto> list, Company company, Instant date) throws RepoException {
        HashMap<String, City> cityMap = new HashMap<>();

        for (ReportDto dtoObject : list) {
            City city = cityMap.get(dtoObject.getCityName());
            if (city == null) {
                city = cityRepo.saveIfNotExist(dtoObject.getCityName());
                cityMap.put(dtoObject.getCityName(), city);
            }

            Department department = null;
            if (dtoObject.getDepartmentName() != null) {
                department = departmentRepo.saveIfNotExist(dtoObject.getDepartmentName());
            }

            Integer departmentId = department == null ? null : department.getId();
            Employee employee = employeeRepo.saveIfNotExist(dtoObject.getEmployeeName(),
                    city.getId(),
                    company.getId(),
                    departmentId);

            turnoverRepo.saveIfNotExist(dtoObject.getTurnoverAmount(), employee.getId(), date);
        }
    }

    private HyperReportParser getXmlHyperReportParser() throws Exception {
        HyperReportParser parser;
        String xmlParserType = SettingsProvider.getConfig(Constants.CONFIG_XML_HYPER_REPORT_PARSER_TYPE);
        if (xmlParserType != null) {
            switch (xmlParserType) {
                case Constants.CONFIG_XML_HYPER_REPORT_PARSER_TYPE_DOM: {
                    parser = new XmlDomHyperReportParser();
                }
                break;
                case Constants.CONFIG_XML_HYPER_REPORT_PARSER_TYPE_SAX: {
                    parser = new XmlSaxHyperReportParser();
                }
                break;
                case Constants.CONFIG_XML_HYPER_REPORT_PARSER_TYPE_JAXB: {
                    parser = new XmlJaxbHyperReportParser();
                }
                break;
                default:
                    throw new Exception("XML hyper report parser type is not supported");
            }
        } else {
            parser = new XmlJaxbHyperReportParser();
        }

        return parser;
    }

}

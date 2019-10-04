package hyper.reports.repository;

import hyper.reports.entity.Employee;
import hyper.reports.repository.error.RepoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo extends BaseRepo {

    public EmployeeRepo(Connection connection) {
        super(connection);
    }

    public List<Employee> getAll() throws RepoException {
        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(resultSetToEmployee(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public List<Employee> getManyByName(String name) throws RepoException {
        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee WHERE name like '%" + name + "%'";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery(query)) {
            if (rs.next()) {
                list.add(resultSetToEmployee(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public Employee getByNameCityIdAndCompanyId(String name, Integer cityId, Integer companyId) throws RepoException {
        String query = "SELECT * FROM employee WHERE name = ? AND city_id = ? AND company_id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, name);
            preparedStmt.setInt(2, cityId);
            preparedStmt.setInt(3, companyId);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToEmployee(rs);
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Employee getByNameCityIdCompanyIdAndDepartmentId(String name, Integer cityId, Integer companyId, Integer departmentId) throws RepoException {
        String query = "SELECT * FROM employee WHERE name = ? AND city_id = ? AND company_id = ? AND department_id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, name);
            preparedStmt.setInt(2, cityId);
            preparedStmt.setInt(3, companyId);
            preparedStmt.setInt(4, departmentId);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToEmployee(rs);
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Employee getById(Integer id) throws RepoException {
        String query = "SELECT * FROM employee WHERE id = " + id;
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery(query)) {
            if (rs.next()) {
                return resultSetToEmployee(rs);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Employee save(Employee employee) throws RepoException {
        if (employee != null && employee.getId() != null) {
            return update(employee);
        } else {
            return create(employee);
        }
    }

    private Employee create(Employee employee) throws RepoException {
        String query = "INSERT INTO employee (name, city_id, company_id, department_id) values (?, ?, ?, ?)";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, employee.getName());
            preparedStmt.setInt(2, employee.getCityId());
            preparedStmt.setInt(3, employee.getCompanyId());
            if (employee.getDepartmentId() == null) {
                preparedStmt.setNull(4, Types.NULL);
            } else {
                preparedStmt.setInt(4, employee.getDepartmentId());
            }

            preparedStmt.execute();
            try (ResultSet rs = preparedStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                  employee.setId(id);
                    return employee;
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    private Employee update(Employee employee) throws RepoException {
        String query = "UPDATE company SET name = ?, city_id = ?, company_id = ?, department_id = ? WHERE id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, employee.getName());
            preparedStmt.setInt(2, employee.getCityId());
            preparedStmt.setInt(3, employee.getCompanyId());
            if (employee.getDepartmentId() == null) {
                preparedStmt.setNull(4, Types.NULL);
            } else {
                preparedStmt.setInt(4, employee.getDepartmentId());
            }
            preparedStmt.setInt(5, employee.getId());

            preparedStmt.execute();
            return employee;
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }
    }

    private Employee resultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setName(rs.getString("name"));
        employee.setId(rs.getInt("id"));
        employee.setCityId(rs.getInt("city_id"));
        employee.setCompanyId(rs.getInt("company_id"));
        employee.setDepartmentId(rs.getInt("department_id"));
        return employee;
    }

    public Employee saveIfNotExist(String employeeName, Integer cityId, Integer companyId) throws RepoException {
        return saveIfNotExist(employeeName, cityId, companyId, null);
    }

    public Employee saveIfNotExist(String employeeName, Integer cityId, Integer companyId, Integer departmentId) throws RepoException {
        Employee employee = null;
        if (departmentId == null) {
            employee = getByNameCityIdAndCompanyId(employeeName, cityId, companyId);
        } else {
            employee = getByNameCityIdCompanyIdAndDepartmentId(employeeName, cityId, companyId, departmentId);
        }

        if (employee == null) {
            employee = new Employee();
            employee.setName(employeeName);
            employee.setCityId(cityId);
            employee.setCompanyId(companyId);
            employee.setDepartmentId(departmentId);
            employee = save(employee);
        }

        return employee;
    }
}

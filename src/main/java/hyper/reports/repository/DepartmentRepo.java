package hyper.reports.repository;

import hyper.reports.entity.Department;
import hyper.reports.repository.error.RepoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepo extends BaseRepo {

    public DepartmentRepo(Connection connection) {
        super(connection);
    }

    public List<Department> getAll() throws RepoException {
        List<Department> list = new ArrayList<>();
        String query = "SELECT * FROM department";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                list.add(resultSetToDepartment(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public List<Department> getManyByName(String name) throws RepoException {
        List<Department> list = new ArrayList<>();
        String query = "SELECT * FROM department WHERE name like %" + name + "%";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                list.add(resultSetToDepartment(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public Department getByName(String name) throws RepoException {
        String query = "SELECT * FROM department WHERE name = '" + name + "'";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                return resultSetToDepartment(rs);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Department getById(Integer id) throws RepoException {
        String query = "SELECT * FROM department WHERE id = " + id;
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                return resultSetToDepartment(rs);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Department save(Department department) throws RepoException {
        if (department != null && department.getId() != null) {
            return update(department);
        } else {
            return create(this.getConnection(), department);
        }
    }

    private Department create(Connection connection, Department department) throws RepoException {
        String query = " INSERT INTO department (name) values (?)";
        try (PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, department.getName());
            preparedStmt.execute();

            try (ResultSet rs = preparedStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
                    return department;
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    private Department update(Department department) throws RepoException {
        String query = "UPDATE department SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, department.getName());
            preparedStmt.setInt(2, department.getId());
            preparedStmt.execute();

            return department;
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }
    }

    private Department resultSetToDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setName(rs.getString("name"));
        department.setId(rs.getInt("id"));
        return department;
    }

    public Department saveIfNotExist(String departmentName) throws RepoException {
        Department department = getByName(departmentName);
        if (department == null) {
            department = new Department();
            department.setName(departmentName);
            department = save(department);
        }

        return department;
    }
}

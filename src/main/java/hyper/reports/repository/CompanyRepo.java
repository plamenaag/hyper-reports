package hyper.reports.repository;

import hyper.reports.entity.Company;
import hyper.reports.repository.error.RepoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepo extends BaseRepo {

    public CompanyRepo(Connection connection) {
        super(connection);
    }

    public List<Company> getAll() throws RepoException {
        List<Company> list = new ArrayList<>();

        String query = "SELECT * FROM company";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                list.add(resultSetToCompany(rs));
            }
        } catch (SQLException e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public List<Company> getManyByName(String name) throws RepoException {
        List<Company> list = new ArrayList<>();

        String query = "SELECT * FROM company WHERE name like %" + name + "%";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                list.add(resultSetToCompany(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public Company getByName(String name) throws RepoException {
        String query = "SELECT * FROM company WHERE name = '" + name + "'";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                return resultSetToCompany(rs);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Company getById(Integer id) throws RepoException {
        String query = "SELECT * FROM company WHERE id = " + id;
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                return resultSetToCompany(rs);
            }

        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Company save(Company company) throws RepoException {
        if (company != null && company.getId() != null) {
            return update(company);
        } else {
            return create(company);
        }
    }

    private Company create(Company company) throws RepoException {
        String query = "INSERT INTO company (name,last_doc_date) values (?,?)";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, company.getName());
            preparedStmt.setTimestamp(2, Timestamp.from(company.getLastDocDate()));
            preparedStmt.execute();
            try (ResultSet rs = preparedStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    company.setId(id);
                    return company;
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    private Company update(Company company) throws RepoException {
        String query = "UPDATE company SET name = ?, last_doc_date = ? WHERE id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, company.getName());
            preparedStmt.setTimestamp(2, Timestamp.from(company.getLastDocDate()));
            preparedStmt.setInt(3, company.getId());
            preparedStmt.execute();

            return company;
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }
    }

    private Company resultSetToCompany(ResultSet rs) throws SQLException {
        Company company = new Company();
        company.setName(rs.getString("name"));
        company.setId(rs.getInt("id"));
        company.setLastDocDate(rs.getTimestamp("last_doc_date").toInstant());

        return company;
    }
}

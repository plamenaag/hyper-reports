package hyper.reports.repository;

import hyper.reports.entity.City;
import hyper.reports.repository.error.RepoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityRepo extends BaseRepo {

    public CityRepo(Connection connection) {
        super(connection);
    }

    public List<City> getAll() throws RepoException {
        List<City> list = new ArrayList<>();
        String query = "SELECT * FROM city";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                list.add(resultSetToCity(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public List<City> getManyByName(String name) throws RepoException {
        List<City> list = new ArrayList<>();
        String query = "SELECT * FROM city WHERE name like %" + name + "%";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                list.add(resultSetToCity(rs));
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public City getByName(String name) throws RepoException {
        String query = "SELECT * FROM city WHERE name = '" + name + "'";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                return resultSetToCity(rs);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public City getById(Integer id) throws RepoException {
        String query = "SELECT * FROM city WHERE id = " + id;
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                return resultSetToCity(rs);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public City save(City city) throws RepoException {
        if (city != null && city.getId() != null) {
            return update(city);
        } else {
            return create(this.getConnection(), city);
        }
    }

    private City create(Connection connection, City city) throws RepoException {
        String query = " INSERT INTO city (name) values (?)";
        try (PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, city.getName());
            preparedStmt.execute();

            try (ResultSet rs = preparedStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    city.setId(id);
                    return city;
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    private City update(City city) throws RepoException {
        String query = "UPDATE city SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, city.getName());
            preparedStmt.setInt(2, city.getId());
            preparedStmt.execute();

            return city;
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }
    }

    private City resultSetToCity(ResultSet rs) throws SQLException {
        City city = new City();
        city.setName(rs.getString("name"));
        city.setId(rs.getInt("id"));
        return city;
    }

    public City saveIfNotExist(String cityName) throws RepoException {
        City city = getByName(cityName);
        if (city == null) {
            city = new City();
            city.setName(cityName);
            city = save(city);
        }

        return city;
    }
}

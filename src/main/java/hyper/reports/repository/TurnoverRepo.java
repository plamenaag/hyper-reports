package hyper.reports.repository;

import hyper.reports.entity.Turnover;
import hyper.reports.repository.error.RepoException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TurnoverRepo extends BaseRepo {

    public TurnoverRepo(Connection connection) {
        super(connection);
    }

    public List<Turnover> getAll() throws RepoException {
        List<Turnover> list = new ArrayList<>();
        String query = "SELECT * FROM turnover";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                Turnover turnover = new Turnover();
                turnover.setAmount(rs.getDouble("amount"));
                turnover.setId(rs.getInt("id"));
                turnover.setEmployeeId(rs.getInt("employee_id"));
                turnover.setDate(rs.getTimestamp("date").toInstant());
                list.add(turnover);
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }

    public Turnover getById(Integer id) throws RepoException {
        String query = "SELECT * FROM turnover WHERE id = " + id;
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query);
             ResultSet rs = preparedStmt.executeQuery()) {
            if (rs.next()) {
                Turnover turnover = new Turnover();
                turnover.setAmount(rs.getDouble("amount"));
                turnover.setId(rs.getInt("id"));
                turnover.setEmployeeId(rs.getInt("employee_id"));
                turnover.setDate(rs.getTimestamp("date").toInstant());
                return turnover;
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    public Turnover save(Turnover turnover) throws RepoException {
        if (turnover != null && turnover.getId() != null) {
            return update(turnover);
        } else {
            return create(turnover);
        }
    }

    private Turnover create(Turnover turnover) throws RepoException {
        String query = "INSERT INTO turnover (amount,date,employee_id) values (?,?,?)";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setDouble(1, turnover.getAmount());
            preparedStmt.setTimestamp(2, Timestamp.from(turnover.getDate()));
            preparedStmt.setInt(3, turnover.getEmployeeId());
            preparedStmt.execute();
            try (ResultSet rs = preparedStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    turnover.setId(id);
                    return turnover;
                }
            }
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }

        return null;
    }

    private Turnover update(Turnover turnover) throws RepoException {
        String query = "UPDATE turnover SET amount = ?, date = ?,employee_id = ? WHERE id = ?";
        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setDouble(1, turnover.getAmount());
            preparedStmt.setTimestamp(2, Timestamp.from(turnover.getDate()));
            preparedStmt.setInt(3, turnover.getEmployeeId());
            preparedStmt.setInt(4, turnover.getId());
            preparedStmt.execute();

            return turnover;
        } catch (Exception e) {
            throw new RepoException(e.getMessage(), e);
        }
    }

    private Turnover resultSetToTurnover(ResultSet rs) throws SQLException {
        Turnover turnover = new Turnover();
        turnover.setAmount(rs.getDouble("amount"));
        turnover.setId(rs.getInt("id"));
        turnover.setDate(rs.getTimestamp("date").toInstant());
        turnover.setEmployeeId(rs.getInt("employee_id"));
        return turnover;
    }

    public Turnover saveIfNotExist(Double amount, Integer employeeId, Instant date) throws RepoException {
        Turnover turnover = new Turnover();
        turnover.setEmployeeId(employeeId);
        turnover.setDate(date);
        turnover.setAmount(amount);

        return save(turnover);
    }
}

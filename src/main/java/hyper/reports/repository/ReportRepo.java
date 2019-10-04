package hyper.reports.repository;

import hyper.reports.Constants;
import hyper.reports.repository.error.RepoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportRepo extends BaseRepo {

    public ReportRepo(Connection connection) {
        super(connection);
    }

    public List<Object[]> getTurnoverReport(String aggregation, String company, String fromDate, String toDate, String order, Integer top) throws RepoException {
        String query = "SELECT " + aggregation + ".name, sum(turnover.amount) AS turnoverSum FROM " + aggregation + " " +
                (!aggregation.equals(Constants.REPORT_TYPE_EMPLOYEE) ? (" LEFT JOIN employee ON " + aggregation + ".id = employee." + aggregation + "_id ") : "") +
                " LEFT JOIN turnover ON employee.id = turnover.employee_id " +
                " LEFT JOIN company ON company.id = employee.company_id " +
                " WHERE company.name = ? AND turnover.date >= ? AND turnover.date <= ? " +
                " GROUP BY " + aggregation + ".id " +
                (order != null ? ("ORDER BY turnoverSum " + order) : "") +
                (top != null ? (" LIMIT " + top) : "");

        return createReportData(company, fromDate, toDate, query, top);
    }

    private List<Object[]> createReportData(String company, String fromDate, String toDate, String query, Integer top) throws RepoException {
        List<Object[]> list = new ArrayList<>();

        try (PreparedStatement preparedStmt = this.getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, company);
            preparedStmt.setString(2, fromDate);
            preparedStmt.setString(3, toDate);

            try (ResultSet rs = preparedStmt.executeQuery()) {
                Double total = 0.0;

                while (rs.next()) {
                    Object[] reportRow = new Object[2];
                    reportRow[0] = rs.getString(1);
                    reportRow[1] = rs.getDouble(2);

                    if (top == null) {
                        total += rs.getDouble(2);
                    }
                    list.add(reportRow);
                }

                Object[] footerRow = new Object[2];
                if (top != null) {
                    footerRow[0] = "Top";
                    footerRow[1] = top;
                } else {
                    footerRow[0] = "Total";
                    footerRow[1] = total;
                }
                list.add(footerRow);
            }
        } catch (SQLException e) {
            throw new RepoException(e.getMessage(), e);
        }

        return list;
    }
}


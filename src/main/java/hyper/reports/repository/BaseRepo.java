package hyper.reports.repository;

import java.sql.Connection;

public abstract class BaseRepo {
    private Connection connection;

    BaseRepo(Connection connection) {
        this.connection = connection;
    }

    Connection getConnection() {
        return connection;
    }
}


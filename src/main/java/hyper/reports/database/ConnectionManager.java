package hyper.reports.database;

import hyper.reports.Constants;
import hyper.reports.util.SettingsProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionManager {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String connectionString = SettingsProvider.getConfig(Constants.CONFIG_DB_CONNECTION_STRING);
            if(connectionString != null){
                connection = DriverManager.getConnection(connectionString,
                        SettingsProvider.getConfig(Constants.CONFIG_DB_USER_STRING), SettingsProvider.getConfig(Constants.CONFIG_DB_PASSWORD_STRING));
            }
        } catch (SQLException e) {
            System.err.println("Error establishing connection to the DB!!!");
        }

        return connection;
    }
}

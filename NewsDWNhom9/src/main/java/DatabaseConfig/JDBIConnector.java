package DatabaseConfig;

import PropertiesConfig.PropertiesConfig;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public class JDBIConnector {
    private static Jdbi jdbi;

    private static void makeConnect() {
        DatabaseConfig dc =new DatabaseConfig(new PropertiesConfig("db.properties"));
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + dc.getDbHost() + ":" + dc.getDbPort() + "/" + dc.getDbName());
        dataSource.setUser(dc.getDbUsername());
        dataSource.setPassword(dc.getDbPassword());
        try {
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        jdbi = Jdbi.create(dataSource);

    }

    public JDBIConnector() {
    }

    public static Jdbi get() {
        if (jdbi == null) makeConnect();
        return jdbi;
    }

}

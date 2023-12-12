package DatabaseConfig;

import PropertiesConfig.PropertiesConfig;
import com.mysql.cj.jdbc.MysqlDataSource;

import org.jdbi.v3.core.Jdbi;

public class JDBIConnector {
private  Jdbi jdbi;

    private  void makeConnect()   {
        DatabaseControl dc =new DatabaseControl(new PropertiesConfig("db.properties"));
        MysqlDataSource dataSource = new MysqlDataSource();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dataSource.setURL("jdbc:mysql://" + dc.getDbHost() + ":" + dc.getDbPort() + "/" + dc.getDbName());
            dataSource.setUser(dc.getDbUsername());
            dataSource.setPassword(dc.getDbPassword());
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.jdbi = Jdbi.create(dataSource);

    }

    private  void makeConnect(String host ,int port, String name, String username, String password)   {
        MysqlDataSource dataSource = new MysqlDataSource();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dataSource.setURL("jdbc:mysql://" + host + ":" + port+ "/" + name);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.jdbi = Jdbi.create(dataSource);


    }

    public JDBIConnector() {

    }

    public  Jdbi get() {
        if (this.jdbi ==null)        makeConnect();
        return this.jdbi;

    }
    public  Jdbi get(String host ,int port, String name, String username, String password) {
        if (this.jdbi ==null)         makeConnect( host , port,  name,  username,  password);
        return this.jdbi;

    }

}

package DAO;

import DatabaseConfig.JDBIConnector;
import Models.Config;

import java.util.List;
import java.util.stream.Collectors;

public class ControlDAO {
    public static Config getCurrentConfig(){

        List<Config> configs = JDBIConnector.get().withHandle(handle -> {
            // Thực hiện câu truy vấn SELECT
            String sql = "SELECT * FROM configs";
            return handle.createQuery(sql)
                    .mapToBean(Config.class)
                    .stream().collect(Collectors.toList());
        });
        Config cur_config = null;

        for (Config c : configs) {
            if (c.getFlag().equals("TRUE")){
                cur_config=c;
            }
        }
        return cur_config;
    }
}

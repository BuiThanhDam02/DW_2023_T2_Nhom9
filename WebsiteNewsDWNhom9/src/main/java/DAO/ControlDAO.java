package DAO;

import DatabaseConfig.JDBIConnector;
import Model.Config;
import Model.ConfigFile;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.stream.Collectors;

public class ControlDAO {
    private Config current_config;
    private Jdbi jdbi;
    private List<ConfigFile> configFileList;
    public ControlDAO(){
        this.jdbi = new JDBIConnector().get();
        List<Config> configs = this.jdbi.withHandle(handle -> {
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
        this.current_config = cur_config;
        List<ConfigFile> listFiles = this.jdbi.withHandle(h ->{
            return h.createQuery("SELECT * FROM `config_files` WHERE df_config_id = ? AND isdelete=0 and status='TRUE'")
                    .bind(0,getCurrentConfig().getId())
                    .mapToBean(ConfigFile.class).stream().collect(Collectors.toList());
        });
        this.configFileList = listFiles;

    }
    public  Config getCurrentConfig(){
        return this.current_config;
    }
    public  Jdbi getControlJDBI(){
        return this.jdbi;
    }
    public List<ConfigFile> getConfigFileList(){
        return this.configFileList;
    }
    public ConfigFile getConfigFile(String name){
        for (ConfigFile cf:
             getConfigFileList()) {
            if (cf.getName().equals(name)) return cf;
        }
        return null;
    }



}

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
    public void setConfigStatus(String status){
        try {
            getControlJDBI().withHandle(handle -> {
                handle.createUpdate("UPDATE configs SET status = ? WHERE flag = 'TRUE' and id = ? ")
                        .bind(0, status)
                        .bind(1,getCurrentConfig().getId())
                        .execute();
                return null;
            });
            this.current_config.setStatus(status);
        }catch (Exception e){
            System.out.println("Error in update status");
            e.printStackTrace();

        }
    }

    public boolean checkConfigStatus(String status){
        try {
            boolean isStatusExists = getControlJDBI().withHandle(handle -> {
                String sql = "SELECT COUNT(*) FROM configs WHERE status = ? and id = ?";
                int count = handle.createQuery(sql)
                        .bind(0, status)
                        .bind(1,getCurrentConfig().getId())
                        .mapTo(Integer.class)
                        .one();
                return count > 0;
            });
            if (isStatusExists) {
                getCurrentConfig().setStatus(status);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            // Xử lý ngoại lệ (exception) tại đây
            return false;
        }
    }
    public void createLog(String name, String message, String level, String source, String note, String content){
        getControlJDBI().withHandle(handle -> {
            handle.createUpdate("INSERT INTO logs (name, message, level, source, note, content)\n" +
                    "VALUES (?,?,?,?,?,?);").bind(0,name)
                    .bind(1,message)
                    .bind(2,level).bind(3,source)
                    .bind(4,note).bind(5,content).execute();
           return null;
        });
    }

    public static void main(String[] args) {
        ControlDAO c = new ControlDAO();
        System.out.println(c.getCurrentConfig());
    }
}

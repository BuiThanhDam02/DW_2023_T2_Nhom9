package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Models.Config;
import Models.Status;
import PropertiesConfig.PropertiesConfig;
import QueryConfig.ReadQuery;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.net.URL;

public class Mart {
    ControlDAO controlDAO;
    public Mart(ControlDAO controlDAO){
        this.controlDAO = controlDAO;
    }
    public void excute(){
        try {
            if (this.controlDAO.checkConfigStatus(Status.AGGREGATED.name())) {
                System.out.println("Loading to Mart in Proccessing...");
                this.controlDAO.setConfigStatus(Status.MARTLOADING.name());
                loadtomart();
                this.controlDAO.setConfigStatus(Status.MARTLOADED.name());
                this.controlDAO.createLog("Mart","Load to Mart Successfully","INFO",getFilePath(),"","Done Loading data from aggregate to table  news in Mart DB!!");

            }else  if (this.controlDAO.checkConfigStatus(Status.MARTLOADING.name())) {
                System.out.println("Loading to Mart in Proccessing...");
                loadtomart();
                this.controlDAO.setConfigStatus(Status.MARTLOADED.name());
                this.controlDAO.createLog("Mart","Load to Mart Successfully","INFO",getFilePath(),"","Done Loading data from aggregate to table  news in Mart DB!!");

            }
        }catch (Exception e){
            this.controlDAO.createLog("Load to Mart Exception","Load to Mart Exception Error","ERROR",getFilePath(),"",e.toString());

            throw new RuntimeException(e);
        }
    }
    public void loadtomart(){
        ControlDAO controlDAO = new ControlDAO();
        Config c = controlDAO.getCurrentConfig();
        Jdbi mart_jdbi = new JDBIConnector().get(c.getMartSourceHost(),c.getMartSourcePort(),c.getMartDbName(),c.getMartSourceUsername(),c.getMartSourcePassword());
        Jdbi wh_jdbi = new JDBIConnector().get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());


        String WH_SQL_path = new PropertiesConfig("path.properties").getResource().get("warehouse_query_path");
        ReadQuery wh_rq = new ReadQuery(WH_SQL_path);
        String select_query_wh =  wh_rq.getSelectStatements().get(0);

        String MART_SQL_path = new PropertiesConfig("path.properties").getResource().get("mart_query_path");
        ReadQuery mart_rq = new ReadQuery(MART_SQL_path);
        String insert_query_mart =  mart_rq.getInsertStatements().get(0);

        wh_jdbi.withHandle(handle -> {
            // Execute the query and get a result set
            handle.createQuery(select_query_wh).mapToMap().forEach(row -> {
                String title = row.get("title").toString();
                String image_path = row.get("image_path").toString();
                String description = row.get("description").toString();
                String content = row.get("content").toString();
                String category_name = row.get("category_name").toString();
                String full_date = row.get("full_date").toString();
                int day = Integer.parseInt(row.get("day").toString());
                int month = Integer.parseInt(row.get("month").toString());
                int year = Integer.parseInt(row.get("year").toString());
                mart_jdbi.withHandle(h ->
                        h.createUpdate(insert_query_mart)
                                .bind("title", title)
                                .bind("image_path", image_path)
                                .bind("description", description)
                                .bind("content", content)
                                .bind("category_name", category_name)
                                .bind("full_date", full_date)
                                .bind("day", day)
                                .bind("month", month)
                                .bind("year", year)
                                .execute()
                );

            });
            return null;
        });

    }
    public String getFilePath(){
        Class<?> clazz = Crawler.class;
        // và lấy CodeSource từ ProtectionDomain
        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        // Chuyển đổi URL thành đường dẫn file
        String filePath = location.getPath();
        String decodedPath = new File(filePath).getAbsolutePath();
        return decodedPath;
    }
    public static void main(String[] args) {
        String MART_SQL_path = new PropertiesConfig("path.properties").getResource().get("mart_query_path");
        ReadQuery mart_rq = new ReadQuery(MART_SQL_path);
        String insert_query_mart =  mart_rq.getInsertStatements().get(0);
        System.out.println(insert_query_mart);
    }

}

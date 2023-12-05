package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Models.Config;
import Models.ConfigFile;
import Models.Status;
import PropertiesConfig.PropertiesConfig;
import QueryConfig.ReadQuery;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class Transform {
    ControlDAO controlDAO;
    public Transform(ControlDAO controlDAO){
        this.controlDAO = controlDAO;
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
    public void excute(){
        try {
            if (this.controlDAO.checkConfigStatus(Status.EXTRACTED.name())) {
                System.out.println("Transforming in Proccessing...");
                this.controlDAO.setConfigStatus(Status.LOADING.name());
                transform();
                this.controlDAO.setConfigStatus(Status.LOADED.name());
                this.controlDAO.createLog("Loading to Warehouse ","Loading Successfully","INFO",getFilePath(),"","Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!");

            }else if (this.controlDAO.checkConfigStatus(Status.LOADING.name())) {
                System.out.println("Loading in Proccessing...");
                transform();
                this.controlDAO.setConfigStatus(Status.LOADED.name());
                this.controlDAO.createLog("Loading to Warehouse ","Loading Successfully","INFO",getFilePath(),"","Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!");

            }else if (this.controlDAO.checkConfigStatus(Status.TRANSFORMING.name())) {
                System.out.println("Loading in Proccessing...");
                transform();
                this.controlDAO.setConfigStatus(Status.LOADED.name());
                this.controlDAO.createLog("Loading to Warehouse ","Loading Successfully","INFO",getFilePath(),"","Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!");

            }

        }catch (Exception e){
            this.controlDAO.createLog("Transform Loading Exception","Transform Loading Exception Error","ERROR",getFilePath(),"",e.toString());

            throw new RuntimeException(e);
        }
    }
    public void transform(){

        ControlDAO controlDAO = new ControlDAO();
        Config c = controlDAO.getCurrentConfig();
        Jdbi s_jdbi = new JDBIConnector().get(c.getStagingSourceHost(),c.getStagingSourcePort(),c.getStagingDbName(),c.getStagingSourceUsername(),c.getStagingSourcePassword());
        Jdbi wh_jdbi = new JDBIConnector().get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());

        ConfigFile cfStaging = controlDAO.getConfigFile("staging_query");
        String staging_SQL_path = cfStaging.getPath()+cfStaging.getName()+cfStaging.getDelimiter()+cfStaging.getExtension();
        ReadQuery srq = new ReadQuery(staging_SQL_path);
        String select_query_staging =  srq.getSelectStatements().get(0);

        ConfigFile cfTransform = controlDAO.getConfigFile("transform_query");
        String transform_SQL_path = cfTransform.getPath()+cfTransform.getName()+cfTransform.getDelimiter()+cfTransform.getExtension();
        ReadQuery transform_rq = new ReadQuery(transform_SQL_path);


        ConfigFile cfWH = controlDAO.getConfigFile("warehouse_query");
        String WH_SQL_path = cfWH.getPath()+cfWH.getName()+cfWH.getDelimiter()+cfWH.getExtension();
        ReadQuery wh_rq = new ReadQuery(WH_SQL_path);
        String insert_query_wh =  wh_rq.getInsertStatements().get(0);
        this.controlDAO.setConfigStatus(Status.TRANSFORMING.name());
        try{
            s_jdbi.withHandle(handle -> {
                // Execute the query and get a result set
                handle.createQuery(select_query_staging).mapToMap().forEach(row -> {
                    // Iterate over the columns of each row
                    String title = row.get("title").toString();
                    if (!(title.equals("") || title == null)){
                        String content = row.get("content").toString();
                        if (!(content.equals("") || content == null)){
                            String date = row.get("date").toString();
                            if (!(date.equals("") || date == null)){
                                String category = row.get("category").toString();

                                if (category.equals("") || category == null) category = "Tổng hợp";

                                String image_url = row.get("image_url").toString();
                                String description = row.get("description").toString();
                                int isDelete =  (int)row.get("isdelete");
                                String finalCategory = category;

                                // Category_dim
                                Map<String ,String> cat = transform_rq.getStatementsByComment("category");
                                String category_insert_query = cat.get("insert");
                                String category_select_query = cat.get("select");
                                wh_jdbi.withHandle(h ->
                                        h.createUpdate(category_insert_query)
                                                .bind(0, finalCategory).bind(1, finalCategory).execute()
                                );
                                int categoryId = wh_jdbi.withHandle(h ->
                                        h.createQuery(category_select_query)
                                                .bind(0, finalCategory)
                                                .mapTo(Integer.class)
                                                .findOne()
                                                .orElse(null)
                                );
                                // image_url
                                Map<String ,String> img = transform_rq.getStatementsByComment("image_url");
                                String image_insert_query = img.get("insert");
                                String image_select_query = img.get("select");
                                wh_jdbi.withHandle(h ->
                                        h.createUpdate(image_insert_query)
                                                .bind(0, image_url).bind(1, image_url).execute()
                                );
                                int imageId = wh_jdbi.withHandle(h ->
                                        h.createQuery(image_select_query)
                                                .bind(0, image_url)
                                                .mapTo(Integer.class)
                                                .findOne()
                                                .orElse(null)
                                );
                                // date
                                Map<String ,String> dateq = transform_rq.getStatementsByComment("date");
                                String date_insert_query = dateq.get("insert");
                                String date_select_query = dateq.get("select");

                                String[] dateParts = date.split("-");

                                // Chuyển đổi các phần tử từ chuỗi sang số nguyên
                                int year = Integer.parseInt(dateParts[0]);
                                int month = Integer.parseInt(dateParts[1]);
                                int day = Integer.parseInt(dateParts[2]);

                                // GMT - Giả sử GMT là 0 cho mục đích ví dụ, bạn có thể điều chỉnh theo nhu cầu thực tế
                                int gmt = 7;

                                wh_jdbi.withHandle(h ->
                                        h.createUpdate(date_insert_query)
                                                .bind(0, date)
                                                .bind(1, day)
                                                .bind(2, month)
                                                .bind(3, year)
                                                .bind(4, gmt)
                                                .bind(5, date)
                                                .execute()
                                );
                                int dateId = wh_jdbi.withHandle(h ->
                                        h.createQuery(date_select_query)
                                                .bind(0, date)
                                                .mapTo(Integer.class)
                                                .findOne()
                                                .orElse(null)
                                );
                                // date_expired
                                Map<String ,String> datee = transform_rq.getStatementsByComment("date_expired");
                                String date_expired_insert_query = datee.get("insert");
                                String date_expired_select_query = datee.get("select");

                                // Chuyển đổi các phần tử từ chuỗi sang số nguyên
                                int ex_year = year + 100;
                                String ex_date = ex_year+"-"+month+"-"+day;
                                wh_jdbi.withHandle(h ->
                                        h.createUpdate(date_expired_insert_query)
                                                .bind(0, ex_date)
                                                .bind(1, day)
                                                .bind(2, month)
                                                .bind(3, ex_year)
                                                .bind(4, gmt)
                                                .bind(5, ex_date)
                                                .execute()
                                );
                                int dateEId = wh_jdbi.withHandle(h ->
                                        h.createQuery(date_expired_select_query)
                                                .bind(0, ex_date)
                                                .mapTo(Integer.class)
                                                .findOne()
                                                .orElse(null)
                                );

                                wh_jdbi.withHandle(h ->
                                        h.createUpdate(insert_query_wh)
                                                .bind(0,title)
                                                .bind(1,imageId)
                                                .bind(2, description)
                                                .bind(3,content)
                                                .bind(4,categoryId)
                                                .bind(5,dateId)
                                                .bind(6,isDelete)
                                                .bind(7,dateEId).execute()
                                );

                            }
                        }
                    }

                });
                return null;
            });
            this.controlDAO.createLog("Transform ","Transform Successfully","INFO",getFilePath(),"","Done Transform data from news_staging to table  news_warehouse in Warehouse DB!!");
            this.controlDAO.setConfigStatus(Status.TRANSFORMED.name());
        }catch (Exception e){
            this.controlDAO.createLog("Transform Exception","Transform Exception Error","ERROR",getFilePath(),"",e.toString());
        }



        System.out.println("Done Transforming!!");
        System.out.println("Loading to Warehouse in proccessing...");


    }
}

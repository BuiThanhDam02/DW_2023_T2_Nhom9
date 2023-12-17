package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Mail.JavaMail;
import Models.Config;
import Models.ConfigFile;
import Models.Status;

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

        String classesFolderPath = new File("").getAbsolutePath();
        boolean isInTarget = classesFolderPath.contains("target");
        if (isInTarget){
            return classesFolderPath+"\\classes";
        }
        return classesFolderPath+"\\target\\classes";
    }
    public void excute(){
        try {
            // Bước Transform 3.1 : kiểm tra status =EXTRACTED
            if (this.controlDAO.checkConfigStatus(Status.EXTRACTED.name())) {
                System.out.println("Transforming in Proccessing...");
                transform();
                // Bước Transform 3.26 -27 : Tạo thông tin LOG , INFO , Ghi nội dung vào bảng LOG với câu Insert
                this.controlDAO.createLog("Loading to Warehouse ","Loading Successfully","INFO",getFilePath(),"","Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!");

            }else if (this.controlDAO.checkConfigStatus(Status.TRANSFORMING.name())) {
                System.out.println("Loading in Proccessing...");
                transform();
                this.controlDAO.createLog("Loading to Warehouse ","Loading Successfully","INFO",getFilePath(),"","Done Loading data from news_staging to table  news_warehouse in Warehouse DB!!");

            }

        }catch (Exception e){
            this.controlDAO.createLog("Transform Loading Exception","Transform Loading Exception Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Transform DW","Transform Loading Exception Error: "+getFilePath());

            throw new RuntimeException(e);
        }
    }
    public void transform(){
        // Bước Transform 3.2 : Cập nhật status = TRANSFORMING
        this.controlDAO.setConfigStatus(Status.TRANSFORMING.name());
        Config c = controlDAO.getCurrentConfig();
        // Bước Transform 3.3 - 3.4 : Đọc dữ liệu config Lấy thông tin kết nối của dtb staging
        Jdbi s_jdbi = new JDBIConnector().get(c.getStagingSourceHost(),c.getStagingSourcePort(),c.getStagingDbName(),c.getStagingSourceUsername(),c.getStagingSourcePassword());

        // Bước Transform 3.5 - 3.6 : Lấy thông tin kết nối của dtb warehouse và Kết nối dtb warehouse
        Jdbi wh_jdbi = new JDBIConnector().get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());

        // Bước Transform 3.7 : Ghép bảng config_file bằng id của config hiện tại
        ConfigFile cfStaging = controlDAO.getConfigFile("staging_query");
        // Bước Transform 3.8 : Đọc thông tin đường dẫn file query của  staging
        String staging_SQL_path = getFilePath()+ cfStaging.getPath()+cfStaging.getName()+cfStaging.getDelimiter()+cfStaging.getExtension();
        // Bước Transform 3.9 : Đọc thông tin file staging_query.sql
        ReadQuery srq = new ReadQuery(staging_SQL_path);
        // Bước Transform 3.10 : Lấy câu  select s
        String select_query_staging =  srq.getSelectStatements().get(0);

        ConfigFile cfTransform = controlDAO.getConfigFile("transform_query");
        String transform_SQL_path = getFilePath()+ cfTransform.getPath()+cfTransform.getName()+cfTransform.getDelimiter()+cfTransform.getExtension();
        ReadQuery transform_rq = new ReadQuery(transform_SQL_path);

        // Bước Transform 3.11 : Đọc thông tin đường dẫn file query của  warehouse
        ConfigFile cfWH = controlDAO.getConfigFile("warehouse_query");
        // Bước Transform 3.12 : Đọc thông tin file warehouse_query.sql
        String WH_SQL_path = getFilePath()+ cfWH.getPath()+cfWH.getName()+cfWH.getDelimiter()+cfWH.getExtension();
        ReadQuery wh_rq = new ReadQuery(WH_SQL_path);
        // Bước Transform 3.13 : Lấy câu insert wh
        String insert_query_wh =  wh_rq.getInsertStatements().get(0);

        try{
            s_jdbi.withHandle(handle -> {
                // Bước Transform 3.14-3.15 : Lặp qua dữ liệu  news_staging bằng câu select và Lấy 1 dòng dữ liệu
                handle.createQuery(select_query_staging).mapToMap().forEach(row -> {
                    // Bước Transform 3.16-3.17 : Tiến hành tách dữ liệu ra thành từng trường , xử lý dữ liệu bảng news_staging
                    //(missing,outlier,null,..)
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
                                // Bước Transform 3.18 :Đọc thông tin file transform_query.sql
                                Map<String ,String> cat = transform_rq.getStatementsByComment("category");
                                // Bước Transform 3.19 :Lấy câu  select id và insert vào dim
                                String category_insert_query = cat.get("insert");
                                String category_select_query = cat.get("select");

                                // Bước Transform 3.20 :Thêm từng trường dữ liệu vào đúng bảng Dim tương ứng
                                wh_jdbi.withHandle(h ->
                                        h.createUpdate(category_insert_query)
                                                .bind(0, finalCategory).bind(1, finalCategory).execute()
                                );
                                // Bước Transform 3.21 : Biến đổi lại dòng dữ liệu theo id từng trường từ bảng Dim
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
                                // Bước Transform 3.22 : Thêm dòng dữ liệu vào bảng news_warehouse bằng câu insert wh
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
                // Bước Transform 3.23 : Lưu lại dữ liệu vào các bảng dim và bảng fact
                return null;
            });
            // Bước Transform 3.24 :Cập nhật status TRANSFORMED
            this.controlDAO.setConfigStatus(Status.TRANSFORMED.name());
            // Bước Transform 3.25 :Ghi log INFO thành công Transform
            this.controlDAO.createLog("Transform ","Transform Successfully","INFO",getFilePath(),"","Done Transform data from news_staging to table  news_warehouse in Warehouse DB!!");

        }catch (Exception e){
            this.controlDAO.createLog("Transform Exception","Transform Exception Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Transform DW","Transform Exception Error: "+getFilePath());

        }



        System.out.println("Done Transforming!!");
        System.out.println("Loading to Warehouse in proccessing...");


    }
}

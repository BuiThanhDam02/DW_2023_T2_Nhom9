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

public class Load {
    ControlDAO controlDAO;
    public Load(ControlDAO controlDAO){
        this.controlDAO = controlDAO;
    }
    public void excute(){
        try {
            // Bước Load 5.1 : kiểm tra status =AGGREGATED
            if (this.controlDAO.checkConfigStatus(Status.AGGREGATED.name())) {
                System.out.println("Loading to Mart in Proccessing...");
                // Bước Load 5.2 : Cập nhật status = LOADING
                this.controlDAO.setConfigStatus(Status.LOADING.name());
                loadtomart();
                // Bước Load 5.23: Cập nhật status LOADED
                this.controlDAO.setConfigStatus(Status.LOADED.name());
                // Bước Load 5.24 -5.25 :Tạo thông tin LOG , INFO và Ghi nội dung vào bảng LOG với câu Insert
                this.controlDAO.createLog("Mart","Load to Mart Successfully","INFO",getFilePath(),"","Done Loading data from aggregate to table  news in Mart DB!!");
                // Bước Load 5.26: Gửi Thông báo về email
                JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),"Load to Mart Successfully","Thông báo hoàn thành Flow Crawler, Extract, Transform, Aggregate, Load DW","Data Warehouse load successfully: "+getFilePath());

            }else  if (this.controlDAO.checkConfigStatus(Status.LOADING.name())) {
                System.out.println("Loading to Mart in Proccessing...");
                loadtomart();
                this.controlDAO.setConfigStatus(Status.LOADED.name());
                this.controlDAO.createLog("Mart","Load to Mart Successfully","INFO",getFilePath(),"","Done Loading data from aggregate to table  news in Mart DB!!");
                JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),"Load to Mart Successfully","Thông báo hoàn thành Flow Crawler, Extract, Transform, Aggregate, Load DW","Data Warehouse load successfully: "+getFilePath());
            }
        }catch (Exception e){
            this.controlDAO.createLog("Load to Mart Exception","Load to Mart Exception Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Load DW","Load to Mart Exception Error: "+getFilePath());

            throw new RuntimeException(e);
        }
    }
    public void loadtomart(){

        // Bước Load 5.3 : Đọc dữ liệu config Lấy thông tin kết nối của dtb warehouse
        Config c = controlDAO.getCurrentConfig();
        // Bước Load 5.4 : Kết nối dtb warehouse
        Jdbi wh_jdbi = new JDBIConnector().get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());
        // Bước Load 5.5 : Ghép bảng config_file bằng id của config hiện tại
        ConfigFile cfWH = controlDAO.getConfigFile("warehouse_query");
        // Bước Load 5.6 :Đọc thông tin đường dẫn file query của  warehouse
        String WH_SQL_path = getFilePath()+ cfWH.getPath()+cfWH.getName()+cfWH.getDelimiter()+cfWH.getExtension();
        // Bước Load 5.7 :Đọc file query của warehouse_query.sql
        ReadQuery wh_rq = new ReadQuery(WH_SQL_path);
        // Bước Load 5.8 :Lấy câu select
        String select_query_wh =  wh_rq.getSelectStatements().get(0);
        // Bước Load 5.9 - 5.10 :Lấy thông tin kết nối của dtb mart và Kết nối dtb mart
        Jdbi mart_jdbi = new JDBIConnector().get(c.getMartSourceHost(),c.getMartSourcePort(),c.getMartDbName(),c.getMartSourceUsername(),c.getMartSourcePassword());
        // Bước Load 5.11 :Ghép bảng config_file bằng id của config hiện tại
        ConfigFile cfMART = controlDAO.getConfigFile("mart_query");
        // Bước Load 5.12 :Đọc thông tin đường dẫn file query của  mart
        String MART_SQL_path = getFilePath()+ cfMART.getPath()+cfMART.getName()+cfMART.getDelimiter()+cfMART.getExtension();
        // Bước Load 5.13 :đọc file mart_query.sql
        ReadQuery mart_rq = new ReadQuery(MART_SQL_path);
        // Bước Load 5.14 :Lấy câu insert into news
        String insert_query_mart =  mart_rq.getInsertStatements().get(0);

        wh_jdbi.withHandle(handle -> {
            // Bước Load 5.15 - 5.16 - 5.17 :Đọc dữ liệu của bảng aggregate và Lặp qua từng dòng dữ liệu , Lấy 1 dòng
            handle.createQuery(select_query_wh).mapToMap().forEach(row -> {
                // Bước Load 5.18:Lấy dữ liệu các trường cần thiết bảng aggregate (select)
                String title = row.get("title").toString();
                String image_path = row.get("image_path").toString();
                String description = row.get("description").toString();
                String content = row.get("content").toString();
                String category_name = row.get("category_name").toString();
                String full_date = row.get("full_date").toString();
                int day = Integer.parseInt(row.get("day").toString());
                int month = Integer.parseInt(row.get("month").toString());
                int year = Integer.parseInt(row.get("year").toString());
                // Bước Load 5.19 :mapping từng trường dữ liệu vào câu insert into news (mart)
                // Bước Load 5.20 - 5.21 :Excute câu query insert và Lưu dòng mới vào bảng news (mart)
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
            // Bước Load 5.22 : Lưu lại dữ liệu vào các bảng mart
            return null;
        });

    }
    public String getFilePath(){
//        Class<?> clazz = Extract.class;
//        // và lấy CodeSource từ ProtectionDomain
//        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
//        String filePath = location.getPath();
//        String decodedPath = new File(filePath).getAbsolutePath();
//        String classesFolderPath = decodedPath.replace("%20", " ");
        String classesFolderPath = new File("").getAbsolutePath();
        boolean isInTarget = classesFolderPath.contains("target");
        if (isInTarget){
            return classesFolderPath+"\\classes";
        }
        return classesFolderPath+"\\target\\classes";
    }
    public static void main(String[] args) {
//        String MART_SQL_path = new PropertiesConfig("path.properties").getResource().get("mart_query_path");
//        ReadQuery mart_rq = new ReadQuery(MART_SQL_path);
//        String insert_query_mart =  mart_rq.getInsertStatements().get(0);
//        System.out.println(insert_query_mart);
    }

}

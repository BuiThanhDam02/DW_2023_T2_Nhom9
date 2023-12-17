package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Mail.JavaMail;
import Models.Config;
import Models.Status;
import PropertiesConfig.PropertiesConfig;
import QueryConfig.ReadQuery;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Aggregate {
    ControlDAO controlDAO;
    public Aggregate(ControlDAO controlDAO){
        this.controlDAO = controlDAO;
    }
    public void aggregate() {
        // Bước Aggregate 4.3 : Đọc dữ liệu bảng config
        Config c = controlDAO.getCurrentConfig();

        // Bước Aggregate 4.4 -4.5  : Lấy thông tin kết nối của dtb warehouse, Kết nối dtb warehouse
            Jdbi a_jdbi = new JDBIConnector().get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());
            try {
                // Bước Aggregate 4.6  : Đọc procedure của aggregate
                // Bước Aggregate 4.7 : Call aggregate_procedure
                a_jdbi.withHandle(handle -> {
                    handle.createCall("call aggregate_procedure()")
                            .invoke();
                    return null;
                });
                // Bước Aggregate 4.8 : Lưu lại dữ liệu vào các bảng aggregate
                System.out.println("Aggregated Successfully!!");
            } catch (Exception e) {
                this.controlDAO.createLog("Aggregate Exception","Aggregate Exception Error","ERROR",getFilePath(),"",e.toString());
                JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Aggregate DW","Aggregate Exception Error: "+getFilePath());


            }


    }
    public void  excute(){
        try {
            // Bước Aggregate 4.1 : Status = TRANSFORMED
            if (this.controlDAO.checkConfigStatus(Status.TRANSFORMED.name())) {
                System.out.println("Aggregate in proccessing..");
                // Bước Aggregate 4.2 : Cập nhật status = AGGREGATING
                this.controlDAO.setConfigStatus(Status.AGGREGATING.name());
                aggregate();
                // Bước Aggregate 4.9 : Cập nhật status AGGREGATED
                this.controlDAO.setConfigStatus(Status.AGGREGATED.name());
                // Bước Aggregate 4.10-11 :Tạo thông tin LOG , INFO, Ghi nội dung vào bảng LOG với câu Insert
                this.controlDAO.createLog("Aggregate","Aggregate Successfully","INFO",getFilePath(),"","Done Aggregating data from news_warehouse to table  aggregate in Warehouse DB!!");

            }else   if (this.controlDAO.checkConfigStatus(Status.AGGREGATING.name())) {
                System.out.println("Aggregate in proccessing..");
                aggregate();
                this.controlDAO.setConfigStatus(Status.AGGREGATED.name());
                this.controlDAO.createLog("Aggregate","Aggregate Successfully","INFO",getFilePath(),"","Done Aggregating data from news_warehouse to table  aggregate in Warehouse DB!!");

            }
        }catch (Exception e){
            this.controlDAO.createLog("Aggregate Exception","Aggregate Exception Error","ERROR",getFilePath(),"",e.toString());
            JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Aggregate DW","Aggregate Exception Error: "+getFilePath());


        }
    }
    public String getFilePath(){

        String classesFolderPath = new File("").getAbsolutePath();
        boolean isInTarget = classesFolderPath.contains("target");
        if (isInTarget){
            return classesFolderPath+"\\classes";
        }
        return classesFolderPath+"\\target\\classes";
    }

}

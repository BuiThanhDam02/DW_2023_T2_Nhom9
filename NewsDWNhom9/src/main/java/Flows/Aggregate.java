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
        Config c = controlDAO.getCurrentConfig();
//        System.out.println(c);

            Jdbi a_jdbi = new JDBIConnector().get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());
            try {
                a_jdbi.withHandle(handle -> {
                    handle.createCall("call aggregate_procedure()")
                            .invoke();
                    return null;
                });
                System.out.println("Aggregated Successfully!!");
            } catch (Exception e) {
                this.controlDAO.createLog("Aggregate Exception","Aggregate Exception Error","ERROR",getFilePath(),"",e.toString());
                JavaMail.getInstance().sendMail(this.controlDAO.getCurrentConfig().getErrorToMail(),e.toString(),"Thông báo lỗi Aggregate DW","Aggregate Exception Error: "+getFilePath());


            }


    }
    public void  excute(){
        try {
            if (this.controlDAO.checkConfigStatus(Status.TRANSFORMED.name())) {
                System.out.println("Aggregate in proccessing..");
                this.controlDAO.setConfigStatus(Status.AGGREGATING.name());
                aggregate();
                this.controlDAO.setConfigStatus(Status.AGGREGATED.name());
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

}

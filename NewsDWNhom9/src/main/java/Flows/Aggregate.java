package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
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
            String aggregate_path = new PropertiesConfig("path.properties").getResource().get("aggregate_procedure_path");
            try {
                a_jdbi.withHandle(handle -> {
                    handle.createCall("call aggregate_procedure()")
                            .invoke();
                    return null;
                });
                System.out.println("Aggregated Successfully!!");
            } catch (Exception e) {
                this.controlDAO.createLog("Aggregate Exception","Aggregate Exception Error","ERROR",getFilePath(),"",e.toString());
                e.printStackTrace();
            }


    }
    public void  excute(){
        try {
            if (this.controlDAO.checkConfigStatus(Status.LOADED.name())) {
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
            e.printStackTrace();
        }
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

}

package Flows;

import DAO.ControlDAO;
import DatabaseConfig.JDBIConnector;
import Models.Config;
import PropertiesConfig.PropertiesConfig;
import QueryConfig.ReadQuery;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Aggregate {

    public void aggregate() {
        Config c = ControlDAO.getCurrentConfig();
        System.out.println(c);
        if (c.getFlag().equals("TRUE") && c.getStatus().equals("LOADED") ) {
            Jdbi a_jdbi = JDBIConnector.get(c.getWhSourceHost(),c.getWhSourcePort(),c.getWhDbName(),c.getWhSourceUsername(),c.getWhSourcePassword());
            String aggregate_path = new PropertiesConfig("path.properties").getResource().get("aggregate_procedure_path");
            try {
                a_jdbi.withHandle(handle -> {
                    handle.createCall("call aggregate_procedure()")
                            .invoke();
                    return null;
                });
            } catch (Exception e) {
                try {
                    String sql = new String(Files.readAllBytes(Paths.get(aggregate_path)));
                    a_jdbi.withHandle(handle -> {
                        handle.createScript(sql)
                                .execute();
                        return null;
                    });
                    aggregate();
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }

    }

    public static void main(String[] args) {
        new Aggregate().aggregate();
    }
}

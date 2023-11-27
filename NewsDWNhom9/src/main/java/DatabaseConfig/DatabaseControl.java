package DatabaseConfig;

import PropertiesConfig.PropertiesConfig;

public class DatabaseControl {
     PropertiesConfig DBPC ;

    public DatabaseControl(PropertiesConfig DBPC) {
        this.DBPC = DBPC;
    }
    public  String getDbHost(){return this.DBPC.getResource().get("control.host").toString();};
    public  String getDbPort(){return this.DBPC.getResource().get("control.port").toString();};
    public  String getDbUsername(){return this.DBPC.getResource().get("control.username").toString();};
    public  String getDbPassword(){return this.DBPC.getResource().get("control.password").toString();};
    public  String getDbOption(){return this.DBPC.getResource().get("control.option").toString();};
    public  String getDbName(){return this.DBPC.getResource().get("control.databaseName").toString();};

    public static void main(String[] args) {
        System.out.println(new DatabaseControl(new PropertiesConfig("db.properties")).getDbName());
    }
}

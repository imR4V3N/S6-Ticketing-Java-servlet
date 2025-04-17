package mg.ticketing.models.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class UtilDb {
    String url = Config.url;
    String user = Config.user;
    String pwd = Config.pwd;
    String driver = "org.postgresql.Driver";


    public Connection getPgConnection() throws Exception{
        Connection c = null;
        try {
            Class.forName(driver);
            c = DriverManager.getConnection(url,user,pwd);
        }
        catch(Exception e){
            throw new Exception("Connection error : " + e.getMessage());
        }
        return c;
    }

}

package connect;
import java.sql.*;
public class ConnectDataBase {
    private static Connection conn = null;
    private static String url = "jdbc:mysql://localhost:3306/hotelmanagementsystem?serverTimezone=UTC";
    private static String user = "root";
    private static String password = "662024";

    public ConnectDataBase(){

    }

    public boolean closeConn(){
        try{
            conn.close();
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public ConnectDataBase(String url, String user, String password){
        ConnectDataBase.url = url;
        ConnectDataBase.user = user;
        ConnectDataBase.password = password;
    }

    public static Connection getConn() {
        try {
            conn = DriverManager.getConnection(ConnectDataBase.url, ConnectDataBase.user, ConnectDataBase.password);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return conn;
    }
}

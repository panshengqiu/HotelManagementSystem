package checkManagement;


import fefualtpacage.Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckManagement extends Management {
    public CheckManagement(){
        super();
    }

    public Connection getConn() {
        return super.getConn();
    }

    public int getCheckInId(int orderDetailId) throws SQLException {
        Connection conn = getConn();
        String sql = "select checkId from checkinandout where orderDetailId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, orderDetailId);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return rs.getInt("checkId");
        }
        return -1;
    }
}

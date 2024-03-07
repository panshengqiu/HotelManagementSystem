package fefualtpacage;

import connect.ConnectDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Management {
    private final Connection conn;

    public Connection getConn(){
        return conn;
    }

    public Management(){
        this.conn = ConnectDataBase.getConn();
    }

    public Vector<Management> getManagementV(Management management, ResultSet rs) throws SQLException {
        Vector<Management> dataV = new Vector<>();
        while(rs.next()){
            dataV.add(management);
        }
        return dataV;
    }

    public ResultSet checkByUserName(String userName) throws SQLException {
        // 使用PreparedStatement来防止SQL注入攻击
        String sql = "select *from customer where cUserName=?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        // 使用setString方法来设置字符串值，并使用单引号将其括起来
        ps.setString(1, userName);
        // 执行查询并返回结果集
        ResultSet rs;
        rs = ps.executeQuery();
        return rs;
    }

    public ResultSet queryAllInfo(String tableName) throws SQLException {
        // 使用PreparedStatement来防止SQL注入攻击
        String sql = "select * from " + tableName + ";";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        // 执行查询并返回结果集
        ResultSet rs;
        rs = ps.executeQuery();
        return rs;
    }


}

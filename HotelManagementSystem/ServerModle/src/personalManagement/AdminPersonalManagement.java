package personalManagement;

import fefualtpacage.Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class AdminPersonalManagement extends Management {
    public AdminPersonalManagement() {
        super();
    }

    private String userName;
    private String password;
    ResultSet rs;
    public AdminPersonalManagement(String userName, String password){
        super();
        this.userName = userName;
        this.password = password;
    }


    public Connection getConn() {
        return super.getConn();
    }

    public Vector<Object> getTitleV(Object[] title){
        Vector<Object> titleV = new Vector<>();
        for (int i = 0; i < title.length; i++) {
            titleV.add(title[i]);
        }
        return titleV;
    }

    public ResultSet queryPerson() throws SQLException {
        String sql = "select *from administrator where aName = ? and aPassword=?;";
        PreparedStatement statement = super.getConn().prepareStatement(sql);
        statement.setString(1, this.userName);
        statement.setString(2, this.password);
        rs = statement.executeQuery();
        return rs;
    }


    public Vector<Object> getDataV() throws SQLException {
        queryPerson();
        Vector<Object> admin = new Vector<>();
        if(rs.next())
            admin.add(rs.getInt("aUserId"));
            admin.add(rs.getString("aName"));
            admin.add(rs.getString("aSex"));
            admin.add(rs.getInt("aAge"));
            admin.add(rs.getString("aPhone"));
            admin.add(rs.getString("aCardId"));
            admin.add(rs.getString("aUserName"));
            admin.add(rs.getString("aPassword"));
        return admin;
    }

    // 更新所登入的管理员个人信息
    public int updateAdminInfo(String name,String sex,int age,String phone,String userName,String password) throws SQLException {
        String sql = "update administrator set aName=?,aSex=?,aAge=?,aPhone=?,aUserName=?,aPassword=? where aUserName=? and aPassword=?;";
        PreparedStatement stmt = super.getConn().prepareStatement(sql);
        stmt.setString(1,name);
        stmt.setString(2,sex);
        stmt.setInt(3,age);
        stmt.setString(4,phone);
        stmt.setString(5,userName);
        stmt.setString(6,password);
        stmt.setString(7,userName);
        stmt.setString(8,password);
        int signal = stmt.executeUpdate();
        return signal;
    }

    // 根据管理员的用户名来查询管理员的ID
    public int checkUserIDByUserName() throws SQLException {
        String sql = "select aUserId from administrator where aUserName=?;";
        PreparedStatement stmt = super.getConn().prepareStatement(sql);
        stmt.setString(1, this.userName);
        ResultSet rs = stmt.executeQuery();
        int aUserId = -1;
        if(rs.next()){
            aUserId = rs.getInt("cUserId");
        }
        return aUserId;
    }
}

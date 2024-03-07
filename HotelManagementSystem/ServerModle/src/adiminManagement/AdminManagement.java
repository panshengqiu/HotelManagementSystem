package adiminManagement;

import fefualtpacage.Management;
import admin.AdminFirstGUI;
import common.LogInGUI;
import personalManagement.AdminPersonalManagement;
import personalManagement.CustomerPersonalManagement;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminManagement extends Management {
    public Connection getConnection(){
        return super.getConn();
    }
    public AdminManagement(){
        super();
    }

    CustomerPersonalManagement customerPersonalManagement;
    public ResultSet checkAdmin (String userName,String userPassword) throws SQLException {
    String sql = "select * from administrator where aUserName=? and aPassword=?";
        PreparedStatement ps = super.getConn().prepareStatement(sql);
        ps.setString(1,userName);
        ps.setString(2,userPassword);
        ResultSet rs;
        rs = ps.executeQuery();
        return rs;
    }
    //通过用户名查询管理员信息
    public ResultSet checkAdminByUserName(String userName) throws SQLException {
        return super.checkByUserName(userName);
    }
    //查询所有管理员信息
    public ResultSet queryAllAdminInfo(String tableName) throws SQLException {
        return super.queryAllInfo(tableName);
    }

    public void judgeAdmin(ResultSet rs, LogInGUI logInGUI) {
        try {
            if(!rs.next()){
                System.out.println("用户名或密码错误！");
                JOptionPane.showMessageDialog(null, "用户名或密码错误！","警告",JOptionPane.ERROR_MESSAGE);
            }else{
                String userName = rs.getString("aUserName");
                String password = rs.getString("aPassword");
                AdminPersonalManagement adminPersonalManagement = new AdminPersonalManagement(userName,password);
                AdminFirstGUI adminFirstGUI = new AdminFirstGUI(userName, adminPersonalManagement);
                adminFirstGUI.getJFrame().setVisible(true);
                // 关闭登入界面
                logInGUI.getJFrame().dispose();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}

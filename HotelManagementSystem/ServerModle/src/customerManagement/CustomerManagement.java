package customerManagement;

import fefualtpacage.Management;
import customer.CustomerFirstGUI;
import common.LogInGUI;
import personalManagement.CustomerPersonalManagement;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class CustomerManagement extends Management{

    private CustomerFirstGUI customerFirstGUI;

    private String userName;

    public Connection getConnection(){
        return super.getConn();
    }

    public CustomerManagement(){
        super();
    }

    //用于注册的信息
    public ResultSet checkCustomerByIdAndName(String userName, String userPassword) throws SQLException {
        // 使用PreparedStatement来防止SQL注入攻击
        String sql = "select *from customer where cUserName= ? and cPassword=?;";
        PreparedStatement ps = super.getConn().prepareStatement(sql);
        // 使用setString方法来设置字符串值，并使用单引号将其括起来
        ps.setString(1, userName);
        ps.setString(2, userPassword);
        // 执行查询并返回结果集
        ResultSet rs;
        rs = ps.executeQuery();
        return rs;
    }

    // 根据姓名和身份证号查找用户是否存在
    public ResultSet checkCustomerByNameAndIDCard(String userName, String idCard) throws SQLException {
        // 使用PreparedStatement来防止SQL注入攻击
        String sql = "select *from customer where cUserName= ? and cIdCard=?;";
        PreparedStatement ps = super.getConn().prepareStatement(sql);
        // 使用setString方法来设置字符串值，并使用单引号将其括起来
        ps.setString(1, userName);
        ps.setString(2, idCard);
        // 执行查询并返回结果集
        ResultSet rs;
        rs = ps.executeQuery();
        return rs;
    }

    // 根据姓名和身份证查找用户id返回
    public int checkCustomerByCardIdAndName(String userName, String idCard) throws SQLException {
        // 使用PreparedStatement来防止SQL注入攻击
        String sql = "select *from customer where cName= ? and cCardId=?;";
        PreparedStatement ps = super.getConn().prepareStatement(sql);
        // 使用setString方法来设置字符串值，并使用单引号将其括起来
        ps.setString(1, userName);
        ps.setString(2, idCard);
        // 执行查询并返回结果集
        ResultSet rs;
        rs = ps.executeQuery();
        int id = -1;
        while (rs.next()){
            id = rs.getInt("cUserId");
        }
        return id;
    }

    // 增加一条用户信息
    public int addCustomerInfo(String name, String sex, String age, String phone, String idCard, String userName, String password) throws SQLException {
        String sql = "insert into customer(cName,cSex,cAge,cPhone,cCardId,cUserName,cPassword) value(?,?,?,?,?,?,?);";
        PreparedStatement ps = super.getConn().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, sex);
        ps.setString(3, age);
        ps.setString(4, phone);
        ps.setString(5, idCard);
        ps.setString(6, userName);
        ps.setString(7, password);
        ps.executeUpdate();
        return 1;
    }

    public ResultSet checkCustomer(String userName, String userPassword) throws SQLException {
        // 使用PreparedStatement来防止SQL注入攻击
        String sql = "select *from customer where cUserName= ? and cPassword=?;";
        PreparedStatement ps = super.getConn().prepareStatement(sql);
        // 使用setString方法来设置字符串值，并使用单引号将其括起来
        ps.setString(1, userName);
        ps.setString(2, userPassword);
        // 执行查询并返回结果集
        ResultSet rs;
        rs = ps.executeQuery();
        return rs;
    }

    public void judgeCustomer(ResultSet rs, LogInGUI logInGUI) {
        try {
            if(!rs.next()){
                System.out.println("用户名或密码错误！");
                JOptionPane.showMessageDialog(null, "用户名或密码错误！","警告",JOptionPane.ERROR_MESSAGE);
            }else{
                String userName = rs.getString("cUserName");
                String password = rs.getString("cPassword");
                CustomerPersonalManagement customerPersonalManagement = new CustomerPersonalManagement(userName,password);
                customerFirstGUI = new CustomerFirstGUI(logInGUI,userName, customerPersonalManagement);
                // 关闭登入界面
                logInGUI.getJFrame().dispose();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    public ResultSet checkCustomerByUserName(String userName) throws SQLException {
        return super.checkByUserName(userName);
    }



    public ResultSet queryAllCustomerInfo(String tableName) throws SQLException {

        return super.queryAllInfo(tableName);
    }
}

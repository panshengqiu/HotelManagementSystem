package personalManagement;

import entityClass.Customer;
import fefualtpacage.Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CustomerPersonalManagement extends Management {
    private Customer customer;
    private String userName;
    private String password;
    ResultSet rs;
    public CustomerPersonalManagement(String userName, String password){
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
        String sql = "select *from customer where cUserName = ? and cPassword=?;";
        PreparedStatement statement = super.getConn().prepareStatement(sql);
        statement.setString(1, this.userName);
        statement.setString(2, this.password);
        rs = statement.executeQuery();
        return rs;
    }

    public Vector<Object> getDataV() throws SQLException {
        queryPerson();
        if(rs.next())
            this.customer = new Customer(rs);

        return customer.getCustomerInfoV();
    }



    public int updateCustomerInfo(String name,String sex,int age,String phone,String userName,String password) throws SQLException {
        String sql = "update customer set cName=?,cSex=?,cAge=?,cPhone=?,cUserName=?,cPassword=? where cUserName=? and cPassword=?;";
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

    public int checkUserIDByUserName() throws SQLException {
        String sql = "select cUserId from customer where cUserName=?;";
        PreparedStatement stmt = super.getConn().prepareStatement(sql);
        stmt.setString(1, this.userName);
        ResultSet rs = stmt.executeQuery();
        int cUserId = -1;
        if(rs.next()){
            cUserId = rs.getInt("cUserId");
        }
        return cUserId;
    }

}

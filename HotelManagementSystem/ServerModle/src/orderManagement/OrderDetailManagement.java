package orderManagement;

import fefualtpacage.Management;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

public class OrderDetailManagement extends Management {

    public OrderDetailManagement(){
        super();
    }

    public Connection getConn() {
        return super.getConn();
    }


    // 给orderdetail表添加一条记录
    public int addOrderDetail(int orderId, String rNumber, int userId, Date checkInDate, Date checkOutDate, int price) throws SQLException {
        String sql = "insert into orderdetail(orderId, rNumber,cUserId, checkInDate, checkOutDate, price) value(?,?,?,?,?,?);";
        PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        // 创建一个Calendar对象，指定时区为Asia/Shanghai
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        ps.setInt(1, orderId);
        ps.setString(2, rNumber);
        ps.setInt(3, userId);
        ps.setDate(4, checkInDate, calendar);
        ps.setDate(5, checkOutDate, calendar);
        ps.setInt(6, price);
        ps.executeUpdate();
        int orderDetailId = -1;
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            orderDetailId = rs.getInt(1);
            return orderDetailId;
        }
        return -1;
    }

    public int getOrderDetailId(int orderId) throws SQLException {
        String sql = "select orderDetailId from orderdetail where orderId = ?";
        PreparedStatement statement = getConn().prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getInt("orderDetailId");
        }
        return  -1;
    }

    public Date getCheckInDate(int orderId) throws SQLException {
        String sql = "select checkInDate from orderdetail where orderId = ?";
        PreparedStatement statement = getConn().prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getDate("checkInDate");
        }
        return  null;
    }
}

package orderManagement;

import fefualtpacage.Management;
import personalManagement.CustomerPersonalManagement;

import java.sql.*;
import java.text.SimpleDateFormat;

public class OrderManagement extends Management {
    public Connection getConn() {
        return super.getConn();
    }

    public OrderManagement(){
        super();
    }

    public int deleteOrderDetail(int orderDetailId) throws SQLException {
        String delete_sql = "delete from orderdetail where orderDetailId = ?;";
        PreparedStatement ps = getConn().prepareStatement(delete_sql);
        ps.setInt(1, orderDetailId);
        return  ps.executeUpdate();
    }

    // 插入orders表一条记录,线上预定
    public int addOrder(CustomerPersonalManagement customerPersonalManagement) throws SQLException {
        int cUserId = customerPersonalManagement.checkUserIDByUserName();
        // 设置订单创建时间为当前时间,获取系统时间，精确到毫秒，使用sql的Datetime
        // 获取当前时间的毫秒数
        long currentTimeMillis = System.currentTimeMillis();
        // 将毫秒数转换为Date类型
        Date currentDate = new Date(currentTimeMillis);
        // 创建一个SimpleDateFormat对象，指定日期和时间的格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // 将Date对象格式化为字符串
        String formattedTime = dateFormat.format(currentDate);
        // 输出当前时间
        System.out.println(formattedTime);
        //设置是否支付为0
        int isPay = 1;
        //设置应支付金额为订单购物车所有房间价格之和
        int shouldPay = 0;
        //设置订单类型为线上
        String orderType = "线上";
        String orderStatus = "生效中";
        // 订单结束时间设置为空
        Date orderEndDate = null;
        //退款金额为0
        int refund = 0;
        // 给orders表添加一条记录
        String insert_sql = "insert into orders(cUserId,createTime,isPaid,shouldPaidNum,orderType,orderStatus,overTime,Refund) value(?,?,?,?,?,?,?,?);";
        PreparedStatement ps = getConn().prepareStatement(insert_sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, cUserId);
        ps.setString(2, formattedTime);
        ps.setInt(3, isPay);
        ps.setInt(4, shouldPay);
        ps.setString(5, orderType);
        ps.setString(6, orderStatus);
        ps.setDate(7, orderEndDate);
        ps.setInt(8, refund);
        // 执行插入，并获取当前orderId
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            int orderId = rs.getInt(1);
            return orderId;
        }
        return -1;
    }

    // 删除订单

    public int deleteOrder(int orderID) throws SQLException {
        String sql = "delete from orders where orderId = ?;";
        PreparedStatement ps = getConn().prepareStatement(sql);
        ps.setInt(1, orderID);
        return ps.executeUpdate();
    }

    public int setOrderStatus(int orderId, String status) throws SQLException {
        System.out.println("status = " + status);
        String sql = "update orders set orderStatus = ? where orders.orderId = ?";
        PreparedStatement stmt = super.getConn().prepareStatement(sql);
        stmt.setString(1, status);
        stmt.setInt(2, orderId);
        int  signal = stmt.executeUpdate();
        System.out.println("设置状态的sql已经执行了！，signal = " + signal);
        return signal;
    }

    public String getOrderStatus(int orderId) throws SQLException {
        String sql = "select orderStatus from orders where orderId = ?;";
        Connection connection = getConn();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getString("orderStatus");
        }
        return  null;
    }

    public int setOrderPrice(int orderID, int price) throws SQLException {
        String sql = "update orders set shouldPaidNum = ? where orders.orderId = ?";
        PreparedStatement statement = getConn().prepareStatement(sql);
        statement.setInt(1, price);
        statement.setInt(2, orderID);
        return statement.executeUpdate();
    }

    public int getShouldPaidNum(int orderId) throws SQLException {
        String sql = "select shouldPaidNum from orders where orderId = ?;";
        Connection connection = getConn();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getInt("shouldPaidNum");
        }
        return  -1;
    }

    public int setOrderRefund(int orderId) throws SQLException {
        int shouldPaidNum = getShouldPaidNum(orderId);
        String sql = "update orders set Refund = ? where orders.orderId = ?";
        PreparedStatement statement = getConn().prepareStatement(sql);
        statement.setInt(1, shouldPaidNum);
        statement.setInt(2, orderId);

        int signal  = statement.executeUpdate();
        System.out.println("设置退款金额的sql已经执行了！，signal = " + signal);
        return signal;
    }

    public int addOrderFromAdmin(int userID) throws SQLException {
        // 设置订单创建时间为当前时间,获取系统时间，精确到毫秒，使用sql的Datetime
        // 获取当前时间的毫秒数
        long currentTimeMillis = System.currentTimeMillis();
        // 将毫秒数转换为Date类型
        Date currentDate = new Date(currentTimeMillis);
        // 创建一个SimpleDateFormat对象，指定日期和时间的格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // 将Date对象格式化为字符串
        String formattedTime = dateFormat.format(currentDate);
        // 输出当前时间
        System.out.println(formattedTime);
        //设置是否支付为0
        int isPay = 1;
        //设置应支付金额为订单购物车所有房间价格之和
        int shouldPay = 0;
        //设置订单类型为线上
        String orderType = "线下";
        String orderStatus = "生效中";
        // 订单结束时间设置为空
        Date orderEndDate = null;
        //退款金额为0
        int refund = 0;
        // 给orders表添加一条记录
        String insert_sql = "insert into orders(cUserId,createTime,isPaid,shouldPaidNum,orderType,orderStatus,overTime,Refund) value(?,?,?,?,?,?,?,?);";
        PreparedStatement ps = getConn().prepareStatement(insert_sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, userID);
        ps.setString(2, formattedTime);
        ps.setInt(3, isPay);
        ps.setInt(4, shouldPay);
        ps.setString(5, orderType);
        ps.setString(6, orderStatus);
        ps.setDate(7, orderEndDate);
        ps.setInt(8, refund);
        // 执行插入，并获取当前orderId
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            int orderId = rs.getInt(1);
            return orderId;
        }
        return -1;
    }
}

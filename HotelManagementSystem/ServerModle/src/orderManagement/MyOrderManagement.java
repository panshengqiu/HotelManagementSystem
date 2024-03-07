package orderManagement;

import fefualtpacage.Management;
import personalManagement.CustomerPersonalManagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;


public class MyOrderManagement extends Management {
    Vector<Object> myOrderDetailTitleV;
    Vector<Vector<Object>> myOrderDetailDataV;
    CustomerPersonalManagement customerPersonalManagement;
    Vector<Vector<Object>> myOrderDataV;
    Vector<Object> myOrderTitleV;

    public Connection getConn() {
        return super.getConn();
    }

    public MyOrderManagement(CustomerPersonalManagement customerPersonalManagement) throws SQLException {
        super();
        this.customerPersonalManagement = customerPersonalManagement;
        myOrderDataV = new Vector<>();
        myOrderDetailDataV = new Vector<>();

        myOrderTitleV = new Vector<>();
        myOrderDetailTitleV = new Vector<>();

        setMyOrderDataV();
        setMyOrderTitleV();
        setMyOrderDetailTitleV();
    }

    // 获取当前用户的所有订单
    public ResultSet getMyOrder() throws SQLException {
        ResultSet rs = null;
        String sql = "select * from orders where cUserId = " + customerPersonalManagement.checkUserIDByUserName() + ";";
        rs = getConn().prepareStatement(sql).executeQuery();
        return rs;
    }

    //
   /* public int createMyOrder() throws SQLException {
        OrderManagement orderManagement = new OrderManagement();
        int orderId = -1;
        // 如果myOrderDataV为空,说明第一次创建订单，调用addOrder方法,给orders表添加一条记录
        if (myOrderDataV == null){
            orderId = orderManagement.addOrder(personalManagement);
            addMyOrderDataV();
        }
        return  orderId;
    }
*/

    // 获取当前用户的所有订单信息
    public void setMyOrderDataV() throws SQLException {
        ResultSet rs = null;
        String sql = "select * from orders where cUserId = " + customerPersonalManagement.checkUserIDByUserName() + ";";
        try {
            rs = getConn().prepareStatement(sql).executeQuery();
            while (rs.next()){
                Vector<Object> v = new Vector<>();
                v.add(rs.getInt(1));
                v.add(rs.getInt(2));
                v.add(rs.getString(3));
                v.add(rs.getInt(4));
                v.add(rs.getInt(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                v.add(rs.getDate(8));
                v.add(rs.getInt(9));
                myOrderDataV.add(v);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Vector<Vector<Object>> getMyOrderDataV() {
        return myOrderDataV;
    }

    public Vector<Object> getMyOrderDetailTitleV(){
        return myOrderDetailTitleV;
    }

    public void setMyOrderTitleV(){
        Object[] title = {"订单号", "用户id", "创建时间", "是否支付", "应支付金额", "订单类型", "订单状态", "订单结束时间", "退款金额"};
        Collections.addAll(myOrderTitleV, title);
    }

    public void setMyOrderDetailTitleV(){
        Object[] shoppingTitle = {"订单详情号","订单号", "房间号", "入住用户ID", "入住日期", "退房日期", "预定价格"};
        Collections.addAll(myOrderDetailTitleV, shoppingTitle);
    }

    public Vector<Vector<Object>> getMyOrderDetailDataV(){
        return myOrderDetailDataV;
    }

    public void setMyOrderDetailDataV(int orderId) {
        // 先清空myOrderDetailDataV
        myOrderDetailDataV.clear();
        String sql = "select * from orderdetail where orderId = " + orderId + ";";
        try {
            ResultSet rs = getConn().prepareStatement(sql).executeQuery();
            while (rs.next()){
                Vector<Object> v = new Vector<>();
                v.add(rs.getInt(1));
                v.add(rs.getInt(2));
                v.add(rs.getString(3));
                v.add(rs.getInt(4));
                v.add(rs.getDate(5));
                v.add(rs.getDate(6));
                v.add(rs.getInt(7));
                myOrderDetailDataV.add(v);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Vector<Object> getMyOrderTitleV(){
        return myOrderTitleV;
    }
}

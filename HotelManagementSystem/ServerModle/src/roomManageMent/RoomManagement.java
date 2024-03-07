package roomManageMent;

import fefualtpacage.Management;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

public class RoomManagement extends Management {

    public Connection getConn() {
        return super.getConn();
    }

    public RoomManagement(){
        super();
    }

    public ResultSet checkRoomByUserName(String userName) throws SQLException {
        return super.checkByUserName(userName);
    }

    public ResultSet queryAllRoomInfo(String tableName) throws SQLException {
        return super.queryAllInfo(tableName);
    }

    // 用户查询房间信息，待修改
    public ResultSet queryToDayRoomInfo() throws SQLException {
        String sql = "select rTypeName, rTypePrice, maxNum from roomtype;";
        return super.getConn().prepareStatement(sql).executeQuery();
    }


    // 根据入住信息以及房间类型随机获取一个房间号
    public String getRandomRoomNumber(Date checkInDate, Date checkOutDay, String roomTypeName){

        String sql = "select rNumber from room where rTypeId in" +
                "(select rTypeId from roomtype where rTypeName = ?) and rNumber not in " +
                "(select rNumber from orderdetail where (checkInDate >= ? and checkInDate < ?)  or (checkOutDate > ? and checkOutDate <= ?))";

        ResultSet rs = null;
        try {
            PreparedStatement stmt = super.getConn().prepareStatement(sql);
            // 创建一个Calendar对象，指定时区为Asia/Shanghai
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            stmt.setString(1, roomTypeName);
            stmt.setDate(2, checkInDate, calendar);
            stmt.setDate(3, checkOutDay, calendar);
            stmt.setDate(4, checkInDate, calendar);
            stmt.setDate(5, checkOutDay, calendar);
            rs = stmt.executeQuery();
            // 返回的房间可能有多个，而且都是未预定的，随机获取第一个就行了
            if(rs.next()){
                return rs.getString("rNumber");
            }else{
                System.out.println("rs为空，没有查到房间号");
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    // 根据入住日期，退房日期，房间类型判断是否有房
    public boolean isRemainRoom(Date checkInDate, Date checkOutDay, String roomTypeName) throws SQLException {
        // 创建一个Calendar对象，并用setTime方法将其设置为Date对象的值
        Calendar calendar = Calendar.getInstance();
        //从开始日期到退房日期前一天判断每天是否有房，
        for (Date date = checkInDate; date.compareTo(checkOutDay) < 0; ) {
            int remain = toDayRemainRoomNum(date, roomTypeName);
            if (remain <= 0) {
                return false;
            }
            //  答应当天日期剩余房间数量
            System.out.println(date+",剩余房间数量：" + remain);
            calendar.setTime(date);
            // 用Calendar的add方法，将其日期加上一天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            // 将Calendar对象转换为Date对象
            date = new Date(calendar.getTime().getTime());
        }
        // 如果有房，返回false
        return true;
    }

    // 根据某天的日期、剩余房间数量占总房间的比例，获取当天某类型房间的价格
    public int getOneDayRoomPrice(Date date, String roomTypeName) throws SQLException {
        // 获取房间的原价
        int roomPrice = getRoomPriceByRoomType(roomTypeName);
        // 获取房间的总数
        int totalRoomNum = totalRoomNum(roomTypeName);
        // 获取当天某类型房间的剩余数量
        int remainRoomNum = toDayRemainRoomNum(date, roomTypeName);
        // 求剩余房间占比
        double remainRoomRate = (double) remainRoomNum / totalRoomNum;
        // 判断当天是周几
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // 获取当天的月份
        int month = calendar.get(Calendar.MONTH);
        // 获取当天的日期
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 如果是周五周六周日，价格为原来的1.5倍
        if(dayOfWeek == 6 || dayOfWeek == 7 || dayOfWeek == 1) {
            roomPrice = (int) (roomPrice * 1.3);
        } else if ((month == 0 && day == 1) || (month == 7 && day == 15) || (month == 11 && day == 31) || (month == 4 && day == 1) || (month == 9 && day == 1)) {
            roomPrice = (int) (roomPrice * 1.6);
        } else if (remainRoomRate <= 0.5) {// 剩余房间比率少于25%
            roomPrice = (int) (roomPrice * 1.2);
        } else if (remainRoomRate <= 0.25) {
            roomPrice = (int) (roomPrice * 1.5);
        } else if (roomPrice <= 0.1) {
            roomPrice = (int) (roomPrice * 1.8);
        }
        return roomPrice;
    }

    // 获取订单详细总价格，房间价格*入住天数
    public int getRoomPriceByRoomType(Date checkInDate, Date checkOutDay, String roomTypeName) throws SQLException {
        //从 check天数到退房日期查询每天房价，调用getRoomPriceByRoomType方法
        int roomPrice = 0;
        // 创建一个Calendar对象，并用setTime方法将其设置为Date对象的值
        Calendar calendar = Calendar.getInstance();
        for (Date date = checkInDate; date.compareTo(checkOutDay) < 0; ) {
            roomPrice += getOneDayRoomPrice(date, roomTypeName);
            calendar.setTime(date);
            // 用Calendar的add方法，将其日期加上一天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            // 将Calendar对象转换为Date对象
            date = new Date(calendar.getTime().getTime());
        }
        return roomPrice;
    }

    // 根据房间类型的定价获取某一类型房价的房间价格
    public int getRoomPriceByRoomType(String roomTypeName) {
        String sql = "select rTypePrice from roomtype where rTypeName=?;";
        try {
            PreparedStatement stmt = super.getConn().prepareStatement(sql);
            stmt.setString(1, roomTypeName);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    // 某类型房间当天空房数
    public int toDayRemainRoomNum(Date queryDate, String roomTypeName) throws SQLException {
        //获取系统当天日期作为开始日期
        Date startDate = Date.valueOf(LocalDate.now());
        // 创建一个Calendar对象，指定时区为Asia/Shanghai
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 如果查询日期小于开始日期，返回-1

        if(queryDate.compareTo(startDate) < 0){
            System.out.println("查询日期小于开始日期");
            return -1;
        }
        //先求当天某类型房间已经预定数量
        String sql = "select count(*) from orderdetail where checkOutDate > ? and checkInDate <= ? and" +
                " rNumber in (select rNumber from room where rTypeId in" +
                " (select rTypeId from roomtype where rTypeName = ?));";
        PreparedStatement stmt = this.getConn().prepareStatement(sql);
        stmt.setDate(1, queryDate, calendar);
        stmt.setDate(2, queryDate, calendar);
        stmt.setString(3, roomTypeName);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return this.totalRoomNum(roomTypeName)-rs.getInt(1);
    }


    // 某类型房间总数
    public int totalRoomNum(String roomTypeName) throws SQLException {
        int roomTypeId = getRoomTypeId(roomTypeName);
        String sql = "select count(*) from room where rTypeId=?;";
        PreparedStatement stmt =super.getConn().prepareStatement(sql);
        stmt.setInt(1, roomTypeId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    // 根据房间类型名称查找所有房间
    public ResultSet getRoomNumber(String roomTypeName) throws SQLException {
        int roomTypeId = getRoomTypeId(roomTypeName);
        String sql = "select rNumber from room where rTypeId=?;";
        PreparedStatement stmt =super.getConn().prepareStatement(sql);
        stmt.setInt(1, roomTypeId);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }


    // 根据房间类型名获取房间类型id
    private int getRoomTypeId(String roomTypeName) throws SQLException {
        String sql = "select rTypeId from roomtype where rTypeName=?;";
        PreparedStatement stmt =super.getConn().prepareStatement(sql);
        stmt.setString(1, roomTypeName);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    // 获取顾客展示页面的房间信息，待修改
    public Vector<Vector<Object>> getRoomV(ResultSet rs) throws SQLException {
        Vector<Vector<Object>> dataV = new Vector<>();
        rs = queryToDayRoomInfo();
        while(rs.next()){
            Vector<Object> temp = new Vector<>();
            // 获取房间类型名
            String typeName = rs.getString(1);
            // 获取房间价格
            int rTypePrice = rs.getInt(2);
            // 获取房间最大入住人数
            int maxNum = rs.getInt(3);
            // 获取当天剩余房间数量
            Date currentDate = Date.valueOf(LocalDate.now());
            System.out.println("系统当前日期："+currentDate);
            int remain = this.toDayRemainRoomNum(currentDate, typeName);
            // 将房间信息添加到dataV中
            temp.add(typeName);
            temp.add(rTypePrice);
            temp.add(maxNum);
            temp.add(remain);
            dataV.add(temp);
        }
        return dataV;
    }

    // 返回用户页面的房间信息标题
    public Vector<Object> getRoomTitleV(){
        Object[] title = {"房间类型", "房间价格", "可入住人数","当前剩余房间数量"};
        Vector<Object> titleV = new Vector<>();
        titleV.addAll(Arrays.asList(title));
        return titleV;
    }

}

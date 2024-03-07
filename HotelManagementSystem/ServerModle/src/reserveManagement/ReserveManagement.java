package reserveManagement;

import customerManagement.CustomerManagement;
import fefualtpacage.Management;
import roomManageMent.RoomManagement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class ReserveManagement extends Management {
    public ReserveManagement(){
        super();
    }
    public Connection getConn(){
        return super.getConn();
    }

    // 根据预定日期以及房间类型判断是否有房
    public boolean isRemainRoom(Date checkInDate, Date checkOutDay, String roomTypeName) throws SQLException {
        RoomManagement roomManagement = new RoomManagement();
        boolean isRemainRoom = roomManagement.isRemainRoom(checkInDate, checkOutDay, roomTypeName);
        return isRemainRoom;
    }

    // 根据预定日期以及房间类型获取房间价格
    public int getRoomPriceByRoomType(Date checkInDate, Date checkOutDay, String roomTypeName) throws SQLException {
        RoomManagement roomManagement = new RoomManagement();
        int roomPrice = roomManagement.getRoomPriceByRoomType(checkInDate, checkOutDay, roomTypeName);
        return roomPrice;
    }

    // 判断客户是否存在
    public int checkCustomer(String name, String cardId) throws SQLException {
        CustomerManagement customerManagement = new CustomerManagement();
        int cUserId = customerManagement.checkCustomerByCardIdAndName(name, cardId);
        return cUserId;
    }

    // 根据预定日期以及房间类型随机获取房间号
    public String GetRandomRoomNumber(Date checkInDate, Date checkOutDay, String roomTypeName) throws SQLException {
        RoomManagement roomManagement = new RoomManagement();
        String roomNumber = roomManagement.getRandomRoomNumber(checkInDate, checkOutDay, roomTypeName);
        return roomNumber;
    }

}

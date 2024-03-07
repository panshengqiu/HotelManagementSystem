package entityClass;
import connect.ConnectDataBase;
import java.sql.*;


public class Room {
    private String roomNumber;//primary key
    private int roomTypeId;//foreign key
    private int isClean;
    private String roomStatus;
    private RoomType roomType;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getIsClean() {
        return isClean;
    }

    public void setIsClean(int isClean) {
        this.isClean = isClean;
    }

    public String getRoomStatus() {
        return roomStatus;
    }
    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public static void main(String[] args) {
        ConnectDataBase con = new ConnectDataBase();
        try{
            Statement stmt = con.getConn().createStatement();
            ResultSet rs = stmt.executeQuery("select *from Room");
            while(rs.next()){
                System.out.println(rs.getString("rNumber"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}



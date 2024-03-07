package entityClass;
public class
        RoomType {
    private String roomTypeName;
    private int roomTypeId;//primary key &auto increment
    private int roomTypePrice;
    private int maxNum;

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getRoomTypePrice() {
        return roomTypePrice;
    }

    public void setRoomTypePrice(int roomTypePrice) {
        this.roomTypePrice = roomTypePrice;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }
}

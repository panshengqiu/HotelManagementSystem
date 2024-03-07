package relationClass;
import java.util.Date;

public class Clean {
    private int cleanId;
    private String roomNumber;
    private int staffId;
    private int adminId;
    private Date cleanTime;

    // get方法
    public int getCleanId() {
        return cleanId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getAdminId() {
        return adminId;
    }

    public Date getCleanTime() {
        return cleanTime;
    }

    // set方法
    public void setCleanId(int cleanId) {
        this.cleanId = cleanId;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setCleanTime(Date cleanTime) {
        this.cleanTime = cleanTime;
    }
}


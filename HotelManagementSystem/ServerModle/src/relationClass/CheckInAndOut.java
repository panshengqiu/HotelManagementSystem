package relationClass;

import java.util.Date;

public class CheckInAndOut {
    private int checkId;
    private int orderDetailId;
    private Date checkInTime;
    private int checkInAdminId;
    private Date checkOutTime;
    private int checkOutAdminId;

    // get方法
    public int getCheckId() {
        return checkId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public int getCheckInAdminId() {
        return checkInAdminId;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public int getCheckOutAdminId() {
        return checkOutAdminId;
    }

    // set方法
    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public void setCheckInAdminId(int checkInAdminId) {
        this.checkInAdminId = checkInAdminId;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public void setCheckOutAdminId(int checkOutAdminId) {
        this.checkOutAdminId = checkOutAdminId;
    }
}


package relationClass;

import java.time.LocalDate;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private String roomNumber;
    private int customerUserId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int price;

    // get方法
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getRNumber() {
        return roomNumber;
    }

    public int getCUserId() {
        return customerUserId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public int getPrice() {
        return price;
    }

    // set方法
    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setRNumber(String rNumber) {
        this.roomNumber = rNumber;
    }

    public void setCUserId(int cUserId) {
        this.customerUserId = cUserId;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}


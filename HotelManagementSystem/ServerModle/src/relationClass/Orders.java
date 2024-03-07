package relationClass;
import java.util.Date;
import java.time.DateTimeException;

public class Orders {
    private int orderId;
    private int customerUserId;
    private Date createTime;
    private int isPaid;
    private int shouldPaidNum;
    private String orderType;
    private String orderStatus;
    private Date overTime;
    private int refund;

    // get方法
    public int getOrderId() {
        return orderId;
    }

    public int getCUserId() {
        return customerUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public int getShouldPaidNum() {
        return shouldPaidNum;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Date getOverTime() {
        return overTime;
    }

    public int getRefund() {
        return refund;
    }

    // set方法
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCUserId(int cUserId) {
        this.customerUserId = cUserId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public void setShouldPaidNum(int shouldPaidNum) {
        this.shouldPaidNum = shouldPaidNum;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }
}


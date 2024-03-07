package relationClass;
import entityClass.*;

public class CheckInfo {
    private int checkInfoId;
    private int checkId;
    private int customerId;

    // get方法
    public int getCheckInfoId() {
        return checkInfoId;
    }

    public int getCheckId() {
        return checkId;
    }

    public int getCustomerId() {
        return customerId;
    }

    // set方法
    public void setCheckInfoId(int checkInfoId) {
        this.checkInfoId = checkInfoId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}


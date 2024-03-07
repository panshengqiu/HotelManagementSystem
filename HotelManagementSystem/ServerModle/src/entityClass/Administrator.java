package entityClass;
public class Administrator extends People{
    private String adminUserName;
    private String adminPassword;

    public Administrator() {
    }


    public Administrator(int userId, String name, String sex, int age, String phone, String cardId, String adminUserName, String adminPassword) {
        super(userId, name, sex, age, phone, cardId);
        this.adminUserName = adminUserName;
        this.adminPassword = adminPassword;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
package entityClass;

public class People {

    private int userId;//primary key &auto increment
    private String Name;
    private String Sex;
    private int Age;
    private String Phone;
    private String CardId;

    public People() {
    }

    public People(int userId, String name, String sex, int age, String phone, String cardId) {
        this.userId = userId;
        Name = name;
        Sex = sex;
        Age = age;
        Phone = phone;
        CardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }
}



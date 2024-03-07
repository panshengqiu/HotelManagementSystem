package entityClass;
public class Staff extends People{

    public Staff(){

    }
    public Staff(int userId, String name, String sex, int age, String phone, String cardId) {
        super(userId, name, sex, age, phone, cardId);
    }


}
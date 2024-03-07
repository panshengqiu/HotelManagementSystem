package entityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Customer extends People{
   private String customerUserName;
   private String customerPassword;
   public Customer(){
      super();
   }

   public Customer(int userId, String name, String sex, int age, String phone, String cardId, String customerUserName, String customerPassword) {
      super(userId, name, sex, age, phone, cardId);
      this.customerUserName = customerUserName;
      this.customerPassword = customerPassword;
   }


   public Customer(ResultSet rs) throws SQLException {
      int userId = rs.getInt(1);
      String name = rs.getString(2);
      String sex = rs.getString(3);
      int age = rs.getInt(4);
      String phone = rs.getString(5);
      String cardId = rs.getString(6);
      super.setUserId(userId);
      super.setName(name);
      super.setAge(age);
      super.setSex(sex);
      super.setPhone(phone);
      super.setCardId(cardId);
      this.customerUserName = rs.getString(7);
      this.customerPassword = rs.getString(8);
   }

   public Vector<Object> getCustomerInfoV(){
      Vector<Object> customerV = new Vector<>();
      customerV.add(super.getUserId());
      customerV.add(super.getName());
      customerV.add(super.getSex());
      customerV.add(super.getAge());
      customerV.add(super.getPhone());
      customerV.add(super.getCardId());
      customerV.add(customerUserName);
      customerV.add(customerPassword);
      return customerV;
   }

   public String getCustomerUserName() {
      return customerUserName;
   }

   public void setCustomerUserId(String customerUserName) {
      this.customerUserName = customerUserName;
   }

   public String getCustomerPassword() {
      return customerPassword;
   }

   public void setCustomerPassword(String customerPassword) {
      this.customerPassword = customerPassword;
   }
}

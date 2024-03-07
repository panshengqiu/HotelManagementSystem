package registerManagement;

import java.sql.Connection;
public class RegisterManagement extends fefualtpacage.Management {
    public Connection getConnection() {
        return super.getConn();
    }

    public RegisterManagement() {
        super();
    }

    //判断数据库中是否已有存在的用户名，若已有则返回false，否则返回true，并且更新数据库
    public boolean register(String userName, String userPassword, String userAge, String userid, String usersex, String userphone, String username) throws Exception {
        // 根据userName字段判断数据库中是否已有存在的用户名
        String sql = "select *from customer where cUserName=?;";
        java.sql.PreparedStatement ps = super.getConn().prepareStatement(sql);
        // 使用setString方法来设置字符串值，并使用单引号将其括起来
        ps.setString(1, userName);
        // 执行查询并返回结果集
        java.sql.ResultSet rs = ps.executeQuery();
        // 如果结果集中有数据，说明数据库中已有存在的用户名，返回false
        if (rs.next()) {
            return false;
        } else {
            try {
                // 使用PreparedStatement来防止SQL注入攻击
                String sql1 = "insert into customer(cUserName,cPassword,cAge,cSex,cCardID,cPhone,cName) values(?,?,?,?,?,?,?);";
                java.sql.PreparedStatement ps1 = super.getConn().prepareStatement(sql1);
                // 使用setString方法来设置字符串值，并使用单引号将其括起来
                ps1.setString(1, userName);
                ps1.setString(2, userPassword);
                ps1.setString(3, userAge);
                ps1.setString(4, usersex);
                ps1.setString(5, userid);
                ps1.setString(6, userphone);
                ps1.setString(7, username);
                // 执行查询并返回结果集
                ps1.executeLargeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
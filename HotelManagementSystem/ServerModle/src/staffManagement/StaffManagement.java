package staffManagement;

import fefualtpacage.Management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class StaffManagement extends Management{
    public StaffManagement(){
        super();
    }
    
    public ResultSet queryAllInfo(String tableName) throws SQLException{
        return super.queryAllInfo(tableName);
    }

    public Vector<Object> getStaffTitleV() throws SQLException{
        Vector<Object> titleV = new Vector<>();
        Object[] title = {"员工编号", "员工姓名", "性别", "年龄","电话","身份证号"};
        for (int i = 0; i < title.length; i++) {
            titleV.add(title[i]);
        }
        return titleV;
    }

    public Vector<Vector<Object>> getStaffDataV() throws SQLException {
        Vector<Vector<Object>> dataV = new Vector<>();
         ResultSet rs = queryAllInfo("staff");
         while(rs.next()){
             Vector<Object> temp = new Vector<>();
             temp.add(rs.getInt(1));
             temp.add(rs.getString(2));
             temp.add(rs.getString(3));
             temp.add(rs.getInt(4));
             temp.add(rs.getString(5));
             temp.add(rs.getString(6));
             dataV.add(temp);
         }

        return  dataV;
    }

}

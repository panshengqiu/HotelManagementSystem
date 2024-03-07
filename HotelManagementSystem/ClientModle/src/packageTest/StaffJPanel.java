package packageTest;

import staffManagement.StaffManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

public class StaffJPanel extends JPanel {
    JTable table;//声明一个JTable对象：table，用于展示信息
    JScrollPane scrollPane;//声明一个对象，滚动界面
    DefaultTableModel model;//
    Vector<Object> titleV;//用于存储数据库表的表名
    Vector<Vector<Object>> dataV;//用于存储数据库表的*多行数据*
    public StaffJPanel() throws SQLException {
//      this.add(new JLabel("这里是员工信息界面"));
        this.setLayout(new BorderLayout());
        StaffManagement staffManagement = new StaffManagement();
        titleV = new Vector<>();
        dataV = new Vector<>();
        titleV = staffManagement.getStaffTitleV();
        dataV = staffManagement.getStaffDataV();

        model = new DefaultTableModel(dataV, titleV);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        this.add(scrollPane);

        table.setPreferredScrollableViewportSize(new Dimension(1000, 800));
        //设置滚动面板自动适应表格的大小
        scrollPane.setPreferredSize(new Dimension(table.getPreferredSize().width + 100, table.getPreferredSize().height + 100));

    }

  /*  public static void main(String[] args) throws SQLException {
        StaffJPanel j= new StaffJPanel();
        JFrame jFrame = new JFrame("");
        jFrame.add(j);
        jFrame.setVisible(true);
    }*/
}

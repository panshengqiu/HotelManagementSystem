package admin;

import common.BackGroundPanel;
import common.PathUtils;
import common.ScreenUtils;
import personalManagement.AdminPersonalManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

public class AdminPersonalInfoGUI {
    AdminFirstGUI adminFirstGUI;
    AdminPersonalManagement adminPersonalManagement;
    JFrame jf;
    JTable table;
    JScrollPane tablePanel;
    Vector<Object> titleV;
    Vector<Object> dataV;
    DefaultTableModel model;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem back;

    final int WIDTH = 1200;
    final int HEIGHT = 900;
    JButton updateBtn;
    public AdminPersonalInfoGUI(){

    }

    public AdminPersonalInfoGUI(AdminFirstGUI adminFirstGUI, AdminPersonalManagement adminPersonalManagement) throws SQLException, IOException {
        this.adminFirstGUI = adminFirstGUI;
        this.adminPersonalManagement = adminPersonalManagement;
        init();
    }

    public void addBackActionListener(){
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                adminFirstGUI.getJFrame().setVisible(true);
            }
        });
    }

    public void addUpdateBtnActionListener(){
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) table.getValueAt(0, 1);
                String sex = (String) table.getValueAt(0, 2);
                String age =  (String)table.getValueAt(0, 3);
                // 将年龄转换成int
                int ageInt = Integer.parseInt(age);
                String phone = (String) table.getValueAt(0, 4);
                String userName = (String) table.getValueAt(0, 6);
                String password = (String) table.getValueAt(0, 7);
                int signal;
                try {
                    signal = adminPersonalManagement.updateAdminInfo(name, sex, ageInt, phone, userName, password);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (signal == 1) {
                    JOptionPane.showMessageDialog(null, "更新成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "更新失败", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public void init() throws IOException, SQLException {
        jf = new JFrame("欢迎来到9店小2管理系统");
        jf.setVisible(false);
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        // 登入背景
        BackGroundPanel bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));
        jf.add(bgPanel);

        menuBar = new JMenuBar();
        menu = new JMenu("设置");
        back = new JMenuItem("返回");
        menu.add(back);
        menuBar.add(menu);

        jf.add(menuBar, BorderLayout.NORTH);

        Object[] title = {"ID","姓名","性别","年龄","电话","身份证","用户名","密码"};
        titleV = adminPersonalManagement.getTitleV(title);
        dataV = adminPersonalManagement.getDataV();
        Vector<Vector<Object>> dataSV = new Vector<>();
        dataSV.add(dataV);
        class MyTableModel extends DefaultTableModel {
            public MyTableModel(Vector<Vector<Object>> dataV, Vector<Object> roomTitleV) {
                super(dataV, roomTitleV);
            }

            //重写isCellEditable方法
            @Override
            public boolean isCellEditable (int row, int column) {
                //all cells false
                if(column == 0 || column == 5)
                    return false;
                else
                    return true;
            }
        }
        model = new MyTableModel(dataSV, titleV);
        table = new JTable(model);
        tablePanel = new JScrollPane(table);
        jf.add(tablePanel,BorderLayout.CENTER);

//        jf.add(bgPanel, BorderLayout.SOUTH);
        Box btnBox = Box.createHorizontalBox();
        updateBtn = new JButton("更新个人信息");
        btnBox.add(updateBtn,BorderLayout.CENTER);
        jf.add(btnBox,BorderLayout.SOUTH);

        addBackActionListener();
        addUpdateBtnActionListener();
    }

    public AdminPersonalInfoGUI getThis(){
        return this;
    }

    public JFrame getJFrame(){
        return jf;
    }
}

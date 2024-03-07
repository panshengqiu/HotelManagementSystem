package admin;

import common.LogInGUI;
import common.OrderJPanel;
import common.PathUtils;
import packageTest.RoomJPanel;
import personalManagement.AdminPersonalManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

// 定义一个AdminFirstGUI类，继承自JFrame类
public class AdminFirstGUI {
    JFrame jf;

    // 定义一个选项卡面板
    JTabbedPane tabbedPane;

    JPanel orderPanel;

    JPanel orderDetailPanel;
    JPanel roomPanel;
    JPanel roomTypePanel;
    AdminPersonalManagement adminPersonalManagement;
    String userName;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem exit;
    JMenuItem personalInfo;
    JButton reserveBtn;
    JButton cancelBtn;

    public JFrame getJFrame(){
        return jf;
    }

    public AdminFirstGUI getAdminFirstGUI(){
        return this;
    }

    // 定义一个构造方法
    public AdminFirstGUI(String userName, AdminPersonalManagement adminPersonalManagement) throws SQLException, IOException {
        this.adminPersonalManagement = adminPersonalManagement;
        this.userName = userName;
        init(); // 添加：调用init()方法来初始化窗口组件
    }
    public AdminFirstGUI() {

    }

    // 修改：添加一个初始化方法，设置窗口可见性和标题
    public void init() throws SQLException, IOException {
        jf = new JFrame("欢迎来到酒店小2管理系统！");
        jf.setVisible(true);
        jf.setTitle("管理员界面");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口大小
        jf.setSize(1100, 850);
        jf.setResizable(false);
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        jf.setLayout(new BorderLayout()); // 添加：设置窗口的布局管理器为边界布局


        // 创建选项卡面板
        tabbedPane = new JTabbedPane();
        // 创建订单信息面板
        roomPanel = new RoomJPanel(this);
        tabbedPane.addTab("房间预定",roomPanel);

        orderPanel = new OrderJPanel(this, adminPersonalManagement);
        tabbedPane.addTab("订单信息",orderPanel);
        jf.add(tabbedPane, BorderLayout.CENTER);


        Box northBox = Box.createHorizontalBox();
        menuBar = new JMenuBar();
        menu = new JMenu("设置");
        personalInfo = new JMenuItem("个人信息");
        exit = new JMenuItem("退出");
        menu.add(personalInfo);
        menu.add(exit);
        menuBar.add(menu);
        northBox.add(menuBar);

        jf.add(northBox, BorderLayout.NORTH);

        Box southBox = Box.createHorizontalBox();
        reserveBtn = new JButton("预定");
        cancelBtn = new JButton("取消预定");
        southBox.add(Box.createHorizontalGlue());
        southBox.add(reserveBtn);
        southBox.add(Box.createHorizontalGlue());
        southBox.add(cancelBtn);
        southBox.add(Box.createHorizontalGlue());
        jf.add(southBox, BorderLayout.SOUTH);
        addPersonalInfoActionListener();
        addReserveBtnListener();
        addExitActionListener();
    }

    // 管理员预定房间
    public void addReserveBtnListener() {
        reserveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AdminReserveGUI adminReserveGUI = new AdminReserveGUI(getAdminFirstGUI(), adminPersonalManagement);
                    getAdminFirstGUI().getJFrame().setVisible(false);
                    adminReserveGUI.getReserveJFrame().setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void addPersonalInfoActionListener(){
        personalInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPersonalInfoGUI adminPersonalInfoGUI = null;
                try {
                    adminPersonalInfoGUI = new AdminPersonalInfoGUI(getAdminFirstGUI(), adminPersonalManagement);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                getAdminFirstGUI().getJFrame().setVisible(false);
                adminPersonalInfoGUI.getJFrame().setVisible(true);

            }
        });
    }

    public void addExitActionListener(){
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jf.dispose();
                    new LogInGUI();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


}

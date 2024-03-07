package customer;

import common.BackGroundPanel;
import common.LogInGUI;
import common.PathUtils;
import customerManagement.CustomerManagement;
import orderManagement.MyOrderManagement;
import orderManagement.OrderManagement;
import packageTest.*;
import personalManagement.CustomerPersonalManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CustomerFirstGUI {
    CustomerManagement customerManagement;
    private JFrame jf;
    private RoomJPanel roomJPanel;
    private CustomerReserveJPanel reserveJPanel;
    JPanel jPanel;
    
    private Box btnBoxOne; // 按钮盒子
    private Box btnBoxTwo; // 按钮盒子
    private JButton okBtn; // 预定按钮
    private JButton cancelBtn;//取消按钮

    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton reserveFunctionBtn;
    private JButton myOrderBtn;
    private final static int  WIDTH = 1200;
    private final static int HEIGHT = 800;
    private JMenuBar menuBar;
    private JMenuItem personalInfo;
    private JMenu setting;
    private JMenuItem exit;

    private ReserveGUI reserveGUI;
    private String userName;
    CustomerPersonalInfoGUI customerPersonalInfoGUI;
    CustomerPersonalManagement customerPersonalManagement;
    MyOrderManagement myOrderManagement;
    MyOrderJPanel myOrderJPanel;
    BackGroundPanel bgPanel;
    LogInGUI logInGUI;


    public CustomerFirstGUI(LogInGUI logInGUI, String userName, CustomerPersonalManagement customerPersonalManagement) throws SQLException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.logInGUI = logInGUI;
        this.userName = userName;
        this.customerPersonalManagement = customerPersonalManagement;
        this.myOrderManagement = new MyOrderManagement(customerPersonalManagement);
        customerPersonalInfoGUI = new CustomerPersonalInfoGUI(getThis(), customerPersonalManagement);
        this.myOrderJPanel = new MyOrderJPanel(getThis(),myOrderManagement, customerPersonalManagement);
        // 设置表格风格为Windows风格
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setJFrame(userName);
        setFunctionBox();
        setButtonBoxTwo();
        setButtonBoxOne();
        setMenuBar();

        roomJPanel = new RoomJPanel(this);
        jf.add(roomJPanel, BorderLayout.CENTER);

        try {
            reserveGUI = new ReserveGUI(getThis(), customerPersonalManagement,myOrderManagement);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        addMyOrderBtnActionListener();
        addExitActionListener();
        addOrderBtnListener();
        addOkBtnListener();
        addPersonalInfoActionListener();
        addCancelBtnListener();
        addDeleteBtnActionListener();
        jf.setVisible(true);
    }

    // 给删除订单按钮添加事件监听器
    public void addDeleteBtnActionListener(){
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = myOrderJPanel.getTable().getSelectedRow();
                int orderID =  (int)myOrderJPanel.getTable().getValueAt(row, 0);
                try {
                    OrderManagement orderManagement = new OrderManagement();

                    // 弹出对话框，传入要显示的消息和标题
                    int result = JOptionPane.showConfirmDialog(null, "您确定要删除这个订单吗？", "删除确认", JOptionPane.YES_NO_OPTION);

                    // 判断用户的选择
                    if (result == JOptionPane.YES_OPTION) {

                        // 用户点击了是按钮，执行删除操作
                        orderManagement.deleteOrder(orderID);
                        jf.remove(myOrderJPanel);
                        myOrderManagement = new MyOrderManagement(customerPersonalManagement);
                        myOrderJPanel = new MyOrderJPanel(getThis(),myOrderManagement, customerPersonalManagement);
                        jf.add(myOrderJPanel, BorderLayout.CENTER);
                        jf.setVisible(true);
                    } else if (result == JOptionPane.NO_OPTION) {
                        // 用户点击了否按钮，不执行任何操作
                        return;
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    // 给查看房间详情按钮添加事件监听器，获取所选表格的信息传入
    public void addCheckRoomDetailActionListener(){
        roomJPanel.getCheckRoomDetailBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = roomJPanel.getJTable().getSelectedRow();
                String roomTypeName = (String) roomJPanel.getJTable().getValueAt(row, 0);
                int roomPrice = (int) roomJPanel.getJTable().getValueAt(row, 1);
                int maxNum = (int) roomJPanel.getJTable().getValueAt(row, 2);
                int remain =  (int)roomJPanel.getJTable().getValueAt(row, 3);
                try {
                    RoomDetailGUI roomDetailGUI = new RoomDetailGUI(getThis(),roomTypeName, roomPrice, maxNum, remain);
                    getJFrame().dispose();
                    roomDetailGUI.getJFrame().setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }



    // 查看个人信息事件监听器
    public void addPersonalInfoActionListener(){
        personalInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
                // 显示个人信息窗口
                customerPersonalInfoGUI.getJFrame().setVisible(true);
                jf.dispose();
            }
        });
    }

    // 退出登入事件监听器
    public void addExitActionListener(){
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //回退到登入页面
                hideCustomerGUIVisible();
                try {
                    logInGUI.getJFrame().setVisible(false);
                    logInGUI = null;
                    LogInGUI newLogIn = new LogInGUI();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // 选择房间预定界面按钮事件监听器
    public void addOrderBtnListener(){
        reserveFunctionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshRoomJPanel();
            }
        });
    }

    public void refreshRoomJPanel() {
        try {
            jf.remove(btnBoxOne);
            jf.remove(btnBoxTwo);
            setButtonBoxOne();
            jf.remove(roomJPanel);
            jf.remove(myOrderJPanel);
            roomJPanel = new RoomJPanel(this);
            addCancelBtnListener();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        jf.add(roomJPanel, BorderLayout.CENTER);
        jf.setVisible(true);
    }

    // 我的订单按钮事件监听器
    public void addMyOrderBtnActionListener(){
        myOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshMyOrderJPanel();
            }
        });
    }

    public void refreshMyOrderJPanel() {
        jf.remove(btnBoxOne);
        jf.remove(btnBoxTwo);
        setButtonBoxTwo();
        jf.remove(roomJPanel);
        jf.remove(myOrderJPanel);
        try {
            myOrderManagement = new MyOrderManagement(customerPersonalManagement);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        myOrderJPanel = new MyOrderJPanel(getThis(),myOrderManagement, customerPersonalManagement);
        addDeleteBtnActionListener();
//                myOrderJPanel.addCancelBtnActionListener();
        jf.add(myOrderJPanel, BorderLayout.CENTER);
        jf.setVisible(true);
    }

    // 确认预定按钮事件监听器
    public void addOkBtnListener(){
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String room = getSelectedRoom();
                reserveGUI.setShoppingRoom(room);
                try {
                    reserveGUI.resetInputJPanel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                reserveGUI.getReserveJFrame().setVisible(true);

                jf.dispose();
            }
        });
    }

    //给取消按钮添加事件监听器，点击取消按钮，房间表格取消选择
    public void addCancelBtnListener(){
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomJPanel.getJTable().clearSelection();
            }
        });
    }

    public String getSelectedRoom(){
        int row = this.roomJPanel.getJTable().getSelectedRow();
        return (String) this.roomJPanel.getJTable().getValueAt(row, 0);
    }



    public CustomerFirstGUI getThis(){
        return this;
    }
    public ReserveGUI getReserveGUI() {
        return reserveGUI;
    }

    public String getUserName() {
        return userName;
    }


    public void setCustomerGUIVisible(){
        jf.setVisible(true);
    }


    public void hideCustomerGUIVisible(){
        jf.dispose();
    }

    public JFrame getJFrame() {
        return jf;
    }

    public void setMenuBar(){
        menuBar = new JMenuBar();
        setting= new JMenu("设置");
        exit = new JMenuItem("退出系统");
        personalInfo = new JMenuItem("查看个人信息");
        setting.add(exit);
        setting.add(personalInfo);
        menuBar.add(setting);

        Font font = new Font("宋体", Font.BOLD, 16);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        setting.setFont(font);
        setting.setForeground(Color.BLACK);
        setting.setBackground(Color.WHITE);

        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        exit.setFont(font);
        exit.setForeground(Color.BLACK);
        exit.setBackground(Color.WHITE);

        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        personalInfo.setFont(font);
        personalInfo.setForeground(Color.BLACK);
        personalInfo.setBackground(Color.WHITE);

        jf.add(menuBar, BorderLayout.NORTH);
    }



    private void setButtonBoxOne() {
        btnBoxOne = Box.createHorizontalBox();
        okBtn = new JButton("预定");
        cancelBtn = new JButton("取消");
        btnBoxOne.add(Box.createHorizontalGlue());
        btnBoxOne.add(okBtn);
        btnBoxOne.add(Box.createHorizontalGlue());
        btnBoxOne.add(cancelBtn);
        btnBoxOne.add(Box.createHorizontalGlue());
        jf.add(btnBoxOne, BorderLayout.SOUTH);

        Font font = new Font("宋体", Font.BOLD, 16);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        okBtn.setFont(font);
        okBtn.setForeground(Color.WHITE);
        okBtn.setBackground(Color.GRAY);

        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        cancelBtn.setFont(font);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(Color.GRAY);

        addOkBtnListener();
    }

    private void setButtonBoxTwo() {
        btnBoxTwo = Box.createHorizontalBox();
        updateBtn = new JButton("更新");
        deleteBtn = new JButton("删除");
        btnBoxTwo.add(Box.createHorizontalGlue());
        btnBoxTwo.add(updateBtn);
        btnBoxTwo.add(Box.createHorizontalGlue());
        btnBoxTwo.add(deleteBtn);
        btnBoxTwo.add(Box.createHorizontalGlue());

        Font font = new Font("宋体", Font.BOLD, 16);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        updateBtn.setFont(font);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(Color.GRAY);
//        updateBtn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 10, true));

        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        deleteBtn.setFont(font);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(Color.GRAY);
//        deleteBtn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 10, true));

        jf.add(btnBoxTwo, BorderLayout.SOUTH);
    }



    private void setFunctionBox() {
        Box functionBox = Box.createVerticalBox();
        // 修改：创建两个功能按钮
        reserveFunctionBtn = new JButton("房间预定");
        myOrderBtn = new JButton("我的订单");
        // 修改：将两个功能按钮添加到menuBox中，并添加垂直间距
        functionBox.add(reserveFunctionBtn);
        functionBox.add(Box.createVerticalStrut(5));
        functionBox.add(myOrderBtn);
        functionBox.setSize(30,80);
        jf.add(functionBox, BorderLayout.WEST);

        Font font = new Font("微软雅黑", Font.BOLD, 16);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        reserveFunctionBtn.setFont(font);
        reserveFunctionBtn.setForeground(Color.BLUE);
        reserveFunctionBtn.setBackground(Color.WHITE);
//        reserveFunctionBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));

        myOrderBtn.setFont(font);
        myOrderBtn.setForeground(Color.BLUE);
        myOrderBtn.setBackground(Color.WHITE);
//        myOrderBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
    }

    private void setJFrame(String userName) throws IOException {
        jf = new JFrame("您好，" + userName + "。欢迎来到酒店小2管理系统！");
        // 获取屏幕宽度和高度
        this.userName = userName;
        bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        jf.setBounds((screenWidth - WIDTH)/2, (screenHeight - HEIGHT)/2, WIDTH, HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(bgPanel);
    }

    public MyOrderJPanel getMyOrderJPanel() {
        return myOrderJPanel;
    }
}

package common;

import adiminManagement.AdminManagement;
import customer.CustomerFirstGUI;
import customerManagement.CustomerManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInGUI {
    // 窗口
    JFrame jf;
    // 用户名和密码的标签
    JLabel userLabel;
    JLabel passwordLabel;
    // 用户名和密码的文本框
    JTextField user;
    JTextField password;
    // 用户类型
    ButtonGroup userType;
    JRadioButton userCheck;
    JRadioButton adminCheck;
    // 窗口的宽和高
    final int WIDTH = 500;
    final int HEIGHT = 400;
    // 登入和注册按钮
    JButton logBtn;
    JButton registerBtn;
    CustomerFirstGUI customerFirstGUI;
    private String userName;
    CustomerManagement customerManagement;

    public CustomerFirstGUI getCustomerFirstGUI() {
        return customerFirstGUI;
    }

    public String getUserName() {
        return userName;
    }

    //设置字体
    public void setFont(){
        Font font = new Font("微软黑体", Font.BOLD, 16);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        logBtn.setFont(font);
        logBtn.setForeground(Color.BLACK);
        logBtn.setBackground(Color.YELLOW);

        registerBtn.setFont(font);
        registerBtn.setForeground(Color.BLACK);
        registerBtn.setBackground(Color.YELLOW);

        // 给用户名和密码标签设置字体格式
        userLabel.setFont(font);
        userLabel.setForeground(Color.YELLOW);
        passwordLabel.setFont(font);
        passwordLabel.setForeground(Color.YELLOW);

        //给单选框设置字体格式
        userCheck.setFont(font);
        userCheck.setForeground(Color.BLACK);
        userCheck.setBackground(Color.WHITE);

        adminCheck.setFont(font);
        adminCheck.setForeground(Color.BLACK);
        adminCheck.setBackground(Color.WHITE);
    }


    public void init() throws Exception {
        jf = new JFrame("欢迎来到9店小2管理系统");
        // 设置窗体的位置和大小在屏幕正中间
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        // 登入背景
        BackGroundPanel bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("logoIn.jpg"))));

        // 组装登入界面
        Box vBox = Box.createVerticalBox();

        Box uBox = Box.createHorizontalBox();
        // 创建用户名和密码的标签
        userLabel = new JLabel("用户名:");
        passwordLabel = new JLabel("密    码:");
        // 创建用户名和密码的文本框
        user = new JTextField(18);
        password = new JTextField(18);

        //组装用户名标签和用户名输入框
        uBox.add(userLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(user);
        //组装密码标签和密码输入框
        Box pBox = Box.createHorizontalBox();
        pBox.add(passwordLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(password);
        //组装用户类型
        Box userTypeBox = Box.createHorizontalBox();
        userType    = new ButtonGroup();
        userCheck  = new JRadioButton("用户", true);
        adminCheck = new JRadioButton("管理员", false);
        userTypeBox.add(userCheck);
        userTypeBox.add(Box.createHorizontalStrut(20));
        userTypeBox.add(adminCheck);
        userType.add(userCheck); // 将用户复选框添加到按钮组中
        userType.add(adminCheck); // 将管理员复选框添加到按钮组中
        userCheck.setActionCommand("用户"); // 设置用户复选框的动作命令
        adminCheck.setActionCommand("管理员"); // 设置管理员复选框的动作命令

        //组装登入与注册按钮
        Box bBox = Box.createHorizontalBox();
        logBtn = new JButton("登入");
        registerBtn = new JButton("注册");

        setFont();

        bBox.add(logBtn);
        bBox.add(Box.createHorizontalStrut(100));
        bBox.add(registerBtn);
        //组装整体
        vBox.add(Box.createVerticalStrut(60));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(userTypeBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(bBox);

        bgPanel.add(vBox);
        jf.add(bgPanel);
        addLogBtnListener();
        jf.setVisible(true);
        System.out.println("System.currentTimeMillis():" +System.currentTimeMillis());
    }

    public LogInGUI() throws Exception {
        // 设置风格为默认格式
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        init();
        addRegisterBtnListener();
    }


    // 为登入按钮添加事件监听器
    public void addLogBtnListener() throws SQLException {
        logBtn.addActionListener(new ActionListener() { // 添加按钮的事件监听器
            public void actionPerformed(ActionEvent e) {
                login(); // 调用登入方法
            }
        });
        // 为用户，密码，和用户类型添加键盘监听器
        user.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() ==
                        KeyEvent.VK_ENTER) { // 如果按下回车键
                    login(); // 调用登入方法
                }
            }
        });
        password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // 如果按下回车键
                    login(); // 调用登入方法
                }
            }
        });

        /*userType.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // 如果按下回车键
                    login(); // 调用登入方法
                }
            }
        });*/

    }
    // 定义一个登入方法，包含原来的登入逻辑
    public void login() {
        // 获取用户输入的信息
        String userName = user.getText();
        String userPassword = password.getText();
        String userRole = userType.getSelection().getActionCommand();
        ResultSet rs;
        switch(userRole){
            case "用户":
                System.out.println("用户登入");
                customerManagement = new CustomerManagement();
                try {
                    rs = customerManagement.checkCustomer(userName, userPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                customerManagement.judgeCustomer(rs,getLogInGUI());
                break;
            case "管理员":
                System.out.println("管理员登入");
                AdminManagement adminManagement = new AdminManagement();
                try {
                    rs = adminManagement.checkAdmin(userName, userPassword);
                    adminManagement.judgeAdmin(rs, getLogInGUI());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            default:
                break;
        }
    }



    //为注册按钮添加事件监听器
    public void addRegisterBtnListener() {
        registerBtn.addActionListener(new ActionListener(){ // 添加按钮的事件监听器
            public void actionPerformed(ActionEvent e) {
                try {
                    RegisterGUI registerGUI = new RegisterGUI();
                    registerGUI.getJFrame().setVisible(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void setJf(JFrame jf) {
        this.jf = jf;
    }

    public LogInGUI getLogInGUI(){
        return  this;
    }

    public JFrame getJFrame() {
        return this.jf;
    }

    public static void main(String[] args) throws Exception {
        new LogInGUI();
    }

}
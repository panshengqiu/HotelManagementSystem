package common;
import registerManagement.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class RegisterGUI {
    JFrame jf;
    // 姓名，性别，年龄，电话，身份证号，用户名,密码,的标签
    JLabel userLabel;
    JLabel passwordLabel;
    JLabel nameLabel;
    JLabel ageLabel;
    JLabel sexLabel;
    JLabel phoneLabel;
    JLabel idLabel;

    // 姓名，性别，年龄，电话，身份证号，用户名,密码,的文本框
    JTextField name;
    JTextField age;
    JTextField sex;
    JTextField phone;
    JTextField id;
    JTextField user;
    JTextField password;

    // 窗口的宽和高
    final int WIDTH = 1500;
    final int HEIGHT = 1000;
    // 注册按钮
    JButton registerBtn;
    public RegisterGUI() throws Exception {
        init();
    }

    public RegisterGUI(String name, String checkInCardId) throws Exception {
        init();
        // 设置姓名和身份证号文本框内容为传入的值
        this.name.setText(name);
        this.id.setText(checkInCardId);

    }


    public JFrame getJFrame() {
        return jf;
    }

    public void init() throws Exception {
        jf = new JFrame("欢迎来到9店小2管理系统");
        // 设置窗体的位置和大小在屏幕正中间
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        // 注册背景
        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));
        //组装注册界面
        Box vBox = Box.createVerticalBox();
        Box uBox = Box.createHorizontalBox();
        // 创建姓名，性别，年龄，电话，身份证号，用户名,密码,的标签
        nameLabel = new JLabel("姓名:");
        ageLabel = new JLabel("年龄:");
        sexLabel = new JLabel("性别:");
        phoneLabel = new JLabel("电话:");
        idLabel = new JLabel("身份证号:");
        userLabel = new JLabel("用户名:");
        passwordLabel = new JLabel("密码:");
        // 创建姓名，性别，年龄，电话，身份证号，用户名,密码,的文本框
        user = new JTextField(20);
        password = new JTextField(20);
        name = new JTextField(20);
        sex = new JTextField(20);
        age = new JTextField(20);
        phone = new JTextField(20);
        id = new JTextField(20);
        // 组装姓名，性别，年龄，电话，身份证号，用户名,密码,的标签和文本框
        uBox.add(nameLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(name);
        Box pBox = Box.createHorizontalBox();
        pBox.add(passwordLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(password);
        Box aBox = Box.createHorizontalBox();
        aBox.add(ageLabel);
        aBox.add(Box.createHorizontalStrut(20));
        aBox.add(age);
        Box sBox = Box.createHorizontalBox();
        sBox.add(sexLabel);
        sBox.add(Box.createHorizontalStrut(20));
        sBox.add(sex);
        uBox.add(userLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(user);
        Box phBox = Box.createHorizontalBox();
        phBox.add(phoneLabel);
        phBox.add(Box.createHorizontalStrut(20));
        phBox.add(phone);
        Box iBox = Box.createHorizontalBox();
        iBox.add(idLabel);
        iBox.add(Box.createHorizontalStrut(20));
        iBox.add(id);
        // 组装注册按钮
        Box bBox = Box.createHorizontalBox();
        registerBtn = new JButton("注册");
        bBox.add(registerBtn);
        // 组装整体
        vBox.add(Box.createVerticalStrut(60));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(aBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(sBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(phBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(iBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(bBox);
        bgPanel.add(vBox);
        jf.add(bgPanel);
        addRegisterBtnListener();
    }
    // 为登入按钮添加事件监听器
    public void addRegisterBtnListener() {
        registerBtn.addActionListener(new ActionListener(){ // 添加按钮的事件监听器
            public void actionPerformed(ActionEvent e) {
                // 获取用户输入的信息
                String userName = user.getText();
                String userPassword = password.getText();
                String userAge = age.getText();
                String userid = id.getText();
                String usersex = sex.getText();
                String userphone = phone.getText();
                String username = name.getText();
                // 判断用户输入的信息是否为空
                if (userName.equals("") || userPassword.equals("") || userAge.equals("") || userid.equals("") || usersex.equals("") || userphone.equals("") || username.equals("")) {
                    JOptionPane.showMessageDialog(jf, "输入的信息不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    // 创建一个RegisterManagement对象
                    RegisterManagement registerManagement = new RegisterManagement();
                    // 调用register方法来判断是否注册成功
                    try {
                        if(registerManagement.register(userName, userPassword, userAge, userid, usersex, userphone, username)){
                            JOptionPane.showMessageDialog(jf, "注册成功！", "提示", JOptionPane.WARNING_MESSAGE);
                            jf.dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(jf, "注册失败！已存在相同用户名！", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        RegisterGUI registerGUI = new RegisterGUI();
        registerGUI.init();
    }
}

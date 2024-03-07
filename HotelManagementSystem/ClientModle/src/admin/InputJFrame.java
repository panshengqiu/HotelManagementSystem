package admin;

import common.BackGroundPanel;
import common.PathUtils;
import common.RegisterGUI;
import common.ScreenUtils;
import reserveManagement.ReserveManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class InputJFrame{
    private JFrame jf;
    private JLabel nameLabel;
    private JLabel idCardLabel;
    private JTextField nameField;
    private JTextField idCardField;
    private Box vBox;
    private Box nameBox;
    private Box idCardBox;
    private Box btnBox;
    private JButton okBtn;
    private JButton cancelBtn;

    // 添加两个变量name和cardId来获取文本框的内容
    private String name;
    private String cardId;
    private int userId;
    private JPanel panel;
    final int WIDTH = 500;
    final int HEIGHT = 300;

    public InputJFrame() throws IOException {
        jf = new JFrame("请输入姓名和身份证号");
        // 设置窗体的位置和大小在屏幕正中间
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        // 登入背景
        BackGroundPanel bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));


        // 初始化组件
        nameLabel = new JLabel("姓         名：");
        idCardLabel = new JLabel("身份证号：");
        nameField = new JTextField(18);
        idCardField = new JTextField(18);
        okBtn = new JButton("确定");
        cancelBtn = new JButton("取消");

        // 创建水平盒子和垂直盒子
        nameBox = Box.createHorizontalBox();
        idCardBox = Box.createHorizontalBox();
        btnBox = Box.createHorizontalBox();
        vBox = Box.createVerticalBox();

        // 将标签和文本框添加到水平盒子中
        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(10)); // 添加水平间距
        nameBox.add(nameField);

        idCardBox.add(idCardLabel);
        idCardBox.add(Box.createHorizontalStrut(10)); // 添加水平间距
        idCardBox.add(idCardField);

        // 将按钮添加到水平盒子中
        btnBox.add(Box.createHorizontalGlue()); // 添加水平胶水，使按钮靠右对齐
        btnBox.add(okBtn);
        btnBox.add(Box.createHorizontalGlue()); // 添加水平间距
        btnBox.add(cancelBtn);
        btnBox.add(Box.createHorizontalGlue()); // 添加水平间距


        // 将水平盒子添加到垂直盒子中
        vBox.add(Box.createVerticalStrut(30)); // 添加垂直间距
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(30)); // 添加垂直间距
        vBox.add(idCardBox);
        vBox.add(Box.createVerticalStrut(50)); // 添加垂直间距
        vBox.add(btnBox);

        // 将垂直盒子添加到窗口中
        bgPanel.add(vBox);

        jf.add(bgPanel);
        // 设置窗口可见和关闭操作
        jf.setVisible(false);
        addCancelBtnActionListener();
        addOkBtnActionListener();
        jf.setVisible(false);
    }
    public void addOkBtnActionListener(){
        okBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                name = nameField.getText();
                cardId = idCardField.getText();
                ReserveManagement reserveManagement = new ReserveManagement();
                try {
                    userId = reserveManagement.checkCustomer(name, cardId);
                    if(userId == -1) {
                        JOptionPane.showMessageDialog(null, "用户不存在，请注册", "提示", JOptionPane.INFORMATION_MESSAGE);
                        RegisterGUI registerGUI = new RegisterGUI();
                        registerGUI.getJFrame().setVisible(true);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                jf.dispose();
            }
        });
    }
    public int getUserId(){
        return userId;
    }

    public JTextField getNameField(){
        return nameField;
    }

    public JTextField getIdCardField(){
        return idCardField;
    }

    public JButton getOkBtn(){
        return okBtn;
    }

    public void addCancelBtnActionListener(){
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                jf.dispose();
            }
        });
    }


    public String getName(){
        return name;
    }

    public String getCardId(){
        return cardId;
    }

    public JFrame getInputJFrame(){
        return jf;
    }
}

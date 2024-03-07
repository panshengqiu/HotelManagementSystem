package common;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class InputJPanel{
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
    BackGroundPanel bgPanel;

    public InputJPanel() throws IOException {

        bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));

        // 初始化组件
        nameLabel = new JLabel("姓         名：", JLabel.CENTER);
        nameLabel.setForeground(Color.WHITE); // 设置字体颜色为白色
        idCardLabel = new JLabel("身份证号：", JLabel.CENTER);
        idCardLabel.setForeground(Color.WHITE); // 设置字体颜色为白色

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
        vBox.add(Box.createVerticalStrut(60)); // 添加垂直间距
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(60)); // 添加垂直间距
        vBox.add(idCardBox);
        vBox.add(Box.createVerticalStrut(60)); // 添加垂直间距
        vBox.add(btnBox);

        // 将垂直盒子添加到窗口中
        bgPanel.add(vBox);
//        this.add(bgPanel);
        addOkActionListener();
    }

    public  BackGroundPanel getBackGroundPanel(){
        return bgPanel;
    }

    public int getUserId(){
        return userId;
    }

    public void addOkActionListener(){
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText();
                cardId = idCardField.getText();
                JOptionPane.showMessageDialog(null, "入住的客户姓名是："+name+"\n，身份证号是："+cardId, "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
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
    public BackGroundPanel getBgPanel(){
        return bgPanel;
    }

    public String getName(){
        return name;
    }

    public String getCardId(){
        return cardId;
    }

}


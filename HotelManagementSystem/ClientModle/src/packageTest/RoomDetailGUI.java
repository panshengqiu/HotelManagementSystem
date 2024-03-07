package packageTest;

import admin.AdminFirstGUI;
import common.BackGroundPanel;
import common.PathUtils;
import common.ScreenUtils;
import customer.CustomerFirstGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RoomDetailGUI {
    JFrame jf;
    JPanel jPanel;
    JMenuBar menuBar;
    JMenu setting;
    JMenuItem exit;
    String roomTypeName;
    int roomPrice;
    int maxNum;
    int remain;
    BackGroundPanel bgPanel;
    CustomerFirstGUI customerFirstGUI;
    AdminFirstGUI adminFirstGUI;

    final int WIDTH = 1200;
    final int HEIGHT = 900;
    int signal = -1;
    public RoomDetailGUI(CustomerFirstGUI customerFirstGUI, String roomTypeName, int roomPrice, int maxNum, int remain) throws IOException {
        this.customerFirstGUI = customerFirstGUI;
        signal = 1;
        init(roomTypeName, roomPrice, maxNum, remain);
    }

    public RoomDetailGUI(AdminFirstGUI adminFirstGUI, String roomTypeName, int roomPrice, int maxNum, int remain) throws IOException {
        this.adminFirstGUI = adminFirstGUI;
        signal = 2;
        init(roomTypeName, roomPrice, maxNum, remain);
    }
    public void init(String roomTypeName, int roomPrice, int maxNum, int remain) throws IOException {
        this.roomPrice = roomPrice;
        this.roomTypeName = roomTypeName;
        this.remain = remain;
        this.maxNum = maxNum;


        bgPanel = new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))) );

        jf = new JFrame("房间详情");
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));

        menuBar = new JMenuBar();
        setting = new JMenu("设置");
        exit = new JMenuItem("退出");
        setting.add(exit);
        menuBar.add(setting);
        jf.add(menuBar, BorderLayout.NORTH);

        // 用一个switch语句来判断房间类型,按照类型展示不同图片，下方展示其他信息
        switch (roomTypeName){
            case "普通大床房":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("普通大床房.jpg"))));
                break;
            case "豪华大床房":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("豪华大床房.jpg"))));
                break;
            case "普通单间":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("普通单间.jpg"))));
                break;
            case "豪华单间":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("豪华单间.jpg"))));
                break;
            case "普通标间":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("普通标间.jpg"))));
                break;
            case "豪华标间":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("豪华标间.jpg"))));
                break;
            case "麻将房":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("麻将房.jpg"))));
                break;
            case "总统套房":
                bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("总统套房.jpg"))));
                break;
            default:
                jPanel = new JPanel();
                jPanel.add(new JLabel("未知房间类型"));
                break;
        }

        jf.add(bgPanel, BorderLayout.CENTER);

        // 创建一个字体对象，设置字体颜色为蓝色
        Font font = new Font("微软雅黑", Font.BOLD, 25);
        Color color = new Color(0, 0, 255);

        jPanel = new JPanel();

        // 使用垂直盒子布局
        Box vBox = Box.createVerticalBox();

        // 创建一个标签对象，设置文本内容、字体和颜色，并添加到盒子中
        JLabel label1 = new JLabel("房间类型:"+roomTypeName);
        label1.setFont(font);
        label1.setForeground(color);
        vBox.add(label1);

        // 添加一个水平间距
        vBox.add(Box.createHorizontalStrut(20));

        // 重复上述步骤，创建其他标签对象
        JLabel label2 = new JLabel("房间价格：" + roomPrice);
        label2.setFont(font);
        label2.setForeground(color);
        vBox.add(label2);

        vBox.add(Box.createHorizontalStrut(20));

        JLabel label3 = new JLabel("最大入住人数：" + maxNum);
        label3.setFont(font);
        label3.setForeground(color);
        vBox.add(label3);

        vBox.add(Box.createHorizontalStrut(20));

        JLabel label4 = new JLabel("当前剩余房间数量：" + remain);
        label4.setFont(font);
        label4.setForeground(color);
        vBox.add(label4);

        vBox.add(Box.createHorizontalStrut(20));

        // 将盒子添加到面板中
        jPanel.add(vBox);

        // 将面板添加到窗体的南边
        jf.add(jPanel, BorderLayout.SOUTH);

        jf.setVisible(true);
        addExitBtnListener();
    }

    // 给退出按钮添加事件监听器
    public void addExitBtnListener() {
        exit.addActionListener(e -> {
            // 隐藏当前窗口
            jf.dispose();
            // 显示登入窗口
            if(signal == 1)
                customerFirstGUI.setCustomerGUIVisible();
            else
                adminFirstGUI.getJFrame().setVisible(true);
        });
    }

    public JFrame getJFrame(){
        return jf;
    }
}

package common;

import customer.CustomerFirstGUI;
import customer.RoomPriceGUI;
import roomManageMent.RoomManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class SelectOneDayGUI {
    JFrame jf;
    JButton ok;
    JButton cancel;
    SingleJPanel calendarJPanel;

    Date date;
    String roomTypeName;
    int roomPrice;
    CustomerFirstGUI customerFirstGUI;

    final int WIDTH = 500;
    final int HEIGHT = 400;
    private int remain;

    public SelectOneDayGUI(CustomerFirstGUI customerFirstGUI) throws IOException {
        this.customerFirstGUI = customerFirstGUI;
        init();
        addOkActionListener();
        addCancelActionListener();
    }



    public void init() throws IOException {
        jf = new JFrame("请选择要查的房间日期！");
        // 设置窗体的位置和大小在屏幕正中间
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));

        calendarJPanel = new SingleJPanel();
        ok = new JButton("确认查询!");
        cancel = new JButton("取消查询");

        // 设置date系统默认时间
        date = new Date(System.currentTimeMillis());

        date = calendarJPanel.getSelectedDate();
        System.out.println("SelectOneDayGUI页面的init()方法中date = " + date);

        Box btnBox = Box.createHorizontalBox();
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(ok);
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(cancel);
        btnBox.add(Box.createHorizontalGlue());

        jf.add(btnBox, BorderLayout.SOUTH);
        jf.add(calendarJPanel, BorderLayout.CENTER);
    }

    public void addOkActionListener(){
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoomPriceGUI roomPriceGUI = null;
                try {
                    date = calendarJPanel.getSelectedDate();
                    RoomManagement roomManagement = new RoomManagement();
                    remain = roomManagement.toDayRemainRoomNum(date, roomTypeName);
                    roomPrice = roomManagement.getOneDayRoomPrice(date, roomTypeName);
                    roomPriceGUI = new RoomPriceGUI(date, roomTypeName, roomPrice, remain);
                    roomPriceGUI.getJFrame().setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                roomPriceGUI.getJFrame().setVisible(true);
            }
        });
    }

    public void addCancelActionListener(){
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getJFrame().setVisible(false);
            }
        });
    }

    // 获取选中的日期
    public Date getDate(){
        System.out.println("SelectedOneDayGUI页面的date = " + date);
        return date;
    }

    public JFrame getJFrame(){
        return jf;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRemainRoomNum(int remain) {
        this.remain = remain;
    }
}

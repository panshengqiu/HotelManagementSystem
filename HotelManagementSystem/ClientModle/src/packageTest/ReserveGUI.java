package packageTest;

import common.BackGroundPanel;
import common.PathUtils;
import customer.CalendarJPanel;
import customer.CustomerFirstGUI;
import orderManagement.MyOrderManagement;
import orderManagement.OrderDetailManagement;
import orderManagement.OrderManagement;
import personalManagement.CustomerPersonalManagement;
import reserveManagement.ReserveManagement;
import common.InputJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class ReserveGUI {
    JFrame jf;
    JButton okBtn;
    JButton cancelBtn;
    CalendarJPanel calendarJPanel;
    final static int  WIDTH = 1000;
    final static int HEIGHT = 700;
    CustomerFirstGUI customerFirstGUI;
    private BackGroundPanel bgPanel;
    private String reserveRoom;
    Date checkInDate;
    Date checkOutDate;
    CustomerPersonalManagement customerPersonalManagement;
    MyOrderManagement myOrderManagement;
    InputJPanel inputJPanel;



    public ReserveGUI(CustomerFirstGUI customerFirstGUI, CustomerPersonalManagement customerPersonalManagement, MyOrderManagement myOrderManagement) throws SQLException, IOException {
        this.customerFirstGUI = customerFirstGUI;
        this.customerPersonalManagement = customerPersonalManagement;
        this.myOrderManagement = myOrderManagement;
        init();
        jf.setVisible(false);
    }

    public void init() throws IOException {
        setJFrame();
        okBtn = new JButton("确定预定");
        cancelBtn = new JButton("取消预定");
        Box btnBox = Box.createHorizontalBox();
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(okBtn);
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(cancelBtn);
        btnBox.add(Box.createHorizontalGlue());
        jf.add(btnBox, BorderLayout.SOUTH);
        calendarJPanel = new CalendarJPanel();
        jf.addWindowListener(calendarJPanel);
        jf.add(calendarJPanel, BorderLayout.CENTER);
        inputJPanel = new InputJPanel();
        jf.add(inputJPanel.getBackGroundPanel(), BorderLayout.EAST);
        addCancelBtnListener();
        addOkBtnListener();
    }

    public void resetInputJPanel() throws IOException {
        jf.remove(inputJPanel.getBackGroundPanel());
        inputJPanel = new InputJPanel();
        jf.add(inputJPanel.getBackGroundPanel(), BorderLayout.EAST);
    }

    // 给预定按钮添加监听器
    public void addOkBtnListener() {
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(calendarJPanel.getFirstSelectedDay().equals("") || calendarJPanel.getSecondSelectedDay().equals("") ){
                    JOptionPane.showMessageDialog(jf, "请选择两个日期！", "提示", JOptionPane.WARNING_MESSAGE);
                } else{
                    // 获取选中的日期
                    Date firstSelectedDate = calendarJPanel.getFirstSelectedDate();
                    Date secondSelectedDate = calendarJPanel.getSecondSelectedDate();
                    // 获取系统当前时间
                    Date today = new Date(System.currentTimeMillis());
                    // 将当前系统时间减少一天
                    today.setTime(today.getTime() - 24*60*60*1000);
                    System.out.println("today:"+today);
                    // 判断是否选中了两个日期
                    System.out.println("firstDate:"+firstSelectedDate);
                    System.out.println("secondDate:"+secondSelectedDate);
                    if (today.after(firstSelectedDate) ){ //如果选择的开始日期小于今天日期，提示用户重新选择
                        JOptionPane.showMessageDialog(jf, "入住日期有误，请重新选择！", "提示", JOptionPane.WARNING_MESSAGE);
                        calendarJPanel.refresh();
                    } else {
                        // 选中了两个日期，弹出提示框
                        JOptionPane.showMessageDialog(jf, "顾客"+customerFirstGUI.getUserName()+"您选择的"+getReserveRoom()+"预定日期是：" + firstSelectedDate + "和" + secondSelectedDate, "提示", JOptionPane.INFORMATION_MESSAGE);
                        calendarJPanel.refresh();
                        // 实现预定的逻辑
                        ReserveManagement reserveManagement = new ReserveManagement();
                        //先判断预定的日期是否有房
                        try {
                            boolean isRemainRoom = reserveManagement.isRemainRoom(firstSelectedDate, secondSelectedDate, getReserveRoom());
                            if(isRemainRoom == false){
                                JOptionPane.showMessageDialog(jf, "您选择的日期无房，请重新选择！", "提示", JOptionPane.WARNING_MESSAGE);
                                calendarJPanel.refresh();
                            }else {
                                // 有房，获取房间号
                                String roomNumber = reserveManagement.GetRandomRoomNumber(firstSelectedDate, secondSelectedDate, getReserveRoom());
                                if (roomNumber == null) {
                                    JOptionPane.showMessageDialog(jf, "房间号获取失败！", "提示", JOptionPane.WARNING_MESSAGE);
                                }else{
                                    // 获取房间价格
                                    int price = reserveManagement.getRoomPriceByRoomType(firstSelectedDate, secondSelectedDate, getReserveRoom());
                                    if (price == -1) {
                                        JOptionPane.showMessageDialog(jf, "房间价格获取失败！", "提示", JOptionPane.WARNING_MESSAGE);
                                    }

                                    String name = inputJPanel.getName();
                                    String idCard = inputJPanel.getCardId();
                                    //判断用户是否存在
                                    int userID = reserveManagement.checkCustomer(name, idCard);
                                    if (userID ==  -1) {
                                        System.out.println("用户不存在,请注册！");
                                        JOptionPane.showMessageDialog(jf, "用户不存在，请注册！", "提示", JOptionPane.WARNING_MESSAGE);
                                        calendarJPanel.refresh();
                                    }else {
                                        // 创建订单
                                        OrderManagement orderManagement = new OrderManagement();
                                        int orderID = orderManagement.addOrder(customerPersonalManagement);
                                        if (orderID == -1) {
                                            JOptionPane.showMessageDialog(jf, "订单创建失败！", "提示", JOptionPane.WARNING_MESSAGE);
                                        }else{
                                            OrderDetailManagement orderDetailManagement = new OrderDetailManagement();
                                            // 创建订单之前，打印入住日期和退房日期
                                            System.out.println("预定界面传入的入住日期为： "+ firstSelectedDate+",预定界面传入的退房日期为：" + secondSelectedDate);
                                            int orderDetailId = orderDetailManagement.addOrderDetail(orderID, roomNumber, userID, firstSelectedDate, secondSelectedDate, price);
                                            if (orderDetailId == -1) {
                                                JOptionPane.showMessageDialog(jf, "订单详情创建失败！", "提示", JOptionPane.WARNING_MESSAGE);
                                            }else{
                                                orderManagement.setOrderPrice(orderID, price);
                                                JOptionPane.showMessageDialog(jf, "订单创建成功！", "提示", JOptionPane.WARNING_MESSAGE);
                                                jf.dispose();
                                                customerFirstGUI.refreshRoomJPanel();
                                                customerFirstGUI.getJFrame().setVisible(true);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
    }

    // 给取消按钮添加监听器
    public void addCancelBtnListener() {
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 隐藏当前窗口
                jf.dispose();
                //刷新日历
                calendarJPanel.refresh();
                // 显示登入窗口
                customerFirstGUI.setCustomerGUIVisible();
            }
        });
    }

    public ReserveGUI getReserveGUI(){
        return this;
    }

    public JFrame getReserveJFrame(){
        return this.jf;
    }

    public String getReserveRoom() {
        return reserveRoom;
    }

    private void setJFrame() throws IOException {
        jf = new JFrame("选择预定日期");
        bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        // 获取屏幕宽度和高度
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        jf.setBounds((screenWidth - WIDTH)/2, (screenHeight - HEIGHT)/2, WIDTH, HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setShoppingRoom(String room) {
        this.reserveRoom = room;
    }
}

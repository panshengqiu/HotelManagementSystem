package customer;

import admin.AdminFirstGUI;
import checkManagement.CheckManagement;
import common.BackGroundPanel;
import customerManagement.CustomerManagement;
import orderManagement.MyOrderManagement;
import orderManagement.OrderDetailManagement;
import orderManagement.OrderManagement;
import common.PathUtils;
import personalManagement.AdminPersonalManagement;
import personalManagement.CustomerPersonalManagement;
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
import java.text.SimpleDateFormat;

public class OrderDetailGUI {
    JFrame jf;
    final int WIDTH = 1000;
    final int HEIGHT = 700;
    MyOrderDetailJPanle orderDetailJPanel;
    CustomerPersonalManagement customerPersonalManagement;
    AdminPersonalManagement adminPersonalManagement;
    MyOrderManagement myOrderManagement;
    CustomerManagement customerManagement;
    RoomManagement roomManagement;
    CustomerFirstGUI customerFirstGUI;
    AdminFirstGUI adminFirstGUI;

    JMenuBar menuBar;
    JMenu setting;
    JMenuItem back;
    JButton deleteBtn;
    JButton cancelBtn;


    public OrderDetailGUI(CustomerFirstGUI customerFirstGUI, CustomerPersonalManagement customerPersonalManagement, MyOrderManagement myOrderManagement) throws SQLException, IOException {
        this.customerPersonalManagement = customerPersonalManagement;
        this.myOrderManagement = myOrderManagement;
        this.customerFirstGUI = customerFirstGUI;

        customerManagement = new CustomerManagement();
        roomManagement = new RoomManagement();
        orderDetailJPanel = new MyOrderDetailJPanle();
        init();
    }

    public OrderDetailGUI(AdminFirstGUI adminFirstGUI, AdminPersonalManagement adminPersonalManagement) throws SQLException, IOException {
        this.adminPersonalManagement = adminPersonalManagement;
        this.myOrderManagement = myOrderManagement;
        this.adminFirstGUI = adminFirstGUI;

        customerManagement = new CustomerManagement();
        roomManagement = new RoomManagement();
        orderDetailJPanel = new MyOrderDetailJPanle();
        init();
    }

    public void init() throws SQLException, IOException {
        jf = new JFrame("您好，" + customerPersonalManagement.checkUserIDByUserName() + ",欢迎来到酒店小2管理系统！");
        // 获取屏幕宽度和高度
        BackGroundPanel bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        jf.setBounds((screenWidth - WIDTH)/2, (screenHeight - HEIGHT)/2, WIDTH, HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(bgPanel);

        // 设置菜单栏
        menuBar = new JMenuBar();
        setting = new JMenu("设置");
        back = new JMenuItem("返回");
        setting.add(back);
        menuBar.add(setting);
        jf.add(menuBar, BorderLayout.NORTH);

        // 设置删除按钮
        deleteBtn = new JButton("删除订单详情");
        cancelBtn = new JButton("取消订单");
        Box btnBox = Box.createHorizontalBox();
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(deleteBtn, BorderLayout.CENTER);
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(cancelBtn);
        btnBox.add(Box.createHorizontalGlue());
        jf.add(btnBox, BorderLayout.SOUTH);

        addBackListener();
        addDeleteBtnActionListener();
        addCancelBtnActionListener();
    }

    // 给cancelBtn按钮添加事件监听器
    public void addCancelBtnActionListener(){
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = orderDetailJPanel.getTable();
                // 获取当前时间的毫秒数
                long currentTimeMillis = System.currentTimeMillis();
                // 将毫秒数转换为Date类型
                Date currentDate = new Date(currentTimeMillis);
                // 创建一个SimpleDateFormat对象，指定日期和时间的格式
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                // 将Date对象格式化为字符串
                String formattedTime = dateFormat.format(currentDate);

                // 查询当前订单的入住日期和退房日期
                OrderDetailManagement orderDetailManagement = new OrderDetailManagement();
                int row  = table.getSelectedRow();
                Date checkInDate = (Date) table.getValueAt(row, 4);
                if(currentDate.after(checkInDate)){
                    JOptionPane.showMessageDialog(jf, "当前时间已经超过您的入住时间，无法取消！","提示", JOptionPane.WARNING_MESSAGE);
                }else{
                    int orderDetailId  = (int)table.getValueAt(row, 1);
                    System.out.println("orderDetail = " + orderDetailId);
                    CheckManagement checkManagement = new CheckManagement();
                    int orderId = (int) table.getValueAt(row, 1);
                    // 将当前的订单详情对应的订单状态改为取消
                    OrderManagement orderManagement = new OrderManagement();
                    try {
                        String status = orderManagement.getOrderStatus(orderId);
                        if(status.equals("取消")){
                            JOptionPane.showMessageDialog(jf, "您已经取消过订单，无法再次取消！！","提示", JOptionPane.WARNING_MESSAGE);
                        }else {
                            int checkId = checkManagement.getCheckInId(orderDetailId);
                            System.out.println("checkId = " + checkId);
                            if (checkId != -1) {
                                JOptionPane.showMessageDialog(jf, "您已经办理过入住，无法取消订单！", "提示", JOptionPane.WARNING_MESSAGE);
                            } else {
                                System.out.println("没有人入住！");

                                // 添加一个对话框，询问您是否确定取消订单
                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult = JOptionPane.showConfirmDialog (jf, "取消后本订单将会失效，您确定取消吗？", "提示", dialogButton);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    // 如果您选择是，那么执行取消的逻辑代码
                                    int isUpdate = orderManagement.setOrderStatus(orderId, "取消");
                                    if (isUpdate == 0) {
                                        System.out.println("订单状态未更新成功！");
                                        JOptionPane.showMessageDialog(jf, "订单未成功取消！", "提示", JOptionPane.WARNING_MESSAGE);
                                    } else {
                                        int isRefund = orderManagement.setOrderRefund(orderId);
                                        System.out.println("isRefund = " + isRefund);
                                        if(isRefund == 0){
                                            System.out.println("退款金额未更新成功！");
                                            JOptionPane.showMessageDialog(jf, "退款未成功！！", "提示", JOptionPane.WARNING_MESSAGE);
                                        }else {
                                            JOptionPane.showMessageDialog(jf, "订单已经取消，欢迎您继续预定！", "提示", JOptionPane.WARNING_MESSAGE);
                                            customerFirstGUI.refreshMyOrderJPanel();
                                        }
                                    }
                                } else {
                                    // 如果您选择否，那么不取消订单
                                    System.out.println("您没有取消订单！");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }
        });
    }


    public void addDeleteBtnActionListener(){
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 弹出提示框，确认则删除，取消则不删除

                    int row = orderDetailJPanel.getTable().getSelectedRow();
                    JTable table = orderDetailJPanel.getTable();
                    int orderDetailId = Integer.parseInt(table.getValueAt(row, 0).toString());
                    OrderManagement orderManagement = new OrderManagement();

                    // 弹出对话框，传入要显示的消息和标题
                    int result = JOptionPane.showConfirmDialog(null, "您确定要删除吗？", "删除确认", JOptionPane.YES_NO_OPTION);

                    // 判断用户的选择
                    if (result == JOptionPane.YES_OPTION) {
                        // 用户点击了是按钮，执行删除操作
                        orderManagement.deleteOrderDetail(orderDetailId);
                        JOptionPane.showMessageDialog(null, "删除成功！");
                        jf.dispose();
                        customerFirstGUI.getJFrame().setVisible(true);
                    } else if (result == JOptionPane.NO_OPTION) {
                        // 用户点击了否按钮，不执行任何操作
                        return;
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public void addBackListener(){
        back.addActionListener(e -> {
            jf.dispose();
            customerFirstGUI.getJFrame().setVisible(true);
        });
    }

    // 从主界面的我的订单点击订单详情进入订单详情页面
    public void fromMyOrderToOrderDetailGUI() throws SQLException {
        int row = customerFirstGUI.getMyOrderJPanel().getTable().getSelectedRow();
        JTable table = customerFirstGUI.getMyOrderJPanel().getTable();
        int orderId = Integer.parseInt(table.getValueAt(row, 0).toString());
        orderDetailJPanel = new MyOrderDetailJPanle(orderId, customerPersonalManagement, myOrderManagement);
        jf.add(orderDetailJPanel, BorderLayout.CENTER);
    }

    // 从预定界面点击预定进入订单详情页面
    public void fromReserveToOrderDetailGUI(int currentId) throws SQLException {
        orderDetailJPanel = new MyOrderDetailJPanle(currentId, customerPersonalManagement, myOrderManagement);
        jf.add(orderDetailJPanel);
    }

    public JFrame getJFrame(){
        return jf;
    }

    public MyOrderDetailJPanle getOrderDetailJPanel(){
        return orderDetailJPanel;
    }
}

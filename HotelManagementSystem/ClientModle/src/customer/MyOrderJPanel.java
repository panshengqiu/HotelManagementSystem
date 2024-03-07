package customer;

import checkManagement.CheckManagement;
import orderManagement.MyOrderManagement;
import orderManagement.OrderDetailManagement;
import orderManagement.OrderManagement;
import personalManagement.CustomerPersonalManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class MyOrderJPanel extends JPanel {
    JTable table;
    JButton checkOrderDetail;
    JButton cancelOrderBtn;
    JScrollPane tablePanel;
    Vector<Object> myOrderTitleV;
    Vector<Vector<Object>> myOrderDataV;
    DefaultTableModel model;
    MyOrderManagement myOrderManagement;
    CustomerPersonalManagement customerPersonalManagement;
    CustomerFirstGUI customerFirstGUI;

    public MyOrderJPanel(CustomerFirstGUI customerFirstGUI,MyOrderManagement myOrderManagement, CustomerPersonalManagement customerPersonalManagement){
        this.setLayout(new BorderLayout());
        this.myOrderManagement = myOrderManagement;
        this.customerPersonalManagement = customerPersonalManagement;
        this.customerFirstGUI = customerFirstGUI;

        class MyTableModel extends DefaultTableModel {
            public MyTableModel(Vector<Vector<Object>> dataV, Vector<Object> roomTitleV) {
                super(dataV, roomTitleV);
            }

            //重写isCellEditable方法
            @Override
            public boolean isCellEditable (int row, int column) {
                //all cells false
                return false;
            }
        }

        myOrderTitleV = new Vector<>();
        myOrderTitleV = myOrderManagement.getMyOrderTitleV();

        myOrderDataV = new Vector<>();
        myOrderDataV = myOrderManagement.getMyOrderDataV();


        // 自定义一个表格模型
        class MyDefaultTableModel extends DefaultTableModel {
            public MyDefaultTableModel(Vector<Vector<Object>> dataV, Vector<Object> orderTitleV) {
                super(dataV, orderTitleV);
            }

            //重写isCellEditable方法
            @Override
            public boolean isCellEditable (int row, int column) {
                //all cells false
                return false;
            }
        }
        model = new MyDefaultTableModel(myOrderDataV, myOrderTitleV);

        table = new JTable(model);
        tablePanel = new JScrollPane(table);
        this.add(tablePanel);

        //设置表头不允许重新排序
        table.getTableHeader().setReorderingAllowed(false);
        table.setEnabled(true);
        //设置表格允许选择单个单元格
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        // 设置表格只能选择一行，不能选择多行
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        //设置表格在视口中的首选大小为800x600
        table.setPreferredScrollableViewportSize(new Dimension(1000, 800));
        //设置滚动面板自动适应表格的大小
        tablePanel.setPreferredSize(new Dimension(table.getPreferredSize().width + 100, table.getPreferredSize().height + 100));

        checkOrderDetail = new JButton("查看订单详情");
        cancelOrderBtn = new JButton("取消订单");
        // 设置表格字体为粗体，颜色为黑色
        Font font = new Font("微软雅黑", 0, 16);
        Color color = new Color(0, 0, 0);

        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        checkOrderDetail.setFont(font);
        checkOrderDetail.setForeground(Color.BLUE);
        checkOrderDetail.setBackground(Color.WHITE);

        checkOrderDetail.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5, true));
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        cancelOrderBtn.setFont(font);
        cancelOrderBtn.setForeground(Color.BLUE);
        cancelOrderBtn.setBackground(Color.WHITE);

        cancelOrderBtn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5, true));

        Box vBox = Box.createVerticalBox();
        vBox.add(checkOrderDetail);
        vBox.add(Box.createVerticalStrut(5));
        vBox.add(cancelOrderBtn);
        vBox.setSize(50,50);
        this.add(vBox, BorderLayout.EAST);

        addCheckOrderDetailBtnListener();
        addCancelBtnActionListener();
    }


    // 查看订单详情按钮添加监听器
    public void addCheckOrderDetailBtnListener(){
        checkOrderDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取当前选择的订单ID，位于第row行0列
                int row = table.getSelectedRow();
                try {
                    OrderDetailGUI orderDetailGUI = new OrderDetailGUI(customerFirstGUI, customerPersonalManagement, myOrderManagement);
                    orderDetailGUI.fromMyOrderToOrderDetailGUI();
                    customerFirstGUI.getJFrame().setVisible(false);
                    orderDetailGUI.getJFrame().setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // 取消订单按钮添加监听器
    public void addCancelBtnActionListener(){
        cancelOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = getTable();
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
                int orderId = (int) table.getValueAt(row, 0);
                int orderDetailId  = -1;
                try {
                    orderDetailId = orderDetailManagement.getOrderDetailId(orderId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("orderDetail = " + orderDetailId);
                if(orderDetailId == -1){
                    // 添加一个对话框，询问您是否确定取消订单
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(customerFirstGUI.getJFrame(), "找不到您的订单详情，是否确认取消吗？取消后本订单将会失效", "提示", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        // 如果您选择是，那么执行取消的逻辑代码
                        OrderManagement orderManagement = new OrderManagement();
                        String status = null;
                        try {
                            status = orderManagement.getOrderStatus(orderId);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (status.equals("取消")) {
                            JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "您已经取消过订单，无法再次取消！！", "提示", JOptionPane.WARNING_MESSAGE);
                        }else {
                            int isUpdate = 0;
                            try {
                                isUpdate = orderManagement.setOrderStatus(orderId, "取消");
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (isUpdate == 0) {
                                System.out.println("订单状态未更新成功！");
                                JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "订单未成功取消！", "提示", JOptionPane.WARNING_MESSAGE);
                            } else {
                                try {
                                    int isRefund = orderManagement.setOrderRefund(orderId);
                                    System.out.println("MyOrderJPanel页面的isRefund = " + isRefund);
                                    if(isRefund == 0){
                                        JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "未成功退款！！", "提示", JOptionPane.WARNING_MESSAGE);
                                    }else {
                                        JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "订单已经取消，欢迎您继续预定！", "提示", JOptionPane.WARNING_MESSAGE);
                                        // 刷新页面，
                                        customerFirstGUI.refreshMyOrderJPanel();
                                    }
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }

                            }
                        }
                    } else {
                        // 如果您选择否，那么不取消订单
                        System.out.println("您没有取消订单！");
                    }
                }else {
                    Date checkInDate = null;
                    try {
                        checkInDate = orderDetailManagement.getCheckInDate(orderId);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (currentDate.after(checkInDate)) {
                        JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "当前时间已经超过您的入住时间，无法取消！", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        CheckManagement checkManagement = new CheckManagement();
                        // 将当前的订单详情对应的订单状态改为取消
                        OrderManagement orderManagement = new OrderManagement();
                        try {
                            String status = orderManagement.getOrderStatus(orderId);
                            if (status.equals("取消")) {
                                JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "您已经取消过订单，无法再次取消！！", "提示", JOptionPane.WARNING_MESSAGE);
                            } else {
                                int checkId = checkManagement.getCheckInId(orderDetailId);
                                System.out.println("checkId = " + checkId);
                                if (checkId != -1) {
                                    JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "您已经办理过入住，无法取消订单！", "提示", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    System.out.println("没有人入住！");

                                    // 添加一个对话框，询问您是否确定取消订单
                                    int dialogButton = JOptionPane.YES_NO_OPTION;
                                    int dialogResult = JOptionPane.showConfirmDialog(customerFirstGUI.getJFrame(), "取消后本订单将会失效，您确定取消吗？", "提示", dialogButton);
                                    if (dialogResult == JOptionPane.YES_OPTION) {
                                        // 如果您选择是，那么执行取消的逻辑代码
                                        int isUpdate = orderManagement.setOrderStatus(orderId, "取消");
                                        if (isUpdate == 0) {
                                            System.out.println("订单状态未更新成功！");
                                            JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "订单未成功取消！", "提示", JOptionPane.WARNING_MESSAGE);
                                        } else {
                                            int isRefund = orderManagement.setOrderRefund(orderId);
                                            System.out.println("MyOrderJPanel页面的isRefund = " + isRefund);
                                            if(isRefund == 0) {
                                                JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "未成功退款！！", "提示", JOptionPane.WARNING_MESSAGE);
                                            }else {
                                                JOptionPane.showMessageDialog(customerFirstGUI.getJFrame(), "订单已经取消，欢迎您继续预定！", "提示", JOptionPane.WARNING_MESSAGE);
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
            }
        });
    }
    public MyOrderJPanel() {
        super();
    }

    public JPanel getThisPanel(){
        return this;
    }

    public JTable getTable(){
        return table;
    }

}

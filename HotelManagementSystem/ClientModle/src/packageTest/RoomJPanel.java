package packageTest;

import admin.AdminFirstGUI;
import common.SelectOneDayGUI;
import customer.CustomerFirstGUI;
import entityClass.Room;
import roomManageMent.RoomManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Vector;

public class RoomJPanel extends JPanel {
    JTable table;
    JScrollPane tablePanel;
    Room room;
    JButton checkRoomDetail;
    JButton checkRoomPrice;
    JButton checkRemainRoom;
    JButton checkRemainRoomNum;
    Vector<Object> roomTitleV;
    Vector<Vector<Object>> dataV;
    DefaultTableModel model;
    AdminFirstGUI adminFirstGUI;
    CustomerFirstGUI customerFirstGUI;
    int signal = -1;


    public RoomJPanel(AdminFirstGUI adminFirstGUI) throws SQLException {
        this.adminFirstGUI = adminFirstGUI;
        signal = 2;
        init();
    }

    public RoomJPanel(CustomerFirstGUI customerFirstGUI) throws SQLException {
        this.customerFirstGUI = customerFirstGUI;
        signal = 1;
        init();
    }
    public void init() throws SQLException {
        this.setLayout(new BorderLayout());
        RoomManagement roomManagement = new RoomManagement();
        roomTitleV = roomManagement.getRoomTitleV();
        dataV = roomManagement.getRoomV(roomManagement.queryToDayRoomInfo());
        //创建一个自定义的数据模型类，继承自DefaultTableModel
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
        //使用自定义的数据模型来创建表格对象
        model = new MyTableModel(dataV, roomTitleV);
        table = new JTable(model);

        tablePanel = new JScrollPane(table);
        //设置表格可用
        table.setEnabled(true);
        //设置表格允许选择单个单元格
        table.setCellSelectionEnabled(false);
        //设置表头不允许重新排序
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        // 设置表格字体为粗体，颜色为黑色
        Font font = new Font("微软雅黑", 0, 16);
        Font titleFont = new Font("微软雅黑", 0, 20);
        Color color = new Color(0, 0, 0);

        table.setFont(font);
        table.setForeground(Color.BLACK);

        table.getTableHeader().setFont(titleFont);
        table.getTableHeader().setBackground(Color.GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        this.add(tablePanel, BorderLayout.CENTER);

        checkRoomDetail = new JButton("查看房间详情");
        checkRoomPrice = new JButton("房价和剩余房间");
        checkRemainRoom = new JButton("查空房");

        setButtonStyle(checkRoomDetail, font);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        setButtonStyle(checkRoomPrice, font);
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        setButtonStyle(checkRemainRoom, font);

        Box vBox = Box.createVerticalBox();

        // 设置盒子对齐方式为居中
        vBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        vBox.add(checkRoomDetail);
        vBox.add(checkRoomPrice);
        vBox.add(checkRemainRoom);

        vBox.setSize(50,50);

        this.add(vBox, BorderLayout.EAST);

        //设置表格在视口中的首选大小为800x600
        table.setPreferredScrollableViewportSize(new Dimension(1000, 800));
        addCheckRoomDetailActionListener();
        addCheckRoomPriceActionListener();

    }

    private void setButtonStyle(JButton checkRoomDetail, Font font) {
        // 设置按钮字体为粗体，颜色为白色，背景为蓝色，边框为圆角
        checkRoomDetail.setFont(font);
        checkRoomDetail.setForeground(Color.BLUE);
        checkRoomDetail.setBackground(Color.WHITE);
        checkRoomDetail.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5, true));
    }

    // 给查空房按钮添加事件监听器


    // 查某一天该类型房间房价
    public void addCheckRoomPriceActionListener(){
        checkRoomPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = getJTable().getSelectedRow();
                String roomTypeName = (String)getJTable().getValueAt(row, 0);
                SelectOneDayGUI selectOneDayGUI = null;
                try {
                    selectOneDayGUI = new SelectOneDayGUI(customerFirstGUI);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                selectOneDayGUI.getJFrame().setVisible(true);
                Date date = new Date(System.currentTimeMillis());
                System.out.println("roomJPanel的当前时间为：" + date);
                date = selectOneDayGUI.getDate();
                System.out.println("roomJPanel的选择时间为：" + date);
                if(date == null){
                    date = new Date(System.currentTimeMillis());
                }
                RoomManagement roomManagement = new RoomManagement();
                try {
                    int roomPrice = roomManagement.getOneDayRoomPrice(date, roomTypeName);
                    int remain = roomManagement.toDayRemainRoomNum(date, roomTypeName);
                    selectOneDayGUI.setRoomPrice(roomPrice);
                    selectOneDayGUI.setRoomTypeName(roomTypeName);
                    selectOneDayGUI.setDate(date);
                    selectOneDayGUI.setRemainRoomNum(remain);
                    // 当点击确认查询按钮时，new RoomPriceGUI对象
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }


    // 查看房间详情
    public void addCheckRoomDetailActionListener(){
        getCheckRoomDetailBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = getJTable().getSelectedRow();
                String roomTypeName = (String) getJTable().getValueAt(row, 0);
                int roomPrice = (int) getJTable().getValueAt(row, 1);
                int maxNum = (int) getJTable().getValueAt(row, 2);
                int remain =  (int)getJTable().getValueAt(row, 3);
                try {
                    if(signal == 2) {
                        RoomDetailGUI roomDetailGUI = new RoomDetailGUI(adminFirstGUI, roomTypeName, roomPrice, maxNum, remain);
                        adminFirstGUI.getJFrame().dispose();
                        roomDetailGUI.getJFrame().setVisible(true);
                    }
                    else if(signal == 1){
                        RoomDetailGUI roomDetailGUI = new RoomDetailGUI(customerFirstGUI, roomTypeName, roomPrice, maxNum, remain);
                        customerFirstGUI.getJFrame().dispose();
                        roomDetailGUI.getJFrame().setVisible(true);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public JTable getJTable(){
        return table;
    }

    public JButton getCheckRoomDetail(){
        return this.checkRoomDetail;
    }


    public JButton getCheckRoomDetailBtn() {
        return checkRoomDetail;
    }
}

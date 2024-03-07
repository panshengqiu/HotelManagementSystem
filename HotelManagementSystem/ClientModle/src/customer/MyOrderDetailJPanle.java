package customer;

import orderManagement.MyOrderManagement;
import personalManagement.CustomerPersonalManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

public class MyOrderDetailJPanle extends JPanel{
    JTable table;
    Vector<Object> shoppingTitleV;
    Vector<Vector<Object>> dataV;
    DefaultTableModel model;
    JScrollPane tablePanel;
    JButton deleteBtn;
    MyOrderManagement myOrderManagement;
    CustomerPersonalManagement customerPersonalManagement;
    int orderId;
    public MyOrderDetailJPanle(){
        super();
    }

    public MyOrderDetailJPanle(int orderId, CustomerPersonalManagement customerPersonalManagement, MyOrderManagement myOrderManagement) throws SQLException {
        this.setLayout(new BorderLayout());
        this.customerPersonalManagement = customerPersonalManagement;
        this.myOrderManagement = myOrderManagement;
        this.orderId = orderId;



        dataV = new Vector<>();
        shoppingTitleV = new Vector<>();
        shoppingTitleV = myOrderManagement.getMyOrderDetailTitleV();
        myOrderManagement.setMyOrderDetailDataV(this.orderId);
        dataV = myOrderManagement.getMyOrderDetailDataV();

        class MyTableModel extends DefaultTableModel {
            public MyTableModel(Vector<Vector<Object>> dataV, Vector<Object> shoppingTitleV) {
                super(dataV, shoppingTitleV);
            }

            //重写isCellEditable方法
            @Override
            public boolean isCellEditable (int row, int column) {
                //all cells false
                return false;
            }
        }
        model = new MyTableModel(dataV, shoppingTitleV);
        table = new JTable(model);
        tablePanel = new JScrollPane(table);
        this.add(tablePanel, BorderLayout.CENTER);

        /*Box hBox = Box.createHorizontalBox();
        insureBtn = new JButton("确认订单");
        deleteOneOrderDetailBtn = new JButton("删除订单");
        continueReserveBtn = new JButton("继续预定");

        hBox.add(Box.createHorizontalGlue());
        hBox.add(insureBtn);
        hBox.add(Box.createHorizontalGlue());
        hBox.add(deleteOneOrderDetailBtn);
        hBox.add(Box.createHorizontalGlue());
        hBox.add(continueReserveBtn);
        hBox.add(Box.createHorizontalGlue());
        this.add(hBox, BorderLayout.SOUTH);*/


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
    }

    // 返回表格
    public JTable getTable(){
        return table;
    }
}

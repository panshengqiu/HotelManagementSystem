package common;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.Calendar;

public class SingleJPanel extends JPanel implements ActionListener, MouseListener, WindowListener {
    // 定义组件
    private JLabel yearLabel, monthLabel;
    private JTextField yearField, monthField;
    private JButton prevButton, nextButton, refreshButton;
    private JTable table;
    private JScrollPane scrollPane;
    private String[] columnNames = {"日", "一", "二", "三", "四", "五", "六"};
    private String[][] data = new String[12][7];
    private Calendar calendar = Calendar.getInstance();

    private int firstSelectedRow = -1; // 第一次选中的单元格行索引
    private int firstSelectedCol = -1; // 第一次选中的单元格列索引
    private String firstSelectedDay = "";
    // 定义颜色常量
    private static final Color SELECTED_COLOR = Color.GREEN; // 选中单元格的颜色
    private static final Color DEFAULT_COLOR = Color.WHITE; // 默认单元格的颜色
    Date queryDate;

    public SingleJPanel(){
        // 初始化组件
        yearLabel = new JLabel("年");
        monthLabel = new JLabel("月");
        yearField = new JTextField(4);
        monthField = new JTextField(2);
        prevButton = new JButton("上个月");
        nextButton = new JButton("下个月");
        refreshButton = new JButton("刷新");
        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);

        // 设置默认查询时间为今天
        queryDate = new Date(System.currentTimeMillis());

        // 设置布局
        this.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.add(prevButton);
        northPanel.add(yearLabel);
        northPanel.add(yearField);
        northPanel.add(monthLabel);
        northPanel.add(monthField);
        northPanel.add(nextButton);
        northPanel.add(refreshButton);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        // 设置表格样式
        table.setRowHeight(20);
        table.setEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);

        // 设置文本框内容
        yearField.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthField.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));

        // 添加事件监听器
        prevButton.addActionListener(this);
        nextButton.addActionListener(this);
        refreshButton.addActionListener(this);
        yearField.addActionListener(this);
        monthField.addActionListener(this);
        table.addMouseListener(this);
        windowClosed(null);
        refresh();

    }


    // 事件处理方法,给日历按钮添加事件监听器： 前一个月按钮、后一个月按钮、刷新按钮
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevButton) {
            // 前一个月
            calendar.add(Calendar.MONTH, -1);
            refresh();
        } else if (e.getSource() == nextButton) {
            // 后一个月
            calendar.add(Calendar.MONTH, 1);
            refresh();
        } else if (e.getSource() == refreshButton || e.getSource() == yearField || e.getSource() == monthField) {
            // 刷新
            try {
                int year = Integer.parseInt(yearField.getText());
                int month = Integer.parseInt(monthField.getText()) - 1;
                calendar.set(year, month, 1); // 设置年份和月份
                refresh();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的年份和月份！");
            }
        }
    }

    public void refresh() {
        // 设置文本框内容
        yearField.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        monthField.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        // 清空表格内容
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = "";
            }
        }
        // 获取当月第一天是星期几
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // 获取当月有多少天
        int daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 获取下个月有多少天
        calendar.add(Calendar.MONTH, 1); // 将日历设置为下个月
        int daysOfNextMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, -1); // 将日历恢复为当前月
        // 填充表格内容
        for (int i = 0; i < daysOfMonth + daysOfNextMonth; i++) {
            int row = (i + dayOfWeek) / 7;
            int col = (i + dayOfWeek) % 7;
            if (i < daysOfMonth) { // 如果是当月的日期
                data[row][col] = String.valueOf(i + 1);
                table.setValueAt(data[row][col], row, col);
                setSelectColor(DEFAULT_COLOR, row, col, data[row][col], true);
            } else { // 如果是下个月的日期
                data[row + 1][col] = String.valueOf(i + 1 - daysOfMonth);
                table.setValueAt(data[row + 1][col], row + 1, col);
                setSelectColor(DEFAULT_COLOR, row + 1, col, data[row + 1][col], false); // 使用不同的颜色来区分下个月的日期
            }
        }
    }

    private void setSelectColor(Color selectColor, int row, int col, Object value, boolean isSelected) {
        // 创建一个自定义渲染器
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // 根据需要设置单元格的背景颜色
                if (isSelected) {
                    component.setBackground(selectColor);
                } else {
                    // 如果没有选中，则使用默认颜色
                    component.setBackground(table.getBackground());
                }

                return component;
            }
        };

        // 将自定义渲染器应用于指定的单元格
        table.getColumnModel().getColumn(col).setCellRenderer(renderer);
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == table){
            // 点击表格
            refresh();
            System.out.println("点击表格");
            int row = table.rowAtPoint(e.getPoint()); // 获取点击的行索引
            int col = table.columnAtPoint(e.getPoint()); // 获取点击的列索引
            String value = (String) table.getValueAt(row, col); // 获取单元格的值
            if (value != null && !value.isEmpty()){
                table.setValueAt(value, row, col); // 设置单元格的值（防止被清空）
                setSelectColor(SELECTED_COLOR,row, col, value,true);
                firstSelectedRow = row;
                firstSelectedCol = col;
                firstSelectedDay= value;

            }else {
                JOptionPane.showMessageDialog(null, "选择的日期无效，请重新选择！","提示",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public Date getSelectedDate() {
        // 获取年文本框的年份
        int year = Integer.parseInt(yearField.getText())-1900;
        // 获取月文本框的月份
        int month;
        if(firstSelectedRow <=5 ){
            month = Integer.parseInt(monthField.getText()) - 1;
        }else{
            month = Integer.parseInt(monthField.getText());
        }
        if(firstSelectedDay.equals("")) {
            return null;
        }
        int day = Integer.parseInt(firstSelectedDay);

        if(firstSelectedDay.equals("") || firstSelectedRow == -1 || firstSelectedCol == -1){
            return queryDate;
        }
        queryDate = new Date(year, month, day);
        System.out.println("SingleJPanel页面的queryDate: " + queryDate);
        return queryDate;
    }

    private void setFirstSelect(int row, int col, String value) {
        firstSelectedRow = row; // 记录第一次选中的行索引
        firstSelectedCol = col; // 记录第一次选中的列索引
        firstSelectedDay = value; // 记录第一次选中的日期字符串
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {
        refresh();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        refresh();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        refresh();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        refresh();
    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {
        refresh();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

package customer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.Calendar;

public class CalendarJPanel extends JPanel implements ActionListener, MouseListener, WindowListener {
    // 定义组件
    private JLabel yearLabel, monthLabel;
    private JTextField yearField, monthField;
    private JButton prevButton, nextButton, refreshButton;
    private JTable table;
    private JScrollPane scrollPane;
    private String[] columnNames = {"日", "一", "二", "三", "四", "五", "六"};
    private String[][] data = new String[12][7];
    private Calendar calendar = Calendar.getInstance();

    // 定义颜色常量
    private static final Color SELECTED_COLOR = Color.GREEN; // 选中单元格的颜色
    private static final Color DEFAULT_COLOR = Color.WHITE; // 默认单元格的颜色

    // 定义变量
    private int selectedCount = 0; // 当前选中的日期个数
    private int firstSelectedRow = -1; // 第一次选中的单元格行索引
    private int firstSelectedCol = -1; // 第一次选中的单元格列索引
    private int secondSelectedRow = -1; // 第二次选中的单元格行索引
    private int secondSelectedCol = -1; // 第二次选中的单元格列索引
    private String firstSelectedDay = ""; // 第一次选中的日期
    private String secondSelectedDay = ""; // 第二次选中的日期

    // 构造方法
    public CalendarJPanel() {
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
        table.setEnabled(false);
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

    // 获取选择的第一个日期
    public Date getFirstSelectedDate() {
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
        return new Date(year, month, day);
    }

    // 获取选择的第二个日期
    public Date getSecondSelectedDate() {
        // 获取年文本框的年份
        int year = Integer.parseInt(yearField.getText())-1900;
        // 获取月文本框的月份
        int month;
        if(secondSelectedRow <=5 ){
            month = Integer.parseInt(monthField.getText()) - 1;
        }else{
            month = Integer.parseInt(monthField.getText());
        }
        if(secondSelectedDay.equals("")) {
            return null;
        }
        int day = Integer.parseInt(secondSelectedDay);
        return new Date(year, month, day);
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

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table) {
            // 点击表格
            int row = table.rowAtPoint(e.getPoint()); // 获取点击的行索引
            int col = table.columnAtPoint(e.getPoint()); // 获取点击的列索引
            String value = (String) table.getValueAt(row, col); // 获取单元格的值
            if (value != null && !value.isEmpty()) { // 如果单元格有有效的日期
                if (selectedCount < 2) { // 如果当前选中的日期个数小于2
                    selectedCount++; // 选中个数加1
                    table.setValueAt(value, row, col); // 设置单元格的值（防止被清空）
                    if (selectedCount == 1) { // 如果是第一次选中
                            setFirstSelect(row, col, value);// 记录第一次选中的日期
                            setSelectColor(SELECTED_COLOR, row, col, value, true);
                    }else if(selectedCount==2) { // 如果是第二次选中
                        // 如果第二次选中的日期行数大于第一个月，记录第二次选中的行数，列数，值
                        if(row > firstSelectedRow){
                            setSelectColor(SELECTED_COLOR, row, col, value, true);// 设置第二次选中的单元格的背景色为选中颜色
                            setSecondSelect(row, col, value);// 记录第二次选中的日期
                            selectedCount++;
                        }else if(row == firstSelectedRow){//如果两次选中的行列值相等，判断列
                            if(col > firstSelectedCol){ //列在第一次选中之后
                                setSelectColor(SELECTED_COLOR, row, col, value, true);// 设置第二次选中的单元格的背景色为选中颜色
                                setSecondSelect(row, col, value);// 记录第二次选中的日期
                                selectedCount++;
                            }else{ // 否则重置选择
                                setSelectColor(DEFAULT_COLOR, firstSelectedRow, firstSelectedCol, value, true); // 将第一次选中的单元格的背景色恢复为默认颜色
                                resetSelect();
                                refresh();
                            }
                        } else {
                            refresh();
                        }
                    }
                }else{
                    refresh();
                }
            }else refresh();
        }
        System.out.println("firstDate:"+firstSelectedDay);
        System.out.println("secondDate:"+secondSelectedDay);
    }

    // 获取文本框的年、和月、日
    public int getYear() {
        return Integer.parseInt(yearField.getText());
    }

    public int getMonth() {
        return Integer.parseInt(monthField.getText());
    }


    private void setSecondSelect(int row, int col, String value) {
        secondSelectedRow = row; // 记录第二次选中的行索引
        secondSelectedCol = col; // 记录第二次选中的列索引
        secondSelectedDay = value; // 记录第二次选中的日期字符串
    }

    private void setFirstSelect(int row, int col, String value) {
        firstSelectedRow = row; // 记录第一次选中的行索引
        firstSelectedCol = col; // 记录第一次选中的列索引
        firstSelectedDay = value; // 记录第一次选中的日期字符串
    }


    // 刷新表格内容
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
        resetSelect();
    }

    private void resetSelect() {
        setFirstSelect(-1, -1, "");
        setSecondSelect(-1, -1, "");
        this.selectedCount = 0; // 选中个数设置为0
    }

    private void setSelectColor(Color selectColor, int row, int col,Object value, boolean isSelected) {
        // 获取指定行和列的单元格的渲染器对象
        TableCellRenderer renderer = table.getCellRenderer(row, col);
        // 返回一个用于显示单元格内容的组件
        JComponent component = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, false, row, col);
        // 设置单元格的背景色为选中颜色
        component.setBackground(selectColor);
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

    public String getFirstSelectedDay() {
        return firstSelectedDay;
    }

    public String getSecondSelectedDay() {
        return secondSelectedDay;
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
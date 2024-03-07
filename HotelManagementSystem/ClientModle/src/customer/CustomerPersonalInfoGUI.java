package customer;

import common.BackGroundPanel;
import common.PathUtils;
import common.ScreenUtils;
import personalManagement.CustomerPersonalManagement;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

public class CustomerPersonalInfoGUI {
    private CustomerFirstGUI customerFirstGUI;
    private CustomerPersonalManagement customerPersonalManagement;
    private JFrame jf;

    JPanel infoPanel; // 用于显示个人信息的面板
    JLabel photoLabel; // 用于显示头像的标签
    JLabel nameLabel; // 用于显示姓名的标签
    JLabel sexLabel; // 用于显示性别的标签
    JLabel ageLabel; // 用于显示年龄的标签
    JLabel phoneLabel; // 用于显示电话的标签
    JLabel userNameLabel; // 用于显示用户名的标签
    JButton changePhotoBtn; // 用于更换头像的按钮
    JTable table;
    JScrollPane tablePanel;
    Vector<Object> titleV;
    Vector<Object> dataV;
    DefaultTableModel model;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem back;

    final int WIDTH = 1200;
    final int HEIGHT = 900;
    JButton updateBtn;

    public CustomerPersonalInfoGUI(){

    }

    public void addBackActionListener(){
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                customerFirstGUI.getJFrame().setVisible(true);
            }
        });
    }

    public void addUpdateBtnActionListener(){
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) table.getValueAt(0, 1);
                String sex = (String) table.getValueAt(0, 2);
                // 获取table中的值
                Object value = table.getValueAt(0, 3);
                int age;
                if (value instanceof Integer) { // 如果是整型
                    age = (Integer) value; // 直接赋值给age
                } else if (value instanceof String) { // 如果是字符串
                    age = Integer.parseInt((String) value); // 转换成整形
                } else { // 如果是其他类型
                    age = Integer.parseInt(value.toString()); // 先转换为字符串，再转换为整形
                }
                String phone = (String) table.getValueAt(0, 4);
                String userName = (String) table.getValueAt(0, 6);
                String password = (String) table.getValueAt(0, 7);
                int signal;
                try {
                    signal = customerPersonalManagement.updateCustomerInfo(name, sex, age, phone, userName, password);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (signal == 1) {
                    JOptionPane.showMessageDialog(null, "更新成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                } else
                    JOptionPane.showMessageDialog(null, "更新失败", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public CustomerPersonalInfoGUI(CustomerFirstGUI customerFirstGUI, CustomerPersonalManagement customerPersonalManagement) throws IOException, SQLException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.customerFirstGUI = customerFirstGUI;
        this.customerPersonalManagement = customerPersonalManagement;
        init();
    }

    public void init() throws IOException, SQLException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        jf = new JFrame(customerFirstGUI.getUserName()+"的个人信息");
        jf.setVisible(false);
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        // 登入背景
        BackGroundPanel bgPanel =new BackGroundPanel(ImageIO.read(new File(PathUtils.getPath("back2.jpg"))));
        jf.add(bgPanel);

        menuBar = new JMenuBar();
        menu = new JMenu("设置");
        back = new JMenuItem("返回");
        menu.add(back);
        menuBar.add(menu);
        jf.add(menuBar, BorderLayout.NORTH);


        Object[] title = {"ID","姓名","性别","年龄","电话","身份证","用户名","密码"};
        titleV = customerPersonalManagement.getTitleV(title);
        dataV = customerPersonalManagement.getDataV();
        Vector<Vector<Object>> dataSV = new Vector<>();
        dataSV.add(dataV);
        class MyTableModel extends DefaultTableModel {
            public MyTableModel(Vector<Vector<Object>> dataV, Vector<Object> roomTitleV) {
                super(dataV, roomTitleV);
            }

            //重写isCellEditable方法
            @Override
            public boolean isCellEditable (int row, int column) {
                //all cells false
                if(column == 0 || column == 5)
                    return false;
                else
                    return true;
            }
        }
        model = new MyTableModel(dataSV, titleV);
        table = new JTable(model);
        tablePanel = new JScrollPane(table);
        JPanel jPanel = new JPanel();
        jPanel.add(tablePanel);

        jf.add(jPanel,BorderLayout.SOUTH);

        jf.add(bgPanel, BorderLayout.CENTER);

        // 创建个人信息面板
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        // 设置面板的盒子为流式布局，上到下
        infoPanel.setBorder(BorderFactory.createTitledBorder("个人信息")); // 设置面板的边框为标题边框，标题为“个人信息”

        // 创建头像标签
        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(60, 60)); // 设置头像标签的大小为100*100像素
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE)); // 设置头像标签的边框为黑色实线
        photoLabel.setIcon(new ImageIcon(PathUtils.getPath("OIG.jpg"))); // 设置头像标签的图标为test.png文件，您可以根据您的需要修改文件名和路径

        // 创建姓名标签
        nameLabel = new JLabel();
        nameLabel.setText("姓名：" + (String) table.getValueAt(0, 1)); // 设置姓名标签的文本为表格中的姓名

        // 创建性别标签
        sexLabel = new JLabel();
        sexLabel.setText("性别：" + (String) table.getValueAt(0, 2)); // 设置性别标签的文本为表格中的性别

        // 创建年龄标签
        ageLabel = new JLabel();
        ageLabel.setText("年龄：" + table.getValueAt(0, 3).toString()); // 设置年龄标签的文本为表格中的年龄

        // 创建电话标签
        phoneLabel = new JLabel();
        phoneLabel.setText("电话：" + (String) table.getValueAt(0, 4)); // 设置电话标签的文本为表格中的电话

        // 创建用户名标签
        userNameLabel = new JLabel();
        userNameLabel.setText("用户名：" + (String) table.getValueAt(0, 6)); // 设置用户名标签的文本为表格中的用户名

        // 创建更换头像按钮
        changePhotoBtn = new JButton("更换头像");
        changePhotoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建一个文件选择器
                JFileChooser fileChooser = new JFileChooser();
                // 设置文件选择器的过滤器，只允许选择图片文件
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        // 获取文件的扩展名
                        String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                        // 只允许选择jpg, png, gif格式的图片文件
                        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif");
                    }

                    @Override
                    public String getDescription() {
                        return "图片文件(*.jpg, *.png, *.gif)";
                    }
                });
                // 显示文件选择器对话框
                int result = fileChooser.showOpenDialog(jf);
                // 如果用户选择了一个文件
                if (result == JFileChooser.APPROVE_OPTION) {
                    // 获取用户选择的文件
                    File file = fileChooser.getSelectedFile();
                    // 将文件转换为图标
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    // 设置头像标签的图标为用户选择的文件
                    photoLabel.setIcon(icon);
                }
            }
        });

        // 将头像标签，姓名标签，性别标签，年龄标签，电话标签，用户名标签，更换头像按钮添加到个人信息面板中
        Box lableBox = Box.createVerticalBox();
        lableBox.add(photoLabel);
        lableBox.add(nameLabel);
        lableBox.add(sexLabel);
        lableBox.add(ageLabel);
        lableBox.add(phoneLabel);
        lableBox.add(userNameLabel);
        lableBox.add(changePhotoBtn);
        infoPanel.add(lableBox);

        // 将个人信息面板添加到窗口的下方
        jf.add(infoPanel, BorderLayout.CENTER);
        Box btnBox = Box.createVerticalBox();
        updateBtn = new JButton("更新个人信息");
        btnBox.add(updateBtn);
        jf.add(btnBox,BorderLayout.EAST);
        addBackActionListener();
        addUpdateBtnActionListener();
    }

    public CustomerPersonalInfoGUI getThis(){
        return this;
    }

    public JFrame getJFrame(){
        return jf;
    }

    public void refresh(){
        // 创建姓名标签
        nameLabel.setText("姓名：" + (String) table.getValueAt(0, 1)); // 设置姓名标签的文本为表格中的姓名
        // 创建性别标签
        sexLabel.setText("性别：" + (String) table.getValueAt(0, 2)); // 设置性别标签的文本为表格中的性别
        // 创建年龄标签
        ageLabel.setText("年龄：" + table.getValueAt(0, 3).toString()); // 设置年龄标签的文本为表格中的年龄
        // 创建电话标签
        phoneLabel.setText("电话：" + (String) table.getValueAt(0, 4)); // 设置电话标签的文本为表格中的电话
        // 创建用户名标签
        userNameLabel.setText("用户名：" + (String) table.getValueAt(0, 6)); // 设置用户名标签的文本为表格中的用户名
    }
}

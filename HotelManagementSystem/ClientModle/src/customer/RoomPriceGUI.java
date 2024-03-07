package customer;

import common.PathUtils;
import common.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;

public class RoomPriceGUI {
    JFrame jf;
    JPanel panel;
    String roomTypeName;
    int roomPrice;
    Date queryDate;
    JLabel roomTypeNameLabel;
    JLabel roomPriceLabel;
    JLabel queryDateLabel;
    JLabel remainLabel;
    final static int WIDTH = 600;
    final static int HEIGHT = 500;
    int remain;

    public RoomPriceGUI(Date queryDate, String roomTypeName, int roomPrice, int remain) throws IOException {
        this.queryDate = queryDate;
        this.roomPrice = roomPrice;
        this.roomTypeName = roomTypeName;
        this.remain = remain;
        init();
        jf.setVisible(true);
    }

    public void init() throws IOException {

        jf  = new JFrame("某天房价");
        panel = new JPanel();

        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH )/ 2, (ScreenUtils.getScreenHeight()- HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false); // 设置窗体不可调整大小
        //给窗口添加图标
        jf.setIconImage(ImageIO.read(new File(PathUtils.getPath("logo1.png"))));
        queryDateLabel = new JLabel("查询日期: " + queryDate);
        roomTypeNameLabel = new JLabel("房间类型: " + roomTypeName);
        roomPriceLabel = new JLabel("某天价格: " + roomPrice);
        remainLabel = new JLabel("剩余房间数: " + remain);
        // 使用表格来显示数据
        JTable table = new JTable(new Object[][]{
                {"查询日期", queryDate},
                {"房间类型", roomTypeName},
                {"某天价格", roomPrice},
                {"剩余房间数", remain}
        }, new Object[]{"属性", "值"});

        // 表格显示剩余房间号
        JTable table1 = new JTable(new Object[][]{
                {"日期", "1,2,3,4,5,6,7,8,9,10"},
                {"剩余房间号", "1,2,3,4,5,6,7,8,9,10"}
        }, new Object[]{"日期", "房间号"});

        // 使用选项卡面板来显示多个面板
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("房价信息", new JScrollPane(table));
        tabbedPane.addTab("剩余房间号", new JLabel("暂无其他信息"));
        // 使用边界布局来分配组件
        panel.setLayout(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        jf.add(panel);
    }

    public JFrame getJFrame(){
        return jf;
    }
}

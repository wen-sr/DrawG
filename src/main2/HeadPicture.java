package main2;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
 
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
 
 
/**
 * 设置头像
 * @author 罗伟富
 *
 */
public class HeadPicture extends JDialog implements ChangeListener, ActionListener{
    /**
     * 头像界面的宽度
     */
    public static final int WIDTH = 380;
    /**
     * 头像界面的高度
     */
    public static final int HEIGHT = 580;
    /**
     * 显示图片版面的宽度
     */
    public static final int PWIDTH = 350;
    /**
     * 显示图片版面的高度
     */
    public static final int PHEIGHT = 300;
    /**
     * 滑块的最大值
     */
    public static final int JS_MAXIMUM = 100;
    Image img;
    JSlider jsliderH, jsliderV;    //水平和垂直滑块
    JPanel uP, dP, picP, uplodP, setP;
    JButton systemB, nativeB, confirmB1, confirmB2, turnH, turnV, slantL, slantR, restoreB; 
    JTextField tf;
    MyCanvas canvas;
    int imgW = PWIDTH, imgH = PHEIGHT;
    int xcentre = PWIDTH/2, ycentre = PHEIGHT/2;
    private int dx1 = xcentre-imgW/2, dy1 = ycentre-imgH/2, dx2 = xcentre + imgW/2, dy2 = ycentre + imgH/2;
    private int sx1 = 0, sy1 = 0, sx2, sy2;
    private float shx = 0, shy = 0;
    /**
     * 构造函数
     */
    public HeadPicture() {
        launchDialog();
    }
     
    /**
     * 构造函数
     * @param editor Editor 对象
     * @param frame 对话框所属的容器
     * @param title 对话框的标题
     * @param model 为 true 时是有模式对话框，false 时允许其他窗口同时处于激活状态 
     */
    public HeadPicture(JFrame frame, String title, boolean model) {
        super(frame, title, model);
        launchDialog();
    }
    /**
     * 返回canvas
     * @return
     */
    public Canvas getCanvas() {
        return canvas;
    }
    /**
     * 界面设计
     */
    private void launchDialog() {
        //初始化图片对象
        //URL imgrul = HeadPicture.class.getResource("/systemPictures/landscape9.jpg");
        //初始化组件
        URL imgrul = HeadPicture.class.getResource("C:/Users/wen-sr/Pictures/1.jpg");
        img = Toolkit.getDefaultToolkit().getImage(imgrul);
        canvas = new MyCanvas();
        jsliderH = new JSlider();
        jsliderH.setMaximum(JS_MAXIMUM);
        jsliderH.setValue(JS_MAXIMUM/2);
        jsliderH.setMinimum(1);
        jsliderH.setOrientation(JSlider.HORIZONTAL);
        jsliderH.addChangeListener(this);
        jsliderV = new JSlider();
        jsliderV.setMaximum(JS_MAXIMUM);
        jsliderV.setValue(JS_MAXIMUM/2);
        jsliderV.setMinimum(1);
        jsliderV.setOrientation(JSlider.VERTICAL);
        jsliderV.addChangeListener(this);
        picP = new JPanel();
        picP.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        uP = new JPanel();
        dP = new JPanel();
        uplodP = new JPanel();
        setP = new JPanel();
        systemB = new JButton("浏览系统图片");
        nativeB = new JButton("浏览本地图片");
        confirmB1 = new JButton("确定");
        confirmB2 = new JButton("确定");
        turnH = new JButton("水平翻转");
        turnV = new JButton("垂直翻转");
        slantL = new JButton("向左倾斜");
        slantR = new JButton("向右倾斜");
        restoreB = new JButton("还原默认设置");
        tf  = new JTextField();
        //添加组件
        picP.setLayout(new BorderLayout());
        picP.add(canvas, BorderLayout.CENTER);
        uP.setLayout(new BorderLayout());
        uP.add(picP, BorderLayout.CENTER);
        uP.add(jsliderH, BorderLayout.SOUTH);
        uP.add(jsliderV, BorderLayout.EAST);
         
        uplodP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2,2,2,2);
        gbc.gridx = 0;
        gbc.gridy = 0;
        uplodP.add(systemB, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        uplodP.add(new JLabel("                           "), gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        uplodP.add(nativeB, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        uplodP.add(tf, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        uplodP.add(confirmB1, gbc);
        uplodP.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED, new Color(50, 160, 130), new Color(120, 180, 60)),"更改图片"));
         
        setP.setLayout(new GridLayout(3, 2, 20, 5));
        setP.add(turnH);
        setP.add(turnV);
        setP.add(slantL);
        setP.add(slantR);
        setP.add(confirmB2);
        setP.add(restoreB);
        setP.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.RAISED, new Color(50, 160, 130), new Color(120, 180, 60)),"图片设置"));
         
        dP.setLayout(new BoxLayout(dP, BoxLayout.Y_AXIS) );
        dP.add(uplodP);
        dP.add(setP);
        //添加响应    //systemB, nativeB, confirmB1, confirmB2, turnH, turnV, slantL, slantR, restoreB;
        systemB.addActionListener(this);
        nativeB.addActionListener(this);
        confirmB1.addActionListener(this);
        confirmB2.addActionListener(this);
        turnH.addActionListener(this);
        turnV.addActionListener(this);
        slantL.addActionListener(this);
        slantR.addActionListener(this);
        restoreB.addActionListener(this);
         
        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(uP);
        c.add(dP);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
         
    }
    /**
     * 事件监听响应
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == systemB) {
            URL picUrl = HeadPicture.class.getResource("/systemPictures");
            String dir = picUrl.toString();
            String dirs[] = dir.split("/");
            StringBuffer picDir = new StringBuffer();
            for(int i=1; i<dirs.length-1; i++) {
                picDir.append(dirs[i] + "\\");
            }
            picDir.append(dirs[dirs.length-1]);
            //以上这一段代码的作用是将file:/E:/Eclipse/workspace/Editor1.3/bin/systemPictures
            //变成E:\Eclipse\workspace\Editor1.3\bin\systemPictures
            JFrame optionPicFrame = new JFrame("打开系统图片");
            FileDialog openFileDialog = new FileDialog(optionPicFrame, "打开系统图片");
            openFileDialog.setMode(FileDialog.LOAD);    //设置此对话框为从文件加载内容
            openFileDialog.setFile("*.jpg;*.jpeg;*.gif;*.png;*.bmp;*.tif;");        //设置可打开文件的类型为：.txt,.java
            openFileDialog.setDirectory(picDir.toString());
            if(!openFileDialog.getDirectory().endsWith("systemPictures")) {
                openFileDialog.setDirectory(picDir.toString());        //设置默认路径
            }
            openFileDialog.setVisible(true);
            String fileName = openFileDialog.getFile();
            String directory = openFileDialog.getDirectory();
            if(null != fileName) {
                String fn = directory + fileName;
                tf.setText(fn);
                tf.setCaretPosition(fn.length());
            } else {
                tf.setText("您已经取消选择了，请重新选择!");
            }
        } else if(e.getSource() == nativeB) {
            JFrame optionPicFrame = new JFrame("打开本地图片");
            FileDialog openFileDialog = new FileDialog(optionPicFrame, "打开本地图片");
            openFileDialog.setMode(FileDialog.LOAD);    //设置此对话框为从文件加载内容
            openFileDialog.setFile("*.jpg;*.jpeg;*.gif;*.png;*.bmp;*.tif;");   
            //设置可打开文件的类型为：.jpg,.jpeg等
            openFileDialog.setDirectory("C:\\Users\\Administrator\\Desktop");        //设置默认路径
            openFileDialog.setVisible(true);
            String fileName = openFileDialog.getFile();
            String directory = openFileDialog.getDirectory();
            if(null != fileName) {
                String fn = directory + fileName;
                tf.setText(fn);
                tf.setCaretPosition(fn.length());
            } else {
                tf.setText("您已经取消选择了，请重新选择!");
            }
        } else if(e.getSource() == confirmB1) {
            if(!tf.getText().equals("您已经取消选择了，请重新选择!")) {
                img = Toolkit.getDefaultToolkit().getImage(tf.getText().trim());
                canvas.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "你还没有选择图片，请先选择图片！");
            }
        } else if(e.getSource() == turnH) {
            int temp = dx1;
            dx1 = dx2;
            dx2 = temp;
            canvas.repaint();
        } else if(e.getSource() == turnV) {
            int temp = dy1;
            dy1 = dy2;
            dy2 = temp;
            canvas.repaint();
        } else if(e.getSource() == slantL) {
            shx -= 0.1;
            canvas.repaint();
        } else if(e.getSource() == slantR) {
            shx += 0.1;
            canvas.repaint();
        } else if(e.getSource() == restoreB) {
            URL imgrul = HeadPicture.class.getResource("/systemPictures/landscape9.jpg");
            img = Toolkit.getDefaultToolkit().getImage(imgrul);
            dx1 = xcentre-imgW/2;
            dy1 = ycentre-imgH/2;
            dx2 = xcentre + imgW/2;
            dy2 = ycentre + imgH/2;
            sx1 = 0;
            sy1 = 0;
            sx2 = img.getWidth(this);
            sy2 = img.getWidth(this);
            canvas.repaint();
        } 
    }
    /**
     * 滑动条滑动响应事件
     */
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == jsliderH) {
            float valueH = jsliderH.getValue();
            imgW = (int)(2*PWIDTH*(valueH/JS_MAXIMUM));   
            if(imgW < PWIDTH/4) {
                imgW = PWIDTH/4;
            }
            dx1 = xcentre-imgW/2;
            dy1 = ycentre-imgH/2;
            dx2 = xcentre + imgW/2;
            dy2 = ycentre + imgH/2;
            canvas.repaint();
        } else if(e.getSource() == jsliderV) {
            float valueV = jsliderV.getValue();
            imgH = (int)(2*PHEIGHT*(valueV/JS_MAXIMUM));
            if(imgH < PHEIGHT/4) {
                imgH = PHEIGHT/4;
            }
            dx1 = xcentre-imgW/2;
            dy1 = ycentre-imgH/2;
            dx2 = xcentre + imgW/2;
            dy2 = ycentre + imgH/2;
            canvas.repaint();
        }
    }
     
    public static void main(String[] args) {
        new HeadPicture();
    }
     
    /**
     * 用于画图像的Canvas
     */
    class MyCanvas extends Canvas {
         
        public MyCanvas() {
             
        }
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            //g.drawImage(img, xcentre-imgW/2, ycentre-imgH/2, imgW, imgH, this);
            sx2  = img.getWidth(this);
            sy2  = img.getHeight(this);
            g2.shear(shx, shy);
            g2.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this);//Color.green, 
        }
    }
     
}

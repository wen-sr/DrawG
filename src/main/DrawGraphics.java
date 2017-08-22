package main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * 
 * @author wen-sr
 *
 */
@SuppressWarnings("serial")
public class DrawGraphics extends JFrame implements ActionListener{
	//定义参数对象，该对象保存画图需要的参数信息
	static MyObject mo;
	JButton bigger = null;
	JButton smaller = null;
	JButton clear = null;
	JButton submit = null;
	JFileChooser fc = new JFileChooser();
	JTextField textfield = null;
	JButton Choosefiles = null;
	
	static File file = null;
	//标记图形坐标已回到源文件大小
	static int flag = 0;
	//放大的倍数
	static int time = 2; 
	
	private int dx = 50;
    private int dy = 50;
	
	MyPanel mp = null;
	
	public static void main(String[] args) {
		try {
			mo = new MyObject();
			//读取根目录下的txt文件
//			File file=new File("/ID5381 Page 1 FrontAndBack-draw89747562404.txt");
//			File file=new File("e:/ID5381 Page 1 head_front-draw89747563443.txt");
			if(file != null && file.isFile() && file.exists()){ 
				mo = getFileData(file,mo);
				flag = mo.getX_list().get(0);
			}
			//实例化画图对象
			DrawGraphics dg = new DrawGraphics(mo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 构造函数
	 */
	public DrawGraphics(MyObject mo) {
		//实例化组件
		JPanel mp_0 = new JPanel();
		mp_0.setBackground(Color.GRAY);
		
		mp = new MyPanel(mo);
		
		
		mp.setPreferredSize(new Dimension(5000, 2000));
		JScrollPane sp=new JScrollPane(mp);
		
		
		Choosefiles = new JButton("选择文件");
		bigger = new JButton("放大");
		smaller = new JButton("缩小");
		clear = new JButton("清屏");
		
		mp_0.add(Choosefiles);
		mp_0.add(bigger);
		mp_0.add(smaller);
		mp_0.add(clear);
		this.add(mp_0,BorderLayout.NORTH);
		this.add(sp,BorderLayout.CENTER);
		
		//注册监听
		bigger.addActionListener(this);
		smaller.addActionListener(this);
		clear.addActionListener(this);
		Choosefiles.addActionListener(this);
		
		//鼠标监听
		MyListener m = new MyListener();

		mp.addMouseListener(m);

		mp.addMouseMotionListener(m);
		
		//设置窗体大小
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//		this.setSize(1000,800);
		//设置窗体显示的位置
		this.setLocation(0, 0);
		//设置窗体大小不可变
//		this.setResizable(false);
		
		//设置关闭窗口时后台也退出
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗体可见
		this.setVisible(true);
	}

	/**
	 * 事件监听
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//选择上传的文件
		if(e.getSource() == Choosefiles) {
			JFileChooser chooser = new JFileChooser(".");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(false);
			int intRetVal = chooser.showOpenDialog(this);
			if (intRetVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				try {
					mo = getFileData(file,mo);
					flag = mo.getX_list().get(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if(DrawGraphics.mo.getName() == null || DrawGraphics.mo.getColorNumber() == null || DrawGraphics.mo.getgSize() == 0 || DrawGraphics.mo.getX_list() == null || DrawGraphics.mo.getY_list() == null ){
			return;
		}
		//按2倍放大
		if(e.getSource() == bigger){
			if(flag != 0 && mo.getX_list().get(0) >= flag *4){
				return;
			}
//			for(int i=0;i<mo.getX_list().size();i++){
//				mo.getX_list().set(i, (int)(mo.getX_list().get(i)*time));
//				mo.getY_list().set(i, (int)(mo.getY_list().get(i)*time));
//			}
			for(int i=0;i<mo.getX_list().size();i++){
				mo.getX_list().set(i, (int)(mo.getX_list().get(i)+dx));
				mo.getY_list().set(i, (int)(mo.getY_list().get(i)+dy));
			}
		}
		//按2倍缩小
		else if(e.getSource() == smaller){
			if(flag != 0 && mo.getX_list().get(0) <= flag){
				return;
			}
			for(int i=0;i<mo.getX_list().size();i++){
				mo.getX_list().set(i, (int)(mo.getX_list().get(i)/time));
				mo.getY_list().set(i, (int)(mo.getY_list().get(i)/time));
			}
			System.out.println(mo.getX_list().get(0));
		}else if(e.getSource() == clear){
			mo.setX_list(null);
			mo.setY_list(null);
			mo.setColorNumber(null);
			mo.setgSize(0);
			mo.setName(null);
		}	flag = 0;
		
		mp.repaint();
	}
	/**
	 * 获取文件内容封装到MyObject对象中
	 * @param file
	 * @param mo
	 * @return
	 * @throws IOException
	 */
	private static MyObject getFileData(File file, MyObject mo) throws IOException {
		InputStreamReader read = new InputStreamReader(new FileInputStream(file));

	    BufferedReader bufferedReader = new BufferedReader(read);

	    String lineTxt = null;
	    
	    //用来保存从txt文件读取的数据，每一行保存成一个对象
	    List<String> list = new ArrayList<String>();
	    while((lineTxt = bufferedReader.readLine()) != null){
	        list.add(lineTxt);
	    }
	    if(list.size() == 3 ){
	    	//处理第一行数据
	    	for (int i=0; i < list.size();i++){
		    	if(i == 0){
		    		mo.setName(list.get(0));
		    	}else if (i == 1) {//将第二行数据分别保存到颜色和画笔大小的属性中
		    		String[] s1 = list.get(1).split(":");
		    		if(s1.length == 2){
		    			String[] s2 = s1[0].split(",");
		    			List<Integer> colorNumber = new ArrayList<Integer>();
		    			colorNumber.add(Integer.parseInt(s2[0]));
		    			colorNumber.add(Integer.parseInt(s2[1]));
		    			colorNumber.add(Integer.parseInt(s2[2]));
		    			mo.setColorNumber(colorNumber);
		    			mo.setgSize(Integer.parseInt(s1[1]));
		    		}else{
		    			System.out.println("该文本颜色格式不符合要求");
		    		}
		    	}else if(i == 2){//将第三行数据保存到x，y坐标的两个list中
		    		String[] s3 = list.get(2).split("\t");
		    		List<Integer> x_list=new ArrayList<Integer>();;
		    		List<Integer> y_list=new ArrayList<Integer>();;
		    		for(String s : s3){
		    			String[] s4 = s.split(",");
		    			x_list.add(Integer.parseInt(s4[0]));
		    			y_list.add(Integer.parseInt(s4[1]));
		    		}
		    		mo.setX_list(x_list);
		    		mo.setY_list(y_list);
		    	}
		    }
	    }else{
	    	System.out.println("该文本格式不符合要求");
	    }
	    read.close();
		return mo;
	}

	

}

/**
 * 自定义面板
 * @author wen-sr
 *
 */
@SuppressWarnings("serial")
class MyPanel extends JPanel{
	
	MyObject myObject;
	
	public MyPanel(MyObject myObject) {
		super();
		this.myObject = myObject;
	}
	
	/**
	 * 重写画图方法
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(myObject.getName() == null || myObject.getColorNumber() == null || myObject.getgSize() == 0 || myObject.getX_list() == null || myObject.getY_list() == null ){
			return;
		}
		
		//左边部分开始--------------------------------
		//创建颜色
		Color color=new Color(myObject.getColorNumber().get(0),myObject.getColorNumber().get(1),myObject.getColorNumber().get(2)); 
		g.setColor(color);
		Graphics2D g2 = (Graphics2D)g;
		//设置画笔大小
		g2.setStroke(new BasicStroke(myObject.getgSize(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		for(int i=0;i<myObject.getX_list().size();i++){
			if(i<myObject.getX_list().size()-1){
				g2.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(i+1), myObject.getY_list().get(i+1));
			}
			//最后一个坐标与第一个坐标相连
//			if(i == myObject.getX_list().size()-1){
//				g2.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(0), myObject.getY_list().get(0));
//			}
		}
		
		//左边实线图
		g2.setColor(Color.DARK_GRAY);
		g2 = (Graphics2D)g;
		//设置画笔大小
		g2.setStroke(new BasicStroke((float) 1.0));

		for(int i=0;i<myObject.getX_list().size();i++){
			if(i<myObject.getX_list().size()-1){
				g.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(i+1), myObject.getY_list().get(i+1));
			}
		}
		//左边部分结束--------------------------------
		
		//右边部分开始--------------------------------
		int distance = Collections.max(myObject.getX_list()) - Collections.min(myObject.getX_list()) + 20;
		//创建颜色
		Color color2=new Color(myObject.getColorNumber().get(0),myObject.getColorNumber().get(1),myObject.getColorNumber().get(2)); 
		g2.setColor(color2);
		//设置画笔大小
		g2.setStroke(new BasicStroke(myObject.getgSize(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		for(int i=0;i<myObject.getX_list().size();i++){
			if(i<myObject.getX_list().size()-1){
				g2.drawLine(myObject.getX_list().get(i)+distance, myObject.getY_list().get(i), myObject.getX_list().get(i+1)+distance, myObject.getY_list().get(i+1));
			}
		}
		
		
		//右边叉图
		g2.setColor(Color.DARK_GRAY);
		g2 = (Graphics2D)g;
		//设置画笔大小
		g2.setStroke(new BasicStroke((float) 1.0));
		for(int i=0;i<myObject.getX_list().size();i++){
			g.drawLine(myObject.getX_list().get(i)+distance-1, myObject.getY_list().get(i)-1, myObject.getX_list().get(i)+distance+1, myObject.getY_list().get(i)+1);
			g.drawLine(myObject.getX_list().get(i)+distance+1, myObject.getY_list().get(i)-1, myObject.getX_list().get(i)+distance-1, myObject.getY_list().get(i)+1);
		}
		//右边部分结束--------------------------------
		
	}
}

/**
 * 鼠标监听
 * @author wen-sr
 *
 */
class MyListener extends MouseAdapter {

	// 这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
	int newX, newY, oldX, oldY;

	// 这两个坐标为组件当前的坐标
	int startX, startY;

	@Override
	public void mousePressed(MouseEvent e) {

		// 得到事件源组件
		Component cp = (Component) e.getSource();

		// 当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
		startX = cp.getX();

		startY = cp.getY();

		oldX = e.getXOnScreen();

		oldY = e.getYOnScreen();

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		Component cp = (Component) e.getSource();

		// 拖动的时候记录新坐标
		
		newX = e.getXOnScreen();

		newY = e.getYOnScreen();

//		System.out.println("width:" + cp.getWidth() + " ,height:" + cp.getHeight());
//		System.out.println("startX:" + startX + " , newX:" + newX);
		
		// 设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
		cp.setBounds(startX + (newX - oldX), startY + (newY - oldY),
				cp.getWidth(), cp.getHeight());
		
		

	}

}
package test;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.MyObject;
/**
 * 
 * @author wen-sr
 *
 */
@SuppressWarnings("serial")
public class DrawGraphics extends JFrame implements ActionListener{
	//定义参数对象，该对象保存画图需要的参数信息
	MyObject mo;
	
	//定义所需要的组件
	MyPanel mp = null;
	public static void main(String[] args) {
		try {
			MyObject mo = new MyObject();
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			System.out.println(path);
			//读取根目录下的txt文件
			File file=new File("/ID5381 Page 1 FrontAndBack-draw89747562404.txt");
			System.out.println(file.exists());
			if(file.isFile() && file.exists()){ 
				
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
			}
			
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
		mp = new MyPanel(mo);
		
		//将组件加入到JFrame中
		this.add(mp,BorderLayout.CENTER);
		
		//设置窗体大小
		this.setSize(1000, 800);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		//设置窗体显示的位置
		this.setLocation(width/2-500, height/2-380);
		//设置窗体大小不可变
		this.setResizable(false);
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
		// TODO Auto-generated method stub
		
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
		myObject.getX_list();
		
		super.paint(g);
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
			if(i == myObject.getX_list().size()-1){
				g2.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(0), myObject.getY_list().get(0));
			}
		}
//		System.out.println(myObject.getX_list());
//		System.out.println(myObject.getY_list());
	}
}
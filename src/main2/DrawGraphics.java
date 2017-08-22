package main2;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

import main.MyObject;

/**
 * 
 * @author wen-sr
 *
 */
@SuppressWarnings("serial")
public class DrawGraphics extends JFrame implements ActionListener{
	//����������󣬸ö��󱣴滭ͼ��Ҫ�Ĳ�����Ϣ
	static MyObject mo;
	
	JButton bigger = null;
	JButton smaller = null;
	JButton clear = null;
	JButton submit = null;
	JFileChooser fc = new JFileChooser();
	JTextField textfield = null;
	JButton Choosefiles = null;
	
	static File file = null;
	//���ͼ�������ѻص�Դ�ļ���С
	static int flagX = 0;
	static int flagY = 0;
	//�Ŵ�ı���
	static int time = 2; 
	
	private int width = 1600;
    private int height = 1200;
	
	MyPanel mp = null;
	
	public static void main(String[] args) {
		mo = new MyObject();
		//ʵ������ͼ����
		DrawGraphics dg = new DrawGraphics(mo);
	}
	
	/**
	 * ���캯��
	 */
	public DrawGraphics(final MyObject mo) {
		//ʵ�������
		JPanel mp_0 = new JPanel();
		mp_0.setBackground(Color.GRAY);
		
		mp = new MyPanel(mo);
		
		Choosefiles = new JButton("ѡ���ļ�");
		bigger = new JButton("�Ŵ�");
		smaller = new JButton("��С");
		clear = new JButton("����");
		
		mp_0.add(Choosefiles);
		mp_0.add(bigger);
		mp_0.add(smaller);
		mp_0.add(clear);
		this.add(mp_0,BorderLayout.NORTH);
		this.add(mp,BorderLayout.CENTER);
		
		//ע�����
		bigger.addActionListener(this);
		smaller.addActionListener(this);
		clear.addActionListener(this);
		Choosefiles.addActionListener(this);
		
		//ע����ּ���
		this.addMouseWheelListener(new MouseWheelListener() {
            
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() < 0){
                    zoom();
                }else{
                    reduce();
                }
            }
        });
		//�������
		MouseAdapter ma = new MouseAdapter(){
            boolean moveEnable = false;
            Point point1 = null;
            Point point2 = null;
            
            //��갴��
            @Override
            public void mousePressed(MouseEvent e) {
                moveEnable = true;
                point1 = e.getPoint();
            }
            //����ɿ�
            @Override
            public void mouseReleased(MouseEvent e) {
                moveEnable = false; 
                point1 = null;
                point2 = null;
            }
            
            //�����ק
            @Override
            public void mouseDragged(MouseEvent e) {
            	//TODO moΪ��ʱ��������
                point2 = e.getPoint();
                if(moveEnable){
                    if(point1 != null && point2 != null){
                        int dx = point2.x - point1.x;
                        int dy = point2.y - point1.y;
                        
                        for(int i=0;i<mo.getX_list().size();i++){
            				mo.getX_list().set(i, (int)(mo.getX_list().get(i)+dx));
            				mo.getY_list().set(i, (int)(mo.getY_list().get(i)+dy));
            			}
                        point1 = point2;
                        repaint();
                    }
                }
            }
        };
		
		
        mp.addMouseMotionListener(ma);
        mp.addMouseListener(ma);
		
		
		//���ô����С
//		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setSize(width,height);
		//���ô�����ʾ��λ��
		this.setLocation(0, 0);
		//���ô����С���ɱ�
//		this.setResizable(false);
		
		//���ùرմ���ʱ��̨Ҳ�˳�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//���ô���ɼ�
		this.setVisible(true);
	}

	/**
	 * ��ť�¼�����
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//ѡ���ϴ����ļ�
		if(e.getSource() == Choosefiles) {
			JFileChooser chooser = new JFileChooser(".");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(false);
			int intRetVal = chooser.showOpenDialog(this);
			if (intRetVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				try {
					mo = getFileData(file,mo);
					this.getXYData();
					System.out.println("x:" + Collections.min(mo.getX_list()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if(DrawGraphics.mo.getName() == null || DrawGraphics.mo.getColorNumber() == null || DrawGraphics.mo.getgSize() == 0 || DrawGraphics.mo.getX_list() == null || DrawGraphics.mo.getY_list() == null ){
			return;
		}
		//��2���Ŵ�
		if(e.getSource() == bigger){
			zoom();
		}
		//��2����С
		else if(e.getSource() == smaller){
			reduce();
		}else if(e.getSource() == clear){
			mo.setX_list(null);
			mo.setY_list(null);
			mo.setColorNumber(null);
			mo.setgSize(0);
			mo.setName(null);
		}
		
		mp.repaint();
	}
	/**
	 * ��ȡ�ļ����ݷ�װ��MyObject������
	 * @param file
	 * @param mo
	 * @return
	 * @throws IOException
	 */
	private static MyObject getFileData(File file, MyObject mo) throws IOException {
		InputStreamReader read = new InputStreamReader(new FileInputStream(file));

	    BufferedReader bufferedReader = new BufferedReader(read);

	    String lineTxt = null;
	    
	    //���������txt�ļ���ȡ�����ݣ�ÿһ�б����һ������
	    List<String> list = new ArrayList<String>();
	    while((lineTxt = bufferedReader.readLine()) != null){
	        list.add(lineTxt);
	    }
	    if(list.size() == 3 ){
	    	//�����һ������
	    	for (int i=0; i < list.size();i++){
		    	if(i == 0){
		    		mo.setName(list.get(0));
		    	}else if (i == 1) {//���ڶ������ݷֱ𱣴浽��ɫ�ͻ��ʴ�С��������
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
		    			System.out.println("���ı���ɫ��ʽ������Ҫ��");
		    		}
		    	}else if(i == 2){//�����������ݱ��浽x��y���������list��
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
	    	System.out.println("���ı���ʽ������Ҫ��");
	    }
	    read.close();
		return mo;
	}

	
	/**
     * ��С
     */
    public void reduce(){
    	if(mo != null){
//    		System.out.println("minx:" + Collections.min(mo.getX_list()));
    		//����С����100��100��������ʱ��������С
    		int min_i = Collections.min(mo.getX_list());
    		if(min_i <= 100){
    			for(int i = 0;i < mo.getX_list().size();i++){
    				if(mo.getX_list().get(i) == min_i){
    					if(mo.getY_list().get(i) <= 100){
    						return;
    					}
    				}
    			}
				return;
			}
    		for(int i=0;i<mo.getX_list().size();i++){
				mo.getX_list().set(i, (int)(mo.getX_list().get(i)/time));
				mo.getY_list().set(i, (int)(mo.getY_list().get(i)/time));
			}
    	}
            
     super.repaint();
         
    }
     
    /**
     * �Ŵ�
     */
    public void zoom(){
    	if(mo != null){
    		for(int i=0;i<mo.getX_list().size();i++){
				mo.getX_list().set(i, (int)(mo.getX_list().get(i)*time));
				mo.getY_list().set(i, (int)(mo.getY_list().get(i)*time));
			}
    	}
        super.repaint();
    }
	/**
	 * ����xy����(�ҳ�����˵ĵ㣬����ƽ������100��100��)
	 * @return
	 */
    public void getXYData(){
		int min_x = Collections.min(mo.getX_list());
		int min_y = 0;
		for (int i = 0;i < mo.getX_list().size();i++ ){
			if(min_x == mo.getX_list().get(i)){
				if(min_y == 0){
					min_y = mo.getY_list().get(i);
				}else{
					if(mo.getY_list().get(i) < min_y)
						min_y = mo.getY_list().get(i);
				}
				
			}
		}
		for(int i = 0;i < mo.getX_list().size();i++){
			mo.getX_list().set(i, mo.getX_list().get(i) - (min_x-100));
		}
		for(int i = 0;i < mo.getY_list().size();i++){
			mo.getY_list().set(i, mo.getY_list().get(i) - (min_y-100));
		}
		
    }

}

/**
 * �Զ������
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
	 * ��д��ͼ����
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(myObject.getName() == null || myObject.getColorNumber() == null || myObject.getgSize() == 0 || myObject.getX_list() == null || myObject.getY_list() == null ){
			return;
		}
		
		System.out.println("x:" + Collections.min(myObject.getX_list()));
		//��߲��ֿ�ʼ--------------------------------
		//������ɫ
		Color color=new Color(myObject.getColorNumber().get(0),myObject.getColorNumber().get(1),myObject.getColorNumber().get(2)); 
		g.setColor(color);
		Graphics2D g2 = (Graphics2D)g;
		//���û��ʴ�С
		g2.setStroke(new BasicStroke(myObject.getgSize(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		for(int i=0;i<myObject.getX_list().size();i++){
			if(i<myObject.getX_list().size()-1){
				g2.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(i+1), myObject.getY_list().get(i+1));
			}
			//���һ���������һ����������
//			if(i == myObject.getX_list().size()-1){
//				g2.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(0), myObject.getY_list().get(0));
//			}
		}
		
		//���ʵ��ͼ
		g2.setColor(Color.DARK_GRAY);
		g2 = (Graphics2D)g;
		//���û��ʴ�С
		g2.setStroke(new BasicStroke((float) 1.0));

		for(int i=0;i<myObject.getX_list().size();i++){
			if(i<myObject.getX_list().size()-1){
				g.drawLine(myObject.getX_list().get(i), myObject.getY_list().get(i), myObject.getX_list().get(i+1), myObject.getY_list().get(i+1));
			}
		}
		//��߲��ֽ���--------------------------------
		
		//�ұ߲��ֿ�ʼ--------------------------------
		int distance = Collections.max(myObject.getX_list()) - Collections.min(myObject.getX_list()) + 20;
		//������ɫ
		Color color2=new Color(myObject.getColorNumber().get(0),myObject.getColorNumber().get(1),myObject.getColorNumber().get(2)); 
		g2.setColor(color2);
		//���û��ʴ�С
		g2.setStroke(new BasicStroke(myObject.getgSize(),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		for(int i=0;i<myObject.getX_list().size();i++){
			if(i<myObject.getX_list().size()-1){
				g2.drawLine(myObject.getX_list().get(i)+distance, myObject.getY_list().get(i), myObject.getX_list().get(i+1)+distance, myObject.getY_list().get(i+1));
			}
		}
		
		
		//�ұ߲�ͼ
		g2.setColor(Color.DARK_GRAY);
		g2 = (Graphics2D)g;
		//���û��ʴ�С
		g2.setStroke(new BasicStroke((float) 1.0));
		for(int i=0;i<myObject.getX_list().size();i++){
			g.drawLine(myObject.getX_list().get(i)+distance-1, myObject.getY_list().get(i)-1, myObject.getX_list().get(i)+distance+1, myObject.getY_list().get(i)+1);
			g.drawLine(myObject.getX_list().get(i)+distance+1, myObject.getY_list().get(i)-1, myObject.getX_list().get(i)+distance-1, myObject.getY_list().get(i)+1);
		}
		//�ұ߲��ֽ���--------------------------------
		
	}
}



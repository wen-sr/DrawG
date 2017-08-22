package main;


import java.awt.*;  
import java.awt.event.*;  
import java.util.*;  
import javax.swing.*;  
import java.awt.geom.*;  

public class Test {
	public static void main (String args[])  
    {  
          new paintboard("��ͼ����");  
     }
}


class Point  
{  
      int x,y;  
      Color col;  
      int tool;  
      int boarder;  
      Point(int x, int y, Color col, int tool, int boarder)  
      {  
            this.x = x;  
            this.y = y;  
            this.col = col;  
            this.tool = tool;  
            this.boarder = boarder;  
       }  
}  
  
  
/* 
*BasicStroke 
*Choice 
*validate(); 
**/  
class paintboard extends Frame implements ActionListener,MouseMotionListener,MouseListener,ItemListener  
{  
     int xx0=0,yy0=0;  
     int xx1=0,yy1=0;  
     int type=6;  
     int x = -1, y = -1;  
     int con = 1;//���ʴ�С  
     int Econ = 5;//��Ƥ��С  
     int toolFlag = 0;//toolFlag:���߱��  
                 //toolFlag���߶�Ӧ��  
                 //��0--���ʣ�����1--��Ƥ������2--�������  
                 //��3--ֱ�ߣ�����4--Բ������5--���Σ���  
                   
     Color c = new Color(0,0,0); //������ɫ  
     BasicStroke size = new BasicStroke (con,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);//���ʴ�ϸ  
     Point cutflag = new Point(-1, -1, c, 6, con);//�ضϱ�־  
     Vector paintInfo = null;//����Ϣ������  
     int n = 1;  
  
     // *�������--���ʣ�ֱ�ߣ�Բ�����Σ������,��Ƥ�����*/  
     Panel toolPanel;  
     Button eraser, drLine,drCircle,drRect;  
     Button clear ,pen;  
     Choice ColChoice,SizeChoice,EraserChoice;  
     Button colchooser;  
     Label ��ɫ,��СB,��СE;  
       
       paintboard(String s)//���캯��  
     {  
            super(s);  
            addMouseMotionListener(this);  
            addMouseListener(this);  
            paintInfo = new Vector();  
          /*�����߰�ť��ѡ����*/  
            //��ɫѡ��  
            ColChoice = new Choice();  
            ColChoice.add("��ɫ");  
            ColChoice.add("��ɫ");  
            ColChoice.add("��ɫ");  
            ColChoice.add("��ɫ");  
            ColChoice.addItemListener(this);  
            //���ʴ�Сѡ��  
            SizeChoice = new Choice();  
            SizeChoice.add("1");  
            SizeChoice.add("3");  
            SizeChoice.add("5");  
            SizeChoice.add("7");  
            SizeChoice.add("9");  
            SizeChoice.addItemListener(this);  
            //��Ƥ��Сѡ��  
            EraserChoice = new Choice();  
            EraserChoice.add("2");  
            EraserChoice.add("3");  
            EraserChoice.add("4");  
            EraserChoice.add("5");  
            EraserChoice.addItemListener(this);  
              
            toolPanel = new Panel();  
            clear = new Button("���");  
            eraser = new Button("��Ƥ");  
            pen = new Button("����");  
            drLine = new Button("��ֱ��");  
            drCircle = new Button("��Բ��");  
            drRect = new Button("������");  
            colchooser = new Button("��ʾ��ɫ��");  
              
            //������¼�����  
            clear.addActionListener(this);  
            eraser.addActionListener(this);  
            pen.addActionListener(this);  
            drLine.addActionListener(this);  
            drCircle.addActionListener(this);  
            drRect.addActionListener(this);  
            colchooser.addActionListener(this);  
            ��ɫ = new Label("������ɫ",Label.CENTER);  
            ��СB = new Label("���ʴ�С",Label.CENTER);  
            ��СE = new Label("��Ƥ��С",Label.CENTER);  
            //���������  
            toolPanel.add(pen);  
            toolPanel.add(drLine);  
            toolPanel.add(drCircle);  
            toolPanel.add(drRect);  
            toolPanel.add(��ɫ); toolPanel.add(ColChoice);  
            toolPanel.add(��СB); toolPanel.add(SizeChoice);  
            toolPanel.add(colchooser);  
            toolPanel.add(eraser);  
            toolPanel.add(��СE); toolPanel.add(EraserChoice);  
            toolPanel.add(clear);  
            //������嵽APPLET���  
            add(toolPanel,BorderLayout.NORTH);  
            setBounds(60,60,800,650);  
            setVisible(true);  
            validate();  
              
            //dialog for save and load  
            addWindowListener(new WindowAdapter()  
            {  
                  public void windowClosing(WindowEvent e)  
                   { System.exit(0);}  
            });  
      }  
        
     public void paint(Graphics g)  
     {  
          Graphics2D g2d = (Graphics2D)g;  
          Point p1,p2;  
        n = paintInfo.size();  
        if(toolFlag==2)  
           g.clearRect(0,0,getSize().width,getSize().height);//���  
             
          switch(type)  
          {  
               case 3:  
          Line2D line = new Line2D.Double(xx0,yy0,xx1,yy1);  
          g2d.draw(line);  
          break;  
               case 4:  
          Ellipse2D ellipse1 = new Ellipse2D.Double(xx0, yy0, Math.abs(xx1-xx0) ,Math.abs(yy1-yy0));  
          g2d.draw(ellipse1);  
          break;  
               case 5:  
          Rectangle2D rect1 = new Rectangle2D.Double(xx0, yy0, Math.abs(xx1-xx0) , Math.abs(yy1-yy0));  
          g2d.draw(rect1);  
          break;  
          default :break;  
          }  
            
       for(int i=0; i<n-1; i++)  
       {  
                  p1 = (Point)paintInfo.elementAt(i);  
                  p2 = (Point)paintInfo.elementAt(i+1);  
                  size = new BasicStroke(p1.boarder,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);  
              g2d.setColor(p1.col);  
                   g2d.setStroke(size);  
  
                 if(p1.tool==p2.tool)  
                  {  
                       switch(p1.tool)  
                   {  
                             case 0://����  
                     Line2D line1 = new Line2D.Double(p1.x, p1.y, p2.x,p2.y);  
                     g2d.draw(line1);  
                    break;  
                        case 1://��Ƥ  
                      g.clearRect(p1.x, p1.y, p1.boarder, p1.boarder);  
                     break;  
                        case 3://��ֱ��  
                     Line2D line2 = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);  
                     g2d.draw(line2);  
                      break;  
                        case 4://��Բ  
                       Ellipse2D ellipse = new Ellipse2D.Double(p1.x, p1.y, Math.abs(p2.x-p1.x) ,Math.abs(p2.y-p1.y));  
                      g2d.draw(ellipse);  
                    break;  
                        case 5://������  
                     Rectangle2D rect = new Rectangle2D.Double(p1.x, p1.y, Math.abs(p2.x-p1.x) ,Math.abs(p2.y-p1.y));  
                       g2d.draw(rect);  
                       break;  
                        case 6://�ضϣ�����  
                      i=i+1;  
                     break;  
                             default :break;  
                         }//end switch  
                   }//end if  
             }//end for  
     }  
     public void itemStateChanged(ItemEvent e)  
     {  
            if(e.getSource()==ColChoice)//Ԥѡ��ɫ  
            {  
                  String name = ColChoice.getSelectedItem();  
                  if(name=="��ɫ")  
              { c = new Color(0,0,0); }  
                  else if(name=="��ɫ")  
              { c = new Color(255,0,0); }  
                  else if(name=="��ɫ")  
              { c = new Color(0,255,0); }  
                  else if(name=="��ɫ")  
              { c = new Color(0,0,255); }  
            }  
            else if(e.getSource()==SizeChoice)//���ʴ�С  
             {  
                  String selected = SizeChoice.getSelectedItem();  
                  if(selected=="1")  
              {  
              con = 1;  
              size = new BasicStroke(con,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);  
              }  
                  else if(selected=="3")  
              {  
              con = 3;  
              size = new BasicStroke(con,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);  
              }  
                  else if(selected=="5")  
              { con = 5;  
              size = new BasicStroke(con,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);  
              }  
                  else if(selected=="7")  
              { con = 7;  
              size = new BasicStroke(con,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);  
              }  
                  else if(selected=="9")  
              { con = 9;  
              size = new BasicStroke(con,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);  
              }  
            }  
            else if(e.getSource()==EraserChoice)//��Ƥ��С  
            {  
                  String Esize = EraserChoice.getSelectedItem();  
                  if(Esize=="2")  
                  { Econ = 3*3; }  
                  else if(Esize=="3")  
                  { Econ = 4*4; }  
                  else if(Esize=="4")  
                  { Econ = 5*5; }  
                  else if(Esize=="5")  
                  { Econ = 6*6; }  
           }  
     }  
       
     public void mouseDragged(MouseEvent e)  
     {  
           xx1 = (int)e.getX();  
           yy1 = (int)e.getY();  
            Point p1 ;  
            switch(toolFlag){  
                  case 0://����  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p1 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p1);  
               repaint();  
               break;  
                 
                  case 1://��Ƥ  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p1 = new Point(x, y, null, toolFlag, Econ);  
               paintInfo.addElement(p1);  
               repaint();  
               break;  
               case 3:  
  
               case 4:  
               case 5:  
               repaint();  
               break;  
                 
                  default :  
            }  
//repaint();  
     }  
     public void mousePressed(MouseEvent e)  
     {  
          xx0= (int)e.getX();  
          yy0= (int)e.getY();  
            Point p2;  
             switch(toolFlag){  
                  case 3://ֱ��  
               type=3;  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p2 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p2);  
               break;  
                 
                  case 4: //Բ  
               type=4;  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p2 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p2);  
               break;  
                 
                  case 5: //����  
               type=5;  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p2 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p2);  
               break;  
                 
                  default :type=6;  
            }  
     }  
       
     public void mouseReleased(MouseEvent e)  
     {  
            Point p3;  
             switch(toolFlag){  
                  case 0: //����  
               paintInfo.addElement(cutflag);  
               break;  
                  case 1: //eraser  
               paintInfo.addElement(cutflag);  
               break;  
                  case 3: //ֱ��  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p3 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p3);  
               paintInfo.addElement(cutflag);  
               repaint();  
               break;  
                  case 4: //Բ  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p3 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p3);  
               paintInfo.addElement(cutflag);  
               repaint();  
               break;  
                  case 5: //����  
               x = (int)e.getX();  
               y = (int)e.getY();  
               p3 = new Point(x, y, c, toolFlag, con);  
               paintInfo.addElement(p3);  
               paintInfo.addElement(cutflag);  
               repaint();  
               break;  
                  default:  
            }  
     }  
     public void mouseEntered(MouseEvent e){}  
     public void mouseExited(MouseEvent e){}  
     public void mouseClicked(MouseEvent e){}  
     public void mouseMoved(MouseEvent e){}  
     public void actionPerformed(ActionEvent e)  
     {  
            if(e.getSource()==pen)//����  
            { toolFlag = 0; }  
            if(e.getSource()==eraser)//��Ƥ  
            { toolFlag = 1; }  
            if(e.getSource()==clear)//���  
            {  
                  toolFlag = 2;  
                  paintInfo.removeAllElements();  
                  repaint();  
            }  
            if(e.getSource()==drLine)//����  
            { toolFlag = 3; }  
            if(e.getSource()==drCircle)//��Բ  
            { toolFlag = 4; }  
            if(e.getSource()==drRect)//������  
            { toolFlag = 5; }  
            if(e.getSource()==colchooser)//��ɫ��  
            {  
                  Color newColor = JColorChooser.showDialog(this,"��ɫ��",c);  
                  c = newColor;}  
            }  
     }//end  
  

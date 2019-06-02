package GUI;

import javax.jws.WebParam;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import layers.layers.Dense;
import layers.model.Model;

public class Center extends JPanel {

	public Dense dense;
	public boolean canCreate;
	public JButton button1, button2, button3;
	public MyJScrollPane bottomScrollPane;
	public JPanel pnlBottom;
	public Model KModel;
	public JPanel pnlHead;
	public RightBar rightBar;
	public int eventNumber=0;
	public boolean isSelected=false;
	public MyButton tmpButton=null;
	public Center(RightBar _rightBar)
	{
		rightBar = _rightBar;
		pnlHead = new JPanel() ;
		KModel=new Model();
		button1 = new JButton("Create");
		button2 = new JButton("Add Line");
		button3 = new JButton("Delete Line");
		button1.addActionListener(new MyActionListener(this));
		button2.addActionListener(new MyActionListener(this));
		button3.addActionListener(new MyActionListener(this));

		pnlHead.add(button1); pnlHead.add(button2); pnlHead.add(button3);
		pnlBottom = new JPanel();
		pnlBottom.setLayout(null);
		bottomScrollPane = new MyJScrollPane(pnlBottom, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		bottomScrollPane.setLayout(null);
		//JLabel score = new JLabel("Your score is:");
		setBorder(BorderFactory.createEtchedBorder());
		//add(score);
		setLayout(new BorderLayout());
		add("North", pnlHead);
		add("Center", bottomScrollPane);
		setFocusable(true);
		setVisible(true);
	}

	public void addLine(MyButton btn1,MyButton btn2,int eventNumber) {
			if(eventNumber==2)
			{
				KModel.config.addEdge(btn1.dense,btn2.dense);
				bottomScrollPane.line.add(new LineParameter(btn1,btn2));
				updateUI();
			}
			else if(eventNumber==3){
				KModel.config.deleteEdge(btn1.dense,btn2.dense);
				int index=0;
				for(;index<bottomScrollPane.line.size();index++){
					if(bottomScrollPane.line.get(index).btn1==btn1&&bottomScrollPane.line.get(index).btn2==btn2)
					{
						bottomScrollPane.line.remove(index);
						break;
					}
				}
				updateUI();
			}
	}
	public void toCenter(Dense temp)
	{
		dense = temp;
		canCreate = true;
	}
}
class MyJScrollPane extends JScrollPane{
	ArrayList<LineParameter> line=new ArrayList<LineParameter>();
	public MyJScrollPane(){
		super();
	}
	public MyJScrollPane(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view, vsbPolicy, hsbPolicy);
	}
	public void paint(Graphics g) {
		super.paint(g);
		int i=0;
		for(;i<line.size();i++){
			LineParameter tmpLine=line.get(i);
			int btn1_x=tmpLine.btn1.getX()+60;
			int btn1_y=tmpLine.btn1.getY();
			int btn2_x=tmpLine.btn2.getX()+60;
			int btn2_y=tmpLine.btn2.getY();
			int mid_y;
			boolean flag=true;
			if(btn2_y>btn1_y+60){
				btn1_y+=60;
				mid_y=(btn1_y+btn2_y)/2;
			}
			else if(btn2_y<btn1_y-60) {
				btn2_y+=60;
				flag=false;
				mid_y=(btn1_y+btn2_y)/2;
			}
			else{
				mid_y=(btn1_y+btn2_y)/2-60;
			}
			g.drawLine(btn1_x,btn1_y,btn1_x,mid_y);
			g.drawLine(btn1_x,mid_y,btn2_x,mid_y);
			g.drawLine(btn2_x,mid_y,btn2_x,btn2_y);
			if(flag){
			g.drawLine(btn2_x,btn2_y,btn2_x+5,btn2_y-5);
			g.drawLine(btn2_x,btn2_y,btn2_x-5,btn2_y-5);
		}
			else{
				g.drawLine(btn2_x,btn2_y,btn2_x+5,btn2_y+5);
				g.drawLine(btn2_x,btn2_y,btn2_x-5,btn2_y+5);
			}
		}
	}
}
class LineParameter{

		public MyButton btn1;
		public MyButton btn2;
		LineParameter(MyButton from,MyButton to){
			btn1=from;
			btn2=to;
		}

}
class MyActionListener implements ActionListener
{
	Center center;
	public MyActionListener(Center _center)
	{
		center = _center;
	}
	public void actionPerformed( ActionEvent e ){
		Object button = e.getSource();
		if(button == center.button1)
		{
			if(center.canCreate)
			{
//				System.out.println(dense.getString("name"));
				MyButton mb =new MyButton(center.dense.getString("name"),center.dense, center);
				center.KModel.config.addLayer(center.dense);
				mb.setSize(120, 60);
				mb.setLocation(500, 40);
				//mb.setVisible(true);
				mb.addActionListener(new MyActionListener(center));
				center.bottomScrollPane.add(mb);
				center.updateUI();
				center.canCreate = false;
			}
		}
		else if(button==center.button2){
			center.eventNumber=2;
		}
		else if(button==center.button3){
			center.eventNumber=3;
		}
		else{
			if(center.eventNumber!=0){
				if(center.isSelected==false){
					center.tmpButton=(MyButton) button;
					center.isSelected=true;
				}
				else{
					System.out.println(center.tmpButton.getText());
					center.addLine(center.tmpButton,(MyButton) button,center.eventNumber);
					center.tmpButton=null;
					center.isSelected=false;
					center.eventNumber=0;
				}
			}
		}
	}
}
class MyButton extends JButton
{
	public Dense dense;
	Center center;
	public MyButton(String text,Dense _dense, Center _center)
	{
		super(text);
		dense = _dense;
		center = _center;
		MouseEventListener mouseListener = new MouseEventListener(this, center);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
	}

}

class MouseEventListener implements MouseInputListener {

	Point origin;
	//锟斤拷锟斤拷锟阶э拷锟揭拷贫锟斤拷锟侥匡拷锟斤拷锟斤拷
	MyButton frame;

	Center center;

	public MouseEventListener(MyButton frame, Center _center) {
		this.frame = frame;
		origin = new Point();
		center = _center;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if(e.getButton() == MouseEvent.BUTTON1)
		{
			MyButton sourceBtn = (MyButton)e.getSource();
			center.rightBar.refresh(sourceBtn.dense);

		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			//System.out.println("lll");
			MyButton sourceBtn = (MyButton)e.getSource();
			//System.out.println(sourceBtn);
			center.KModel.config.deleteLayer(sourceBtn.dense);

			boolean flag=true;
			while(flag){
				flag=false;
				int index=0;
			for(;index<center.bottomScrollPane.line.size();index++){
				if(center.bottomScrollPane.line.get(index).btn1==sourceBtn||center.bottomScrollPane.line.get(index).btn2==sourceBtn)
				{
					center.bottomScrollPane.line.remove(index);
					flag=true;
				}
			}}
			center.bottomScrollPane.remove(sourceBtn);
			//center.bottomScrollPane.revalidate();
			center.bottomScrollPane.repaint();
			//center.updateUI();
		}
	}

	//锟斤拷录锟斤拷臧达拷锟绞憋拷牡锟�

	@Override
	public void mousePressed(MouseEvent e) {
		origin.x = e.getX();
		origin.y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		center.bottomScrollPane.repaint();
	}

	//锟斤拷锟斤拷平锟斤拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟斤拷锟斤拷锟斤拷图锟斤拷为锟狡讹拷图锟斤拷

	@Override
	public void mouseEntered(MouseEvent e) {
		this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	// 锟斤拷锟斤拷瞥锟斤拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟斤拷锟斤拷锟斤拷图锟斤拷为默锟斤拷指锟斤拷

	@Override
	public void mouseExited(MouseEvent e) {
		this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	//锟斤拷锟斤拷诒锟斤拷锟斤拷锟斤拷锟阶憋拷锟斤拷锟斤拷么锟斤拷诘锟斤拷锟斤拷锟轿伙拷锟�
	//锟斤拷锟斤拷锟铰碉拷锟斤拷锟斤拷位锟斤拷  = 锟狡讹拷前锟斤拷锟斤拷位锟斤拷+锟斤拷锟斤拷锟街革拷氲鼻帮拷锟斤拷锟�-锟斤拷臧达拷锟绞敝革拷锟斤拷位锟矫ｏ拷

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = this.frame.getLocation();
		this.frame.setLocation(
				p.x + (e.getX() - origin.x),
				p.y + (e.getY() - origin.y));
		center.bottomScrollPane.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

}
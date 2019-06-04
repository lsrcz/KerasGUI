package GUI;

import javax.jws.WebParam;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import layers.layers.Layer;
import layers.model.Model;
import fileio.*;

public class Center extends JPanel {
	final int Time2AddLine=2;
	final int Time2DeleteLine=3;
	public Layer layer;
	public boolean canCreate;
	public JButton button1, button2, button3;
	//public MyJScrollPane paintPanel;
	public MyJPanel paintPanel;
	public JPanel pnlBottom;
	public Model KModel;
	public JPanel pnlHead;
	public RightBar rightBar;
	public int eventNumber=0;
	public boolean isSelected=false;
	public MyButton tmpButton=null;
	public SaveObject SO;
	public Center(RightBar _rightBar, SaveObject _SO)
	{
		rightBar = _rightBar;
		SO = _SO;
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

		paintPanel = new MyJPanel();
		paintPanel.setLayout(null);
		//JLabel score = new JLabel("Your score is:");
		setBorder(BorderFactory.createEtchedBorder());
		//add(score);
		setLayout(new BorderLayout());
		add("North", pnlHead);
		add("Center", new JScrollPane(paintPanel));
		//add("Center", paintPanel);
		setFocusable(true);
		setVisible(true);
	}

	public void getBack()
	{
		ArrayList<MyButton> op = SO.getButton();
		for(MyButton bt: op)
		{
			MyButton newButton = new MyButton(bt.getText(), bt.layer, this, bt.x, bt.y, bt.next);
			SO.deleButton(bt);

			newButton.setSize(120,60);
			newButton.setLocation(newButton.x,newButton.y);
			newButton.addActionListener(new MyActionListener(this));
			paintPanel.add(newButton);
			SO.setButton(newButton);
		}
		updateUI();
		op = SO.getButton();
		for(MyButton bt: op)
		{
			for(String Name:bt.next){
				for(MyButton bt2:op){
					if(bt2.getText().equals(Name)){
						paintPanel.line.add(new LineParameter(bt,bt2));
					}
				}
			}
		}
		updateUI();
	}

	public void ModifyLine(MyButton btn1,MyButton btn2,int eventNumber) {
		if(eventNumber==Time2AddLine)
		{
			if(KModel.config.addEdge(btn1.layer,btn2.layer)==false)
				return;
			paintPanel.line.add(new LineParameter(btn1,btn2));
			btn1.addNext(btn2);//???
			SwingUtilities.invokeLater(() -> {
				updateUI();
			});
		}
		else if(eventNumber==Time2DeleteLine){
			deleteLine(btn1,btn2);
			deleteLine(btn2,btn1);
			SwingUtilities.invokeLater(() -> {
				updateUI();
			});
		}
	}
	public void deleteLine(MyButton btn1,MyButton btn2){
		KModel.config.deleteEdge(btn1.layer,btn2.layer);
		int index=0;
		for(;index<paintPanel.line.size();index++){
			if(paintPanel.line.get(index).btn1==btn1&&paintPanel.line.get(index).btn2==btn2)
			{
				btn1.deleNext(btn2);//???
				paintPanel.line.remove(index);
				break;
			}
		}
	}
	public void toCenter(Layer temp)
	{
		layer = temp;
		canCreate = true;
		eventNumber=0;
	}
}

class MyJPanel extends JPanel{
	ArrayList<LineParameter> line=new ArrayList<LineParameter>();
	int maxY;
	MyButton maxButton;
	public MyJPanel(){
		super();
		maxY = 0;
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
			drawLine(g,btn1_x,btn1_y,btn2_x,btn2_y);
		}
	}
	public void drawLine(Graphics g,int x1,int y1,int x2,int y2){
		int mid_y;
		boolean flag=true;
		if(y2>y1+60){
			y1+=60;
			mid_y=(y1+y2)/2;
		}
		else if(y2<y1-60) {
			y2+=60;
			flag=false;
			mid_y=(y1+y2)/2;
		}
		else{
			mid_y=(y1+y2)/2-60;
		}
		g.drawLine(x1,y1,x1,mid_y);
		g.drawLine(x1,mid_y,x2,mid_y);
		g.drawLine(x2,mid_y,x2,y2);
		if(flag){
			g.drawLine(x2,y2,x2+5,y2-5);
			g.drawLine(x2,y2,x2-5,y2-5);
		}
		else{
			g.drawLine(x2,y2,x2+5,y2+5);
			g.drawLine(x2,y2,x2-5,y2+5);
		}
	}
}

class LineParameter {

	public MyButton btn1;
	public MyButton btn2;

	LineParameter(MyButton from, MyButton to) {
		btn1 = from;
		btn2 = to;
	}

}

class MyActionListener implements ActionListener {
	Center center;

	public MyActionListener(Center _center) {
		center = _center;
	}

	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button == center.button1) {
			if (center.canCreate) {
//				System.out.println(dense.getString("name"));
				MyButton mb =new MyButton(center.layer.getName(),center.layer, center);
				center.KModel.config.addLayer(center.layer);
				mb.setSize(120, 60);
				mb.setLocation(500, 40);
				mb.x = 500;//???
				mb.y = 40;//???
				if(center.paintPanel.maxButton == null)
				{
					center.paintPanel.maxButton = mb;
					center.paintPanel.maxY = 40;
					center.paintPanel.setPreferredSize(new Dimension(0,140));
				}
				//mb.setVisible(true);
				mb.addActionListener(new MyActionListener(center));
				center.paintPanel.add(mb);
				center.SO.setButton(mb);//???
				SwingUtilities.invokeLater(() -> {
					center.updateUI();
				});
				center.canCreate = false;
				center.eventNumber=0;
			}
		}
		else if(button==center.button2){
			center.isSelected=false;
			center.tmpButton=null;
			center.eventNumber=center.Time2AddLine;
		}
		else if(button==center.button3){
			center.isSelected=false;
			center.tmpButton=null;
			center.eventNumber=center.Time2DeleteLine;
		}
		else{
			if(center.eventNumber!=0){
				if(center.isSelected==false){
					center.tmpButton=(MyButton) button;
					center.isSelected=true;
				}
				else{
					System.out.println(center.tmpButton.getText());
					center.ModifyLine(center.tmpButton,(MyButton) button,center.eventNumber);
					center.tmpButton=null;
					center.isSelected=false;
					center.eventNumber=0;
				}
			}
		}
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
			center.rightBar.refresh(sourceBtn.layer);
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			//System.out.println("lll");
			MyButton sourceBtn = (MyButton)e.getSource();
			//System.out.println(sourceBtn);
			center.KModel.config.deleteLayer(sourceBtn.layer);

			boolean flag=true;
			while(flag){
				flag=false;
				int index=0;
				for(;index<center.paintPanel.line.size();index++){
					if(center.paintPanel.line.get(index).btn1==sourceBtn||center.paintPanel.line.get(index).btn2==sourceBtn)
					{
						center.paintPanel.line.remove(index);
						flag=true;
					}
				}}

			ArrayList<MyButton> op = center.SO.getButton();
			for(MyButton bt:op){
				bt.deleNext(sourceBtn);
			}
			center.SO.deleButton(sourceBtn);//???????
			center.paintPanel.remove(sourceBtn);
			//center.paintPanel.revalidate();
			SwingUtilities.invokeLater(() -> {
				center.paintPanel.repaint();
			});
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
		SwingUtilities.invokeLater(() -> {
			center.paintPanel.repaint();
		});
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
		int x = p.x + (e.getX() - origin.x);
		int y = p.y + (e.getY() - origin.y);
		this.frame.setLocation(x, y);
		this.frame.x = x;//???
		this.frame.y = y;//???
		if(center.paintPanel.maxY < y)
		{
			center.paintPanel.maxButton = this.frame;
			center.paintPanel.maxY = y;
			center.paintPanel.setPreferredSize(new Dimension(0,y+100));
		}
		SwingUtilities.invokeLater(() -> {
			center.paintPanel.repaint();
		});
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

}


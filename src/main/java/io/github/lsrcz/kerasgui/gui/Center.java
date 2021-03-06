package io.github.lsrcz.kerasgui.gui;

import io.github.lsrcz.kerasgui.fileio.SaveObject;
import io.github.lsrcz.kerasgui.layers.layers.Layer;
import io.github.lsrcz.kerasgui.layers.model.Model;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
/**
 *  This is the center of the GUI, providing several operation
 * @author Chun Ning, Jiayu Chen
 */
public class Center extends JPanel {
	final int Time2AddLine=2;
	final int Time2DeleteLine=3;
	public Layer layer;
	public boolean canCreate;
	public JButton button1, button2, button3;
	public MyJPanel paintPanel;
	public JPanel pnlBottom;
	public static Model KModel;
	public JPanel pnlHead;
	public RightBar rightBar;
	public int eventNumber=0;
	public boolean isSelected=false;
	public MyButton tmpButton=null;
	public SaveObject SO;
	public Editor MyEditor;
	public Center(RightBar _rightBar, SaveObject _SO,Editor _editor)
	{
		rightBar = _rightBar;
		MyEditor=_editor;
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
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BorderLayout());
		add("North", pnlHead);
		add("Center", new JScrollPane(paintPanel));
		setFocusable(true);
		setVisible(true);
	}

	/**
	 * This function is used for loading an archive file
     */

	public void getBack()
	{
		ArrayList<ButtonAttribute> op = SO.getButton();
		ArrayList<MyButton> BtArray=new ArrayList<MyButton>();
		for(ButtonAttribute bt: op)
		{
			MyButton newButton = new MyButton(this,bt);

			newButton.setSize(120,60);
			newButton.setLocation(newButton.BtA.x,newButton.BtA.y);
			newButton.addActionListener(new MyActionListener(this));
			paintPanel.add(newButton);
			BtArray.add(newButton);
		}
		updateUI();
		op = SO.getButton();
		for(MyButton bt: BtArray)
		{
			for(String Name:bt.BtA.next){
				for(MyButton bt2:BtArray){
					if(bt2.getText().equals(Name)){
						paintPanel.line.add(new LineParameter(bt,bt2));
					}
				}
			}
		}
		updateUI();

	}

	/**
	 * This is the function for Addline and DeleteLine
	 * @param btn1 start button
	 * @param btn2	end button
	 * @param eventNumber choose add or delete
	 */
	public void ModifyLine(MyButton btn1,MyButton btn2,int eventNumber) {
		if(eventNumber==Time2AddLine)
		{
			if(KModel.config.addEdge(btn1.layer,btn2.layer)==false)
				return;
			paintPanel.line.add(new LineParameter(btn1,btn2));
			btn1.BtA.addNext(btn2);
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

	/**
	 * delete line and also delete edge in model
	 * @param btn1 start button
	 * @param btn2 end button
	 */
	public void deleteLine(MyButton btn1,MyButton btn2){
		KModel.config.deleteEdge(btn1.layer,btn2.layer);
		int index=0;
		for(;index<paintPanel.line.size();index++){
			if(paintPanel.line.get(index).btn1==btn1&&paintPanel.line.get(index).btn2==btn2)
			{
				btn1.BtA.deleNext(btn2);
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

/**
 *  This class is designed for the painting part in center.
 * @author Chun Ning, Hang Zhang, Jiayu Chen
 */
class MyJPanel extends JPanel{
	ArrayList<LineParameter> line=new ArrayList<LineParameter>();
	// Store the information of the maximum Y value of all buttons
	int maxY;
	// Store the button which has the maximum Y value
	MyButton maxButton;
	public MyJPanel(){
		super();
		maxY = 0;
	}

	/**
	 * override function paint to make the line permanent display
	 * @param g
	 */
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

	/**
	 * draw three broken lines
	 * @param g
	 * @param x1 start point x
	 * @param y1 start point y
	 * @param x2 end point x
	 * @param y2 end point y
	 */
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
/**
 *  This class is used for storage line parameter
 * @author Chun Ning
 */
class LineParameter {

	public MyButton btn1;
	public MyButton btn2;

	LineParameter(MyButton from, MyButton to) {
		btn1 = from;
		btn2 = to;
	}

}
/**
 *  This is the class for response the button
 * @author Jiayu Chen, Chun Ning
 */
class MyActionListener implements ActionListener {
	Center center;

	public MyActionListener(Center _center) {
		center = _center;
	}

	/**
	 * Response to all button events
	 * @param e to know which button you choose
	 */
	public void actionPerformed(ActionEvent e) {
		Object button = e.getSource();
		if (button == center.button1) {
			if (center.canCreate) {
				MyButton mb =new MyButton(center.layer.getName(),center.layer, center);
                Center.KModel.config.addLayer(center.layer);
				mb.setSize(120, 60);
				mb.setLocation(500, 40);
				mb.BtA.x = 500;
				mb.BtA.y = 40;
				/*
					Resize the size of the paintPanel if necessary and store the information about maxY
					and the button whose Y is the maximum.
				*/
				if(center.paintPanel.maxButton == null)
				{
					center.paintPanel.maxButton = mb;
					center.paintPanel.maxY = 40;
					center.paintPanel.setPreferredSize(new Dimension(0,140));
				}
				mb.addActionListener(new MyActionListener(center));
				center.paintPanel.add(mb);
				center.SO.setButton(mb.BtA);
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

/**
 *  This is the class for response the mouse action
 *  Mainly including the mouse-click and mouse-drag event
 * @author Jiayu Chen, Chun Ning
 */

class MouseEventListener implements MouseInputListener {
	Point origin;
	MyButton frame;
	Center center;
	
	public MouseEventListener(MyButton frame, Center _center) {
		this.frame = frame;
		origin = new Point();
		center = _center;
	}
	
	@Override
	/**
	 * Left-click for displaying the information and Right-click for deleting the button
	 * @param e get Left-click and Right-click
	 */
	public void mouseClicked(MouseEvent e) {

		if(e.getButton() == MouseEvent.BUTTON1)
		{
			MyButton sourceBtn = (MyButton)e.getSource();
			center.rightBar.refresh(sourceBtn.layer);
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			MyButton sourceBtn = (MyButton)e.getSource();
            Center.KModel.config.deleteLayer(sourceBtn.layer);
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

			ArrayList<ButtonAttribute> op = center.SO.getButton();
			for(ButtonAttribute bt:op){
				bt.deleNext(sourceBtn);
			}
			center.SO.deleButton(sourceBtn.BtA);
			center.paintPanel.remove(sourceBtn);
			SwingUtilities.invokeLater(() -> {
				center.paintPanel.repaint();
			});
		}
	}

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

	@Override
	public void mouseEntered(MouseEvent e) {
		this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	/**
	 * drag the button
	 * @param e get location
	 */
	public void mouseDragged(MouseEvent e) {
		Point p = this.frame.getLocation();
		int x = p.x + (e.getX() - origin.x);
		int y = p.y + (e.getY() - origin.y);
		this.frame.setLocation(x, y);
		this.frame.BtA.x = x;
		this.frame.BtA.y= y;
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


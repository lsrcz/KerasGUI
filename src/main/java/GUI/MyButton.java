package GUI;

import java.util.ArrayList;

import javax.swing.JButton;

import layers.layers.Layer;

public class MyButton extends JButton
{
    public Layer layer;
    Center center;
    public int x, y;
    public ArrayList<String> next;
    public MyButton(String text,Layer _layer, Center _center)
    {
        super(text);
        layer = _layer;
        center = _center;
        MouseEventListener mouseListener = new MouseEventListener(this, center);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        next = new ArrayList<String>();
    }

    public MyButton(String text,Layer _layer, Center _center, int _x, int _y, ArrayList<String> _next)
    {
        super(text);
        layer = _layer;
        center = _center;
        MouseEventListener mouseListener = new MouseEventListener(this, center);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        x = _x; y = _y;
        next = _next;
    }

    public void addNext(MyButton _next)
    {
        String n_name = _next.getText();
        next.add(n_name);
    }

    public void deleNext(MyButton _next)
    {
        String n_name = _next.getText();
        for(int i=0;i<next.size();i++)
        {
            if(next.get(i)==n_name)
            {
                next.remove(i);
                break;
            }
        }
    }
}
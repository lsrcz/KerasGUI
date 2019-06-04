package GUI;

import java.util.ArrayList;

import javax.swing.JButton;

import layers.layers.Layer;

public class MyButton extends JButton
{
    public Layer layer;
    Center center;
    ButtonAttribute BtA;
    public MyButton(String text,Layer _layer, Center _center)
    {

        super(text);
        BtA=new ButtonAttribute();
        layer = _layer;
        BtA.LayerName=layer.getName();
        center = _center;
        MouseEventListener mouseListener = new MouseEventListener(this, center);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        BtA.next = new ArrayList<String>();
    }

    public MyButton(Center _center,ButtonAttribute _BtA)
    {
        super(_BtA.LayerName);
        center = _center;
        MouseEventListener mouseListener = new MouseEventListener(this, center);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        BtA=_BtA;
        layer=center.KModel.config.layerFromName(BtA.LayerName);
    }


}
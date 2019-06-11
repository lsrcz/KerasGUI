package io.github.lsrcz.gui;

import java.io.Serializable;
import java.util.ArrayList;

public class ButtonAttribute implements Serializable {
    public int x=0, y=0;
    public ArrayList<String> next;
    public String LayerName=null;
    ButtonAttribute(){
        ArrayList<String> next=new ArrayList<String>();
    }
    ButtonAttribute(int _x, int _y, ArrayList<String> _next,String _LayerName){
        x = _x; y = _y;
        next = _next;
        LayerName=_LayerName;
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

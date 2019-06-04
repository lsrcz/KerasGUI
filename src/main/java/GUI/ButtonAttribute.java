package GUI;

import java.util.ArrayList;

public class ButtonAttribute {
    public int x, y;
    public ArrayList<String> next;
    public String LayerName;
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

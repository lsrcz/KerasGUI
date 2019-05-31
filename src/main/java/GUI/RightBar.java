package GUI;

import layers.ConfigurableObject;
import layers.layers.Dense;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.ArrayList;

public class RightBar extends JPanel {
    ConfigurableObject object;
    RightBar()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        //setSize(400, 720);
        setFocusable(true);
    }
    void refresh(ConfigurableObject object)
    {
        this.removeAll();
        this.object = object;
        ArrayList<String> configureList = object.getConfigurableObject("config").getConfigureList();
        JPanel temp = new JPanel();
        temp.setLayout(new FlowLayout());
        temp.add(new JLabel("Name:"));
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150,30));
        temp.add(textField);
        add(temp);
        getConfig(object.getConfigurableObject("config"), 0);
        updateUI();
    }

    JPanel getPanel(ConfigurableObject obj, String str, int depth){
        JPanel temp = new JPanel();
        String name = "";
        for(int i = 0; i < depth; i++)
            name += " ";
        temp.add(new JLabel(name + str + ":"));
        if(obj.isNullableIntegerConfig(str))
        {
            System.out.println(str);
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            JTextField textField = new JTextField("null");
            checkBox.addChangeListener((e)->{
                JCheckBox source = (JCheckBox) e.getSource();
                if(source.isSelected())
                    textField.setEnabled(true);
                else
                    textField.setEnabled(false);
            });
            textField.setPreferredSize(new Dimension(150,30));
            textField.setEnabled(false);
            temp.add(checkBox);
            temp.add(textField);
        }
        if(obj.isIntegerConfig(str))
        {
            System.out.println(str);
            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(150,30));
            temp.add(textField);
        }
        if(obj.isDoubleConfig(str))
        {
            System.out.println(str);
            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(150,30));
            temp.add(textField);
        }
        if(obj.isConfigurableObjectConfig(str))
        {
            System.out.println(str);
            String[] selections = object.getConfigurableObject("config").getSelection(str);
            if(selections != null){
                temp.add(new JComboBox(selections));
            }else{
                JTextField textField = new JTextField("config");
                textField.setPreferredSize(new Dimension(150,30));
                temp.add(textField);
            }
        }
        if(obj.isBooleanConfig(str))
        {
            String[] tempString = new String[]{"True", "False"};
            temp.add(new JComboBox(tempString));
        }
        if(obj.isStringConfig(str))
        {
            System.out.println(str);
            String[] selections = object.getConfigurableObject("config").getSelection(str);
            if(selections != null){
                temp.add(new JComboBox(selections));
            }else{
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(150,30));
                temp.add(textField);
            }
        }
        return temp;
    }

    void getConfig(ConfigurableObject denseConfig, int depth)
    {
        ConfigurableObject config = null;
        for (String str : denseConfig.getConfigureList()) {
            if(!str.equals("config"))
                add(getPanel(denseConfig, str, depth));
            config = denseConfig.getConfigurableObject(str);
            if(config != null)
                getConfig(config, depth + 1);
        }
    }
}
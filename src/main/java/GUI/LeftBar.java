package GUI;

import layers.ConfigurableObject;
import layers.layers.Dense;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LeftBar extends JPanel {
    LeftBar(RightBar rightBar)
    {
        setLayout(new FlowLayout());
        JButton dense = new JButton("Dense");
        dense.setPreferredSize(new Dimension(150, 30));
        dense.addActionListener((e)->{
            Dense temp = new Dense();
            //toCenter(temp);
            rightBar.refresh(temp);
        });
        JButton test = new JButton("test");
        test.setPreferredSize(new Dimension(150, 30));
        add(dense);
        add(test);
        setFocusable(true);
    }
}

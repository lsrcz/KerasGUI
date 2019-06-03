package GUI;

import layers.layers.Conv1D;
import layers.layers.Dense;

import javax.swing.*;
import java.awt.*;

public class LeftBar extends JPanel {
    LeftBar(RightBar rightBar, Center center) {
        setLayout(new FlowLayout());
        JButton dense = new JButton("Dense");
        dense.setPreferredSize(new Dimension(150, 30));
        dense.addActionListener((e) -> {
            Dense temp = new Dense();
            temp.init();
            center.toCenter(temp);
            rightBar.refresh(temp);
        });
        JButton conv1d = new JButton("Conv1D");
        conv1d.setPreferredSize(new Dimension(150, 30));
        conv1d.addActionListener((e) -> {
            Conv1D temp = new Conv1D();
            temp.init();
            center.toCenter(temp);
            rightBar.refresh(temp);
        });
        JButton test = new JButton("test");
        test.setPreferredSize(new Dimension(150, 30));
        add(dense);
        add(conv1d);
        add(test);
        setFocusable(true);
    }
}

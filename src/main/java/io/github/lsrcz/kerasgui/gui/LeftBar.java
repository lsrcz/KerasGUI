package io.github.lsrcz.kerasgui.gui;

import io.github.lsrcz.kerasgui.layers.layers.Add;
import io.github.lsrcz.kerasgui.layers.layers.Conv1D;
import io.github.lsrcz.kerasgui.layers.layers.Dense;
import io.github.lsrcz.kerasgui.layers.layers.InputLayer;

import javax.swing.*;
import java.awt.*;

/**
 *  This is the left bar of the GUI, providing selections for add layers into the network
 * @author Hang Zhang
 */
public class LeftBar extends JPanel {
    LeftBar(RightBar rightBar, Center center) {
        setLayout(new FlowLayout());
        JButton dense = new JButton("Dense");
        dense.setPreferredSize(new Dimension(150, 30));
        dense.addActionListener((e) -> {
            Dense temp = new Dense();
            temp.init();
            // Send new information of the new layer to the center part
            center.toCenter(temp);
            // Show the information of the layer in the right bar part
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
        JButton inputLayer = new JButton("Input Layer");
        inputLayer.setPreferredSize(new Dimension(150, 30));
        inputLayer.addActionListener((e) -> {
            InputLayer temp = new InputLayer();
            temp.init();
            center.toCenter(temp);
            rightBar.refresh(temp);
        });
        JButton addLayer = new JButton("Add");
        addLayer.setPreferredSize(new Dimension(150, 30));
        addLayer.addActionListener((e) -> {
            Add temp = new Add();
            temp.init();
            center.toCenter(temp);
            rightBar.refresh(temp);
        });
        add(dense);
        add(conv1d);
        add(inputLayer);
        add(addLayer);
        setFocusable(true);
    }
}

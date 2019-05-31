package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    GUI()
    {
        super("Keras GUI");

        setLayout(new BorderLayout());
        RightBar rightBar = new RightBar();
        JScrollPane rightScrollPane = new JScrollPane(rightBar);
        Center center = new Center();
        LeftBar leftBar = new LeftBar(rightBar);
        add("East", rightScrollPane);
        add("West", new JScrollPane(leftBar));
        add("Center", center);
        leftBar.setPreferredSize(new Dimension(250,0));
        rightBar.setPreferredSize(new Dimension(250, 0));
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new GUI();
    }
}

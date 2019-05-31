package GUI;

import javax.swing.*;
import java.awt.*;

public class RightBar extends JPanel {
    RightBar()
    {
        JLabel score = new JLabel("Your score is:");
        setBorder(BorderFactory.createEtchedBorder());
        add(score);
        setLayout(new FlowLayout());
        //setSize(400, 720);
        setFocusable(true);
    }
}

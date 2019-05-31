package GUI;

import javax.swing.*;
import java.awt.*;

public class Center extends JPanel {
    Center()
    {
        JLabel score = new JLabel("Your score is:");
        setBorder(BorderFactory.createEtchedBorder());
        add(score);
        setLayout(new FlowLayout());
        setFocusable(true);
    }
}

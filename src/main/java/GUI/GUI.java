package GUI;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import java.awt.*;

import fileio.SaveObject;

import fileio.*;

public class GUI extends JFrame {
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem editorItem;
    JMenuItem save;
    SaveObject SO;
    Editor editor;
    GUI()
    {
        super("Keras GUI");

        setLayout(new BorderLayout());
        SO = new SaveObject();
        RightBar rightBar = new RightBar();
        JScrollPane rightScrollPane = new JScrollPane(rightBar);
        Center center = new Center(rightBar, SO);
        LeftBar leftBar = new LeftBar(rightBar, center);
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        editorItem = new JMenuItem("Open Python Editor");
        save = new JMenuItem("Save");
        add("East", rightScrollPane);
        add("West", new JScrollPane(leftBar));
        add("Center", center);
        leftBar.setPreferredSize(new Dimension(250, 0));
        rightScrollPane.setPreferredSize(new Dimension(250, 0));
        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(editorItem);
        menu.add(save);
        editor = new Editor();
        editor.init();
        editorItem.addActionListener((e) -> {
            editor.setVisible(true);
        });
        save.addActionListener((e) ->{
            SaveObject saveObject = new SaveObject();

        });

        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            new GUI();
        });
    }
}

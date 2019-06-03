package GUI;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GUI extends JFrame {
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem editor;
    JMenuItem save;
    GUI()
    {
        super("Keras GUI");

        setLayout(new BorderLayout());
        RightBar rightBar = new RightBar();
        JScrollPane rightScrollPane = new JScrollPane(rightBar);
        Center center = new Center(rightBar);
        LeftBar leftBar = new LeftBar(rightBar, center);
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        editor = new JMenuItem("Open Python Editor");
        save = new JMenuItem("Save");
        add("East", rightScrollPane);
        add("West", new JScrollPane(leftBar));
        add("Center", center);
        leftBar.setPreferredSize(new Dimension(250, 0));
        rightScrollPane.setPreferredSize(new Dimension(250, 0));
        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(editor);
        menu.add(save);
        editor.addActionListener((e) -> {
            class MyThread extends Thread{
                @Override
                public void run() {
                    super.run();
                    SwingUtilities.invokeLater(() -> {
                        //WebLookAndFeel.install();
                        new Editor().init();
                    });
                }
            }
            Thread thread = new MyThread();
            thread.start();
            /*SwingUtilities.invokeLater(() -> {
                //WebLookAndFeel.install();
                new Editor().init();
            });*/
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

package GUI;

import com.alee.laf.WebLookAndFeel;
import fileio.SaveObject;
import layers.UniqueNameGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


/**
 *  This is core part of the GUI, initalize all part of the gui.
 * @author Hang Zhang
 */
public class GUI extends JFrame {
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem editorItem;
    JMenuItem load;
    JMenuItem save;
    JMenuItem saveAt;
    SaveObject SO;
    Editor editor;
    Center center;

    private String fileName = null;

    GUI() {
        super("Keras GUI[Not saved]");

        editor = new Editor(this);
        editor.init();
        setLayout(new BorderLayout());
        SO = new SaveObject();
        RightBar rightBar = new RightBar(editor);
        JScrollPane rightScrollPane = new JScrollPane(rightBar);
        center = new Center(rightBar, SO,editor);
        LeftBar leftBar = new LeftBar(rightBar, center);
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        editorItem = new JMenuItem("Open Python Editor");
        saveAt = new JMenuItem("Save as");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        add("East", rightScrollPane);
        add("West", new JScrollPane(leftBar));
        add("Center", center);
        leftBar.setPreferredSize(new Dimension(250, 0));
        rightScrollPane.setPreferredSize(new Dimension(250, 0));
        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(editorItem);
        menu.add(save);
        menu.add(saveAt);
        menu.add(load);
        editorItem.addActionListener((e) -> {
            editor.refresh();
            editor.setVisible(true);
        });
        save.addActionListener((e) -> {
            save();
        });
        load.addActionListener((e) -> {
            load();
        });
        saveAt.addActionListener((e) -> {
            saveNew();
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

    /**
     * Save a new file
     */
    public void saveNew() {
        String newFileName = JOptionPane.showInputDialog("Please input file name:  ");
        if (newFileName != null) {
            fileName = newFileName;
            save();
        }
    }

    public void save() {
        if (fileName == null) {
            fileName = JOptionPane.showInputDialog("Please input file name： ");
        }
        // if cancel
        if(fileName == null)
            return;
        // update the title
        this.setTitle("Keras GUI[" + fileName + "]");
        // Save what we need
        try {
            center.SO.setModel(Center.KModel);
            center.SO.setEditorContents(editor.getTextPane());
            center.SO.setNameGenerator(UniqueNameGenerator.getInstance());
            center.SO.toFile(fileName + ".obj");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void load(){
        String tempFileName = JOptionPane.showInputDialog("Please input file name： ");
        // if cancel
        if (tempFileName == null)
            return;
        else
            fileName = tempFileName;
        // Update the title
        this.setTitle("Keras GUI[" + fileName + "]");
        // Get all the information
        try{
            center.SO = SaveObject.fromFile(fileName + ".obj");
        }catch (IOException ex){
            ex.printStackTrace();
        }
        // Setting what we want
        center.paintPanel.removeAll();
        center.paintPanel.line.clear();
        Center.KModel = center.SO.getModel();
        editor.setTextPane(center.SO.getEditorContents());
        UniqueNameGenerator.updateInstance(center.SO.getNameGenerator());
        center.getBack();
        editor.refresh();
    }

    public String getFileName() {
        return fileName;
    }
}

package GUI;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fileio.SaveObject;

import fileio.*;

public class GUI extends JFrame {
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem editorItem;
    JMenuItem load;
    JMenuItem save;
    SaveObject SO;
    Editor editor;
    GUI()
    {
        super("Keras GUI");

        editor = new Editor(this);
        editor.init();
        setLayout(new BorderLayout());
        SO = new SaveObject();
        RightBar rightBar = new RightBar(editor);
        JScrollPane rightScrollPane = new JScrollPane(rightBar);
        Center center = new Center(rightBar, SO,editor);
        LeftBar leftBar = new LeftBar(rightBar, center);
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        editorItem = new JMenuItem("Open Python Editor");
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
        menu.add(load);
        editorItem.addActionListener((e) -> {
            editor.setVisible(true);
        });
        save.addActionListener((e) ->{
            save();
        });
        /*load.addActionListener((e)->{
            JFileChooser fileChooser = new JFileChooser();
            FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
            System.out.println(fsv.getHomeDirectory());                //得到桌面路径
            fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
            fileChooser.setDialogTitle("请选择要上传的文件...");
            fileChooser.setApproveButtonText("确定");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            result = fileChooser.showOpenDialog(chatFrame);
            if (JFileChooser.APPROVE_OPTION == result) {
                path=fileChooser.getSelectedFile().getPath();
                System.out.println("path: "+path);
            }
        });*/

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

    public void save(){
        String fileName = editor.getObjName();
        if(fileName == null) {
            fileName = JOptionPane.showInputDialog("Please input file name： ");
            editor.setObjName(fileName);
        }
            /*try {
                SO.toFile(fileName + ".obj");
            } catch (IOException ex) {
                ex.printStackTrace();
            }*/
    }
}

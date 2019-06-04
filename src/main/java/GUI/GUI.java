package GUI;

import com.alee.laf.WebLookAndFeel;
import fileio.SaveObject;
import layers.UniqueNameGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
        super("Keras GUI");

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
        saveAt = new JMenuItem("Save at");
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
        if(fileName == null)
            return;
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
        if (tempFileName == null)
            return;
        else
            fileName = tempFileName;
        try{
            center.SO = SaveObject.fromFile(fileName + ".obj");
        }catch (IOException ex){
            ex.printStackTrace();
        }
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

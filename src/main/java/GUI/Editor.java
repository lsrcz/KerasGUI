package GUI;

import com.alee.laf.WebLookAndFeel;
import layers.model.Model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;


public class Editor extends JFrame {

    public JTextPane textPane, modelTextPane;
    public JScrollPane scrollPane, modelScrollPane;
    public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem saveItem;
    public JMenuItem runItem;
    public String osName;
    public String objName;
    public GUI gui;

    // public String currentFileName;
    public final static String[] keyWord = new String[]{"False", "None", "True", "and", "as", "assert", "break", "class", "continue",
            "def", "del", "elif", "else", "except", "finally", "for", "from", "global", "if", "import", "in", "is",
            "lambda", "nonlocal", "not", "or", "pass", "raise", "return", "try", "while", "with", "yield"};
    public final static String[] builtinFunction = new String[]{"abs", "dict", "help", "min", "setattr", "all", "dir", "hex", "next",
            "slice", "any", "divmod", "id", "object", "sorted", "ascii", "enumerate", "input", "oct",
            "staticmethod", "bin", "eval", "int", "open", "str", "bool", "exec", "isinstance", "ord", "sum",
            "bytearray", "filter", "issubclass", "pow", "super", "bytes", "float", "iter", "print", "tuple",
            "callable", "format", "len", "property", "type", "chr", "frozenset", "list", "range", "vars",
            "classmethod", "getattr", "locals", "repr", "zip", "compile", "globals", "map", "reversed",
            "__import__", "complex", "hasattr", "max", "round", "delattr", "hash", "memoryview", "set"};


    public Editor(GUI gui) {
        this.gui = gui;
        objName = null;
        textPane = new JTextPane();
        scrollPane = new JScrollPane(textPane);
        modelTextPane = new JTextPane();
        modelScrollPane = new JScrollPane(modelTextPane);
        menuBar = new JMenuBar();
        menu = new JMenu("Mean");
        // saveItem = new JMenuItem("保存");
        runItem = new JMenuItem("Run");
        if (System.getProperty("os.name").indexOf("Windows") != -1) osName = "Windows";
        else if (System.getProperty("os.name").indexOf("Linux") != -1) osName = "Linux";
        else osName = "Mac";
    }

    public void refresh(){
        modelTextPane.setText(Center.KModel.dumpJSON());
    }

    public void init() {

        Model model = new Model();
        System.out.println(model.dumpJSON());
        setLayout(new BorderLayout());
        add(modelScrollPane, BorderLayout.NORTH);
        modelTextPane.setEditable(false);
        modelTextPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (osName.equals("Windows")) checkStyleWin(modelTextPane);
                else if (osName.equals("Mac")) checkStyleMac(modelTextPane);
                else checkStyleLinux(modelTextPane);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (osName.equals("Windows")) checkStyleWin(modelTextPane);
                else if (osName.equals("Mac")) checkStyleMac(modelTextPane);
                else checkStyleLinux(modelTextPane);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        String head = "import tensorflow as tf\n" + "model = tf.keras.models.model_from_json('''\n" +
                model.dumpJSON() + "''')";
        modelTextPane.setText(head);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                String inputText = textPane.getText();
                int caretPosition = textPane.getCaretPosition();
                if (osName.equals("Windows")) {
                    for (int i = 0; i < inputText.length(); i++) {
                        if (inputText.charAt(i) == '\n') {
                            caretPosition += 1;
                        }
                    }
                }
                char inputChar = textPane.getText().charAt(caretPosition);
//                System.out.println((int)inputChar);
                if ((inputChar == '\n' && !osName.equals("Mac")) || (inputChar == '\r' && osName.equals("Mac"))) {
                    String[] inputTextSplitedByEnter = inputText.split("[\r\n]+");
                    String lastLine = inputTextSplitedByEnter[inputTextSplitedByEnter.length - 1];
                    int spacesInFrontOfLastLine = countSpacesInFont(lastLine);
                    int spacesThisLine;
                    if (needIndents(lastLine)) {
                        spacesThisLine = spacesInFrontOfLastLine + 4;
                    } else {
                        spacesThisLine = spacesInFrontOfLastLine;
                    }
                    new Thread(() -> {
                        for (int i = 0; i < spacesThisLine; i++) {
                            try {
                                textPane.getDocument().insertString(textPane.getCaretPosition() + 1,
                                        " ", MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet));
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                        }
                        textPane.setCaretPosition(textPane.getDocument().getLength());
                    }).start();
                } else if (inputChar == '\'' || inputChar == '\"') {
                    if (countChar(inputText, inputChar) % 2 != 0) {
                        new Thread(() -> {
                            try {
                                textPane.getDocument().insertString(textPane.getCaretPosition(),
                                        String.valueOf(inputChar),
                                        MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet));
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    }
                } else if (inputChar == '(') {
                    new Thread(() -> {
                        try {
                            textPane.getDocument().insertString(textPane.getCaretPosition() + 1,
                                    ")", MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet));
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                } else {
                }
                if (osName.equals("Windows")) checkStyleWin(textPane);
                else if (osName.equals("Mac")) checkStyleMac(textPane);
                else checkStyleLinux(textPane);
//                System.out.println("insert");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (osName.equals("Windows")) checkStyleWin(textPane);
                else if (osName.equals("Mac")) checkStyleMac(textPane);
                else checkStyleLinux(textPane);
//                System.out.println("remove");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
//                System.out.println("changed");
            }
        });

        setJMenuBar(menuBar);
        menuBar.add(menu);
        // menu.add(saveItem);
        menu.add(runItem);
        /*
        saveItem.addActionListener(e -> {
            currentFileName =
            currentFileName = JOptionPane.showInputDialog("请输入文件名： ");
            try {
                File file = new File(currentFileName + ".py");
                FileWriter fileWriter = new FileWriter(file.getName());
                fileWriter.write(modelTextPane.getText() + "\n" + textPane.getText());
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            setTitle("python编辑器    " + currentFileName + ".py");
        });
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        */

        runItem.addActionListener(e -> {
            gui.save();
            String currentFileName = gui.getFileName();
            try {
                CallPython(currentFileName + ".py");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));

        setSize(1280, 960);
        setTitle("python编辑器");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        //setVisible(true);
    }

    private void checkStyleMac(JTextPane pane) {
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> builtins = new ArrayList<>();
        for (String keyword : keyWord) keywords.add(keyword);
        for (String builtin : builtinFunction) builtins.add(builtin);

        String inputText = pane.getText();
        for (int i = 0; i < inputText.length(); i++) {
            // 装饰器橙色，注释灰色，关键字青色，内置函数红色，数字绿色，字符串橙色，普通字体黑色
            if (inputText.charAt(i) == '@') {
                int end, begin = i;
                for (int j = begin; ; j++) {
                    if (isSpace(inputText.charAt(j)) || j == inputText.length() - 1) {
                        end = j;
                        break;
                    }
                }
                setCharacterAttributes(begin, end - begin + 1,
                        MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                i = end;
                continue;
            } else if (inputText.charAt(i) == '#') {
                int end, begin = i;
                for (int j = begin; ; j++) {
                    if (inputText.charAt(j) == '\r' || j == inputText.length() - 1) {
                        end = j;
                        break;
                    }
                }
                setCharacterAttributes(begin, end - begin + 1,
                        MyAttributeSet.getAttribute(MyAttributeSet.grayAttributeSet), pane);
                i = end;
                continue;
            } else if (isCharacter(inputText.charAt(i))) {
                int end, begin = i;
                boolean isKeyWord = false;
                for (int j = begin; ; j++) {
                    if (!isCharacter(inputText.charAt(j)) || j == inputText.length() - 1) {
                        if (!isCharacter(inputText.charAt(j))) {
                            end = j;
                        } else {
                            end = j + 1;
                        }
                        break;
                    }
                }
                String word = inputText.substring(begin, end);
                for (String keyWord : keywords) {
                    if (word.equals(keyWord)) {
                        setCharacterAttributes(begin, end - begin,
                                MyAttributeSet.getAttribute(MyAttributeSet.cyanAttributeSet), pane);
                        isKeyWord = true;
                        break;
                    }
                }
                for (String builtin : builtins) {
                    if (word.equals(builtin)) {
                        setCharacterAttributes(begin, end - begin,
                                MyAttributeSet.getAttribute(MyAttributeSet.redAttributeSet), pane);
                        isKeyWord = true;
                        break;
                    }
                }
                if (isKeyWord) {
                    i = end - 1;
                    continue;
                }
                setCharacterAttributes(begin, end - begin,
                        MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet), pane);
                i = end - 1;
                continue;
            } else if (isNumber(inputText.charAt(i))) {
                int begin = i;
                setCharacterAttributes(begin, 1, MyAttributeSet.getAttribute(MyAttributeSet.greenAttributeSet), pane);

            } else if (inputText.charAt(i) == '\'' || inputText.charAt(i) == '\"') {
                int end, begin = i;
                if (i == inputText.length() - 1) {
                    setCharacterAttributes(begin, 1,
                            MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                    continue;
                }
                for (int j = begin + 1; ; j++) {
                    if (j == inputText.length() - 1 || inputText.charAt(j) == '\'' || inputText.charAt(j) == '\"') {
                        if (j == inputText.length() - 1) {
                            end = j;
                        } else {
                            end = j + 1;
                        }
                        break;
                    }
                }
                setCharacterAttributes(begin, end - begin,
                        MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                i = end - 1;
                continue;
            } else {
                int begin = i;
                setCharacterAttributes(begin, 1, MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet), pane);
            }
        }
    }

    private void checkStyleLinux(JTextPane pane) {
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> builtins = new ArrayList<>();
        for (String keyword : keyWord) keywords.add(keyword);
        for (String builtin : builtinFunction) builtins.add(builtin);

        String inputText = pane.getText();
        for (int i = 0; i < inputText.length(); i++) {
            // decorator橙色，注释灰色，关键字青色，内置函数红色，数字绿色，字符串橙色，普通字体黑色
            if (inputText.charAt(i) == '@') {
                int end, begin = i;
                for (int j = begin; ; j++) {
                    if (isSpace(inputText.charAt(j)) || j == inputText.length() - 1) {
                        end = j;
                        break;
                    }
                }
                setCharacterAttributes(begin, end - begin + 1,
                        MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                i = end;
                continue;
            } else if (inputText.charAt(i) == '#') {
                int end, begin = i;
                for (int j = begin; ; j++) {
                    if (inputText.charAt(j) == '\n' || j == inputText.length() - 1) {
                        end = j;
                        break;
                    }
                }
                setCharacterAttributes(begin, end - begin + 1,
                        MyAttributeSet.getAttribute(MyAttributeSet.grayAttributeSet), pane);
                i = end;
                continue;
            } else if (isCharacter(inputText.charAt(i))) {
                int end, begin = i;
                boolean isKeyWord = false;
                for (int j = begin; ; j++) {
                    if (!isCharacter(inputText.charAt(j)) || j == inputText.length() - 1) {
                        if (!isCharacter(inputText.charAt(j))) {
                            end = j;
                        } else {
                            end = j + 1;
                        }
                        break;
                    }
                }
                String word = inputText.substring(begin, end);
                for (String keyWord : keywords) {
                    if (word.equals(keyWord)) {
                        setCharacterAttributes(begin, end - begin,
                                MyAttributeSet.getAttribute(MyAttributeSet.cyanAttributeSet), pane);
                        isKeyWord = true;
                        break;
                    }
                }
                for (String builtin : builtins) {
                    if (word.equals(builtin)) {
                        setCharacterAttributes(begin, end - begin,
                                MyAttributeSet.getAttribute(MyAttributeSet.redAttributeSet), pane);
                        isKeyWord = true;
                        break;
                    }
                }
                if (isKeyWord) {
                    i = end - 1;
                    continue;
                }
                setCharacterAttributes(begin, end - begin,
                        MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet), pane);
                i = end - 1;
                continue;
            } else if (isNumber(inputText.charAt(i))) {
                int begin = i;
                setCharacterAttributes(begin, 1, MyAttributeSet.getAttribute(MyAttributeSet.greenAttributeSet), pane);

            } else if (inputText.charAt(i) == '\'' || inputText.charAt(i) == '\"') {
                int end, begin = i;
                if (i == inputText.length() - 1) {
                    setCharacterAttributes(begin, 1,
                            MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                    continue;
                }
                for (int j = begin + 1; ; j++) {
                    if (j == inputText.length() - 1 || inputText.charAt(j) == '\'' || inputText.charAt(j) == '\"') {
                        if (j == inputText.length() - 1) {
                            end = j;
                        } else {
                            end = j + 1;
                        }
                        break;
                    }
                }
                setCharacterAttributes(begin, end - begin,
                        MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                i = end - 1;
                continue;
            } else {
                int begin = i;
                setCharacterAttributes(begin, 1, MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet), pane);
            }
        }
    }

    private void checkStyleWin(JTextPane pane) {
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> builtins = new ArrayList<>();
        for (String keyword : keyWord) keywords.add(keyword);
        for (String builtin : builtinFunction) builtins.add(builtin);

        String inputText = pane.getText();
        for (int i = 0; i < inputText.length(); i++) {
            // decorator橙色，注释灰色，关键字青色，内置函数红色，数字绿色，字符串橙色，普通字体黑色
            if (inputText.charAt(i) == '@') {
                int end, begin = i;
                for (int j = begin; ; j++) {
                    if (isSpace(inputText.charAt(j))) {
                        end = j;
                        break;
                    }
                    if (j == inputText.length() - 1) {
                        end = j + 1;
                        break;
                    }
                }
                int length = end - begin;
                int textBegin = begin;
                for (int k = 0; k < begin; k++) {
                    if (inputText.charAt(k) == '\n') {
                        textBegin--;
                    }
                }
                setCharacterAttributes(textBegin, length,
                        MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                if (end != inputText.length() && inputText.charAt(end) == '\r') i = end + 1;
                else i = end;
                continue;
            } else if (inputText.charAt(i) == '#') {
                int end, begin = i;
                for (int j = begin; ; j++) {
                    if (inputText.charAt(j) == '\r') {
                        end = j;
                        break;
                    }
                    if (j == inputText.length() - 1) {
                        end = j + 1;
                        break;
                    }
                }
                int length = end - begin;
                int textBegin = begin;
                for (int k = 0; k < begin; k++) {
                    if (inputText.charAt(k) == '\n') {
                        textBegin--;
                    }
                }
                System.out.println(textBegin);
                setCharacterAttributes(textBegin, length,
                        MyAttributeSet.getAttribute(MyAttributeSet.grayAttributeSet), pane);
                if (end != inputText.length()) i = end + 1;
                else i = end;
                continue;
            } else if (isCharacter(inputText.charAt(i))) {
                int end, begin = i;
                boolean isKeyWord = false;
                for (int j = begin; ; j++) {
                    if (!isCharacter(inputText.charAt(j)) || j == inputText.length() - 1) {
                        if (!isCharacter(inputText.charAt(j))) {
                            end = j;
                        } else {
                            end = j + 1;
                        }
                        break;
                    }
                }
                String word = inputText.substring(begin, end);
                int textBegin = begin, length = end - begin;
                for (int k = 0; k < begin; k++) {
                    if (inputText.charAt(k) == '\n') {
                        textBegin--;
                    }
                }
                for (String keyWord : keywords) {
                    if (word.equals(keyWord)) {
                        setCharacterAttributes(textBegin, length,
                                MyAttributeSet.getAttribute(MyAttributeSet.cyanAttributeSet), pane);
                        isKeyWord = true;
                        break;
                    }
                }
                for (String builtin : builtins) {
                    if (word.equals(builtin)) {
                        setCharacterAttributes(textBegin, length,
                                MyAttributeSet.getAttribute(MyAttributeSet.redAttributeSet), pane);
                        isKeyWord = true;
                        break;
                    }
                }
                if (isKeyWord) {
                    i = end - 1;
                    continue;
                }
                setCharacterAttributes(textBegin, length,
                        MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet), pane);
                i = end - 1;
                continue;
            } else if (isNumber(inputText.charAt(i))) {
                int begin = i;
                int textBegin = begin;
                for (int k = 0; k < begin; k++) {
                    if (inputText.charAt(k) == '\n') {
                        textBegin--;
                    }
                }
                setCharacterAttributes(textBegin, 1, MyAttributeSet.getAttribute(MyAttributeSet.greenAttributeSet), pane);

            } else if (inputText.charAt(i) == '\'' || inputText.charAt(i) == '\"') {
                int end, begin = i;
                if (i == inputText.length() - 1) {
                    int textBegin = begin;
                    for (int k = 0; k < begin; k++) {
                        if (inputText.charAt(k) == '\n') {
                            textBegin--;
                        }
                    }
                    setCharacterAttributes(textBegin, 1,
                            MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                    continue;
                }
                for (int j = begin + 1; ; j++) {
                    if (j == inputText.length() - 1 || inputText.charAt(j) == '\'' || inputText.charAt(j) == '\"') {
                        if (j == inputText.length() - 1) {
                            end = j;
                        } else {
                            end = j + 1;
                        }
                        break;
                    }
                }
                int textBegin = begin, length = end - begin;
                for (int k = 0; k < begin; k++) {
                    if (inputText.charAt(k) == '\n') {
                        textBegin--;
                    }
                }
                setCharacterAttributes(textBegin, length,
                        MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                i = end - 1;
                continue;
            } else if (!isSpace(inputText.charAt(i))) {
                int begin = i;
                int textBegin = begin;
                for (int k = 0; k < begin; k++) {
                    if (inputText.charAt(k) == '\n') {
                        textBegin--;
                    }
                }
                setCharacterAttributes(textBegin, 1, MyAttributeSet.getAttribute(MyAttributeSet.blackAttributeSet), pane);
            } else {

            }
        }
    }

    private void setCharacterAttributes(int begin, int length, SimpleAttributeSet simpleAttributeSet, JTextPane pane) {
        new Thread(() -> pane.getStyledDocument()
                .setCharacterAttributes(begin, length, simpleAttributeSet, true)).start();
    }

    private boolean needIndents(String lastline) {
        String[] words = lastline.split("[^a-zA-Z]+");
        String[] indentWords = new String[]{"while", "if", "for", "def", "with"};
        for (String word : words) {
            for (String indentWord : indentWords) {
                if (word.equals(indentWord)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int countChar(String text, char Char) {
        int count = 0;
        for (int index = 0; index < text.length(); index++) {
            if (text.charAt(index) == Char) {
                count++;
            }
        }
        return count;
    }

    private int countSpacesInFont(String text) {
        int count = 0;
        for (int index = 0; index < text.length(); index++) {
            if (text.charAt(index) == ' ') {
                count++;
                continue;
            }
            break;
        }
        return count;
    }

    public void CallPython(String path) throws IOException, InterruptedException {

        // define the command string
//        path = "\"" + path + "\"";
        String[] commandStr;
        if (osName.equals("Windows")) {
            commandStr = new String[]{"python", path};
        } else {
            commandStr = new String[]{"python3", path};
        }
        //Create a Process instance and execute commands
        Process pr = Runtime.getRuntime().exec(commandStr);
        //Get the result produced by executing the above commands
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = null;
        String result = "";
        BufferedWriter out = new BufferedWriter(new FileWriter("wdnmd.txt"));
        while ((line = in.readLine()) != null) {
            if (osName.equals("Windows")) result += line + "\r\n";
            else if (osName.equals("Linux")) result += line + "\n";
            else result += line + "\r";
        }
        System.out.println(result);
        out.write(result);
        in.close();
        out.close();
        int endFlag = pr.waitFor();
        if (endFlag == 0) {
            System.out.println("The process ends normally.");
        }
    }

    private boolean isCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isSpace(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            //new Editor(this).init();
        });
//        try {
//            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//            UIManager.put("RootPane.setupButtonVisible", false);
//        } catch (Exception e) {
//
//        }
//        new Editor().init();

    }
}
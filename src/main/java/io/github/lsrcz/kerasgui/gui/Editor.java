package io.github.lsrcz.kerasgui.gui;

import com.alee.laf.WebLookAndFeel;

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

/**
 * this editor contains two button and two panel
 * top panel is uneditable, code in top panel is auto-generated from the graph
 * bottom panel is editable, user can type their code here and use code in top panel
 * one button is to run the code, using your system's interpreter
 * one button is to refresh the code, when user have changed the code
 * the code in panel is highlighted, and can be auto-completed
 *
 * @author Nianqi Liu
 */
public class Editor extends JFrame {

    public JTextPane textPane, modelTextPane;
    public JScrollPane scrollPane, modelScrollPane;
    public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem refreshItem;
    public JMenuItem runItem;
    public String osName;
    public String objName;
    public GUI gui;

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
        runItem = new JMenuItem("Run");
        refreshItem = new JMenuItem("Refresh");
        // check the current system Win or Linux
        if (System.getProperty("os.name").indexOf("Windows") != -1) osName = "Windows";
        else osName = "Linux";
    }

    public void init() {

        setLayout(new BorderLayout());
        add(modelScrollPane, BorderLayout.NORTH);
        modelTextPane.setEditable(false);
        modelScrollPane.setPreferredSize(new Dimension(0, 400));

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(refreshItem);
        menu.add(runItem);
        // add shortcut
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));

        addListener();
        setSize(1280, 960);
        setTitle("python Editor");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    /**
     * add listeners to text panel, run button, refresh button
     *
     * @author Nianqi Liu
     */
    public void addListener() {

        modelTextPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (osName.equals("Windows")) checkStyleWin(modelTextPane);
                else checkStyleLinux(modelTextPane);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (osName.equals("Windows")) checkStyleWin(modelTextPane);
                else checkStyleLinux(modelTextPane);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

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
                if (inputChar == '\n') {
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
                else checkStyleLinux(textPane);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (osName.equals("Windows")) checkStyleWin(textPane);
                else checkStyleLinux(textPane);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        refreshItem.addActionListener(e -> refresh());
        // run the code in current editor
        runItem.addActionListener(e -> {
            gui.save();
            String currentFileName = gui.getFileName();
            try {
                File file = new File(currentFileName + ".py");
                FileWriter fileWriter = new FileWriter(file.getName());
                if (osName.equals("Windows"))
                    fileWriter.write(modelTextPane.getText() + "\r\n" + textPane.getText());
                else
                    fileWriter.write(modelTextPane.getText() + "\n" + textPane.getText());
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                CallPython(currentFileName + ".py");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * highlight specific text in Linux
     * decorator-orange, annotation-gray, keyword-cyan, builtin-red,
     * number-green, string-orange, default-black
     *
     * @author Nianqi Liu
     */
    private void checkStyleLinux(JTextPane pane) {
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> builtins = new ArrayList<>();
        for (String keyword : keyWord) keywords.add(keyword);
        for (String builtin : builtinFunction) builtins.add(builtin);

        String inputText = pane.getText();
        for (int i = 0; i < inputText.length(); i++) {
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
                if ((i - 2) >= 0 && inputText.charAt(i - 1) == '\'' && inputText.charAt(i - 2) == '\'') {
                    int textBegin = begin;
                    setCharacterAttributes(textBegin, 1,
                            MyAttributeSet.getAttribute(MyAttributeSet.orangeAttributeSet), pane);
                    continue;
                }
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

    /**
     * highlight specific text in Win
     * decorator-orange, annotation-gray, keyword-cyan, builtin-red,
     * number-green, string-orange, default-black
     *
     * @author Nianqi Liu
     */
    private void checkStyleWin(JTextPane pane) {
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> builtins = new ArrayList<>();
        for (String keyword : keyWord) keywords.add(keyword);
        for (String builtin : builtinFunction) builtins.add(builtin);

        String inputText = pane.getText();
        for (int i = 0; i < inputText.length(); i++) {
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
                if ((i - 2) >= 0 && inputText.charAt(i - 1) == '\'' && inputText.charAt(i - 2) == '\'') {
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
                //TODO
                //more functions can be added to highlight more different specific text
            }
        }
    }

    /**
     * change specific text's color
     *
     * @author Nianqi Liu
     */
    private void setCharacterAttributes(int begin, int length, SimpleAttributeSet simpleAttributeSet, JTextPane pane) {
        new Thread(() -> pane.getStyledDocument()
                .setCharacterAttributes(begin, length, simpleAttributeSet, true)).start();
    }

    /**
     * check if dentation is needed when you press enter
     *
     * @author Nianqi Liu
     */
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

    /**
     * This is the function for using generated code
     *
     * @author Nianqi Liu, Chun Ning, Sirui Lu
     */
    private void CallPython(String path) throws IOException, InterruptedException {

        // define the command string
        String[] commandStr;
        // different interpreter for different system
        if (osName.equals("Windows")) {
            commandStr = new String[]{"python", path};
        } else {
            commandStr = new String[]{"python3", path};
        }
        //Create a Process instance and execute commands
        Process pr = Runtime.getRuntime().exec(commandStr);
        //Get the result produced by executing the above commands
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
        Thread stdout = new Thread(() -> {
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        stdout.start();
        Thread stderr = new Thread(() -> {
            String line;
            try {
                while ((line = err.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        stderr.start();
        stdout.join();
        stderr.join();
        in.close();
        err.close();
        int endFlag = pr.waitFor();
        if (endFlag == 0) {
            System.out.println("The process ends normally.");
        }
    }

    /**
     * refresh editor when I change the graph
     *
     * @author Nianqi Liu
     */
    public void refresh() {
        String head;
        if (osName.equals("Windows"))
            head = "import tensorflow as tf\r\n" + "model = tf.keras.models.model_from_json('''\r\n" + Center.KModel.dumpJSON() + "''')";
        else
            head = "import tensorflow as tf\n" + "model = tf.keras.models.model_from_json('''\n" + Center.KModel.dumpJSON() + "''')";
        modelTextPane.setText(head);
    }

    public String getTextPane() {
        return textPane.getText();
    }

    public void setTextPane(String text) {
        textPane.setText(text);
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
        SwingUtilities.invokeLater(WebLookAndFeel::install);
    }
}
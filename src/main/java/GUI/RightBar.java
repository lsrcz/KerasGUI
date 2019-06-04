package GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import layers.ConfigurableObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;

public class RightBar extends JPanel {
    final int TRUE = 1;
    final int FALSE = 0;
    ConfigurableObject object;
    Editor editor;

    RightBar(Editor editor) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.editor = editor;
        //setSize(250, 720);
        setFocusable(true);
    }

    void refresh(ConfigurableObject object) {
        this.removeAll();
        this.object = object;
        int count = getConfig(object.getConfigurableObject("config"), 0);
        setPreferredSize(new Dimension(250, count * 42));
        SwingUtilities.invokeLater(() -> {
            updateUI();
        });
    }

    JPanel getPanel(ConfigurableObject obj, String str, int depth) {
        RightBar rightBar = this;
        JPanel temp = new JPanel();
        String name = "";
        for (int i = 0; i < depth; i++)
            name += " ";
        temp.add(new JLabel(name + str + ":"));
        if (obj.isNullableIntegerConfig(str)) {
            JCheckBox checkBox = new JCheckBox();
            JTextField textField;
            if (obj.getNullableInteger(str) == null) {
                checkBox.setSelected(false);
                textField = new JTextField("null");
                textField.setEnabled(false);
            } else {
                checkBox.setSelected(true);
                textField = new JTextField(String.valueOf(obj.getNullableInteger(str)));
                textField.setEnabled(true);
            }
            checkBox.addChangeListener((e) -> {
                JCheckBox source = (JCheckBox) e.getSource();
                if (source.isSelected())
                    textField.setEnabled(true);
                else
                    textField.setEnabled(false);
            });
            textField.setPreferredSize(new Dimension(150, 30));
            textField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        Integer.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        if (obj.getNullableInteger(str) == null) {
                            checkBox.setSelected(false);
                            textField.setText("null");
                            textField.setEnabled(false);
                        } else
                            textField.setText(String.valueOf(obj.getNullableInteger(str)));
                    }
                }
            });
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Integer integer;
                    try {
                        integer = Integer.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        return;
                    }
                    try {
                        obj.setNullableInt(str, Integer.valueOf(integer));
                        editor.refresh();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                    //System.out.println(gson.toJson(object));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            temp.add(checkBox);
            temp.add(textField);
        }
        if (obj.isIntegerConfig(str)) {
            JTextField textField = new JTextField(String.valueOf(obj.getInteger(str)));
            textField.setPreferredSize(new Dimension(150, 30));
            textField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        Integer.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        textField.setText(String.valueOf(obj.getInteger(str)));
                    }
                }
            });
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Integer integer;
                    try {
                        integer = Integer.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        return;
                    }
                    try {
                        obj.setInt(str, Integer.valueOf(integer));
                        editor.refresh();

                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                    //System.out.println(gson.toJson(object));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            temp.add(textField);
        }
        if (obj.isDoubleConfig(str)) {
            JTextField textField = new JTextField(String.valueOf(obj.getDouble(str)));
            textField.setPreferredSize(new Dimension(150, 30));
            textField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        Double.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        textField.setText(String.valueOf(obj.getDouble(str)));
                    }
                }
            });
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Double tempDouble;
                    try {
                        tempDouble = Double.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        return;
                    }
                    try {
                        obj.setDouble(str, Double.valueOf(tempDouble));
                        editor.refresh();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                    //System.out.println(gson.toJson(object));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    insertUpdate(e);
                }
            });
            temp.add(textField);
        }
        if (obj.isConfigurableObjectConfig(str)) {
            ConfigurableObject configurableObject = obj.getConfigurableObject(str);
            String tempString;
            if (configurableObject == null) {
                tempString = "None";
            } else {
                tempString = configurableObject.getClass().getName();
                String[] split = tempString.split("\\.");
                tempString = split[split.length - 1];
            }
            String[] selections = obj.getSelection(str);
            JComboBox comboBox = new JComboBox(selections);
            int pos;
            for (pos = 0; pos < selections.length; pos++)
                if (tempString.equals(selections[pos]))
                    break;
            comboBox.setSelectedIndex(pos);
            comboBox.addItemListener((e) -> {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        String choice = (String) comboBox.getSelectedItem();
                        try {
                            obj.setString(str, choice);
                            editor.refresh();
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                        rightBar.refresh(rightBar.object);
                        //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                        //System.out.println(gson.toJson(object));
                        break;
                    case ItemEvent.DESELECTED:
                        break;
                }
            });
            temp.add(comboBox);
        }
        if (obj.isIntegerArrayConfig(str)) {
            int[] tempIntArray = obj.getIntegerArray(str);
            JCheckBox checkBox = new JCheckBox();
            int dimension = 0;
            SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 10, 1);
            JSpinner spinner = new JSpinner(spinnerModel);
            if (tempIntArray == null) {
                checkBox.setSelected(false);
            } else {
                checkBox.setSelected(true);
                spinner.setValue(tempIntArray.length);
                dimension = tempIntArray.length;
            }
            temp.setLayout(new FlowLayout(FlowLayout.LEFT));
            temp.setPreferredSize(new Dimension(200, (dimension + 1) * 44));
            System.out.println(dimension);
            checkBox.addActionListener((e) -> {
                JCheckBox source = (JCheckBox) e.getSource();
                if (source.isSelected())
                    spinner.setEnabled(true);
                else {
                    spinner.setEnabled(false);
                    spinner.setValue(0);
                    try{
                        obj.setIntegerArray(str, null);
                    }catch (NoSuchFieldException ex)
                    {
                        ex.printStackTrace();
                    }
                    rightBar.refresh(rightBar.object);
                    //editor.refresh();
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                    System.out.println(gson.toJson(object));
                }
            });
            spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int dimension = (Integer) spinner.getValue();
                    if(dimension != 0)
                    {
                        try{
                            obj.setIntegerArray(str, new int[(Integer) spinner.getValue()]);
                        }catch (NoSuchFieldException ex)
                        {
                            ex.printStackTrace();
                        }
                        //editor.refresh();
                    }
                    else
                    {
                        try{
                            obj.setIntegerArray(str, null);
                        }catch (NoSuchFieldException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    rightBar.refresh(rightBar.object);
                }
            });
            JPanel tempPanel = new JPanel();
            tempPanel.add(checkBox);
            tempPanel.add(spinner);
            temp.add(tempPanel);
            if(dimension != 0)
            {
                MyTextField[] textFields = new MyTextField[dimension];
                for(int i = 0; i < dimension; i++)
                {
                    textFields[i] = new MyTextField(i);
                    textFields[i].setText(String.valueOf(tempIntArray[i]));
                    tempPanel = new JPanel();
                    tempPanel.add(textFields[i]);
                    temp.add(tempPanel);
                    textFields[i].addFocusListener(new FocusListener() {
                        @Override
                        public void focusGained(FocusEvent e) {

                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            MyTextField source = (MyTextField) e.getSource();
                            try {
                                Integer.valueOf(source.getText());
                            } catch (NumberFormatException exp) {
                                source.setText(String.valueOf(tempIntArray[source.pos]));
                            }
                        }
                    });
                    textFields[i].getDocument().putProperty("owner", textFields[i]);
                    textFields[i].setPreferredSize(new Dimension(100,30));
                    textFields[i].getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            MyTextField source = (MyTextField) e.getDocument().getProperty("owner");
                            int integer;
                            try {
                                integer = Integer.valueOf(source.getText());
                            } catch (NumberFormatException exp) {
                                return;
                            }
                            try {
                                tempIntArray[source.pos] = integer;
                                obj.setIntegerArray(str, tempIntArray);
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                            editor.refresh();
                           // Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                           // System.out.println(gson.toJson(object));
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            insertUpdate(e);
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            insertUpdate(e);
                        }
                    });
                }
            }
        }
        if (obj.isBooleanConfig(str)) {
            boolean tempBoolean = obj.getBoolean(str);
            String[] tempString = new String[]{"False", "True"};
            JComboBox comboBox = new JComboBox(tempString);
            if (tempBoolean)
                comboBox.setSelectedIndex(TRUE);
            else
                comboBox.setSelectedIndex(FALSE);
            comboBox.addItemListener((e) -> {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        int choice = comboBox.getSelectedIndex();
                        if (choice == TRUE) {
                            try {
                                obj.setBoolean(str, true);
                                editor.refresh();
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                        } else {
                            try {
                                obj.setBoolean(str, false);
                                editor.refresh();
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                        }
                        rightBar.refresh(rightBar.object);
                        //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                        //System.out.println(gson.toJson(object));
                        break;
                    case ItemEvent.DESELECTED:
                        break;
                }
            });
            temp.add(comboBox);
        }
        if (obj.isStringConfig(str)) {
            String tempString = obj.getString(str);
            String[] selections = obj.getSelection(str);
            int nullPos = -1;
            if (selections != null) {
                String[] selectionsForJComboBox = new String[selections.length];
                System.arraycopy(selections, 0, selectionsForJComboBox, 0, selections.length);
                for (int i = 0; i < selectionsForJComboBox.length; i++) {
                    if (selectionsForJComboBox[i].equals("__null__")) {
                        nullPos = i;
                        selectionsForJComboBox[i] = "None";
                    }
                }
                final int nullPosFinal = nullPos;
                JComboBox comboBox = new JComboBox(selectionsForJComboBox);
                int pos;
                for (pos = 0; pos < selections.length; pos++) {
                    if (tempString == null) {
                        if (selections[pos].equals("__null__"))
                            break;
                    } else {
                        if (tempString.equals(selections[pos]))
                            break;
                    }
                }
                comboBox.setSelectedIndex(pos);
                comboBox.addItemListener((e) -> {
                    switch (e.getStateChange()) {
                        case ItemEvent.SELECTED:
                            if (comboBox.getSelectedIndex() == nullPosFinal) {
                                try {
                                    obj.setString(str, "__null__");
                                    editor.refresh();
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            } else {
                                String choice = (String) comboBox.getSelectedItem();
                                try {
                                    obj.setString(str, choice);
                                    editor.refresh();
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            }
                            rightBar.refresh(rightBar.object);
                            // Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                            // System.out.println(gson.toJson(object));
                            break;
                        case ItemEvent.DESELECTED:
                            break;
                    }
                });
                temp.add(comboBox);
            } else {
                if(!str.equals("name"))
                {
                    JTextField textField = new JTextField(tempString);
                    textField.setPreferredSize(new Dimension(150, 30));
                    textField.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            String string = textField.getText();
                            try {
                                obj.setString(str, string);
                                editor.refresh();
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                            //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
                            //System.out.println(gson.toJson(object));
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            insertUpdate(e);
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            insertUpdate(e);
                        }
                    });
                    if(str.equals("name"))
                        textField.setEnabled(false);
                    temp.add(textField);
                }
                else{
                    JLabel text = new JLabel(tempString);
                    text.setPreferredSize(new Dimension(150, 30));
                    temp.add(text);
                }
            }
        }
        return temp;
    }

    int getConfig(ConfigurableObject config, int depth) {
        ConfigurableObject tempConfig = null;
        int count = 0;
        for (String str : config.getConfigureList()) {
            if (!str.equals("config")) {
                add(getPanel(config, str, depth));
                count += 1;
            }
            if (config.isConfigurableObjectConfig(str)) {
                tempConfig = config.getConfigurableObject(str);
                if (tempConfig != null)
                    count += getConfig(tempConfig, depth + 1);
            }
        }
        return count;
    }

}

class MyTextField extends JTextField{
    int pos;
    MyTextField(int pos)
    {
        super();
        this.pos = pos;
    }
}
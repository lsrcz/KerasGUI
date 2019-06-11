package io.github.lsrcz.gui;

import io.github.lsrcz.layers.ConfigurableObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;

/**
 *  This is the right bar of the GUI, showing and providing selections for attributes of a particular layer.
 * @author Hang Zhang
 */
public class RightBar extends JPanel {
    final int TRUE = 1;
    final int FALSE = 0;
    // Store the layer that is shown in the right bar.
    ConfigurableObject object;
    // Store the editor that is used.
    Editor editor;

    RightBar(Editor editor) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.editor = editor;
        setFocusable(true);
    }

    /**
     *  This function is to update the content in the right bar in order to show the information of the input.
     * @param object:layer which is needed to be shown in the right bar
     * @return nothing
     */
    void refresh(ConfigurableObject object) {
        this.removeAll();
        this.object = object;
        int count = getConfig(object.getConfigurableObject("config"), 0);
        setPreferredSize(new Dimension(250, count * 42));
        SwingUtilities.invokeLater(() -> {
            updateUI();
        });
    }

    /**
     * This function is to get the JPanel containing the name and selections, input textfield or other things of
     * the attribute naming given input str in order to let the contents of the right bar look properly.
     * @param obj: layer which is needed to be shown in the right bar
     * @param str: the name of attribute that need to be added into the JPanel
     * @param depth: the depth in the recursion
     * @return the JPanel containing
     */
    private JPanel getPanel(ConfigurableObject obj, String str, int depth) {
        RightBar rightBar = this;
        JPanel temp = new JPanel();
        String name = "";
        // Add space in front of the name according the depth in the recursion
        for (int i = 0; i < depth; i++)
            name += " ";
        // Add the name of the attribute.
        temp.add(new JLabel(name + str + ":"));
        // If the attribute is of Integer class
        if (obj.isNullableIntegerConfig(str)) {
            JCheckBox checkBox = new JCheckBox();
            JTextField textField;
            // if the content of attribute is null
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
                    // try if the input is an Integer
                    try {
                        Integer.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        // Set the value in the textfield to previous one
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
                        // Set the value of the attribute to integer.
                        obj.setNullableInt(str, Integer.valueOf(integer));
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
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
        // If the attribute is an int
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

                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
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
        // If the attribute is a double
        if (obj.isDoubleConfig(str)) {
            JTextField textField = new JTextField(String.valueOf(obj.getDouble(str)));
            textField.setPreferredSize(new Dimension(150, 30));
            textField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    // Check the input is a double
                    try {
                        Double.valueOf(textField.getText());
                    } catch (NumberFormatException exp) {
                        // Set it to the previous value
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
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
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
        // If the attribute is a configurable object
        if (obj.isConfigurableObjectConfig(str)) {
            // Get current selection
            ConfigurableObject configurableObject = obj.getConfigurableObject(str);
            String tempString;
            // Get the default value of selection
            if (configurableObject == null) {
                tempString = "None";
            } else {
                // Get the class of configurable object
                tempString = configurableObject.getClass().getName();
                String[] split = tempString.split("\\.");
                // Get the name of the class
                tempString = split[split.length - 1];
            }
            // Get selections of the attribute
            String[] selections = obj.getSelection(str);
            JComboBox comboBox = new JComboBox(selections);
            int pos;
            for (pos = 0; pos < selections.length; pos++)
                if (tempString.equals(selections[pos]))
                    break;
            // Set the default selection of checkbox to the desired one
            comboBox.setSelectedIndex(pos);
            comboBox.addItemListener((e) -> {
                switch (e.getStateChange()) {
                    case ItemEvent.SELECTED:
                        String choice = (String) comboBox.getSelectedItem();
                        // Set the value of the attribute to choice
                        try {
                            obj.setString(str, choice);
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                        // Ask the right bar to update it content
                        rightBar.refresh(rightBar.object);
                        break;
                    case ItemEvent.DESELECTED:
                        break;
                }
            });
            temp.add(comboBox);
        }
        // If the attribute is an int[]
        if (obj.isIntegerArrayConfig(str)) {
            // Get the default value which can be a null
            int[] tempIntArray = obj.getIntegerArray(str);
            JCheckBox checkBox = new JCheckBox();
            int dimension = 0;
            SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 10, 1);
            JSpinner spinner = new JSpinner(spinnerModel);
            // Initialize the spinner and set the dimension to the length of the array
            if (tempIntArray == null) {
                checkBox.setSelected(false);
            } else {
                checkBox.setSelected(true);
                spinner.setValue(tempIntArray.length);
                dimension = tempIntArray.length;
            }
            temp.setLayout(new FlowLayout(FlowLayout.LEFT));
            temp.setPreferredSize(new Dimension(200, (dimension + 1) * 44));
            checkBox.addActionListener((e) -> {
                JCheckBox source = (JCheckBox) e.getSource();
                if (source.isSelected())
                    spinner.setEnabled(true);
                else {
                    spinner.setEnabled(false);
                    spinner.setValue(0);
                    // Set the value of the attribute to a null
                    try{
                        obj.setIntegerArray(str, null);
                    }catch (NoSuchFieldException ex)
                    {
                        ex.printStackTrace();
                    }
                    rightBar.refresh(rightBar.object);
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
                    // Ask the right bar to update it content
                    rightBar.refresh(rightBar.object);
                }
            });
            JPanel tempPanel = new JPanel();
            tempPanel.add(checkBox);
            tempPanel.add(spinner);
            temp.add(tempPanel);
            // Add the textfield for each dimension in the array
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
                    // By setting the owner property to know the source of a document event
                    textFields[i].getDocument().putProperty("owner", textFields[i]);
                    textFields[i].setPreferredSize(new Dimension(100,30));
                    textFields[i].getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            // Get the source of this event
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
        // If the attribute is a boolean
        if (obj.isBooleanConfig(str)) {
            boolean tempBoolean = obj.getBoolean(str);
            // The selections
            final String[] tempString = new String[]{"False", "True"};
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
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                        } else {
                            try {
                                obj.setBoolean(str, false);
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
                        }
                        rightBar.refresh(rightBar.object);
                        break;
                    case ItemEvent.DESELECTED:
                        break;
                }
            });
            temp.add(comboBox);
        }
        // If the attribute is a string
        if (obj.isStringConfig(str)) {
            // Get the default value
            String tempString = obj.getString(str);
            String[] selections = obj.getSelection(str);
            int nullPos = -1;
            // If the attribute is a selection
            if (selections != null) {
                String[] selectionsForJComboBox = new String[selections.length];
                System.arraycopy(selections, 0, selectionsForJComboBox, 0, selections.length);
                for (int i = 0; i < selectionsForJComboBox.length; i++) {
                    // To special value __null__ as None
                    if (selectionsForJComboBox[i].equals("__null__")) {
                        nullPos = i;
                        selectionsForJComboBox[i] = "None";
                    }
                }
                final int nullPosFinal = nullPos;
                JComboBox comboBox = new JComboBox(selectionsForJComboBox);
                int pos;
                // Get the default position of the default value
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
                            // If the selected value is __null__
                            if (comboBox.getSelectedIndex() == nullPosFinal) {
                                try {
                                    obj.setString(str, "__null__");
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            } else {
                                String choice = (String) comboBox.getSelectedItem();
                                try {
                                    obj.setString(str, choice);
                                } catch (Exception exp) {
                                    exp.printStackTrace();
                                }
                            }
                            rightBar.refresh(rightBar.object);
                            break;
                        case ItemEvent.DESELECTED:
                            break;
                    }
                });
                temp.add(comboBox);
            } else {
                // Set the name attribute to be unchangeable
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
                            } catch (Exception exp) {
                                exp.printStackTrace();
                            }
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
                else{
                    JLabel text = new JLabel(tempString);
                    text.setPreferredSize(new Dimension(150, 30));
                    temp.add(text);
                }
            }
        }
        return temp;
    }

    /**
     * Use a recursion to get all the attributes need to be shown
     * @param config: the config field
     * @param depth: the depth of the recursion
     * @return the amount of the attribute
     */
    private int getConfig(ConfigurableObject config, int depth) {
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
/**
 *  The textfield used for int array
 * @author Hang Zhang
 */
class MyTextField extends JTextField{
    int pos;
    MyTextField(int pos)
    {
        super();
        this.pos = pos;
    }
}
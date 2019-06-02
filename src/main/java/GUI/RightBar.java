package GUI;

import layers.ConfigurableObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class RightBar extends JPanel {
    final int TRUE = 1;
    final int FALSE = 0;
    ConfigurableObject object;

    RightBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        //setSize(250, 720);
        setFocusable(true);
    }

    void refresh(ConfigurableObject object) {
        this.removeAll();
        this.object = object;
        JPanel temp = new JPanel();
        temp.setLayout(new FlowLayout());
        temp.add(new JLabel("Name:"));
        JTextField textField = new JTextField(object.getString("name"));
        textField.setPreferredSize(new Dimension(150, 30));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String string = textField.getText();
                try {
                    object.setString("name", string);
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
        add(temp);
        int count = getConfig(object.getConfigurableObject("config"), 0) + 1;
        setPreferredSize(new Dimension(250, count * 42));
        updateUI();
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
            if (selections != null) {
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
            } else {
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
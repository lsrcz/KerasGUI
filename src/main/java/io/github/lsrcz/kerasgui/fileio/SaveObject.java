package io.github.lsrcz.kerasgui.fileio;

import io.github.lsrcz.kerasgui.gui.ButtonAttribute;
import io.github.lsrcz.kerasgui.layers.UniqueNameGenerator;
import io.github.lsrcz.kerasgui.layers.model.Model;

import java.io.*;
import java.util.ArrayList;

/**
 * The save object for restoring the project.
 *
 * @author Sirui Lu
 * @author Jiayu Chen
 * @author Chun Ning
 */
public class SaveObject implements Serializable {
    private Model model;
    private String editorContents;
    private ArrayList<ButtonAttribute> buttonAtt;
    private UniqueNameGenerator nameGenerator;

    public UniqueNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public void setNameGenerator(UniqueNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public SaveObject() {
        buttonAtt = new ArrayList<ButtonAttribute>();
    }

    public static SaveObject fromFile(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             ObjectInputStream is = new ObjectInputStream(fileInputStream)) {
            return (SaveObject) is.readObject();
        } catch (ClassNotFoundException ex) {
            throw new Error("BUG!!!");
        }
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ArrayList<ButtonAttribute> getButton() {
        return buttonAtt;
    }

    public void setButton(ButtonAttribute _button) {
        buttonAtt.add(_button);
    }

    public void deleButton(ButtonAttribute _button) {

        for(int i=0; i<buttonAtt.size(); i++) {
            if(buttonAtt.get(i).equals(_button)) {
                buttonAtt.remove(i);
                break;
            }
        }
    }

    public String getEditorContents() {
        return editorContents;
    }

    public void setEditorContents(String editorContents) {
        this.editorContents = editorContents;
    }

    public void toFile(String filename) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
             ObjectOutputStream os = new ObjectOutputStream(fileOutputStream)) {
            os.writeObject(this);
        }
    }
}





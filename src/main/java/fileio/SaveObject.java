package fileio;

import GUI.MyButton;
import layers.UniqueNameGenerator;
import layers.model.Model;

import java.io.*;
import java.util.ArrayList;

public class SaveObject implements Serializable {
    private Model model;
    private String editorContents;
    //private MyButton[] button;
    private ArrayList<MyButton> button;
    private UniqueNameGenerator nameGenerator;

    public UniqueNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public void setNameGenerator(UniqueNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public SaveObject()
    {
        button = new ArrayList<MyButton>();
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

//    public MyButton[] getButton() {
//        return button;
//    }

    public ArrayList<MyButton> getButton() {
        return button;
    }
//    public void setButton(MyButton _button) {
//    	MyButton[] newButton = new MyButton[button.length + 1];
//        System.arraycopy(button, 0, newButton, 0, button.length);
//        newButton[button.length] = _button;
//        button = newButton;
//    }
//
//    public void deleButton(MyButton _button) {
//    	MyButton[] newButton = new MyButton[button.length - 1];
//        for (int i = 0; i < button.length; ++i) {
//            if (newButton[i] == _button) {
//                if (i != 0)
//                    System.arraycopy(button, 0, newButton, 0, i);
//                if (i != button.length - 1)
//                    System.arraycopy(button, i + 1, newButton, i, button.length - 1 - i);
//                button = newButton;
//            }
//        }
//    }
//

    public void setButton(MyButton _button) {
        button.add(_button);
    }

    public void deleButton(MyButton _button) {

        for(int i=0;i<button.size();i++)
        {
            if(button.get(i)==_button)
            {
                button.remove(i);
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





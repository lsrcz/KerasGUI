package fileio;

import layers.model.Model;

import java.io.*;

public class SaveObject implements Serializable {
    private Model model;
    private String editorContents;

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

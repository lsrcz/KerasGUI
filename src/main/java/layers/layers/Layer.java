package layers.layers;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.LinkedProperty;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class Layer extends ConfigurableObject {
    @Expose
    public final String class_name = "Dense";

    @LinkedProperty(name = "name")
    @Expose
    public String name;

    @Expose
    Object[][][] inbound_nodes;

    {
        clearEdges();
    }


    public void clearEdges() {
        inbound_nodes = new Object[1][][];
        inbound_nodes[0] = new Object[0][];
    }

    public void addInEdge(String str) {
        Object[][] new_inbound_nodes = new Object[inbound_nodes[0].length + 1][];
        System.arraycopy(inbound_nodes, 0, new_inbound_nodes, 0, inbound_nodes.length);
        Object[] newEdge = new Object[4];
        newEdge[0] = str;
        newEdge[1] = 0;
        newEdge[2] = 0;
        newEdge[3] = new HashMap<>();
        new_inbound_nodes[inbound_nodes[0].length] = newEdge;
        inbound_nodes[0] = new_inbound_nodes;
    }

    public String getName() {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), "name");
            return (String) f.get(this);
        } catch (Exception ex) {
            throw new Error("BUG!!!");
        }
    }
}
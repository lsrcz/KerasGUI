package io.github.lsrcz.kerasgui.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.LinkedProperty;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Layer class(Keras object)
 *
 * @author Sirui Lu
 */
public abstract class Layer extends ConfigurableObject {

    @LinkedProperty(name = "name")
    @Expose
    public String name;

    @Expose
    Object[][][] inbound_nodes;

    {
        clearEdges();
    }


    /**
     * Clear the edges in the internal representations.
     */
    public void clearEdges() {
        inbound_nodes = new Object[1][][];
        inbound_nodes[0] = new Object[0][];
    }

    /**
     * Add an in edge to the internal representations.
     * @param str The name of the edge.
     */
    public void addInEdge(String str) {
        Object[][] new_inbound_nodes = new Object[inbound_nodes[0].length + 1][];
        System.arraycopy(inbound_nodes[0], 0, new_inbound_nodes, 0, inbound_nodes[0].length);
        Object[] newEdge = new Object[4];
        newEdge[0] = str;
        newEdge[1] = 0;
        newEdge[2] = 0;
        newEdge[3] = new HashMap<>();
        new_inbound_nodes[inbound_nodes[0].length] = newEdge;
        inbound_nodes[0] = new_inbound_nodes;
    }

    /**
     * Get the name of the layer.
     * @return The name of the layer.
     */
    public String getName() {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), "name");
            return (String) f.get(this);
        } catch (Exception ex) {
            throw new Error("BUG!!!");
        }
    }
}

package io.github.lsrcz.kerasgui.layers.model;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.Pair;
import io.github.lsrcz.kerasgui.layers.UniqueNameGenerator;
import io.github.lsrcz.kerasgui.layers.layers.Layer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Model config (from keras)
 *
 * @author Sirui Lu
 */
public class ModelConfig implements Serializable {
    @Expose
    private String name = UniqueNameGenerator
            .getInstance().generate("model", "model");
    @Expose
    private Layer[] layers;
    @Expose
    private Object[][] input_layers;
    @Expose
    private Object[][] output_layers;
    private Map<Layer, Set<Layer>> edges = new HashMap<>();

    {
        layers = new Layer[0];
        clearInputLayer();
        clearOutputLayer();
    }

    /**
     * Get the name of the model.
     * @return The name of the model.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the model.
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add an layer to the model.
     * @param obj The layer object.
     */
    public void addLayer(Layer obj) {
        for (Layer o : layers) {
            if (o == obj) {
                throw new Error("BUG!!!");
            }
        }
        Layer[] newlayers = new Layer[layers.length + 1];
        System.arraycopy(layers, 0, newlayers, 0, layers.length);
        newlayers[layers.length] = obj;
        layers = newlayers;
        inferInputOutputLayer();
    }

    /**
     * Delete a layer and all associated edges.
     * @param obj The layer object.
     */
    public void deleteLayer(Layer obj) {
        Layer[] newlayers = new Layer[layers.length - 1];
        for (int i = 0; i < layers.length; ++i) {
            if (layers[i] == obj) {
                if (i != 0)
                    System.arraycopy(layers, 0, newlayers, 0, i);
                if (i != layers.length - 1)
                    System.arraycopy(layers, i + 1, newlayers, i, layers.length - 1 - i);
                layers = newlayers;

                Set<Pair<Layer, Layer>> shouldDelete = new HashSet<>();

                edges.remove(obj);

                for (Layer from : edges.keySet()) {
                    for (Layer to : edges.get(from)) {
                        if (to == obj) {
                            shouldDelete.add(new Pair<>(from, to));
                        }
                    }
                }

                for (Pair<Layer, Layer> edge : shouldDelete) {
                    deleteEdge(edge.getKey(), edge.getValue());
                }
                updateEdge();
                inferInputOutputLayer();
                return;
            }
        }
        throw new Error("BUG!!!");
    }

    /**
     * Check if the model has loop.
     * @return True if the model is loop free.
     */
    private boolean loopFree() {
        Map<Layer, Integer> inDegree = new HashMap<>();
        for (Layer l : layers) {
            inDegree.put(l, 0);
        }
        for (Layer from : edges.keySet()) {
            for (Layer to : edges.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }
        while (true) {
            // delete nodes with zero in degree iteratively
            Set<Layer> zeroSet = new HashSet<>();
            for (Layer l : inDegree.keySet()) {
                if (inDegree.get(l) == 0) {
                    zeroSet.add(l);
                }
            }
            if (zeroSet.isEmpty() && !inDegree.isEmpty()) {
                return false;
            }
            for (Layer l : zeroSet) {
                inDegree.remove(l);
                if (edges.containsKey(l)) {
                    for (Layer l1 : edges.get(l)) {
                        if (inDegree.containsKey(l1)) {
                            inDegree.put(l1, inDegree.get(l1) - 1);
                        } else {
                            throw new Error("BUG!!!");
                        }
                    }
                }
            }
            if (inDegree.isEmpty()) {
                return true;
            }
        }
    }

    /**
     * Add an edge with loop checking.
     * @param from The layer which the edge is from.
     * @param to The layer which the edge is to.
     * @return True if the edge is added to the model.
     */
    public boolean addEdge(Layer from, Layer to) {
        if (!edges.containsKey(from))
            edges.put(from, new HashSet<>());
        edges.get(from).add(to);
        if (!loopFree()) {
            deleteEdge(from, to);
            return false;
        }
        updateEdge();
        inferInputOutputLayer();
        return true;
    }

    // delete an edge

    /**
     * Delete an edge.
     * @param from The layer which the edge is from.
     * @param to The layer which the edge is to.
     */
    public void deleteEdge(Layer from, Layer to) {
        if (!edges.containsKey(from))
            return;
        edges.get(from).remove(to);
        if (edges.get(from).isEmpty())
            edges.remove(from);
        inferInputOutputLayer();
        updateEdge();
    }

    /**
     * Update the edge to the model's internal representation(for keras JSON format).
     */
    private void updateEdge() {
        for (Layer layer : layers) {
            layer.clearEdges();
        }
        for (Layer from : edges.keySet()) {
            for (Layer to : edges.get(from)) {
                to.addInEdge(from.getName());
            }
        }
    }

    /**
     * Clear input layers.
     */
    public void clearInputLayer() {
        input_layers = new Object[0][];
    }

    /**
     * Clear output layers.
     */
    public void clearOutputLayer() {
        output_layers = new Object[0][];
    }

    private Object[][] addIOLayer(Layer l, Object[][] orig) {
        Object[][] new_layers = new Object[orig.length + 1][];
        System.arraycopy(orig, 0, new_layers, 0, orig.length);
        Object[] new_layer = new Object[3];
        new_layer[0] = l.getName();
        new_layer[1] = 0;
        new_layer[2] = 0;
        new_layers[orig.length] = new_layer;
        return new_layers;
    }

    /**
     * Add input layer. Not used.
     */
    public void addInputLayer(Layer l) {
        input_layers = addIOLayer(l, input_layers);
    }

    /**
     * Add output layer. Not used.
     */
    public void addOutputLayer(Layer l) {
        output_layers = addIOLayer(l, output_layers);
    }

    private Object[][] deleteIOLayer(Layer l, Object[][] orig) {
        String name = l.getName();
        Object[][] new_layer = new Object[orig.length - 1][];
        for (int i = 0; i < orig.length; ++i) {
            if (orig[i][0].equals(name)) {
                if (i != 0) {
                    System.arraycopy(orig, 0, new_layer, 0, i);
                }
                if (i == orig.length - 1) {
                    System.arraycopy(orig, i + 1, new_layer, i, orig.length - i - 1);
                }
                return new_layer;
            }
        }
        throw new Error("BUG!!!");
    }

    /**
     * Delete input layer
     * @param l the layer to be deleted.
     */
    public void deleteInputLayer(Layer l) {
        input_layers = deleteIOLayer(l, input_layers);
    }

    /**
     * Delete output layer
     * @param l the layer to be deleted.
     */
    public void deleteOutputLayer(Layer l) {
        output_layers = deleteIOLayer(l, output_layers);
    }

    /**
     * Get the layer by name.
     * @param name The name of the layer.
     * @return The layer object. Null if not found.
     */
    public Layer layerFromName(String name) {
        for (Layer layer : layers) {
            if (layer.getName().equals(name))
                return layer;
        }
        return null;
    }

    /**
     * Automatically infer the input and output layers.
     */
    public void inferInputOutputLayer() {
        Set<Layer> inputLayers = new HashSet<>();
        Set<Layer> outputLayers = new HashSet<>();
        for (Layer l : layers) {
            inputLayers.add(l);
            outputLayers.add(l);
        }
        for (Layer from : edges.keySet()) {
            outputLayers.remove(from);
            for (Layer to : edges.get(from)) {
                inputLayers.remove(to);
            }
        }
        clearInputLayer();
        clearOutputLayer();
        for (Layer l : inputLayers)
            addInputLayer(l);
        for (Layer l : outputLayers)
            addOutputLayer(l);
    }
}

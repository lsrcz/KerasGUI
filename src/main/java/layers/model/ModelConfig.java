package layers.model;

import com.google.gson.annotations.Expose;
import javafx.util.Pair;
import layers.UniqueNameGenerator;
import layers.layers.Layer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelConfig {
    @Expose
    private String name = UniqueNameGenerator.getInstance().generate("model", "model");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    }

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
                return;
            }
        }
        throw new Error("BUG!!!");
    }

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
            }
            if (inDegree.isEmpty()) {
                return true;
            }
        }
    }

    public boolean addEdge(Layer from, Layer to) {
        if (!edges.containsKey(from))
            edges.put(from, new HashSet<>());
        edges.get(from).add(to);
        if (!loopFree()) {
            deleteEdge(from, to);
            return false;
        }
        updateEdge();
        return true;
    }

    public void deleteEdge(Layer from, Layer to) {
        if (!edges.containsKey(from))
            return;
        edges.get(from).remove(to);
        if (edges.get(from).isEmpty())
            edges.remove(from);
        updateEdge();
    }

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

    public void clearInputLayer() {
        input_layers = new Object[0][];
    }

    public void clearOutputLayer() {
        output_layers = new Object[0][];
    }

    private Object[][] addIOLayer(Layer l, Object[][] orig) {
        Object[][] new_layers = new Object[orig.length + 1][];
        System.arraycopy(new_layers, 0, orig, 0, orig.length);
        Object[] new_layer = new Object[3];
        new_layer[0] = l.getName();
        new_layer[1] = 0;
        new_layer[2] = 0;
        new_layers[orig.length] = new_layer;
        return new_layers;
    }

    public void addInputLayer(Layer l) {
        input_layers = addIOLayer(l, input_layers);
    }


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

    public void deleteInputLayer(Layer l) {
        input_layers = deleteIOLayer(l, input_layers);
    }

    public void deleteOutputLayer(Layer l) {
        output_layers = deleteIOLayer(l, output_layers);
    }

}

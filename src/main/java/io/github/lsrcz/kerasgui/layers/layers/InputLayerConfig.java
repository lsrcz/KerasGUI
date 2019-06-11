package io.github.lsrcz.kerasgui.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.*;

public class InputLayerConfig extends ConfigurableObject {
    @LinkedProperty(name = "name")
    @UniqueProperty(scope = "name", prefix = "input")
    @ConfigProperty
    @Expose
    String name;

    @ConfigProperty
    @Expose
    int[] input_shape;

    @ConfigProperty
    @Expose
    Integer batch_size;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;

    @ConfigProperty
    @Expose
    boolean sparse;
}

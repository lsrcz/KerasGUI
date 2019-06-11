package io.github.lsrcz.kerasgui.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.*;

public class AddConfig extends ConfigurableObject {
    @LinkedProperty(name = "name")
    @UniqueProperty(scope = "name", prefix = "add")
    @ConfigProperty
    @Expose
    String name;

    @ConfigProperty
    @Expose
    boolean trainable = true;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;
}

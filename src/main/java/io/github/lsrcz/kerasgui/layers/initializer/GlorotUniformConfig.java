package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;
import io.github.lsrcz.kerasgui.layers.annotation.DefaultStringProperty;
import io.github.lsrcz.kerasgui.layers.annotation.SelectStringProperty;

public class GlorotUniformConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    Integer seed;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;
}

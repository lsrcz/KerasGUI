package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.ConfigProperty;
import io.github.lsrcz.layers.annotation.DefaultStringProperty;
import io.github.lsrcz.layers.annotation.SelectStringProperty;

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

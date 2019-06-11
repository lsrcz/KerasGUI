package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;
import io.github.lsrcz.kerasgui.layers.annotation.DefaultStringProperty;
import io.github.lsrcz.kerasgui.layers.annotation.SelectStringProperty;

public class ConstantConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    int value = 0;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;
}

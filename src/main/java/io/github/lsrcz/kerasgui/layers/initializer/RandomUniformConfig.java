package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class RandomUniformConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double minval = 0.0;

    @ConfigProperty
    @Expose
    double maxval = 0.05;

    @ConfigProperty
    @Expose
    Integer seed = null;
}

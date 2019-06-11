package io.github.lsrcz.kerasgui.layers.constraint;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class MinMaxNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double min_value = 0;

    @ConfigProperty
    @Expose
    double max_value = 1;

    @ConfigProperty
    @Expose
    double rate = 1;

    @ConfigProperty
    @Expose
    int axis = 0;
}

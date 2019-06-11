package io.github.lsrcz.layers.constraint;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class MaxNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double max_value = 2;

    @ConfigProperty
    @Expose
    int axis = 0;
}

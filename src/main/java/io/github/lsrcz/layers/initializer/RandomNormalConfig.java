package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class RandomNormalConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double mean = 0.0;

    @ConfigProperty
    @Expose
    double stddev = 0.05;

    @ConfigProperty
    @Expose
    Integer seed = null;
}

package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class OrthogonalConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double gain = 1.0;

    @ConfigProperty
    @Expose
    Integer seed = null;
}

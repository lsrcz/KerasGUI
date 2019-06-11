package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class OrthogonalConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double gain = 1.0;

    @ConfigProperty
    @Expose
    Integer seed = null;
}

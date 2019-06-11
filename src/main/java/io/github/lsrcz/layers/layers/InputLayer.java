package io.github.lsrcz.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class InputLayer extends Layer {
    @Expose
    public final String class_name = "InputLayer";

    @ConfigProperty
    @Expose
    public InputLayerConfig config;
}

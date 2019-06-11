package io.github.lsrcz.kerasgui.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class InputLayer extends Layer {
    @Expose
    public final String class_name = "InputLayer";

    @ConfigProperty
    @Expose
    public InputLayerConfig config;
}

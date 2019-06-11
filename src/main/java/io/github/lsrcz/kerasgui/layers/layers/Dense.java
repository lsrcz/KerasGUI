package io.github.lsrcz.kerasgui.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;


public class Dense extends Layer {
    @Expose
    public final String class_name = "Dense";

    @ConfigProperty
    @Expose
    public DenseConfig config;
}

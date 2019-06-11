package io.github.lsrcz.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.annotation.ConfigProperty;


public class Dense extends Layer {
    @Expose
    public final String class_name = "Dense";

    @ConfigProperty
    @Expose
    public DenseConfig config;
}

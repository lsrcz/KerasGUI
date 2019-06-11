package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class RandomUniform extends BaseInitializer {
    @Expose
    public final String class_name = "RandomUniform";

    @ConfigProperty
    @Expose
    RandomUniformConfig config;
}

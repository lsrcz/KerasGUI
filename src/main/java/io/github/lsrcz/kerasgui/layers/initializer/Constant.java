package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class Constant extends BaseInitializer {
    @Expose
    public final String class_name = "Constant";

    @ConfigProperty
    @Expose
    ConstantConfig config;
}

package io.github.lsrcz.kerasgui.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class GlorotNormal extends BaseInitializer {
    @Expose
    public final String class_name = "GlorotNormal";

    @ConfigProperty
    @Expose
    GlorotNormalConfig config;
}

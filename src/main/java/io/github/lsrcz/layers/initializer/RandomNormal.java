package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class RandomNormal extends BaseInitializer {
    @Expose
    public final String class_name = "RandomNormal";

    @ConfigProperty
    @Expose
    RandomNormalConfig config;
}

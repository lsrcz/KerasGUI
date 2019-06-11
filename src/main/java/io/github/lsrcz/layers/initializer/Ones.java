package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class Ones extends BaseInitializer {
    @Expose
    public final String class_name = "Ones";

    @ConfigProperty
    @Expose
    OnesConfig config;
}

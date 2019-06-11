package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class Identity extends BaseInitializer {
    @Expose
    public final String class_name = "Identity";

    @ConfigProperty
    @Expose
    IdentityConfig config;
}

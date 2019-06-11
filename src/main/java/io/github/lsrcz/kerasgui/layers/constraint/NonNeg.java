package io.github.lsrcz.kerasgui.layers.constraint;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class NonNeg extends BaseConstraint {
    @Expose
    public final String class_name = "NonNeg";

    @ConfigProperty
    @Expose
    NonNegConfig config;
}

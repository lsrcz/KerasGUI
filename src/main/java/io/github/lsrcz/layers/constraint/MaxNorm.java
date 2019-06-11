package io.github.lsrcz.layers.constraint;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class MaxNorm extends BaseConstraint {
    @Expose
    public final String class_name = "MaxNorm";

    @ConfigProperty
    @Expose
    MaxNormConfig config;
}

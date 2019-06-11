package io.github.lsrcz.layers.initializer;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.ConfigProperty;
import io.github.lsrcz.layers.annotation.DefaultStringProperty;
import io.github.lsrcz.layers.annotation.SelectStringProperty;

public class VarianceScalingConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double scale;

    @ConfigProperty
    @SelectStringProperty(selections = {"fan_in", "fan_out", "fan_avg"})
    @DefaultStringProperty(defaultString = "fan_in")
    @Expose
    String mode;

    @ConfigProperty
    @SelectStringProperty(selections = {"normal", "uniform"})
    @DefaultStringProperty(defaultString = "normal")
    @Expose
    String distribution;

    @ConfigProperty
    @Expose
    Integer seed = null;
}

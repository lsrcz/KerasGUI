package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.SelectStringProperty;

public class VarianceScalingConfig {
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

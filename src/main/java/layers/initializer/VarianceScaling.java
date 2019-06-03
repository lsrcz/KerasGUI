package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class VarianceScaling {
    @Expose
    public final String class_name = "VarianceScaling";

    @ConfigProperty
    @Expose
    VarianceScalingConfig config;
}

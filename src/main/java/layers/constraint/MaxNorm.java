package layers.constraint;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class MaxNorm extends BaseConstraint {
    @Expose
    public final String class_name = "MaxNorm";

    @ConfigProperty
    @Expose
    MaxNormConfig config;
}

package layers.constraint;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class MinMaxNorm extends BaseConstraint {
    @Expose
    public final String class_name = "MinMaxNorm";

    @ConfigProperty
    @Expose
    MaxNormConfig config;
}

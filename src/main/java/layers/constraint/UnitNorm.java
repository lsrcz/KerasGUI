package layers.constraint;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class UnitNorm extends BaseConstraint {
    @Expose
    public final String class_name = "UnitNorm";

    @ConfigProperty
    @Expose
    MaxNormConfig config;
}

package layers.constraint;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;

public class MaxNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double max_value = 2;

    @ConfigProperty
    @Expose
    int axis = 0;
}

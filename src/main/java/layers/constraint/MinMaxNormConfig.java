package layers.constraint;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;

public class MinMaxNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double min_value = 0;

    @ConfigProperty
    @Expose
    double max_value = 1;

    @ConfigProperty
    @Expose
    double rate = 1;

    @ConfigProperty
    @Expose
    int axis = 0;
}

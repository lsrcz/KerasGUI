package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;

public class RandomUniformConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double minval = 0.0;

    @ConfigProperty
    @Expose
    double maxval = 0.05;

    @ConfigProperty
    @Expose
    Integer seed = null;
}

package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class RandomUniformConfig {
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

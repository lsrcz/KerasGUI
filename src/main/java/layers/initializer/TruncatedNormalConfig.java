package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class TruncatedNormalConfig {
    @ConfigProperty
    @Expose
    double mean = 0.0;

    @ConfigProperty
    @Expose
    double stddev = 0.05;

    @ConfigProperty
    @Expose
    Integer seed = null;
}
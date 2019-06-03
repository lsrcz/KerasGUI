package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class OrthogonalConfig {
    @ConfigProperty
    @Expose
    double gain = 1.0;

    @ConfigProperty
    @Expose
    Integer seed = null;
}
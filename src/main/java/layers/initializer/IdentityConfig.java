package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class IdentityConfig {
    @ConfigProperty
    @Expose
    double gain = 1.0;
}

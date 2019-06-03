package layers.regularizer;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;

public class L1L2Config extends ConfigurableObject {
    @ConfigProperty
    @Expose
    double l1 = 0.0;

    @ConfigProperty
    @Expose
    double l2 = 0.0;
}

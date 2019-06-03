package layers.regularizer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class L1L2 extends BaseRegularizer {
    @Expose
    public final String class_name = "L1L2";

    @ConfigProperty
    @Expose
    L1L2Config config;
}

package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class TruncatedNormal {
    @Expose
    public final String class_name = "TruncatedNormal";

    @ConfigProperty
    @Expose
    TruncatedNormalConfig config;
}

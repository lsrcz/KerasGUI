package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class Zeros extends BaseInitializer {
    @Expose
    public final String class_name = "Zeros";

    @ConfigProperty
    @Expose
    ZerosConfig config;
}

package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class GlorotUniform extends BaseInitializer {
    @Expose
    public final String class_name = "GlorotUniform";

    @ConfigProperty
    @Expose
    GlorotUniformConfig config;
}

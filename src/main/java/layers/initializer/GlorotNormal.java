package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class GlorotNormal extends BaseInitializer {
    @Expose
    public final String class_name = "GlorotNormal";

    @ConfigProperty
    @Expose
    GlorotNormalConfig config;
}

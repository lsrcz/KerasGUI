package layers.initializer;

import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.SelectStringProperty;

public class GlorotUniform extends BaseInitializer {
    public final String class_name = "GlorotUniform";

    @ConfigProperty
    GlorotUniformConfig config;
}

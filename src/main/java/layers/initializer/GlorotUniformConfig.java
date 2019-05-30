package layers.initializer;

import layers.ConfigurableObject;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.SelectStringProperty;

public class GlorotUniformConfig extends ConfigurableObject {
    @ConfigProperty
    Integer seed;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    String dtype;
}

package layers.initializer;

import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.SelectStringProperty;

public class ZerosConfig extends ConfigurableObject {
    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    String dtype;
}

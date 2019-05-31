package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.SelectStringProperty;

public class ConstantConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    int value = 0;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;
}

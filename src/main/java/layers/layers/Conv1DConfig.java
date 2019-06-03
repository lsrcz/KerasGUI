package layers.layers;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.*;

public class Conv1DConfig extends ConfigurableObject {
    @LinkedProperty(name = "name")
    @UniqueProperty(scope = "name", prefix = "conv1d")
    @ConfigProperty
    @Expose
    String name;

    @ConfigProperty
    @Expose
    boolean trainable = true;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;

    @ConfigProperty
    @Expose
    int filters;

    @ConfigProperty
    @Expose
    int kernel_size;

    @ConfigProperty
    @Expose
    int strides = 1;

    @ConfigProperty
    @SelectStringProperty(selections = {"valid", "causal", "same"})
    @DefaultStringProperty(defaultString = "valid")
    @Expose
    String padding;

    @ConfigProperty
    @SelectStringProperty(selections = {"channels_last", "channels_first"})
    @DefaultStringProperty(defaultString = "channels_last")
    @Expose
    String data_format;

    @ConfigProperty
    @Expose
    int dilation_rate = 1;

    @ConfigProperty
    @SelectStringProperty(selections = {"__null__", "relu"})
    @DefaultStringProperty(defaultString = "__null__")
    @Expose
    String activation;
}

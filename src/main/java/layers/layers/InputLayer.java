package layers.layers;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class InputLayer extends Layer {
    @Expose
    public final String class_name = "InputLayer";

    @ConfigProperty
    @Expose
    public InputLayerConfig config;
}

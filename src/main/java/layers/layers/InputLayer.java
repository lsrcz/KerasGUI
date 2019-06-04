package layers.layers;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class InputLayer extends Layer {
    @ConfigProperty
    @Expose
    public InputLayerConfig config;
}

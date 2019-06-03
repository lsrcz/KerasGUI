package layers.layers;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class Conv1D extends Layer {
    @ConfigProperty
    @Expose
    public Conv1DConfig config;
}

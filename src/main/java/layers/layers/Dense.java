package layers.layers;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;


public class Dense extends Layer {
    @ConfigProperty
    @Expose
    public DenseConfig config;
}

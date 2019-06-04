package layers.layers;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class Conv1D extends Layer {
    @Expose
    public final String class_name = "Conv1D";

    @ConfigProperty
    @Expose
    public Conv1DConfig config;
}

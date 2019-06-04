package layers.layers;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class Add extends Layer {
    @Expose
    public final String class_name = "Add";

    @ConfigProperty
    @Expose
    public AddConfig config;
}

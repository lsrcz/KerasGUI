package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class Orthogonal {
    @Expose
    public final String class_name = "Orthogonal";

    @ConfigProperty
    @Expose
    OrthogonalConfig config;
}
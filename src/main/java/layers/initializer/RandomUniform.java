package layers.initializer;

import com.google.gson.annotations.Expose;
import layers.annotation.ConfigProperty;

public class RandomUniform {
    @Expose
    public final String class_name = "RandomUniform";

    @ConfigProperty
    @Expose
    RandomUniformConfig config;
}
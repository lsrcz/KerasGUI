package layers.layers;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;
import layers.annotation.LinkedProperty;
import layers.annotation.MustExistProperty;
import layers.annotation.UniqueProperty;


public class Dense extends ConfigurableObject {

    @Expose
    public final String class_name = "Dense";
    @MustExistProperty
    @UniqueProperty(scope = "name", prefix="dense")
    @LinkedProperty(name = "name")
    @ConfigProperty
    @Expose
    public String name;
    @ConfigProperty
    @Expose
    public DenseConfig config;
}

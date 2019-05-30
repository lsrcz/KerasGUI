package layers.layers;

import layers.ConfigurableObject;
import layers.annotation.*;


public class Dense extends ConfigurableObject {

    @MustExistProperty
    @UniqueProperty(scope = "name", prefix="dense")
    @LinkedProperty(name = "name")
    @ConfigProperty
    public String name;
    public final String class_name = "Dense";
    @ConfigProperty
    public DenseConfig config;
}

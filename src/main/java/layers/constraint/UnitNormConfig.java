package layers.constraint;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;

public class UnitNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    int axis = 0;
}

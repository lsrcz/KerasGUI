package io.github.lsrcz.layers.constraint;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.ConfigProperty;

public class UnitNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    int axis = 0;
}

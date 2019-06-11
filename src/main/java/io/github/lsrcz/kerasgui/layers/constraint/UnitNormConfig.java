package io.github.lsrcz.kerasgui.layers.constraint;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.ConfigProperty;

public class UnitNormConfig extends ConfigurableObject {
    @ConfigProperty
    @Expose
    int axis = 0;
}

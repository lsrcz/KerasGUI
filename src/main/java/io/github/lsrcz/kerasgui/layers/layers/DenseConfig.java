package io.github.lsrcz.kerasgui.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.kerasgui.layers.ConfigurableObject;
import io.github.lsrcz.kerasgui.layers.annotation.*;
import io.github.lsrcz.kerasgui.layers.constraint.BaseConstraint;
import io.github.lsrcz.kerasgui.layers.initializer.BaseInitializer;
import io.github.lsrcz.kerasgui.layers.regularizer.BaseRegularizer;

public class DenseConfig extends ConfigurableObject {

    @LinkedProperty(name = "name")
    @UniqueProperty(scope = "name", prefix = "dense")
    @ConfigProperty
    @Expose
    String name;

    @ConfigProperty
    @Expose
    boolean trainable = true;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    @Expose
    String dtype;

    @ConfigProperty
    @Expose
    int units;

    @ConfigProperty
    @SelectStringProperty(selections = {"relu", "sigmoid"})
    @DefaultStringProperty(defaultString = "relu")
    @Expose
    String activation;

    @ConfigProperty
    @Expose
    boolean use_bias = true;

    @ConfigProperty
    @DefaultStringProperty(defaultString = "GlorotUniform")
    @Expose
    BaseInitializer kernel_initializer;

    @ConfigProperty
    @DefaultStringProperty(defaultString = "Zeros")
    @Expose
    BaseInitializer bias_initializer;

    @ConfigProperty
    @Expose
    @DefaultStringProperty(defaultString = "None")
    BaseRegularizer kernel_regularizer;

    @ConfigProperty
    @Expose
    @DefaultStringProperty(defaultString = "None")
    BaseRegularizer bias_regularizer;

    @ConfigProperty
    @Expose
    @DefaultStringProperty(defaultString = "None")
    BaseRegularizer activity_regularizer;

    @ConfigProperty
    @Expose
    @DefaultStringProperty(defaultString = "None")
    BaseConstraint kernel_constraint;

    @ConfigProperty
    @Expose
    @DefaultStringProperty(defaultString = "None")
    BaseConstraint bias_constraint;



}

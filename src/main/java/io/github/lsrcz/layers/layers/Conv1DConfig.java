package io.github.lsrcz.layers.layers;

import com.google.gson.annotations.Expose;
import io.github.lsrcz.layers.ConfigurableObject;
import io.github.lsrcz.layers.annotation.*;
import io.github.lsrcz.layers.constraint.BaseConstraint;
import io.github.lsrcz.layers.initializer.BaseInitializer;
import io.github.lsrcz.layers.regularizer.BaseRegularizer;

public class Conv1DConfig extends ConfigurableObject {
    @LinkedProperty(name = "name")
    @UniqueProperty(scope = "name", prefix = "conv1d")
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
    int filters;

    @ConfigProperty
    @Expose
    int kernel_size;

    @ConfigProperty
    @Expose
    int strides = 1;

    @ConfigProperty
    @SelectStringProperty(selections = {"valid", "causal", "same"})
    @DefaultStringProperty(defaultString = "valid")
    @Expose
    String padding;

    @ConfigProperty
    @SelectStringProperty(selections = {"channels_last", "channels_first"})
    @DefaultStringProperty(defaultString = "channels_last")
    @Expose
    String data_format;

    @ConfigProperty
    @Expose
    int dilation_rate = 1;

    @ConfigProperty
    @SelectStringProperty(selections = {"__null__", "softmax", "elu", "selu", "softplus", "softsign",
            "relu", "tanh", "sigmoid", "hard_sigmoid", "exponential", "linear"})
    @DefaultStringProperty(defaultString = "__null__")
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

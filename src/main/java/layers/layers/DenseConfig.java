package layers.layers;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.*;
import layers.constraint.BaseConstraint;
import layers.initializer.BaseInitializer;
import layers.regularizer.BaseRegularizer;

public class DenseConfig extends ConfigurableObject {
    @Expose(serialize = false, deserialize = false)
    Dense father;

    @LinkedProperty(name = "name")
    String name;

    @ConfigProperty
    boolean trainable = true;

    @ConfigProperty
    @SelectStringProperty(selections = {"float32", "float16"})
    @DefaultStringProperty(defaultString = "float32")
    String dtype;

    @ConfigProperty
    int units;

    @ConfigProperty
    @SelectStringProperty(selections = {"relu", "sigmoid"})
    @DefaultStringProperty(defaultString = "relu")
    String activation;

    @ConfigProperty
    boolean use_bias = false;

    @ConfigProperty
    @DefaultStringProperty(defaultString = "GlorotUniform")
    BaseInitializer kernel_initializer;

    @ConfigProperty
    @DefaultStringProperty(defaultString = "Zeros")
    BaseInitializer bias_initializer;

    @ConfigProperty
    BaseRegularizer kernel_regularizer;

    @ConfigProperty
    BaseRegularizer bias_regularizer;

    @ConfigProperty
    BaseRegularizer activity_regularizer;

    @ConfigProperty
    BaseConstraint kernel_constraint;

    @ConfigProperty
    BaseConstraint bias_constraint;

    Object[][][] inbound_nodes;

    {
        inbound_nodes = new Object[1][][];
        inbound_nodes[0] = new Object[0][];
    }

}

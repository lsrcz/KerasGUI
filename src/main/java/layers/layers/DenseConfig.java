package layers.layers;

import com.google.gson.annotations.Expose;
import layers.ConfigurableObject;
import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.LinkedProperty;
import layers.annotation.SelectStringProperty;
import layers.constraint.BaseConstraint;
import layers.initializer.BaseInitializer;
import layers.regularizer.BaseRegularizer;

public class DenseConfig extends ConfigurableObject {
    Dense father;

    @LinkedProperty(name = "name")
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
    boolean use_bias = false;

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

    @Expose
    Object[][][] inbound_nodes;

    {
        inbound_nodes = new Object[1][][];
        inbound_nodes[0] = new Object[0][];
    }

}

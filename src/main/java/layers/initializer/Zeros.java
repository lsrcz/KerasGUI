package layers.initializer;

import layers.annotation.ConfigProperty;
import layers.annotation.DefaultStringProperty;
import layers.annotation.SelectStringProperty;

public class Zeros extends BaseInitializer {
    public final String class_name = "Zeros";

    @ConfigProperty
    ZerosConfig config;
}

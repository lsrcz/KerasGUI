package io.github.lsrcz.kerasgui.layers.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Keras model
 *
 * @author Sirui Lu
 */
public class Model implements Serializable {
    @Expose
    public final String class_name = "Model";
    @Expose
    public final String keras_version = "2.2.4-tf";
    @Expose
    public final String backend = "tensorflow";
    @Expose
    public ModelConfig config = new ModelConfig();

    /**
     * Generate JSON from the object.
     * @return The JSON in String format
     */
    public String dumpJSON() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()
                .setVersion(1.0)
                .create();

        return gson.toJson(this);
    }
}

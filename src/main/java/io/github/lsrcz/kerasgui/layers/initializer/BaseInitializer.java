package io.github.lsrcz.kerasgui.layers.initializer;

import io.github.lsrcz.kerasgui.layers.ConfigurableObject;

import java.util.HashMap;
import java.util.Map;

public class BaseInitializer extends ConfigurableObject {

    public final static Map<String, Class<?>> map;

    static {
        map = new HashMap<>();
        map.put("None", null);
        map.put("GlorotUniform", GlorotUniform.class);
        map.put("GlorotNormal", GlorotNormal.class);
        map.put("Zeros", Zeros.class);
        map.put("Ones", Ones.class);
        map.put("Constant", Constant.class);
        map.put("Identity", Identity.class);
        map.put("Orthogonal", Orthogonal.class);
        map.put("RandomNormal", RandomNormal.class);
        map.put("RandomUniform", RandomUniform.class);
        map.put("TruncatedNormal", TruncatedNormal.class);
        map.put("VarianceScaling", VarianceScaling.class);
    }

    public static String[] getSelections() {
        String[] ret = new String[map.size()];
        map.keySet().toArray(ret);
        return ret;
    }

    public static BaseInitializer select(String str) {
        if (map.containsKey(str)) {
            Class<?> cls = map.get(str);
            if (cls == null)
                return null;
            try {
                return (BaseInitializer) cls.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
        return null;
    }

}

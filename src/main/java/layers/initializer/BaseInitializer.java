package layers.initializer;

import layers.ConfigurableObject;

import java.util.HashMap;
import java.util.Map;

public class BaseInitializer extends ConfigurableObject {

    public final static Map<String, Class<?>> map;
    public final static String defaultSelection = "GlorotUniform";

    static {
        map = new HashMap<>();
        map.put("None", null);
        map.put("GlorotUniform", GlorotUniform.class);
        map.put("Zeros", Zeros.class);
    }

    public static String[] getSelections() {
        String[] ret = new String[map.size()];
        map.keySet().toArray(ret);
        return ret;
    }

    public static BaseInitializer select(String str) {
        if (map.containsKey(str)) {
            Class<?> cls = map.get(str);
            if (cls != null) {
                try {
                    return (BaseInitializer) cls.newInstance();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;
    }

}

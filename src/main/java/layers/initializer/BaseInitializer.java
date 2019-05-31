package layers.initializer;

import layers.ConfigurableObject;

import java.util.HashMap;
import java.util.Map;

public class BaseInitializer extends ConfigurableObject {

    public final static Map<String, Class<?>> map;

    static {
        map = new HashMap<>();
        map.put("GlorotUniform", GlorotUniform.class);
        map.put("Zeros", Zeros.class);
        map.put("Ones", Ones.class);
        map.put("Constant", Constant.class);
    }

    public static String[] getSelections() {
        String[] ret = new String[map.size()];
        map.keySet().toArray(ret);
        return ret;
    }

    public static BaseInitializer select(String str) {
        if (map.containsKey(str)) {
            Class<?> cls = map.get(str);
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

package layers.regularizer;

import layers.ConfigurableObject;

import java.util.HashMap;
import java.util.Map;

public class BaseRegularizer extends ConfigurableObject {

    public final static Map<String, Class<?>> map;

    static {
        map = new HashMap<>();
    }

    public static String[] getSelections() {
        String[] ret = new String[map.size()];
        map.keySet().toArray(ret);
        return ret;
    }

    public static BaseRegularizer select(String str) {
        if (map.containsKey(str)) {
            Class<?> cls = map.get(str);
            try {
                return (BaseRegularizer) cls.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
        return null;
    }
}

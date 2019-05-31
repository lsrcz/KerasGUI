package layers.constraint;

import layers.ConfigurableObject;

import java.util.HashMap;
import java.util.Map;

public class BaseConstraint extends ConfigurableObject {

    public final static Map<String, Class<?>> map;

    static {
        map = new HashMap<>();
        map.put("None", null);
        map.put("MaxNorm", MaxNorm.class);
        map.put("NonNeg", NonNeg.class);
        map.put("MinMaxNorm", MinMaxNorm.class);
        map.put("UnitNorm", UnitNorm.class);
    }

    public static String[] getSelections() {
        String[] ret = new String[map.size()];
        map.keySet().toArray(ret);
        return ret;
    }


    public static BaseConstraint select(String str) {
        if (map.containsKey(str)) {
            Class<?> cls = map.get(str);
            if (cls == null)
                return null;
            try {
                return (BaseConstraint) cls.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
        return null;
    }
}

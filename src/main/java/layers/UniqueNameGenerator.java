package layers;

import java.util.HashMap;
import java.util.Map;

// generate unique names
public class UniqueNameGenerator {
    private Map<String, Map<String, Integer>> m;
    private static UniqueNameGenerator instance = new UniqueNameGenerator();

    private UniqueNameGenerator() {
        m = new HashMap<>();
    }

    public static UniqueNameGenerator getInstance() {
        return instance;
    }

    public String generate(String scope, String name) {
        if (!m.containsKey(scope)) {
            m.put(scope, new HashMap<>());
        }
        Map<String, Integer> scopeMap = m.get(scope);
        if (!scopeMap.containsKey(name)) {
            scopeMap.put(name, 0);
        }
        int num = scopeMap.get(name);
        String ret = name + '_' + num;
        scopeMap.put(name, num + 1);
        return ret;
    }
}

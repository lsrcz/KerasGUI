package io.github.lsrcz.kerasgui.layers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for generate unique names.
 *
 * @author Sirui Lu
 */
public class UniqueNameGenerator implements Serializable {
    private Map<String, Map<String, Integer>> m;
    private static UniqueNameGenerator instance = new UniqueNameGenerator();

    private UniqueNameGenerator() {
        m = new HashMap<>();
    }

    /**
     * Get singleton instance.
     * @return The instance.
     */
    public static UniqueNameGenerator getInstance() {
        return instance;
    }

    /**
     * Util used in the saving and loading.
     *
     * @param instance The replacement.
     */
    public static void updateInstance(UniqueNameGenerator instance) {
        if (instance == null)
            throw new IllegalArgumentException("Don't save null");
        UniqueNameGenerator.instance = instance;
    }

    /**
     * Generate a name.
     * @param scope The scope for unique names.
     * @param name The name prefix.
     * @return An unique name.
     */
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

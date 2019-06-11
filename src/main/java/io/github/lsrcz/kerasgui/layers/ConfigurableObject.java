package io.github.lsrcz.kerasgui.layers;

import io.github.lsrcz.kerasgui.layers.annotation.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The class is the base class for all configurable keras objects.
 *
 * @author Sirui Lu
 */
public abstract class ConfigurableObject implements Serializable {
    ConfigurableObject link;

    /**
     * Basic initialization of configurable objects, should be used with init().
     * @see ConfigurableObject#init()
     */
    public ConfigurableObject() {
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (!f.isAnnotationPresent(ConfigProperty.class)) {
                continue;
            }
            if (!ConfigurableObject.class.isAssignableFrom(f.getType())) {
                continue;
            }
            f.setAccessible(true);
            if (f.isAnnotationPresent(DefaultStringProperty.class)) {
                DefaultStringProperty annotation = f.getAnnotation(DefaultStringProperty.class);
                try {
                    setString(f.getName(), annotation.defaultString());
                    if (f.get(this) != null)
                        ((ConfigurableObject) f.get(this)).link = this;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            } else {
                try {
                    ConfigurableObject obj = (ConfigurableObject) f.getType().newInstance();
                    obj.link = this;
                    f.set(this, obj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    protected ArrayList<Field> getDeclaredFieldsUntilConfigurableObject(Class<?> cls) {
        return ClassUtils.getDeclaredFieldsUntilClass(cls, ConfigurableObject.class, true);
    }

    protected Field getDeclaredFieldUntilConfigurableObject(Class<?> cls, String name) throws NoSuchFieldException {
        return ClassUtils.getDeclaredFieldUntilClass(cls, ConfigurableObject.class, name, true);
    }

    protected Method getDeclaredMethodUntilConfigurableObject(Class<?> cls, String name) throws NoSuchMethodException {
        return ClassUtils.getDeclaredMethodUntilClass(cls, ConfigurableObject.class, name, true);
    }

    /**
     * Initialization, correctly handle links.
     * @see ConfigurableObject#ConfigurableObject()
     */
    public void init() {
        // recursively init
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (!f.isAnnotationPresent(ConfigProperty.class)) {
                continue;
            }
            if (!ConfigurableObject.class.isAssignableFrom(f.getType())) {
                continue;
            }
            f.setAccessible(true);
            try {
                ConfigurableObject obj = (ConfigurableObject) f.get(this);
                if (obj != null)
                    obj.init();
            } catch (Exception ex) {
                throw new Error("BUG!!!");
            }
        }

        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (!f.isAnnotationPresent(ConfigProperty.class)) {
                continue;
            }
            // generate unique names
            if (f.isAnnotationPresent(UniqueProperty.class)) {
                UniqueProperty annotation = f.getAnnotation(UniqueProperty.class);
                try {
                    setString(f.getName(),
                            UniqueNameGenerator.getInstance()
                                    .generate(annotation.scope(), annotation.prefix()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
            // apply default strings
            if (f.isAnnotationPresent(DefaultStringProperty.class) &&
                    !ConfigurableObject.class.isAssignableFrom(f.getType())) {
                DefaultStringProperty annotation = f.getAnnotation(DefaultStringProperty.class);
                try {
                    setString(f.getName(), annotation.defaultString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    /**
     * Get all configurable property.
     * @return An ArrayList of the name of all the configurable properties.
     */
    public ArrayList<String> getConfigureList() {
        ArrayList<String> ret = new ArrayList<>();
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                ret.add(f.getName());
            }
        }
        return ret;
    }

    /**
     * Find all linked property by DFS.
     * @param name The name of the field.
     * @param walked An helper set for the algorithm.
     * @return An Set of the object-field pairs.
     */
    private Set<Pair<Object, Field>> doWalkLinked(String name, Set<Object> walked) {
        walked.add(this);
        Set<Pair<Object, Field>> ret = new HashSet<>();
        ArrayList<Field> fields = getDeclaredFieldsUntilConfigurableObject(this.getClass());
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (f.isAnnotationPresent(LinkedProperty.class)) {
                LinkedProperty annotation = f.getAnnotation(LinkedProperty.class);
                if (annotation.name().equals(name)) {
                    ret.add(new Pair<>(this, f));
                }
            } else if (ConfigurableObject.class.isAssignableFrom(f.getType())) {
                try {
                    f.setAccessible(true);
                    ConfigurableObject obj = (ConfigurableObject) f.get(this);
                    if (obj != null && !walked.contains(obj))
                        ret.addAll(obj.doWalkLinked(name, walked));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return ret;
    }

    private Set<Pair<Object, Field>> walkLinked(String name) {
        Set<Object> walked = new HashSet<>();
        return doWalkLinked(name, walked);
    }

    /**
     * Return all the fields that are needed to be set in a single assign operation.
     * @param name The name of the field.
     * @return An Set of the object-field pairs for all the fields that are needed to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @see NoSuchFieldException
     */
    private Set<Pair<Object, Field>> getNeedSetSet(String name) throws NoSuchFieldException {
        Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), name);
        if (!f.isAnnotationPresent(ConfigProperty.class))
            throw new IllegalArgumentException("Not configurable");
        Set<Pair<Object, Field>> needSet = new HashSet<>();
        needSet.add(new Pair<>(this, f));
        if (f.isAnnotationPresent(LinkedProperty.class)) {
            LinkedProperty annotation = f.getAnnotation(LinkedProperty.class);
            needSet = walkLinked(annotation.name());
        }
        return needSet;
    }

    /**
     * Set an integer field.
     * @param name The name of the field.
     * @param num The value to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @see NoSuchFieldException
     */
    public void setInt(String name, int num) throws NoSuchFieldException {
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() != int.class)
                throw new IllegalArgumentException("Not int type");
            try {
                f.setInt(obj, num);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Set an boolean field.
     * @param name The name of the field.
     * @param choice The value to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @see NoSuchFieldException
     */
    public void setBoolean(String name, boolean choice) throws NoSuchFieldException {
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() != boolean.class)
                throw new IllegalArgumentException("Not boolean type");
            try {
                f.setBoolean(obj, choice);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Set an integer array field.
     * @param name The name of the field.
     * @param array The value to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @see NoSuchFieldException
     */
    public void setIntegerArray(String name, int[] array) throws NoSuchFieldException {
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() != int[].class)
                throw new IllegalArgumentException("Not int[] type");
            try {
                f.set(obj, array);
            } catch (IllegalAccessException ex) {
                throw new Error("BUG!!!");
            }
        }
    }

    /**
     * Set an Integer(nullable) field.
     * @param name The name of the field.
     * @param num The value to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @see NoSuchFieldException
     */
    public void setNullableInt(String name, Integer num) throws NoSuchFieldException {
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() != Integer.class)
                throw new IllegalArgumentException("Not Integer type");
            try {
                f.set(obj, num);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Set an double field.
     * @param name The name of the field.
     * @param num The value to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @see NoSuchFieldException
     */
    public void setDouble(String name, double num) throws NoSuchFieldException {
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() != double.class)
                throw new IllegalArgumentException("Not double type");
            try {
                f.setDouble(obj, num);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Set an String/ConfigurableObject with selection field.
     * @param name The name of the field.
     * @param str The value to be set.
     * @throws NoSuchFieldException If the name is not corresponded to a configurable property.
     * @throws NoSuchMethodException If the name is not corresponded to a configurable String and the type don't have an select(String) method.
     * @see NoSuchFieldException
     * @see NoSuchMethodException
     */
    public void setString(String name, String str) throws NoSuchFieldException, NoSuchMethodException {
        Object newObj = null;
        boolean newObjAssigned = false;
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() == String.class) {
                if ("__null__".equals(str))
                    str = null;
                try {
                    f.set(obj, str);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            } else if (ConfigurableObject.class.isAssignableFrom(f.getType())) {
                Method m = f.getType().getMethod("select", String.class);
                m.setAccessible(true);
                try {
                    if (!newObjAssigned) {
                        newObj = m.invoke(null, str);
                        newObjAssigned = true;
                        if (newObj != null) {
                            ((ConfigurableObject) newObj).link = (ConfigurableObject) obj;
                            ((ConfigurableObject) newObj).init();
                        }
                    }
                    f.set(obj, newObj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            } else {
                throw new NoSuchFieldException();
            }
        }
    }

    private Class<?> getConfigType(String name) {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), name);
            return f.getType();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get all selections for string or selectable configurable objects.
     * @param name The name of the field.
     * @return The selections.
     */
    public String[] getSelection(String name) {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), name);
            if (f.isAnnotationPresent(SelectStringProperty.class)) {
                SelectStringProperty annotation = f.getAnnotation(SelectStringProperty.class);
                return annotation.selections();
            } else if (ConfigurableObject.class.isAssignableFrom(f.getType())) {
                Method m = getDeclaredMethodUntilConfigurableObject(f.getType(), "getSelections");
                m.setAccessible(true);
                try {
                    return (String[]) m.invoke(null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new Error("BUG!!!");
        }
    }

    private Object getObject(String name, Class<?> cls, boolean nonNull) {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), name);
            if (!f.isAnnotationPresent(ConfigProperty.class))
                throw new IllegalAccessException("Not configurable");
            f.setAccessible(true);
            Object got = f.get(this);
            if (nonNull && got == null)
                throw new Error("BUG!!!");
            return cls.cast(f.get(this));
        } catch (Exception ex) {
            throw new Error("BUG!!!");
        }
    }

    /**
     * Get an ConfigurableObject.
     * @param name The name of the field.
     * @return The object.
     */
    public ConfigurableObject getConfigurableObject(String name) {
        return (ConfigurableObject) getObject(name, ConfigurableObject.class, false);
    }

    /**
     * Get an String.
     * @param name The name of the field.
     * @return The object.
     */
    public String getString(String name) {
        return (String) getObject(name, String.class, false);
    }

    /**
     * Get an int.
     * @param name The name of the field.
     * @return The object.
     */
    public int getInteger(String name) {
        return (int) getObject(name, Integer.class, true);
    }

    /**
     * Get an int array.
     * @param name The name of the field.
     * @return The object.
     */
    public int[] getIntegerArray(String name) {
        return (int[]) getObject(name, int[].class, false);
    }

    /**
     * Get an Integer(nullable).
     * @param name The name of the field.
     * @return The object.
     */
    public Integer getNullableInteger(String name) {
        return (Integer) getObject(name, Integer.class, false);
    }

    /**
     * Get an double.
     * @param name The name of the field.
     * @return The object.
     */
    public double getDouble(String name) {
        return (double) getObject(name, Double.class, true);
    }

    /**
     * Get an boolean.
     * @param name The name of the field.
     * @return The object.
     */
    public boolean getBoolean(String name) {
        return (boolean) getObject(name, Boolean.class, true);
    }

    /**
     * Decide if the name is corresponded to an int property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an int property.
     */
    public boolean isIntegerConfig(String name) {
        return getConfigType(name) == int.class;
    }

    /**
     * Decide if the name is corresponded to an int[] property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an int[] property.
     */
    public boolean isIntegerArrayConfig(String name) {
        return getConfigType(name) == int[].class;
    }

    /**
     * Decide if the name is corresponded to an Integer property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an Integer property.
     */
    public boolean isNullableIntegerConfig(String name) {
        return getConfigType(name) == Integer.class;
    }

    /**
     * Decide if the name is corresponded to an double property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an double property.
     */
    public boolean isDoubleConfig(String name) {
        return getConfigType(name) == double.class;
    }

    /**
     * Decide if the name is corresponded to an String property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an String property.
     */
    public boolean isStringConfig(String name) {
        return getConfigType(name) == String.class;
    }

    /**
     * Decide if the name is corresponded to an boolean property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an boolean property.
     */
    public boolean isBooleanConfig(String name) {
        return getConfigType(name) == boolean.class;
    }

    /**
     * Decide if the name is corresponded to an ConfigurableObjectConfig property.
     * @param name The name of the field.
     * @return True if the name is corresponded to an ConfigurableObjectConfig property.
     */
    public boolean isConfigurableObjectConfig(String name) {
        Class<?> t = getConfigType(name);
        if (t != null) {
            return ConfigurableObject.class.isAssignableFrom(t);
        }
        return false;
    }
}

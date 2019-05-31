package layers;

import javafx.util.Pair;
import layers.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ConfigurableObject {
    ConfigurableObject link;

    public ConfigurableObject() {
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                f.setAccessible(true);
                if (ConfigurableObject.class.isAssignableFrom(f.getType())) {
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
        }
    }

    protected static ArrayList<Field> getDeclaredFieldsUntilConfigurableObject(Class<?> cls) {
        ArrayList<Field> ret = new ArrayList<>();
        if (ConfigurableObject.class.isAssignableFrom(cls)) {
            while (true) {
                Collections.addAll(ret, cls.getDeclaredFields());
                if (cls == ConfigurableObject.class)
                    break;
                cls = cls.getSuperclass();
            }
            return ret;
        } else {
            throw new Error("BUG!!!");
        }
    }

    protected static Field getDeclaredFieldUntilConfigurableObject(Class<?> cls, String name) throws NoSuchFieldException {
        if (ConfigurableObject.class.isAssignableFrom(cls)) {
            while (true) {
                try {
                    return cls.getDeclaredField(name);
                } catch (Exception ex) {
                    if (cls == ConfigurableObject.class)
                        break;
                    cls = cls.getSuperclass();
                }
            }
            throw new NoSuchFieldException();
        } else {
            throw new Error("BUG!!!");
        }
    }

    protected static Method getDeclaredMethodUntilConfigurableObject(Class<?> cls, String name) throws NoSuchMethodException {
        if (ConfigurableObject.class.isAssignableFrom(cls)) {
            while (true) {
                try {
                    return cls.getDeclaredMethod(name);
                } catch (Exception ex) {
                    if (cls == ConfigurableObject.class)
                        break;
                    cls = cls.getSuperclass();
                }
            }
            throw new NoSuchMethodException();
        } else {
            throw new Error("BUG!!!");
        }
    }

    public void init() {
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                f.setAccessible(true);
                if (ConfigurableObject.class.isAssignableFrom(f.getType())) {
                    try {
                        ConfigurableObject obj = (ConfigurableObject) f.get(this);
                        if (obj != null)
                            obj.init();
                    } catch (Exception ex) {
                        throw new Error("BUG!!!");
                    }
                }
            }
        }
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                if (f.isAnnotationPresent(UniqueProperty.class)) {
                    UniqueProperty annotation = f.getAnnotation(UniqueProperty.class);
                    try {
                        setString(f.getName(), UniqueNameGenerator.getInstance().generate(annotation.scope(), annotation.prefix()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(-1);
                    }
                }
                if (f.isAnnotationPresent(DefaultStringProperty.class) && !ConfigurableObject.class.isAssignableFrom(f.getType())) {
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
    }

    public ArrayList<String> getConfigureList() {
        ArrayList<String> ret = new ArrayList<>();
        for (Field f : getDeclaredFieldsUntilConfigurableObject(this.getClass())) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                ret.add(f.getName());
            }
        }
        return ret;
    }

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

    public void setNullableInt(String name, Integer num) throws NoSuchFieldException {
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() != Integer.class)
                throw new IllegalArgumentException("Not Integer type");
            try {
                f.setInt(obj, num);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

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

    public void setString(String name, String str) throws NoSuchFieldException, NoSuchMethodException {
        Object newObj = null;
        boolean newObjAssigned = false;
        for (Pair<Object, Field> p : getNeedSetSet(name)) {
            Object obj = p.getKey();
            Field f = p.getValue();
            f.setAccessible(true);
            if (f.getType() == String.class) {
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
                        if (newObj != null)
                            ((ConfigurableObject) newObj).link = (ConfigurableObject) obj;
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

    public String[] getSelection(String name) {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), name);
            if (f.isAnnotationPresent(SelectStringProperty.class)) {
                SelectStringProperty annotation = f.getAnnotation(SelectStringProperty.class);
                return annotation.selections();
            } else {
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

    private Object getObject(String name, Class<?> cls) {
        try {
            Field f = getDeclaredFieldUntilConfigurableObject(this.getClass(), name);
            f.setAccessible(true);
            return cls.cast(f.get(this));
        } catch (Exception ex) {
            throw new Error("BUG!!!");
        }
    }

    public ConfigurableObject getConfigurableObject(String name) {
        return (ConfigurableObject) getObject(name, ConfigurableObject.class);
    }

    public String getString(String name) {
        return (String) getObject(name, String.class);
    }

    public int getInteger(String name) {
        return (int) getObject(name, int.class);
    }

    public int getNullableInteger(String name) {
        return (Integer) getObject(name, Integer.class);
    }

    public double getDouble(String name) {
        return (double) getObject(name, double.class);
    }

    public boolean isIntegerConfig(String name) {
        return getConfigType(name) == int.class;
    }

    public boolean isNullableIntegerConfig(String name) {
        return getConfigType(name) == Integer.class;
    }

    public boolean isDoubleConfig(String name) {
        return getConfigType(name) == double.class;
    }

    public boolean isStringConfig(String name) {
        return getConfigType(name) == String.class;
    }

    public boolean isBooleanConfig(String name) {
        return getConfigType(name) == boolean.class;
    }

    public boolean isConfigurableObjectConfig(String name) {
        Class<?> t = getConfigType(name);
        if (t != null) {
            return ConfigurableObject.class.isAssignableFrom(t);
        }
        return false;
    }
}

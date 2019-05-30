package layers;

import com.google.gson.annotations.Expose;
import javafx.util.Pair;
import layers.annotation.*;
import sun.awt.image.ImageWatched;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public abstract class ConfigurableObject {

    public ConfigurableObject() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                f.setAccessible(true);
                if (ConfigurableObject.class.isAssignableFrom(f.getType())) {
                    if (f.isAnnotationPresent(DefaultStringProperty.class))
                        continue;
                    try {
                        f.set(this, f.getType().newInstance());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(-1);
                    }
                }
            }
        }

        for (Field f : this.getClass().getDeclaredFields()) {
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
                if (f.isAnnotationPresent(DefaultStringProperty.class)) {
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
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(ConfigProperty.class)) {
                ret.add(f.getName());
            }
        }
        return ret;
    }

    private Set<Pair<Object, Field>> doWalkLinked(String name, Set<Object> walked) {
        walked.add(this);
        Set<Pair<Object, Field>> ret = new HashSet<>();
        for (Field f : this.getClass().getDeclaredFields()) {
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

    public Set<Pair<Object, Field>> walkLinked(String name) {
        Set<Object> walked = new HashSet<>();
        return doWalkLinked(name, walked);
    }

    private Set<Pair<Object, Field>> getNeedSetSet(String name) throws NoSuchFieldException {
        Field f = this.getClass().getDeclaredField(name);
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
            Field f = this.getClass().getDeclaredField(name);
            return f.getType();
        } catch (Exception ex) {
            return null;
        }
    }

    public String[] getSelection(String name) {
        try {
            Field f = this.getClass().getDeclaredField(name);
            if (f.isAnnotationPresent(SelectStringProperty.class)) {
                SelectStringProperty annotation = f.getAnnotation(SelectStringProperty.class);
                return annotation.selections();
            } else {
                Method m = f.getType().getDeclaredMethod("getSelections");
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
            return null;
        }
    }

    public ConfigurableObject getConfigurableObject(String name) {
        try {
            Field f = this.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (ConfigurableObject) f.get(this);
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isIntegerConfig(String name) {
        return getConfigType(name) == int.class;
    }

    public boolean isNullableIntegerConfig(String name) {
        return getConfigType(name) == Integer.class;
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

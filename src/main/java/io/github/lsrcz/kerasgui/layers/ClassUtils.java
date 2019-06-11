package io.github.lsrcz.kerasgui.layers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Utils for getting the fields and methods.
 *
 * @author Sirui Lu
 */
public class ClassUtils {

    /**
     * Get declared fields with respect to the super classes
     * @param cls The class to get.
     * @param until A super class of cls.
     * @param include If true, will return the field in the until class.
     * @return An ArrayList of all the declared fields.
     */
    public static ArrayList<Field> getDeclaredFieldsUntilClass(Class<?> cls, Class<?> until, boolean include) {
        ArrayList<Field> ret = new ArrayList<>();
        if (until.isAssignableFrom(cls)) {
            while (true) {
                if (!include && cls == until)
                    break;
                Collections.addAll(ret, cls.getDeclaredFields());
                if (cls == until)
                    break;
                cls = cls.getSuperclass();
            }
            return ret;
        } else {
            throw new Error("BUG!!!");
        }
    }

    /**
     * Get declared field with respect to the super classes
     * @param cls The class to get.
     * @param until A super class of cls.
     * @param name The name of the field
     * @param include If true, will return the field in the until class.
     * @return The field found.
     * @throws NoSuchFieldException If the name is not corresponded to a field.
     * @see NoSuchFieldException
     */
    public static Field getDeclaredFieldUntilClass(Class<?> cls, Class<?> until, String name, boolean include) throws NoSuchFieldException {
        if (until.isAssignableFrom(cls)) {
            while (true) {
                if (!include && cls == until)
                    break;
                try {
                    return cls.getDeclaredField(name);
                } catch (Exception ex) {
                    if (cls == until)
                        break;
                    cls = cls.getSuperclass();
                }
            }
            throw new NoSuchFieldException();
        } else {
            throw new Error("BUG!!!");
        }
    }

    /**
     * Get declared method with respect to the super classes
     * @param cls The class to get.
     * @param until A super class of cls.
     * @param name The name of the method
     * @param include If true, will return the method in the until class.
     * @return The method found.
     * @throws NoSuchMethodException If the name is not corresponded to a method.
     * @see NoSuchMethodException
     */
    public static Method getDeclaredMethodUntilClass(Class<?> cls, Class<?> until, String name, boolean include) throws NoSuchMethodException {
        if (until.isAssignableFrom(cls)) {
            while (true) {
                if (!include && cls == until)
                    break;
                try {
                    return cls.getDeclaredMethod(name);
                } catch (Exception ex) {
                    if (cls == until)
                        break;
                    cls = cls.getSuperclass();
                }
            }
            throw new NoSuchMethodException();
        } else {
            throw new Error("BUG!!!");
        }
    }
}

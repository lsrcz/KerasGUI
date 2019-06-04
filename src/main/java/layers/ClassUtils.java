package layers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class ClassUtils {

    // get declared fields with respect to the super classes
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

    // get declared field with respect to the super classes
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

    // get declared method with respect to the super classes
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

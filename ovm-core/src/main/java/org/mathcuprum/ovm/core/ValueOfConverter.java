package org.mathcuprum.ovm.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ebuldygin on 19.08.2017.
 */
public class ValueOfConverter extends StringConverter {

    private final Class<?> fieldType;

    public ValueOfConverter(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public Object convertToObject(String value) {
        return valueOf(super.convertToObject(value));
    }

    protected Object valueOf(Object value) {
        if (value == null || "".equals(value)) {
            return null;
        }
        try {
            Method valueOf = fieldType.getMethod("valueOf", String.class);
            valueOf.setAccessible(true);
            return valueOf.invoke(null, value.toString());
        } catch (IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new ConvertException("Couldn't parse " + value, e, getViewType(), value);
        }
    }
}

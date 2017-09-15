package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by ebuldygin on 20.08.2017.
 */
class FieldProperty implements Property {

    private final String name;
    private final int order;
    private final Field field;

    FieldProperty(Field field, String name, int order) {
        this.field = field;
        this.order = order;
        if (name == null || "".equals(name)) {
            this.name = field.getName();
        } else {
            this.name = name;
        }
        field.setAccessible(true);
    }

    @Override
    public void set(Object target, Object value) {
        try {
            field.set(target, value);
        } catch (Exception e) {
            throw new ReflectionException("Couldn't set field value " + value + " for " + field.getName(), e);
        }
    }

    @Override
    public Object get(Object source) {
        try {
            return field.get(source);
        } catch (Exception e) {
            throw new ReflectionException("Couldn't get field value from " + field.getName(), e);
        }
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "FieldProperty{" +
                "name='" + name + '\'' +
                ", order=" + order +
                ", field=true" +
                '}';
    }
}

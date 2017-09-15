package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by ebuldygin on 20.08.2017.
 */
class MethodProperty implements Property {

    private final String name;
    private final int order;
    private final Method accessor;
    private final Method mutator;

    MethodProperty(java.beans.PropertyDescriptor p, String name, int order) {
        this.accessor = p.getReadMethod();
        this.mutator = p.getWriteMethod();
        this.order = order;
        if (name == null || "".equals(name)) {
            this.name = p.getName();
        } else {
            this.name = name;
        }
        this.accessor.setAccessible(true);
        if (mutator != null) {
            this.mutator.setAccessible(true);
        }
    }

    @Override
    public void set(Object target, Object value) {
        if (mutator == null) {
            return;
        }
        try {
            mutator.invoke(target, value);
        } catch (Exception e) {
            throw new ReflectionException("Couldn't invoke setter on value " + value + " for " + mutator.getName(), e);
        }
    }

    @Override
    public Object get(Object source) {
        try {
            return accessor.invoke(source);
        } catch (Exception e) {
            throw new ReflectionException("Couldn't invoke getter for " + accessor.getDeclaringClass() + "." + accessor.getName(), e);
        }
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return accessor.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return accessor.getReturnType();
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "MethodProperty{" +
                "name='" + name + '\'' +
                ", order=" + order +
                ", method=true" +
                '}';
    }
}

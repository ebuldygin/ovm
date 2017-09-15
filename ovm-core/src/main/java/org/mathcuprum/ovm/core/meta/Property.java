package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.Annotation;

/**
 * Created by ebuldygin on 20.08.2017.
 */
interface Property {

    void set(Object target, Object value);

    Object get(Object source);

    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    String getName();

    Class<?> getType();

    int getOrder();

}

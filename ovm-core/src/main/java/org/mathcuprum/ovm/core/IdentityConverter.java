package org.mathcuprum.ovm.core;

import org.mathcuprum.ovm.core.ConvertException;
import org.mathcuprum.ovm.core.Converter;

/**
 * Created by ebuldygin on 20.08.2017.
 */
public class IdentityConverter<T> implements Converter<T> {

    private final Class<T> fieldType;

    public IdentityConverter(Class<T> fieldType) {
        this.fieldType = fieldType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convertToView(Object value) {
        return (T) value;
    }

    @Override
    public Object convertToObject(T value) {
        return value;
    }

    @Override
    public Class<T> getViewType() {
        return fieldType;
    }
}

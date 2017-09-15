package org.mathcuprum.ovm.core.meta;

import org.mathcuprum.ovm.core.ConvertException;
import org.mathcuprum.ovm.core.Converter;
import org.mathcuprum.ovm.core.ParameterDescriptor;

/**
 * Created by ebuldygin on 20.08.2017.
 */
class ConverterWrapper<T> implements Converter<T> {

    private final Converter<T> converter;
    private final ParameterDescriptor descriptor;

    ConverterWrapper(Converter<T> converter, ParameterDescriptor descriptor) {
        this.converter = converter;
        this.descriptor = descriptor;
    }

    @Override
    public Class<T> getViewType() {
        return converter.getViewType();
    }

    @Override
    public Object convertToObject(T value) {
        try {
            return converter.convertToObject(value);
        } catch (ConvertException e) {
            e.setDescriptor(descriptor);
            throw e;
        }
    }

    @Override
    public T convertToView(Object value) {
        try {
            return converter.convertToView(value);
        } catch (ConvertException e) {
            e.setDescriptor(descriptor);
            throw e;
        }
    }
}

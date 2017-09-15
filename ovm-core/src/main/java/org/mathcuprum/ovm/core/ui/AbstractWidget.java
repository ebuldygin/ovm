package org.mathcuprum.ovm.core.ui;

import org.mathcuprum.ovm.core.*;

/**
 * Created by ebuldygin on 20.08.2017.
 */
public abstract class AbstractWidget<T> implements Widget, Searchable {

    protected final Widget parent;
    protected final ParameterDescriptor descriptor;
    protected final Converter<T> converter;

    protected AbstractWidget(Widget parent, ParameterDescriptor descriptor) {
        this.parent = parent;
        this.converter = descriptor.converterFor(getViewType());
        this.descriptor = descriptor;
    }

    @Override
    public Object get() throws ValidationException, ConvertException {
        Object res = converter.convertToObject(getViewValue());
        return res;
    }

    @Override
    public void set(Object value) {
        setViewValue(converter.convertToView(value));
    }

    @Override
    public void writeToModel(Object model) {
        Object value = get();
        descriptor.setInstanceValue(model, value);
    }

    @Override
    public void readFomModel(Object model) {
        Object value = descriptor.getInstanceValue(model);
        set(value);
    }

    @Override
    public String getId() {
        return descriptor.getPropertyName();
    }

    @Override
    public String getTitle() {
        return descriptor.getTitle();
    }

    @Override
    public Widget searchById(String id) {
        if (id.equals(getId())) {
            return this;
        } else {
            return null;
        }
    }

    protected abstract Class<T> getViewType();

    protected abstract void setViewValue(T value);

    protected abstract T getViewValue();
}

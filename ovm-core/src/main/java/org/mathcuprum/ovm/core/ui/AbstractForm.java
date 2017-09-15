package org.mathcuprum.ovm.core.ui;

import org.mathcuprum.ovm.core.*;
import org.mathcuprum.ovm.core.meta.ClassDescriptor;

import java.util.*;

/**
 * Created by ebuldygin on 13.08.2017.
 */
public abstract class AbstractForm<T> implements Widget, Searchable {

    private final Map<String, Widget> widgets = new LinkedHashMap<>();
    private final Descriptor descriptor;
    private final List<Validator> validators = new LinkedList<>();

    public AbstractForm(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public AbstractForm(Class<T> formClass) {
        this(ClassDescriptor.from(formClass));
    }

    protected void initWidgets() {
        for (Widget widget : buildWidgets(descriptor)) {
            widgets.put(widget.getId(), widget);
        }
    }

    protected abstract List<Widget> buildWidgets(Descriptor descriptor);

    @Override
    public T get() {
        return getAs((Class<T>) descriptor.getValueType());
    }

    public <S extends T> S getAs(Class<S> target) {
        S res;
        try {
            res = target.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "Couldn't instantiate object " + descriptor.getValueType().getName(), e);
        }
        for (Widget widget : widgets.values()) {
            widget.writeToModel(res);
        }
        for (Validator v : validators) {
            v.validate(res);
        }

        return res;
    }

    @Override
    public void set(Object newValue) {
        // TODO сделать откат, если пытаются поставить кривые значения
        for (Widget widget : widgets.values()) {
            widget.readFomModel(newValue);
        }
    }

    public void setValidators(Validator... validators) {
        this.validators.clear();
        this.validators.addAll(Arrays.asList(validators));
    }

    @Override
    public String getId() {
        if (descriptor.getPropertyName() == null) {
            return "";
        }
        return descriptor.getPropertyName();
    }

    @Override
    public void writeToModel(Object model) {
        if (descriptor instanceof Accessible) {
            Object value = get();
            ((Accessible) descriptor).setInstanceValue(model, value);
        } else {
            for (Widget widget : widgets.values()) {
                widget.writeToModel(model);
            }
        }
    }

    @Override
    public void readFomModel(Object model) {
        if (descriptor instanceof Accessible) {
            model = ((Accessible) descriptor).getInstanceValue(model);
        }
        set(model);
    }

    @Override
    public String getTitle() {
        return descriptor.getTitle();
    }

    @Override
    public Widget searchById(String id) {
        if (id.startsWith(ParameterDescriptor.DELIMITER)
                || id.startsWith(ParameterDescriptor.DELIMITER)) {
            throw new IllegalArgumentException(id);
        }
        if (id.contains(ParameterDescriptor.DELIMITER)) {
            int delimiterPos = id.indexOf(ParameterDescriptor.DELIMITER);
            String parentId = id.substring(0, delimiterPos);
            String childId = id.substring(delimiterPos + 1);
            Widget found = widgets.get(parentId);
            if (found instanceof Searchable) {
                return ((Searchable) found).searchById(childId);
            }
        } else {
            return widgets.get(id);
        }
        return null;
    }

    public Class<T> getModelClass() {
        return (Class<T>) descriptor.getValueType();
    }
}

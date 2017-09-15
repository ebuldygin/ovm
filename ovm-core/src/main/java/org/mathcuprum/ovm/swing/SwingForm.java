package org.mathcuprum.ovm.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mathcuprum.ovm.core.Descriptor;
import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.Widget;
import org.mathcuprum.ovm.core.ui.AbstractForm;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ebuldygin on 13.08.2017.
 */
public class SwingForm<T> extends AbstractForm<T> implements SwingWidget {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingForm.class);

    private final SwingFactory factory;
    private JPanel panel;

    public SwingForm(Class<T> formClass) {
        this(formClass, SwingFactory.SIMPLE_FACTORY);
    }

    public SwingForm(ParameterDescriptor descriptor) {
        this(descriptor, new SwingFactory());
    }

    public SwingForm(Class<T> formClass, SwingFactory factory) {
        super(formClass);
        this.factory = factory;
        initWidgets();
    }

    public SwingForm(ParameterDescriptor descriptor, SwingFactory factory) {
        super(descriptor);
        this.factory = factory;
        initWidgets();
    }

    @Override
    protected List<Widget> buildWidgets(final Descriptor descriptor) {
        final SwingBuilder builder = factory.createBuilder(this, descriptor);
        final List<Widget> widgets = new LinkedList<>();
        for (ParameterDescriptor field : descriptor.getChildren()) {
            if (field.getChildren().isEmpty() ) {
                // простой параметр
                widgets.add(builder.add(field));
            } else {
                // параметр-сложный объект
                SwingForm<?> nested = new SwingForm<>(field, factory);
                builder.add(nested);
                widgets.add(nested);
            }
        }
        for (Widget widget : widgets) {
            LOGGER.debug("Widget was added {} ({})", widget.getId(), widget.getClass().getName());
        }
        panel = builder.build();
        return widgets;
    }

    @Override
    public JPanel getComponent() {
        return panel;
    }
}

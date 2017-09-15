package org.mathcuprum.ovm.swing.widgets;

import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.ui.AbstractWidget;
import org.mathcuprum.ovm.swing.SwingWidget;

import javax.swing.*;

/**
 * Created by ebuldygin on 16.08.2017.
 */
public class LogicalSingle extends AbstractWidget<Boolean> implements SwingWidget {

    private final JRadioButton radioButton = new JRadioButton();

    public LogicalSingle(SwingWidget parent, ParameterDescriptor descriptor) {
        super(parent, descriptor);
        this.radioButton.setText(descriptor.getTitle());
    }

    @Override
    protected Boolean getViewValue() {
        return radioButton.isSelected();
    }

    @Override
    protected void setViewValue(Boolean value) {
        radioButton.setSelected(Boolean.TRUE.equals(value));
    }

    @Override
    protected Class<Boolean> getViewType() {
        return Boolean.class;
    }

    @Override
    public JRadioButton getComponent() {
        return radioButton;
    }
}

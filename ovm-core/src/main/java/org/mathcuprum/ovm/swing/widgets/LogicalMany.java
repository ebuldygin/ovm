package org.mathcuprum.ovm.swing.widgets;

import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.ui.AbstractWidget;
import org.mathcuprum.ovm.swing.SwingWidget;

import javax.swing.*;

/**
 * Created by ebuldygin on 16.08.2017.
 */
public class LogicalMany extends AbstractWidget<Boolean> implements SwingWidget {

    private final JCheckBox checkBox = new JCheckBox();

    public LogicalMany(SwingWidget parent, ParameterDescriptor descriptor) {
        super(parent, descriptor);
        checkBox.setText(descriptor.getTitle());
    }

    @Override
    protected Boolean getViewValue() {
        return checkBox.isSelected();
    }

    @Override
    protected void setViewValue(Boolean value) {
        checkBox.setSelected(Boolean.TRUE.equals(value));
    }

    @Override
    protected Class<Boolean> getViewType() {
        return Boolean.class;
    }

    @Override
    public JCheckBox getComponent() {
        return checkBox;
    }
}

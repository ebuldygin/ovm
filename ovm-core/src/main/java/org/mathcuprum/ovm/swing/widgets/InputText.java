package org.mathcuprum.ovm.swing.widgets;

import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.ui.AbstractWidget;
import org.mathcuprum.ovm.swing.SwingWidget;

import javax.swing.*;

/**
 * Created by ebuldygin on 13.08.2017.
 */
public class InputText extends AbstractWidget<String> implements SwingWidget {

    private final JLabel label = new JLabel();
    private final JTextField textField = new JTextField();

    public InputText(SwingWidget parent, ParameterDescriptor descriptor) {
        super(parent, descriptor);
        this.label.setText(descriptor.getTitle());
    }

    @Override
    protected String getViewValue() {
        return textField.getText();
    }

    @Override
    protected void setViewValue(String value) {
        textField.setText(value);
    }

    @Override
    protected Class<String> getViewType() {
        return String.class;
    }

    @Override
    public JTextField getComponent() {
        return textField;
    }

    public JLabel getLabel() {
        return label;
    }
}

package org.mathcuprum.ovm.swing.widgets;

import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.ui.AbstractWidget;
import org.mathcuprum.ovm.swing.RangedNumber;
import org.mathcuprum.ovm.swing.SwingWidget;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by ebuldygin on 23.09.2017.
 */
public class RangedNumberInput extends AbstractWidget<RangedNumber> implements SwingWidget {

    private final JLabel label = new JLabel();
    private final JSlider slider = new JSlider();
    private final JLabel valueLabel = new JLabel();
    private final JPanel sliderWithValue = new JPanel();
    private RangedNumber rangedNumber;

    public RangedNumberInput(SwingWidget parent, ParameterDescriptor descriptor) {
        super(parent, descriptor);
        label.setText(descriptor.getTitle());
        slider.setMinimum(0);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (rangedNumber != null) {
                    valueLabel.setText("" + (rangedNumber.getMin() + slider.getValue() * rangedNumber.getDelta()));
                } else {
                    valueLabel.setText("" + null);
                }
            }
        });
        GroupLayout layout = new GroupLayout(sliderWithValue);
        sliderWithValue.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(slider)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueLabel));
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(slider)
                .addComponent(valueLabel));
    }

    @Override
    protected RangedNumber getViewValue() {
        if (rangedNumber != null) {
            return new RangedNumber(
                    rangedNumber.getMin(),
                    rangedNumber.getMax(),
                    rangedNumber.getSteps(),
                    slider.getValue());
        } else {
            return null;
        }
    }

    @Override
    protected void setViewValue(RangedNumber value) {
        this.rangedNumber = value;
        if (value != null) {
            sliderWithValue.setVisible(true);
            slider.setMaximum(value.getSteps());
            slider.setValue(value.getStep());
        } else {
            sliderWithValue.setVisible(false);
        }
    }

    @Override
    protected Class<RangedNumber> getViewType() {
        return RangedNumber.class;
    }

    @Override
    public JSlider getComponent() {
        return slider;
    }

    public JLabel getLabel() {
        return label;
    }

    public JPanel getSliderWithValue() {
        return sliderWithValue;
    }

}

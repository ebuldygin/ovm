package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.Descriptor;
import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.ValueOfConverter;
import org.mathcuprum.ovm.core.Widget;
import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.Parameter;
import org.mathcuprum.ovm.core.ui.AbstractWidget;
import org.mathcuprum.ovm.demo.DemoPanel;
import org.mathcuprum.ovm.swing.SwingBuilder;
import org.mathcuprum.ovm.swing.SwingFactory;
import org.mathcuprum.ovm.swing.SwingForm;
import org.mathcuprum.ovm.swing.SwingWidget;

import javax.swing.*;

public class CustomWidgetPanel extends DemoPanel<CustomWidgetPanel.CustomWidgetForm> {

    public CustomWidgetPanel(JFrame parent) {
        super(parent, CustomWidgetForm.class);
    }

    @Form(title = "Расширенный виджет")
    public static class CustomWidgetForm {
        private BigText text;
        private boolean primitiveBool;
        private Boolean objectBool;

        @Parameter(title = "Большой текст (нестандартный виджет)", order = 10)
        public BigText getText() {
            return text;
        }

        @Parameter(title = "Логическое значение (boolean)",
                converter = ValueOfConverter.class, order = 20)
        public boolean isPrimitiveBool() {
            return primitiveBool;
        }

        @Parameter(title = "Логическое значение (Boolean)",
                converter = ValueOfConverter.class, order = 30)
        public Boolean getObjectBool() {
            return objectBool;
        }

        public void setText(BigText text) {
            this.text = text;
        }

        public void setPrimitiveBool(boolean primitiveBool) {
            this.primitiveBool = primitiveBool;
        }

        public void setObjectBool(Boolean objectBool) {
            this.objectBool = objectBool;
        }
    }

    public static class BigText {

        private final String text;

        public BigText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static BigText valueOf(String text) {
            if (text == null) {
                return null;
            }
            return new BigText(text);
        }

        @Override
        public String toString() {
            return text;
        }

    }

    @Override
    protected SwingForm<CustomWidgetForm> createSwingForm(Class<CustomWidgetForm> formClass) {
        return new SwingForm<>(formClass, new CustomSwingFactory());
    }

    @Override
    protected JComponent createSource(Class<?> modelClass) {
        return super.createSource(getClass());
    }

    static class CustomSwingFactory extends SwingFactory {

        @Override
        public SwingBuilder createBuilder(SwingWidget target, Descriptor descriptor) {
            return new SwingBuilder(target, descriptor) {
                @Override
                public Widget add(ParameterDescriptor descriptor) {
                    if (descriptor.getValueType() == BigText.class) {
                        return addTextAreaComponent(descriptor);
                    }
                    return super.add(descriptor);
                }

                private Widget addTextAreaComponent(ParameterDescriptor descriptor) {
                    TextAreaWidget textAreaWidget = new TextAreaWidget(target, descriptor);
                    addLabeledWidget(textAreaWidget.getLabel(), textAreaWidget.getComponent());
                    return textAreaWidget;
                }
            };
        }
    }

    static class TextAreaWidget extends AbstractWidget<String> implements SwingWidget {

        private final JTextArea textArea = new JTextArea(5, 40);
        private final JLabel label = new JLabel();

        TextAreaWidget(Widget parent, ParameterDescriptor descriptor) {
            super(parent, descriptor);
            label.setText(descriptor.getTitle());
        }

        @Override
        protected Class<String> getViewType() {
            return String.class;
        }

        @Override
        protected void setViewValue(String value) {
            textArea.setText(value);
        }

        @Override
        protected String getViewValue() {
            return textArea.getText();
        }

        @Override
        public JComponent getComponent() {
            return textArea;
        }

        public JLabel getLabel() {
            return label;
        }
    }


}

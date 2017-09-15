package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.Parameter;
import org.mathcuprum.ovm.demo.DemoPanel;
import org.mathcuprum.ovm.swing.SwingForm;
import org.mathcuprum.ovm.swing.SwingWidget;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SearchWidgetPanel extends DemoPanel<SearchWidgetPanel.InOutTextForm> {

    public SearchWidgetPanel(JFrame parent) {
        super(parent, InOutTextForm.class);
    }

    @Form(title = "Добавление действий")
    public static class InOutTextForm {

        private String inputText;

        @Parameter(title = "Введите текст", name = "input", order = 10)
        public String getInputText() {
            return inputText;
        }

        public void setInputText(String inputText) {
            this.inputText = inputText;
        }

        @Parameter(title = "Введённый текст", name = "out", order = 20)
        public String getOutputText() {
            return inputText;
        }

    }

    @Override
    protected SwingForm<InOutTextForm> createSwingForm(Class<InOutTextForm> formClass) {
        SwingForm<InOutTextForm> res = super.createSwingForm(formClass);
        final SwingWidget input = (SwingWidget) res.searchById("input");
        final SwingWidget out = (SwingWidget) res.searchById("out");
        out.getComponent().setEnabled(false);
        input.getComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                out.set(input.get());
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        return res;
    }

    @Override
    protected JComponent createSource(Class<?> modelClass) {
        return super.createSource(getClass());
    }
}

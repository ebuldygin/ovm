package org.mathcuprum.ovm.demo;

import org.mathcuprum.ovm.core.ConvertException;
import org.mathcuprum.ovm.core.ValidationException;
import org.mathcuprum.ovm.swing.SwingForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by ebuldygin on 08.09.2017.
 */
public class DemoPanel<T> extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoPanel.class);

    private final String title;
    private final Class<T> formClass;
    private final JFrame parent;
    private final SwingForm<T> form;
    private final JButton button = new JButton("Считать параметры");

    public DemoPanel(JFrame parent, Class<T> formClass) {
        this.parent = parent;
        this.formClass = formClass;
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.form = createSwingForm(formClass);
        this.title = form.getTitle();
        JComponent demo = form.getComponent();
        JComponent source = createSource(formClass);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(demo)
                        .addComponent(button)
                        .addComponent(source))
                .addContainerGap());
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(demo)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(source)
                .addContainerGap());

        this.add(form.getComponent());
        this.add(createSource(formClass));
        button.addActionListener(applyListener());
    }

    protected SwingForm<T> createSwingForm(Class<T> formClass) {
        return new SwingForm<T>(formClass);
    }

    protected JComponent createSource(Class<?> modelClass) {
        JTextArea sourceTextArea = new JTextArea(25, 81);
        sourceTextArea.setEditable(false);
        sourceTextArea.setFont(Font.decode(Font.MONOSPACED));
        StringBuilder sourceCode = new StringBuilder();
        try (InputStream resource = modelClass.getResourceAsStream(modelClass.getSimpleName() + ".java");
             BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCode.append(line);
                sourceCode.append("\n");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            sourceCode.append("Исходники не найдены: ").append(modelClass);
        }
        sourceTextArea.setText(sourceCode.toString());
        sourceTextArea.setCaretPosition(0);
        return new JScrollPane(sourceTextArea);
    }

    public String getTitle() {
        return title;
    }

    private ActionListener applyListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    T model = form.get();
                    SwingForm<T> swingForm = createSwingForm(formClass);
                    swingForm.set(model);
                    JOptionPane.showMessageDialog(parent,
                            swingForm.getComponent(),
                            "Введёные значения",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (ConvertException e) {
                    LOGGER.error(e.getMessage(), e);
                    JOptionPane.showMessageDialog(
                            DemoPanel.this.getParent(),
                            String.format("Ошибка чтения поля '%s'", e.getDescriptor().getTitle()),
                            "Ошибка ввода",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ValidationException e) {
                    LOGGER.error(e.getMessage(), e);
                    JOptionPane.showMessageDialog(DemoPanel.this.getParent(), e.getMessage());
                }
            }
        };
    }
}

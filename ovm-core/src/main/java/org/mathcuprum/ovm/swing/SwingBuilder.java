package org.mathcuprum.ovm.swing;

import org.mathcuprum.ovm.swing.widgets.RangedNumberInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mathcuprum.ovm.core.Descriptor;
import org.mathcuprum.ovm.core.ParameterDescriptor;
import org.mathcuprum.ovm.core.Widget;
import org.mathcuprum.ovm.core.meta.ClassDescriptor;
import org.mathcuprum.ovm.swing.widgets.InputText;
import org.mathcuprum.ovm.swing.widgets.LogicalMany;
import org.mathcuprum.ovm.swing.widgets.LogicalSingle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ebuldygin on 16.08.2017.
 */
public class SwingBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingBuilder.class);

    protected final SwingWidget target;
    private final JPanel mainPanel = new JPanel();
    private Map<ParameterDescriptor, ButtonGroup> groups = new HashMap<>();
    private Map<ParameterDescriptor, Integer> logicalGroups = new HashMap<>();
    private final GroupLayout layout;
    private final GroupLayout.ParallelGroup labels;
    private final GroupLayout.ParallelGroup labeledFields;
    private final GroupLayout.ParallelGroup notLabeledFields;
    private final GroupLayout.SequentialGroup vertical;

    private JPanel currentLogicalPanel;
    private ParameterDescriptor lastDescriptor;

    public SwingBuilder(SwingWidget target, Descriptor descriptor) {
        this.target = target;
        if (descriptor instanceof ClassDescriptor) {
            readLogicalGroups((ClassDescriptor<?>) descriptor);
        } else if (!descriptor.getChildren().isEmpty()) {
            readLogicalGroups(descriptor.getChildren().get(0).getDeclaredClass());
        }
        this.layout = new GroupLayout(mainPanel);
        if (LOGGER.isTraceEnabled()) {
            mainPanel.setBorder(new LineBorder(Color.RED));
        }
        this.mainPanel.setLayout(layout);
        this.labels = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        this.labeledFields = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        this.notLabeledFields = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        this.vertical = layout.createSequentialGroup();
        JLabel caption = new JLabel(descriptor.getTitle());
        float captionFontSize = caption.getFont().getSize2D();
        caption.setFont(caption.getFont().deriveFont(captionFontSize + 4f));
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(labels)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(labeledFields))
                        .addGroup(notLabeledFields)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caption)))
                .addContainerGap());
        layout.setVerticalGroup(vertical);
        vertical.addComponent(caption)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    }

    private void readLogicalGroups(ClassDescriptor<?> descriptor) {
        int index = 0;
        for (List<ParameterDescriptor> group : descriptor.getSingleSelection()) {
            ButtonGroup bg = new ButtonGroup();
            index++;
            for (ParameterDescriptor p : group) {
                groups.put(p, bg);
                logicalGroups.put(p, index);
            }
        }
        for (List<ParameterDescriptor> group : descriptor.getManySelection()) {
            index++;
            for (ParameterDescriptor p : group) {
                logicalGroups.put(p, index);
            }
        }
    }

    public SwingWidget add(SwingForm<?> nested) {
        addNotLabeledComponent(nested.getComponent());
        return nested;
    }

    public Widget add(ParameterDescriptor descriptor) {
        SwingWidget result;
        if (isLogical(descriptor) && descriptor.converterFor(Boolean.class) != null) {
            result = addLogicalComponent(descriptor);
        } else if (isRangedNumber(descriptor)) {
            result = addRangedNumberComponent(descriptor);
        } else {
            result = addTextComponent(descriptor);
        }
        lastDescriptor = descriptor;
        return result;
    }

    public final JPanel build() {
        vertical.addContainerGap();
        return mainPanel;
    }

    private boolean isLogical(ParameterDescriptor descriptor) {
        return Boolean.class.equals(descriptor.getValueType())
                || boolean.class.equals(descriptor.getValueType());
    }

    private boolean isRangedNumber(ParameterDescriptor descriptor) {
        return RangedNumber.class.equals(descriptor.getValueType());
    }

    private SwingWidget addTextComponent(ParameterDescriptor descriptor) {
        InputText textField = new InputText(target, descriptor);
        addLabeledWidget(textField.getLabel(), textField.getComponent());
        return textField;
    }


    private SwingWidget addRangedNumberComponent(ParameterDescriptor descriptor) {
        RangedNumberInput rangedInput = new RangedNumberInput(target, descriptor);
        addLabeledWidget(rangedInput.getLabel(), rangedInput.getSliderWithValue());
        return rangedInput;
    }

    private SwingWidget addLogicalComponent(ParameterDescriptor descriptor) {
        SwingWidget b;
        if (groups.containsKey(descriptor)) {
            LogicalSingle w = new LogicalSingle(target, descriptor);
            groups.get(descriptor).add(w.getComponent());
            b = w;
        } else {
            b = new LogicalMany(target, descriptor);
        }
        appendLogicalToPanel(descriptor, b);
        return b;
    }

    private void appendLogicalToPanel(ParameterDescriptor descriptor, SwingWidget b) {
        if (lastDescriptor != null) {
            Integer previousGroup = logicalGroups.get(lastDescriptor);
            Integer currentGroup = logicalGroups.get(descriptor);
            if (!Objects.equals(previousGroup, currentGroup)) {
                currentLogicalPanel = null;
            }
        }
        if (currentLogicalPanel == null) {
            currentLogicalPanel = new JPanel();
            if (LOGGER.isTraceEnabled()) {
                currentLogicalPanel.setBorder(new LineBorder(Color.CYAN));
            }
            currentLogicalPanel.setLayout(new GridLayout(0, 2));
            addNotLabeledComponent(currentLogicalPanel);
        }
        currentLogicalPanel.add(b.getComponent());
    }

    protected final void addLabeledWidget(JComponent label, JComponent input) {
        labels.addComponent(label, GroupLayout.Alignment.TRAILING);
        labeledFields.addComponent(input);
        addVerticalGap();
        vertical.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(label)
                .addComponent(input));
    }

    protected final void addNotLabeledComponent(JComponent b) {
        notLabeledFields.addComponent(b);
        addVerticalGap();
        vertical.addComponent(b);
    }

    private final void addVerticalGap() {
        if (mainPanel.getComponentCount() != 0) {
            vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        }
    }

}

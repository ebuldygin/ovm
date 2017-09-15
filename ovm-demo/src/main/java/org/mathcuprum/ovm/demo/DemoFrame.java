package org.mathcuprum.ovm.demo;

import org.mathcuprum.ovm.demo.samples.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ebuldygin on 08.09.2017.
 */
public class DemoFrame extends JFrame {

    DemoFrame(String s) {
        super(s);
    }

    void init() {
        JTabbedPane tabbedPane = new JTabbedPane();
        List<DemoPanel<?>> demos = new LinkedList<>();
        demos.add(new DemoPanel<>(this, SimpleForm.class));
        //demos.add(new DemoPanel<>(this, DemoSimpleForm.class));
        demos.add(new DemoPanel<>(this, PlainInheritedForm.class));
        demos.add(new DemoPanel<>(this, OverrideParameterForm.class));
        demos.add(new DemoPanel<>(this, CompositionForm.class));
        demos.add(new CustomWidgetPanel(this));
        demos.add(new SearchWidgetPanel(this));
        for (DemoPanel<?> demo : demos) {
            tabbedPane.addTab(demo.getTitle(), demo);

        }
        getContentPane().add(tabbedPane);
    }

    public static void main(String[] args) throws InterruptedException,
            InvocationTargetException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DemoFrame mw = new DemoFrame("Демонстрация компоновки");
                mw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mw.init();
                mw.pack();
                mw.setVisible(true);
                mw.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        System.exit(0);
                    }
                });
            }
        });
    }
}

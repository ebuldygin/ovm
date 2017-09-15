package org.mathcuprum.ovm.swing;

import org.mathcuprum.ovm.core.Descriptor;
import org.mathcuprum.ovm.core.ParameterDescriptor;

/**
 * Created by ebuldygin on 16.08.2017.
 */
public class SwingFactory {

    public static final SwingFactory SIMPLE_FACTORY = new SwingFactory();

    public SwingBuilder createBuilder(SwingWidget target, Descriptor descriptor) {
        return new SwingBuilder(target, descriptor);
    }

}

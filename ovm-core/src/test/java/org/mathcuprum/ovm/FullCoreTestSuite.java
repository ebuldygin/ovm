package org.mathcuprum.ovm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mathcuprum.ovm.core.*;
import org.mathcuprum.ovm.swing.RangedNumberTest;
import org.mathcuprum.ovm.swing.SwingFormTest;

/**
 * Created by ebuldygin on 27.08.2017.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClassDescriptorTest.class,
        ParameterDescriptorTest.class,
        InheritenceFeatureTest.class,
        PrimitiveParameterTest.class,
        ConvertersTest.class,
        SwingFormTest.class,
        RangedNumberTest.class
})
public class FullCoreTestSuite {
}

package org.mathcuprum.ovm.swing;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.Parameter;
import org.mathcuprum.ovm.swing.widgets.RangedNumberInput;

/**
 * Created by ebuldygin on 23.09.2017.
 */
public class RangedNumberTest {

    @Form
    private static class A {
        @Parameter(name = "ranged")
        RangedNumber rangedNumber;
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangedNumberIncorrectBounds() {
        new RangedNumber(1.0, 0.0, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangedNumberIncorrectStepsNumber() {
        new RangedNumber(0.0, 1.0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rangedNumberIncorrectStep() {
        new RangedNumber(0.0, 1.0, 10, -1);
    }

    @Test
    public void readWriteValue() {
        SwingForm<A> form = new SwingForm<>(A.class);
        RangedNumberInput input = (RangedNumberInput) form.searchById("ranged");
        RangedNumber number = new RangedNumber(-2.0, 1.0, 98, 4);
        input.set(number);
        Assert.assertEquals(number, input.get());
        RangedNumber expected = new RangedNumber(number.getMin(), number.getMax(), number.getSteps(), 32);
        input.getComponent().setValue(expected.getStep());
        Assert.assertEquals(expected, input.get());
    }
}

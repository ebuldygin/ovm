package org.mathcuprum.ovm.swing;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.ValidationException;
import org.mathcuprum.ovm.core.Validator;
import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.LogicalGroup;
import org.mathcuprum.ovm.core.meta.Parameter;
import org.mathcuprum.ovm.core.meta.SelectionType;

import javax.swing.*;

/**
 * Created by ebuldygin on 03.09.2017.
 */
public class SwingFormTest {

    @Form
    @LogicalGroup(type = SelectionType.MANY)
    public static class A {
        @Parameter(name = "int")
        Integer integer;
        @Parameter(name = "boolean")
        Boolean bool;
    }

    @Form
    @LogicalGroup(type = SelectionType.SINGLE)
    public static class B extends A {
        @Parameter
        A a;
        @Parameter(name = "extraBool")
        Boolean extraBool;
    }

    @Test
    public void buildAndFindTest() {
        SwingForm<B> b = new SwingForm<>(B.class);
        SwingWidget bInt = (SwingWidget) b.searchById("int");
        SwingWidget bBool = (SwingWidget) b.searchById("boolean");
        SwingWidget bbBool = (SwingWidget) b.searchById("extraBool");
        SwingWidget baInt = (SwingWidget) b.searchById("a.int");
        SwingWidget baBool = (SwingWidget) b.searchById("a.boolean");
        Assert.assertNotNull(bInt);
        Assert.assertNotNull(bBool);
        Assert.assertNotNull(bbBool);
        Assert.assertNotNull(baInt);
        Assert.assertNotNull(baBool);
        ((JTextField) bInt.getComponent()).setText("1");
        ((JToggleButton) bBool.getComponent()).setSelected(true);
        ((JToggleButton) bbBool.getComponent()).setSelected(false);
        ((JTextField) baInt.getComponent()).setText("-1");
        ((JToggleButton) baBool.getComponent()).setSelected(false);
        B res = b.get();
        Assert.assertEquals(Integer.valueOf(1), res.integer);
        Assert.assertEquals(true, res.bool);
        Assert.assertEquals(false, res.extraBool);
        Assert.assertEquals(Integer.valueOf(-1), res.a.integer);
        Assert.assertEquals(false, res.a.bool);
        res.integer = -2;
        res.bool = false;
        res.extraBool = true;
        res.a.integer = 2;
        res.a.bool = true;
        b.set(res);
        Assert.assertEquals(-2, bInt.get());
        Assert.assertEquals(false, bBool.get());
        Assert.assertEquals(true, bbBool.get());
        Assert.assertEquals(2, baInt.get());
        Assert.assertEquals(true, baBool.get());
    }

    @Test
    public void readIntoInheritedClass() {
        SwingForm<A> a = new SwingForm<>(A.class);
        SwingWidget aInt = (SwingWidget) a.searchById("int");
        SwingWidget aBool = (SwingWidget) a.searchById("boolean");
        Assert.assertNotNull(aInt);
        Assert.assertNotNull(aBool);
        ((JTextField) aInt.getComponent()).setText("1");
        ((JToggleButton) aBool.getComponent()).setSelected(true);
        B extendedModel = a.getAs(B.class);
        Assert.assertEquals(Integer.valueOf(1), extendedModel.integer);
        Assert.assertEquals(true, extendedModel.bool);
        extendedModel.integer = -2;
        extendedModel.bool = false;
        a.set(extendedModel);
        Assert.assertEquals(-2, aInt.get());
        Assert.assertEquals(false, aBool.get());
    }

    @Test
    public void readIntoInheritedInstance() {
        SwingForm<A> a = new SwingForm<>(A.class);
        SwingWidget aInt = (SwingWidget) a.searchById("int");
        SwingWidget aBool = (SwingWidget) a.searchById("boolean");
        Assert.assertNotNull(aInt);
        Assert.assertNotNull(aBool);
        ((JTextField) aInt.getComponent()).setText("1");
        ((JToggleButton) aBool.getComponent()).setSelected(true);
        B extendedModel = new B();
        a.writeToModel(extendedModel);
        Assert.assertEquals(Integer.valueOf(1), extendedModel.integer);
        Assert.assertEquals(true, extendedModel.bool);
        extendedModel.integer = -2;
        extendedModel.bool = false;
        a.set(extendedModel);
        Assert.assertEquals(-2, aInt.get());
        Assert.assertEquals(false, aBool.get());
    }


    @Test
    public void validationSuccess() {
        SwingForm<A> a = createValidatedForm();
        SwingWidget aBool = (SwingWidget) a.searchById("boolean");
        aBool.set(false);
        A model = a.get();
        Assert.assertEquals(false, model.bool);
    }

    @Test(expected = ValidationException.class)
    public void validationFailed() {
        SwingForm<A> a = createValidatedForm();
        SwingWidget aBool = (SwingWidget) a.searchById("boolean");
        aBool.set(true);
        A model = a.get();
    }

    private SwingForm<A> createValidatedForm() {
        SwingForm<A> a = new SwingForm<>(A.class);
        a.setValidators(new Validator() {
            @Override
            public void validate(Object value) throws ValidationException {
                A val = (A) value;
                if (val.bool) {
                    throw new ValidationException();
                }
            }
        });
        return a;
    }
}

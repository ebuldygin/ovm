package org.mathcuprum.ovm.core;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.meta.*;

import static org.mathcuprum.ovm.core.Utils.*;

/**
 * Created by ebuldygin on 26.08.2017.
 */
public class PrimitiveParameterTest {

    private static String BOOL = "b";
    private static String BYTE = "bt";
    private static String CHAR = "c";
    private static String SHORT = "s";
    private static String INT = "i";
    private static String LONG = "l";
    private static String FLOAT = "f";
    private static String DOUBLE = "d";

    @Form
    private static class Af {
        @Parameter
        boolean b;
        @Parameter
        byte bt;
        @Parameter
        char c;
        @Parameter
        short s;
        @Parameter
        int i;
        @Parameter
        long l;
        @Parameter
        float f;
        @Parameter
        double d;
    }

    @Test
    public void readWriteFields() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        Af model = new Af();

        setProperty(a, BOOL, model, true);
        Assert.assertEquals(true, model.b);
        setProperty(a, BOOL, model, null);
        Assert.assertEquals(false, model.b);

        setProperty(a, BYTE, model, (byte) 69);
        Assert.assertEquals((byte) 69, model.bt);
        setProperty(a, BYTE, model, null);
        Assert.assertEquals((byte) 0, model.bt);

        setProperty(a, CHAR, model, '0');
        Assert.assertEquals('0', model.c);
        setProperty(a, CHAR, model, null);
        Assert.assertEquals((char) 0, model.c);

        setProperty(a, SHORT, model, (short) 1023);
        Assert.assertEquals((short) 1023, model.s);
        setProperty(a, SHORT, model, null);
        Assert.assertEquals((short) 0, model.s);

        setProperty(a, INT, model, 22);
        Assert.assertEquals(22, model.i);
        setProperty(a, INT, model, null);
        Assert.assertEquals(0, model.i);

        setProperty(a, LONG, model, 98L);
        Assert.assertEquals(98, model.l);
        setProperty(a, LONG, model, null);
        Assert.assertEquals(0, model.l);

        setProperty(a, FLOAT, model, 101f);
        Assert.assertTrue(101.0f == model.f);
        setProperty(a, FLOAT, model, null);
        Assert.assertTrue(0.0f == model.f);

        setProperty(a, DOUBLE, model, 104.0);
        Assert.assertTrue(104.0 == model.d);
        setProperty(a, DOUBLE, model, null);
        Assert.assertTrue(0.0 == model.d);
    }

    @Test
    public void getDefaultIdentityConverter() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        Converter<Boolean> converter = a.getChildren().get(0).converterFor(Boolean.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(Boolean.TRUE, converter.convertToView(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, converter.convertToObject(Boolean.TRUE));
    }

    @Test
    public void getDefaultValueOfConverter() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        Converter<String> converter = a.getChildren().get(0).converterFor(String.class);
        Assert.assertEquals(String.valueOf(Boolean.TRUE), converter.convertToView(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, converter.convertToObject(String.valueOf(Boolean.TRUE)));
    }
}

package org.mathcuprum.ovm.core;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.meta.ClassDescriptor;
import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.Parameter;

/**
 * Created by ebuldygin on 27.08.2017.
 */
public class ConvertersTest {

    @Form
    private static class A {
        @Parameter(order = 1)
        Boolean b;
        @Parameter(order = 2, converter = ValueOfConverter.class)
        Boolean c;
    }

    @Form
    private static class B {
        @Parameter(converter = {
                CustomConverter.class,
                ValueOfConverter.class
        })
        Integer b;
    }

    private static class CustomConverter implements Converter<Object> {

        static Object VIEW_VALUE = "view";
        static Integer MODEL_VALUE = -1;

        @Override
        public Object convertToView(Object value) {
            return VIEW_VALUE;
        }

        @Override
        public Object convertToObject(Object value) {
            return MODEL_VALUE;
        }

        @Override
        public Class<Object> getViewType() {
            return Object.class;
        }
    }

    @Test
    public void getDefaultIdentityConverter() {
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Converter<Boolean> converter = a.getChildren().get(0).converterFor(Boolean.class);
        Assert.assertEquals(Boolean.TRUE, converter.convertToView(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, converter.convertToObject(Boolean.TRUE));
    }

    @Test
    public void getDefaultValueOfConverter() {
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Converter<String> converter = a.getChildren().get(0).converterFor(String.class);
        Assert.assertEquals(String.valueOf(Boolean.TRUE), converter.convertToView(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, converter.convertToObject(String.valueOf(Boolean.TRUE)));
    }

    @Test
    public void notExistedConverter() {
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Converter<?> converter = a.getChildren().get(0).converterFor(Object.class);
        Assert.assertNull(converter);
    }

    @Test
    public void customConverter() {
        ClassDescriptor<B> b = ClassDescriptor.from(B.class);
        Converter<Object> converter = b.getChildren().get(0).converterFor(Object.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(CustomConverter.VIEW_VALUE, converter.convertToView(100));
        Assert.assertEquals(CustomConverter.MODEL_VALUE, converter.convertToObject(new Object()));
    }

    @Test(expected = ConvertException.class)
    public void converterError() {
        ClassDescriptor<B> b = ClassDescriptor.from(B.class);
        Converter<String> converter = b.getChildren().get(0).converterFor(String.class);
        Assert.assertNotNull(converter);
        converter.convertToObject("Not a number");
    }

    @Test
    public void noDefaultConverter() {
        @Form
        class C {
            @Parameter
            Object b;
        }
        ClassDescriptor<C> c = ClassDescriptor.from(C.class);
        Converter<String> converter = c.getChildren().get(0).converterFor(String.class);
        Assert.assertNull(converter);
    }


    @Test
    public void customConverterDisablesDefault() {
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Converter<String> stringConverter = a.getChildren().get(1).converterFor(String.class);
        Assert.assertNotNull(stringConverter);
        Converter<Boolean> booleanConverter = a.getChildren().get(1).converterFor(Boolean.class);
        Assert.assertNull(booleanConverter);
    }

    @Test
    public void valueOfConverterForBoolean() {
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Converter<String> stringConverter = a.getChildren().get(1).converterFor(String.class);
        Assert.assertNotNull(stringConverter);
        Assert.assertTrue((Boolean) stringConverter.convertToObject(Boolean.TRUE.toString()));
        Assert.assertFalse((Boolean) stringConverter.convertToObject(Boolean.FALSE.toString()));
    }
}

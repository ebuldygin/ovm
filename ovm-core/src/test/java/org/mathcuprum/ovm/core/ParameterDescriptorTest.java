package org.mathcuprum.ovm.core;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.meta.*;

import java.util.*;

import static org.mathcuprum.ovm.core.Utils.*;

/**
 * Created by ebuldygin on 26.08.2017.
 */
public class ParameterDescriptorTest {

    private static final String FORM_NAME = "FORM";
    private static final String PARAM_NAME = "PARAM";

    @Form(title = FORM_NAME)
    private static class Af {
        @Parameter(title = PARAM_NAME, order = 10)
        Integer b;
    }

    @Test
    public void readWriteFields() {
        ClassDescriptor<BaseFieldsParameter> base = ClassDescriptor.from(BaseFieldsParameter.class);
        List<String> parameters = getParameterNames(base.getChildren());
        Assert.assertEquals(BaseFieldsParameter.ALL_FIELDS, parameters);

        BaseFieldsParameter model = new BaseFieldsParameter();
        setProperty(base, BaseFieldsParameter.F_MANY, model, Boolean.TRUE);
        setProperty(base, BaseFieldsParameter.F_SINGLE1, model, Boolean.TRUE);
        setProperty(base, BaseFieldsParameter.F_SINGLE2, model, Boolean.TRUE);
        setProperty(base, BaseFieldsParameter.F_TEXT, model, BaseFieldsParameter.F_TEXT);
        setProperty(base, BaseFieldsParameter.F_INT, model, 1);
        Assert.assertEquals(Boolean.TRUE, getProperty(base, BaseFieldsParameter.F_MANY, model));
        Assert.assertEquals(Boolean.TRUE, getProperty(base, BaseFieldsParameter.F_SINGLE1, model));
        Assert.assertEquals(Boolean.TRUE, getProperty(base, BaseFieldsParameter.F_SINGLE2, model));
        Assert.assertEquals(BaseFieldsParameter.F_TEXT, getProperty(base, BaseFieldsParameter.F_TEXT, model));
        Assert.assertEquals(1, getProperty(base, BaseFieldsParameter.F_INT, model));
    }

    @Test
    public void readWriteFieldsMethods() throws InstantiationException, IllegalAccessException {
        testReadWriteMetods(BaseFieldsParameter.class);
    }

    @Test
    public void readWriteMethodMethods() throws InstantiationException, IllegalAccessException {
        testReadWriteMetods(BaseMethodsParameter.class);
    }

    private <T> void testReadWriteMetods(Class<T> baseClass) throws IllegalAccessException, InstantiationException {
        ClassDescriptor<T> base = ClassDescriptor.from(baseClass);
        List<String> parameters = getParameterNames(base.getChildren());
        Assert.assertEquals(BaseFieldsParameter.ALL_FIELDS, parameters);

        T model = baseClass.newInstance();
        setProperty(base, BaseFieldsParameter.F_MANY, model, Boolean.TRUE);
        setProperty(base, BaseFieldsParameter.F_SINGLE1, model, Boolean.TRUE);
        setProperty(base, BaseFieldsParameter.F_SINGLE2, model, Boolean.TRUE);
        setProperty(base, BaseFieldsParameter.F_TEXT, model, BaseFieldsParameter.F_TEXT);
        setProperty(base, BaseFieldsParameter.F_INT, model, 1);
        Assert.assertEquals(Boolean.TRUE, getProperty(base, BaseFieldsParameter.F_MANY, model));
        Assert.assertEquals(Boolean.TRUE, getProperty(base, BaseFieldsParameter.F_SINGLE1, model));
        Assert.assertEquals(Boolean.TRUE, getProperty(base, BaseFieldsParameter.F_SINGLE2, model));
        Assert.assertEquals(BaseFieldsParameter.F_TEXT, getProperty(base, BaseFieldsParameter.F_TEXT, model));
        Assert.assertEquals(1, getProperty(base, BaseFieldsParameter.F_INT, model));
    }

    @Test
    public void parameterName() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        Assert.assertEquals(FORM_NAME, a.getTitle());
        Assert.assertEquals(PARAM_NAME, a.getChildren().get(0).getTitle());
    }

    @Test
    public void getValueFromNullInstance() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        Object res = a.getChildren().get(0).getInstanceValue(null);
        Assert.assertNull(res);
    }

    @Test(expected = ReflectionException.class)
    public void incorrectSetFieldValue() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        a.getChildren().get(0).setInstanceValue(new Af(), new Object());
    }

    @Test(expected = ReflectionException.class)
    public void incorrectGetFieldValue() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        a.getChildren().get(0).getInstanceValue(new Object());
    }

    @Test
    public void equalParameterDescriptors() {
        ClassDescriptor<Af> a = ClassDescriptor.from(Af.class);
        ClassDescriptor<Af> a2 = ClassDescriptor.from(Af.class);
        Assert.assertEquals(a, a2);
        Assert.assertEquals(a.getChildren(), a2.getChildren());
    }

    @Test
    public void namedParameters() {
        @Form
        class A {
            @Parameter(name = "c")
            Boolean a;
            @Parameter
            Boolean b;
        }
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Set<String> parameters = new HashSet<>(a.getChildren().size());
        for (ParameterDescriptor p : a.getChildren()) {
            parameters.add(p.getPropertyName());
        }
        Set<String> expected = new HashSet<>(Arrays.asList("b", "c"));
        Assert.assertEquals(expected, parameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateParameterDeclaration() {
        @Form
        class A {
            @Parameter
            Boolean b;

            @Parameter
            public Boolean getB() {
                return b;
            }

            public void setB(Boolean b) {
                this.b = b;
            }
        }
        ClassDescriptor.from(A.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateNameParameter() {
        @Form
        class A {
            @Parameter(name = "b")
            Boolean a;
            @Parameter
            Boolean b;
        }
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
    }

}

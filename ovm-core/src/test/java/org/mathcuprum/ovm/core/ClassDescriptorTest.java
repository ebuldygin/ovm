package org.mathcuprum.ovm.core;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.meta.*;

import java.util.List;

import static org.mathcuprum.ovm.core.Utils.*;

/**
 * Created by ebuldygin on 26.08.2017.
 */
public class ClassDescriptorTest {

    @Test
    public void buildByFieldsClassDescriptor() {
        testClassDescriptor(BaseFieldsParameter.class);
    }

    @Test
    public void buildByGetterClassDescriptor() {
        testClassDescriptor(BaseMethodsParameter.class);
    }

    private <T> void testClassDescriptor(Class<T> baseClass) {
        ClassDescriptor<T> base = ClassDescriptor.from(baseClass);
        Assert.assertEquals("Incorrect class name!", baseClass, base.getValueType());
        Assert.assertEquals("Incorrect number of fields!", 5, base.getChildren().size());
        Assert.assertEquals("Incorrect number of 'single' logical group!",
                1, base.getSingleSelection().size());
        Assert.assertEquals("Incorrect number of 'many' logical group!",
                1, base.getManySelection().size());
        List<ParameterDescriptor> single = base.getSingleSelection().iterator().next();
        List<ParameterDescriptor> many = base.getManySelection().iterator().next();
        Assert.assertEquals("Incorrect number of arguments in 'single' logical group!",
                2, single.size());
        Assert.assertEquals("Incorrect number of arguments in 'many' logical group!",
                1, many.size());
        List<String> readSingleFields = getParameterNames(single);
        List<String> readManyFields = getParameterNames(many);
        Assert.assertEquals("Incorrect field in 'many' group!", BaseFieldsParameter.MANY, readManyFields);
        Assert.assertEquals("Incorrect field in 'single' group!", BaseFieldsParameter.SINGLE, readSingleFields);
        ClassDescriptor<T> base2 = ClassDescriptor.from(baseClass);
        Assert.assertEquals("Class descriptors for the same class must be equals!", base, base2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notFormObject() {
        ClassDescriptor.from(Object.class);
    }

    @Test
    public void formName() {
        @Form(title = "form")
        class A {
        }
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
        Assert.assertEquals("form", a.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notMappedFieldInLogicalGroups() {
        @Form
        @LogicalGroup(fields = "b")
        class A {
        }
        ClassDescriptor.from(A.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void manyDefaultLogicalGroups() {
        @Form
        @LogicalGroups({@LogicalGroup, @LogicalGroup})
        class A {
        }
        ClassDescriptor.from(A.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void multipleLogicalFieldBinding() {
        @Form
        @LogicalGroups({
                @LogicalGroup(type = SelectionType.MANY, fields = "b"),
                @LogicalGroup(type = SelectionType.SINGLE, fields = {"b2", "b"}),})
        class A {
            @Parameter
            Boolean b;
            @Parameter
            Boolean b2;
        }
        ClassDescriptor.from(A.class);
    }

}

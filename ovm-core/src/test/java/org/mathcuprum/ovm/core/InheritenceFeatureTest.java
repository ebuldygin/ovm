package org.mathcuprum.ovm.core;

import org.junit.Assert;
import org.junit.Test;
import org.mathcuprum.ovm.core.meta.*;

import java.util.*;

import static org.mathcuprum.ovm.core.Utils.*;

/**
 * Created by ebuldygin on 26.08.2017.
 */
public class InheritenceFeatureTest {

    private static final String FORM_NAME = "FORM";
    private static final String PARAM_NAME = "PARAM";

    @Form(title = FORM_NAME)
    private static class Af {
        @Parameter(title = PARAM_NAME, order = 10)
        Integer b;
    }

    @Form
    private static class Am {
        Long c;

        @Parameter
        public Long getC() {
            return c;
        }

        public void setC(Long c) {
            this.c = c;
        }
    }

    @Test
    public void inheritanceFields() {
        @Form
        class B extends Af {
            @Parameter(name = "a")
            Boolean b;
        }
        ClassDescriptor<B> b = ClassDescriptor.from(B.class);
        ParameterDescriptor Ab = b.getChildren().get(0);
        ParameterDescriptor Bb = b.getChildren().get(1);
        Assert.assertEquals(Af.class, Ab.getDeclaredClass().getValueType());
        Assert.assertEquals(B.class, Bb.getDeclaredClass().getValueType());
        B model = new B();
        Ab.setInstanceValue(model, 1);
        Bb.setInstanceValue(model, Boolean.TRUE);
        Assert.assertEquals(Integer.valueOf(1), ((Af) model).b);
        Assert.assertEquals(Boolean.TRUE, model.b);
    }


    @Test(expected = ReflectionException.class)
    public void incorrectSetMethodValue() {
        ClassDescriptor<Am> a = ClassDescriptor.from(Am.class);
        a.getChildren().get(0).setInstanceValue(new Am(), new Object());
    }

    @Test(expected = ReflectionException.class)
    public void incorrectGetMethodValue() {
        ClassDescriptor<Am> a = ClassDescriptor.from(Am.class);
        a.getChildren().get(0).getInstanceValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateNameParameterWithInheritance() {
        @Form
        class A extends Af {
            @Parameter(name = "b")
            Boolean a;
        }
        ClassDescriptor<A> a = ClassDescriptor.from(A.class);
    }

    @Test
    public void excludeParameterTest() {
        @Form
        class A {
            @Parameter(name = "c")
            Boolean a;
            @Parameter
            Boolean b;
            @Parameter(name = "d")
            Boolean c;
        }
        @Form
        @ExcludeInherited({"c", "b"})
        class B extends A {
        }
        ClassDescriptor<B> b = ClassDescriptor.from(B.class);
        Set<String> properties = new HashSet<>(b.getChildren().size());
        for (ParameterDescriptor p : b.getChildren()) {
            properties.add(p.getPropertyName());
        }
        Set<String> expected = Collections.singleton("d");
        Assert.assertEquals(expected, properties);
    }

    @Test
    public void excludeInheritedParameterTest() {
        @Form
        class A {
            @Parameter(name = "c")
            Boolean a;
            @Parameter
            Boolean b;
        }
        @Form
        @ExcludeInherited({"c"})
        class B extends A {
            @Parameter(name = "d")
            Boolean c;
        }
        @Form
        @ExcludeInherited({"d", "b"})
        class C extends B {
            @Parameter(name = "c")
            Boolean e;
        }
        ClassDescriptor<C> c = ClassDescriptor.from(C.class);
        Set<String> properties = new HashSet<>(c.getChildren().size());
        for (ParameterDescriptor p : c.getChildren()) {
            properties.add(p.getPropertyName());
        }
        Set<String> expected = Collections.singleton("c");
        Assert.assertEquals(expected, properties);
    }

    @Test
    public void inheritLogicalGroup() {
        @Form
        @LogicalGroup(type = SelectionType.MANY)
        class A {
            @Parameter
            Boolean b;
        }
        @Form
        @LogicalGroup(type = SelectionType.SINGLE)
        class B extends A {
            @Parameter(name = "a")
            Boolean b;
        }
        ClassDescriptor<B> b = ClassDescriptor.from(B.class);
        Assert.assertEquals("Incorrect number of 'single' logical group!",
                1, b.getSingleSelection().size());
        Assert.assertEquals("Incorrect number of 'many' logical group!",
                1, b.getManySelection().size());
        List<ParameterDescriptor> single = b.getSingleSelection().iterator().next();
        List<ParameterDescriptor> many = b.getManySelection().iterator().next();
        Assert.assertEquals("Incorrect number of arguments in 'single' logical group!",
                1, single.size());
        Assert.assertEquals("Incorrect number of arguments in 'many' logical group!",
                1, many.size());
        List<String> readSingleFields = getParameterNames(single);
        List<String> readManyFields = getParameterNames(many);
        Assert.assertEquals("Incorrect field in 'many' group!",
                Collections.singletonList("b"), readManyFields);
        Assert.assertEquals("Incorrect field in 'single' group!",
                Collections.singletonList("a"), readSingleFields);
    }

    @Test
    public void inheritLogicalGroupWithExclusion() {
        @Form
        @LogicalGroups({
                @LogicalGroup(type = SelectionType.SINGLE, fields = {"a", "c"}),
                @LogicalGroup(type = SelectionType.MANY, fields = {"b", "d"}),
        })
        class A {
            @Parameter
            Boolean a;
            @Parameter
            Boolean b;
            @Parameter
            Boolean c;
            @Parameter
            Boolean d;
        }
        @Form
        @ExcludeInherited({"a", "d"})
        class B extends A {
        }
        ClassDescriptor<B> b = ClassDescriptor.from(B.class);
        Map<String, ParameterDescriptor> properties = new HashMap<>(b.getChildren().size());
        for (ParameterDescriptor p : b.getChildren()) {
            properties.put(p.getPropertyName(), p);
        }
        Assert.assertEquals(1, b.getManySelection().size());
        Assert.assertEquals(1, b.getSingleSelection().size());
        List<ParameterDescriptor> singleList = b.getSingleSelection().iterator().next();
        List<ParameterDescriptor> manyList = b.getManySelection().iterator().next();
        Assert.assertEquals(1, singleList.size());
        Assert.assertEquals(1, manyList.size());
        Assert.assertEquals(properties.get("c"), singleList.get(0));
        Assert.assertEquals(properties.get("b"), manyList.get(0));
    }
}

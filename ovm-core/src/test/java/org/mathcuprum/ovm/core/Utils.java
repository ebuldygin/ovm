package org.mathcuprum.ovm.core;

import org.mathcuprum.ovm.core.meta.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ebuldygin on 26.08.2017.
 */
public class Utils {

    @Form
    @LogicalGroups({
            @LogicalGroup(type = SelectionType.MANY),
            @LogicalGroup(type = SelectionType.SINGLE, fields = {"single1", "single2"})
    })
    public static class BaseFieldsParameter {

        static final String F_MANY = "many";
        static final String F_SINGLE1 = "single1";
        static final String F_SINGLE2 = "single2";
        static final String F_TEXT = "text";
        static final String F_INT = "integer";

        static final List<String> MANY = Collections.singletonList(F_MANY);
        static final List<String> SINGLE = Arrays.asList(F_SINGLE1, F_SINGLE2);
        static final List<String> ALL_FIELDS = Arrays.asList(F_MANY, F_SINGLE1, F_SINGLE2, F_TEXT, F_INT);

        @Parameter
        Boolean many;
        @Parameter
        Boolean single1;
        @Parameter
        Boolean single2;
        @Parameter
        String text;
        @Parameter
        Integer integer;
    }

    @Form
    @LogicalGroups({
            @LogicalGroup(type = SelectionType.MANY),
            @LogicalGroup(type = SelectionType.SINGLE, fields = {"single1", "single2"})
    })
    public static class BaseMethodsParameter {
        Boolean many;
        Boolean single1;
        Boolean single2;
        String text;
        Integer integer;

        @Parameter(order = 10)
        public Boolean getMany() {
            return many;
        }

        public void setMany(Boolean many) {
            this.many = many;
        }

        @Parameter(order = 20)
        public Boolean getSingle1() {
            return single1;
        }

        public void setSingle1(Boolean single1) {
            this.single1 = single1;
        }

        @Parameter(order = 30)
        public Boolean getSingle2() {
            return single2;
        }

        @Parameter(order = 40)
        public void setSingle2(Boolean single2) {
            this.single2 = single2;
        }

        @Parameter(order = 50)
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Parameter
        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }
    }

    static List<String> getParameterNames(List<ParameterDescriptor> single) {
        List<String> readSingleFields = new ArrayList<>(single.size());
        for (ParameterDescriptor p : single) {
            readSingleFields.add(p.getPropertyName());
        }
        return readSingleFields;
    }

    static <T> void setProperty(ClassDescriptor<T> descriptor, String propertyName, Object model, Object value) {
        for (ParameterDescriptor parameterDescriptor : descriptor.getChildren()) {
            if (propertyName.equals(parameterDescriptor.getPropertyName())) {
                parameterDescriptor.setInstanceValue(model, value);
                return;
            }
        }
        throw new IllegalArgumentException("Property not found: " + propertyName);
    }

    static <T> Object getProperty(ClassDescriptor<T> descriptor, String propertyName, Object model) {
        for (ParameterDescriptor parameterDescriptor : descriptor.getChildren()) {
            if (propertyName.equals(parameterDescriptor.getPropertyName())) {
                return parameterDescriptor.getInstanceValue(model);
            }
        }
        throw new IllegalArgumentException("Property not found: " + propertyName);
    }
}

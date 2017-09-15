package org.mathcuprum.ovm.core;

import org.mathcuprum.ovm.core.ConvertException;
import org.mathcuprum.ovm.core.Converter;

/**
 * Created by ebuldygin on 19.08.2017.
 */
public class StringConverter implements Converter<String> {

    @Override
    public String convertToView(Object value) {
        if (value == null || "".equals(value)) {
            return null;
        } else {
            return value.toString();
        }
    }

    @Override
    public Object convertToObject(String value) {
        if (value == null || "".equals(value)) {
            return null;
        }
        return value;
    }

    @Override
    public Class<String> getViewType() {
        return String.class;
    }

}

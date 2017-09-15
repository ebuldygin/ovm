package org.mathcuprum.ovm.core.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mathcuprum.ovm.core.Converter;
import org.mathcuprum.ovm.core.IdentityConverter;
import org.mathcuprum.ovm.core.ValueOfConverter;

/**
 * Created by ebuldygin on 19.08.2017.
 */
class Converters {

    private static final Logger LOGGER = LoggerFactory.getLogger(Converters.class);

    static Converter<?> getDefaultConverter(Class<?> viewClass, Class<?> valueClass) {
        if (viewClass == valueClass) {
            return new IdentityConverter<>(valueClass);
        }
        if (viewClass == String.class) {
            try {
                valueClass.getMethod("valueOf", String.class);
                return new ValueOfConverter(valueClass);
            } catch (NoSuchMethodException e) {
                LOGGER.debug("Class " + valueClass.getName() + " hasn't 'valueOf(String) method'", e);
            }
        }
        LOGGER.debug("No default converter from " + valueClass.getCanonicalName()
                + " to " + viewClass.getName());
        return null;
    }

}

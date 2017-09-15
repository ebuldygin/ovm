package org.mathcuprum.ovm.core;

/**
 * Интерфейс, предоставляющий метаинформацию о поле, помеченном
 * аннотацией {@link org.mathcuprum.ovm.core.meta.Parameter}
 * <p>
 * Created by ebuldygin on 13.08.2017.
 */
public interface ParameterDescriptor extends Descriptor, Accessible {
    String DELIMITER = ".";
}

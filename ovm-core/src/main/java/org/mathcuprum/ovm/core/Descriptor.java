package org.mathcuprum.ovm.core;

import org.mathcuprum.ovm.core.meta.ClassDescriptor;

import java.util.List;

/**
 * Интерфейс, предоставляющий метаинформацию о поле, помеченном аннотацией
 * {@link org.mathcuprum.ovm.core.meta.Parameter} или о классе, помеченном
 * аннотацией {@link org.mathcuprum.ovm.core.meta.Form}
 * <p>
 * Created by ebuldygin on 13.08.2017.
 */
public interface Descriptor {

    /**
     * @return название поля/свойства (по умолчанию в соответствии с JavaBeans)
     * для дескрипторов классов возвращает null
     */
    String getPropertyName();

    /**
     * @return заголовок для представления
     */
    String getTitle();

    /**
     * @return тип свойства
     */
    Class<?> getValueType();

    /**
     * @return метаинформация об отображении полей класса или полей класса-формы свойства.
     */
    List<ParameterDescriptor> getChildren();

}

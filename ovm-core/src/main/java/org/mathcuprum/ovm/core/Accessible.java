package org.mathcuprum.ovm.core;

import org.mathcuprum.ovm.core.meta.ClassDescriptor;

import java.util.List;

/**
 * Интерфейс, предоставляющий метаинформацию о поле, помеченном аннотацией
 * {@link org.mathcuprum.ovm.core.meta.Parameter} и описывающий действия
 * для извлечения/установки данных в модели
 * <p>
 * Created by ebuldygin on 13.08.2017.
 */
public interface Accessible {

    /**
     * Установка значения в соответствующее свойство целевого объекта
     *
     * @param target целевой объект
     * @param value  значение свойства
     */
    void setInstanceValue(Object target, Object value);

    /**
     * Извлечение значения из соответствующего свойства объекта
     *
     * @param source объект, из которого извлекается свойство, можно передавать null
     * @return значение свойства или null, если исходный объект равен null
     */
    Object getInstanceValue(Object source);

    /**
     * Получение подходящего конвертера
     *
     * @param widgetClass метаинформация о классе
     * @param <T>         класс представления, в который необходимо преобразовывать значение свойства
     * @return подходящий конвертер, или null, если такой не найдётся
     */
    <T> Converter<T> converterFor(Class<T> widgetClass);

    /**
     * @return метаинформация об отображении полей класса, содержащего это свойство
     */
    ClassDescriptor<?> getDeclaredClass();

    /**
     * @return порядок параметра на форме
     */
    int getOrder();


}

package org.mathcuprum.ovm.core;

import org.mathcuprum.ovm.core.meta.Parameter;

/**
 * Интерфейс, описывающий контракт для виджета
 * <p>
 * Created by ebuldygin on 13.08.2017.
 */
public interface Widget {

    /**
     * @return ключ, позволяющий идентифицировать виджет
     */
    String getId();

    /**
     * Получение значения элемента модели из виджета
     *
     * @return значение соотвествующей части модели
     * @throws ValidationException
     * @throws ConvertException
     */
    Object get();

    /**
     * Установка в виджете значения элемента модели
     *
     * @param value значение элемента модели
     * @throws ConvertException
     */
    void set(Object value);

    /**
     * Установка в виджете соответствующего элемента из полной модели
     *
     * @param model объект модели
     * @throws ConvertException
     */
    void readFomModel(Object model);

    /**
     * Установка в модели значения из виджета
     *
     * @param model полная модель
     * @throws ValidationException
     * @throws ConvertException
     */
    void writeToModel(Object model);

    /**
     * @return название связанного параметра (см. {@link Parameter#title()})
     */
    String getTitle();

}

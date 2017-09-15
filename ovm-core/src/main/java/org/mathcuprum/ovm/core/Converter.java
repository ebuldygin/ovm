package org.mathcuprum.ovm.core;

/**
 * Интерфейс для преобразователей объектов между классом модели и классом для виджета
 * @param <V> класс в виджете
 *
 * Created by ebuldygin on 13.08.2017.
 */
public interface Converter<V> {

    /**
     * Преобразование значения свойства в объект для виджета
     *
     * @param value значение свойства
     * @return объект, представляющий значение для виджета
     * @throws ConvertException если преобразование не может быть выполнено
     */
    V convertToView(Object value);

    /**
     * Преобразование значения из виджета в значение свойства
     *
     * @param value значение свойства из виджета
     * @return значение свойства
     * @throws ConvertException если преобразование не может быть выполнено
     */
    Object convertToObject(V value);

    /**
     * @return класс, поддерживаемый виджетом
     */
    Class<V> getViewType();
}

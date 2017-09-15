package org.mathcuprum.ovm.core;

/**
 * Интерфейс для валидаторов параметров
 *
 * Created by ebuldygin on 20.08.2017.
 */
public interface Validator {

    /**
     * Валидация объекта
     *
     * @param value валидируемое значение
     * @throws ValidationException возникает при ошибке валидации
     */
    void validate(Object value) throws ValidationException;

}

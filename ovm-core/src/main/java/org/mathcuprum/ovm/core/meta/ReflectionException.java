package org.mathcuprum.ovm.core.meta;

/**
 * Исключение, описывающее ошибку при попытке чтения/записи свойств
 *
 * Created by ebuldygin on 19.08.2017.
 */
public class ReflectionException extends RuntimeException {

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

}

package org.mathcuprum.ovm.core;

/**
 * Исключение преобразования
 * <p>
 * Created by ebuldygin on 13.08.2017.
 */
public class ConvertException extends RuntimeException {

    private final Class<?> destinationType;
    private final Object value;
    private ParameterDescriptor descriptor;

    /**
     * @param message         сообщение об ошибке
     * @param cause           причина ошибки
     * @param destinationType тип, в который проводилось преобразование
     * @param value           значение, которое необходимо было преобразовать
     * @param descriptor      свойство, с которым возникла проблема
     */
    public ConvertException(String message,
                            Throwable cause,
                            Class<?> destinationType,
                            Object value,
                            ParameterDescriptor descriptor) {
        super(message, cause);
        this.destinationType = destinationType;
        this.value = value;
        this.descriptor = descriptor;
    }

    /**
     * @param message         сообщение об ошибке
     * @param cause           причина ошибки
     * @param destinationType тип, в который проводилось преобразование
     * @param value           значение, которое необходимо было преобразовать
     */
    public ConvertException(String message,
                            Throwable cause,
                            Class<?> destinationType,
                            Object value) {
        this(message, cause, destinationType, value, null);
    }

    /**
     * @return целевой тип, в который не удалось преобразовать значение
     */
    public Class<?> getDestinationType() {
        return destinationType;
    }

    /**
     * @return объект, который не удалось преобразовать
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return свойство, у которого возникла проблема
     */
    public ParameterDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(ParameterDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}

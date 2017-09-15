package org.mathcuprum.ovm.core.meta;

import org.mathcuprum.ovm.core.Converter;
import org.mathcuprum.ovm.core.IdentityConverter;
import org.mathcuprum.ovm.core.ValueOfConverter;

import java.lang.annotation.*;

/**
 * Created by ebuldygin on 13.08.2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Parameter {

    /**
     * @return название параметра на форме
     */
    String title() default "";

    /**
     * Класс для преобразования параметра в значение виджета
     * По умолчанию поддерживаются:
     * - тождественное преобразование {@link IdentityConverter}
     * - преобразование строка (виджет) - класс, содержащий статический
     * метод valueOf(String) (параметр модели) {@link ValueOfConverter}
     *
     * @return список возможных преобразователей
     */
    Class<? extends Converter<?>>[] converter() default {};

    /**
     * Уникальное название параметра (в рамках класса с учётом унаследованных параметров)
     *
     * @return название параметра
     */
    String name() default "";

    int order() default Integer.MAX_VALUE;

}

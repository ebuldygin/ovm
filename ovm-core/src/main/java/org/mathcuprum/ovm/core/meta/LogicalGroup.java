package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.*;

/**
 * Created by ebuldygin on 16.08.2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogicalGroup {

    /**
     * @return тип логической группы: множественный выбор или исключающий
     */
    SelectionType type() default SelectionType.MANY;

    /**
     * @return список свойств в логической группе (см. {@link Parameter#name()})
     */
    String[] fields() default {};

}

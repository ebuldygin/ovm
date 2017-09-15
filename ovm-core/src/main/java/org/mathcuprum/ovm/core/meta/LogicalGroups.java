package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.*;

/**
 * Created by ebuldygin on 16.08.2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogicalGroups {

    /**
     * @return список логических групп
     */
    LogicalGroup[] value() default {};

}

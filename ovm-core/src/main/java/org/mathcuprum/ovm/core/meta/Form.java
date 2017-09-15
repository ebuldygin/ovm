package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.*;

/**
 * Created by ebuldygin on 13.08.2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Form {

    /**
     * @return название формы
     */
    String title() default "";

}

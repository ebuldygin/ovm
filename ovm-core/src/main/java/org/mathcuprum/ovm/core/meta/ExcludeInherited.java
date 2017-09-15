package org.mathcuprum.ovm.core.meta;

import java.lang.annotation.*;

/**
 * Created by ebuldygin on 13.08.2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcludeInherited {

    /**
     * Список названий параметров, которые не должны наследоваться
     * Если в списке объявлены несуществующие свойства или он не являются параметрами,
     * то они будут игнорироваться (см. {@link Parameter#name()})
     *
     * @return список названий параметров
     */
    String[] value();

}

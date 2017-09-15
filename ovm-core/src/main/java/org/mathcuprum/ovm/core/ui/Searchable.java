package org.mathcuprum.ovm.core.ui;

import org.mathcuprum.ovm.core.Widget;

/**
 * Created by ebuldygin on 03.09.2017.
 */
public interface Searchable {

    /**
     * Поиск виджета в компоненте по идентификатору
     *
     * @param id регулярное выражение для идентификатора
     * @return найденный компонент или null
     */
    Widget searchById(String id);

}

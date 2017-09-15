package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.*;

@Form(title = "Название формы")
@LogicalGroups({
        @LogicalGroup(type = SelectionType.SINGLE),
        @LogicalGroup(type = SelectionType.MANY, fields = {"manyLogical1", "manyLogical2"})
})
public class DemoSimpleForm {
    @Parameter(title = "Текстовый параметр", order = 10)
    private String text;
    @Parameter(title = "Логический параметр 1 (исключающий выбор)", order = 20)
    private boolean singleLogical1;
    @Parameter(title = "Целое число (Integer)", order = 30)
    private Integer objectInt;
    @Parameter(title = "Логический параметр 1 (множественный выбор)", order = 40)
    private boolean manyLogical1;
    @Parameter(title = "Логический параметр 2 (исключающий выбор)", order = 50)
    private boolean singleLogical2;
    @Parameter(title = "Целое число (int)", order = 60)
    private int primitiveInt;
    @Parameter(title = "Логический параметр 1 (множественный выбор)", order = 70)
    private boolean manyLogical2;
}


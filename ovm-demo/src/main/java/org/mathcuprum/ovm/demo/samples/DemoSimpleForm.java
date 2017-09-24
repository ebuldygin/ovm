package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.*;

@Form(title = "Название формы")
@LogicalGroups({
        @LogicalGroup(type = SelectionType.MANY),
        @LogicalGroup(type = SelectionType.SINGLE,
                fields = {"singleLogical1", "singleLogical2"})
})
public class DemoSimpleForm {
    @Parameter(title = "Текстовый параметр", order = 10)
    private String text;
    @Parameter(title = "Радиобаттон 1", order = 20)
    private boolean singleLogical1;
    @Parameter(title = "Целое число (Integer)", order = 30)
    private Integer objectInt;
    @Parameter(title = "Чекбокс 1", order = 40)
    private boolean manyLogical1;
    @Parameter(title = "Радиобаттон 2", order = 50)
    private boolean singleLogical2;
    @Parameter(title = "Целое число (int)", order = 60)
    private int primitiveInt;
    @Parameter(title = "Чекбокс 2", order = 70)
    private boolean manyLogical2;
    @Parameter(title = "Чекбокс 3", order = 80)
    private boolean manyLogical3;
}


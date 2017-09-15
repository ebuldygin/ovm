package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.*;

@Form(title = "Простая компоновка")
@LogicalGroups({
        @LogicalGroup(type = SelectionType.SINGLE),
        @LogicalGroup(type = SelectionType.MANY, fields = {"manyLogical1", "manyLogical2"})
})
public class SimpleForm {
    private String text;
    private int primitiveInt;
    private Integer objectInt;
    private boolean singleLogical1;
    private boolean singleLogical2;
    private boolean singleLogical3;
    private boolean manyLogical1;
    private boolean manyLogical2;

    @Parameter(title = "Текстовый параметр", order = 10)
    public String getText() {
        return text;
    }

    @Parameter(title = "Целое число (int)", order = 11)
    public int getPrimitiveInt() {
        return primitiveInt;
    }

    @Parameter(title = "Целое число (Integer)", order = 12)
    public Integer getObjectInt() {
        return objectInt;
    }

    @Parameter(title = "Логический параметр 1 (исключающий выбор)", order = 20)
    public boolean isSingleLogical1() {
        return singleLogical1;
    }

    @Parameter(title = "Логический параметр 2 (исключающий выбор)", order = 30)
    public boolean isSingleLogical2() {
        return singleLogical2;
    }

    @Parameter(title = "Логический параметр 3 (исключающий выбор)", order = 40)
    public boolean isSingleLogical3() {
        return singleLogical3;
    }

    @Parameter(title = "Логический параметр 1 (множественный выбор)", order = 50)
    public boolean isManyLogical1() {
        return manyLogical1;
    }

    @Parameter(title = "Логический параметр 1 (множественный выбор)", order = 60)
    public boolean isManyLogical2() {
        return manyLogical2;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }

    public void setObjectInt(Integer objectInt) {
        this.objectInt = objectInt;
    }

    public void setSingleLogical1(boolean singleLogical1) {
        this.singleLogical1 = singleLogical1;
    }

    public void setSingleLogical2(boolean singleLogical2) {
        this.singleLogical2 = singleLogical2;
    }

    public void setSingleLogical3(boolean singleLogical3) {
        this.singleLogical3 = singleLogical3;
    }

    public void setManyLogical1(boolean manyLogical1) {
        this.manyLogical1 = manyLogical1;
    }

    public void setManyLogical2(boolean manyLogical2) {
        this.manyLogical2 = manyLogical2;
    }
}

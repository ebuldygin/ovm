package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.*;

@Form(title = "Простое наследование")
public class PlainInheritedForm extends SimpleForm {
    private double extraDouble;
    private int extraInt;

    @Parameter(title = "Число с плавающей точкой (double)", order = 11)
    public double getExtraDouble() {
        return extraDouble;
    }

    @Parameter(title = "Целое число (int)", order = 41)
    public int getExtraInt() {
        return extraInt;
    }

    public void setExtraDouble(double extraDouble) {
        this.extraDouble = extraDouble;
    }

    public void setExtraInt(int extraInt) {
        this.extraInt = extraInt;
    }
}

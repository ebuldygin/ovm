package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.Parameter;

@Form(title = "Композиция форм")
public class CompositionForm {
    private SimpleForm nested;
    private String text;

    @Parameter(title = "Вложенная форма", order = 10)
    public SimpleForm getNested() {
        return nested;
    }

    @Parameter(title = "Текст с основной формы", order = 40)
    public String getText() {
        return text;
    }

    public void setNested(SimpleForm nested) {
        this.nested = nested;
    }

    public void setText(String text) {
        this.text = text;
    }
}

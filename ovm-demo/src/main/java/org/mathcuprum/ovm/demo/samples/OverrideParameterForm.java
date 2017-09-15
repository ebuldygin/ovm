package org.mathcuprum.ovm.demo.samples;

import org.mathcuprum.ovm.core.meta.ExcludeInherited;
import org.mathcuprum.ovm.core.meta.Form;
import org.mathcuprum.ovm.core.meta.Parameter;

@Form(title = "Наследование с исключением")
@ExcludeInherited({"singleLogical1", "singleLogical2", "manyLogical2"})
public class OverrideParameterForm extends PlainInheritedForm {
}

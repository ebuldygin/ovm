package org.mathcuprum.ovm.core.meta;

import org.mathcuprum.ovm.core.Descriptor;
import org.mathcuprum.ovm.core.ParameterDescriptor;

import static org.mathcuprum.ovm.core.meta.DescriptorUtils.*;

import java.util.*;

/**
 * Объектное представление отображения свойств класса T
 * <p>
 * Created by ebuldygin on 13.08.2017.
 */
public class ClassDescriptor<T> implements Descriptor {

    private final Class<T> clazz;
    private final String title;
    private final List<ParameterDescriptor> children;
    private final Collection<List<ParameterDescriptor>> singleSelection;
    private final Collection<List<ParameterDescriptor>> manySelection;

    /**
     * Извлечение метаинформации об отображении из класса V
     *
     * @param clazz тип класса
     * @param <V>   класс, помеченный аннотацией @Form
     * @return объектное представление метаинформации
     * @throws IllegalArgumentException когда
     *                                  - класс не помечен аннотацией {@link Form},
     *                                  - имеются две аннотации {@link LogicalGroup} без списка полей,
     *                                  - свойство объявлено в нескольких аннотациях {@link LogicalGroup},
     *                                  - свойство, объявленное в {@link LogicalGroup} не помечено аннотацией {@link Parameter},
     *                                  - поле и геттер одного свойства помечены аннотацией {@link Parameter}
     */
    public static <V> ClassDescriptor<V> from(Class<V> clazz) {
        return new ClassDescriptor<>(clazz);
    }

    private ClassDescriptor(Class<T> clazz) {
        Form f = clazz.getAnnotation(Form.class);
        if (f == null) {
            throw new IllegalArgumentException("Class " + clazz.getCanonicalName()
                    + " not marked as @" + Form.class.getName());
        }
        this.clazz = clazz;
        this.title = f.title();
        this.children = Collections.unmodifiableList(readChildDescriptors(this));
        Collection<List<ParameterDescriptor>> singleSelection = new HashSet<>();
        Collection<List<ParameterDescriptor>> manySelection = new HashSet<>();
        readLogicalGroup(this, singleSelection, manySelection);
        this.singleSelection = unmodifiable(singleSelection);
        this.manySelection = unmodifiable(manySelection);
    }

    @Override
    public Class<T> getValueType() {
        return clazz;
    }

    @Override
    public List<ParameterDescriptor> getChildren() {
        return children;
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Collection<List<ParameterDescriptor>> getSingleSelection() {
        return singleSelection;
    }

    public Collection<List<ParameterDescriptor>> getManySelection() {
        return manySelection;
    }

    @Override
    public String toString() {
        return "ClassDescriptor{" +
                "clazz=" + clazz +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassDescriptor<?> that = (ClassDescriptor<?>) o;

        return clazz.equals(that.clazz);

    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }
}

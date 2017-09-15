package org.mathcuprum.ovm.core.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mathcuprum.ovm.core.Converter;
import org.mathcuprum.ovm.core.ParameterDescriptor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Вспомогательный класс, содержащий логику начитки метаинформации для {@link ClassDescriptor}
 * <p>
 * Created by ebuldygin on 14.08.2017.
 */
final class DescriptorUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DescriptorUtils.class);

    private DescriptorUtils() {
        throw new InstantiationError();
    }

    /**
     * Извлечение информации об отображении полей в классе
     *
     * @param parent объектное представление класса
     * @return список с метаинформцией об отображении полей/свойств
     * @throws IllegalArgumentException - поле и соответствующий геттер помечены аннотацией @Parameter
     */
    static List<ParameterDescriptor> readChildDescriptors(ClassDescriptor parent) {
        Collection<PropertyDescriptor> fields = readAnnotatedFields(parent);
        Collection<PropertyDescriptor> methods = readAnnotatedMethods(parent);
        checkUniquenessInDeclaredClass(fields, methods);
        final List<ParameterDescriptor> descriptors = new LinkedList<>();
        descriptors.addAll(fields);
        descriptors.addAll(methods);
        checkUniquenessInClass(descriptors);
        Collections.sort(descriptors, new Comparator<ParameterDescriptor>() {
            @Override
            public int compare(ParameterDescriptor p1, ParameterDescriptor p2) {
                return Integer.compare(p1.getOrder(), p2.getOrder());
            }
        });
        return descriptors;
    }

    private static Collection<PropertyDescriptor> readAnnotatedFields(ClassDescriptor<?> current) {
        List<PropertyDescriptor> result = new LinkedList<>();
        Class<?> superclass = current.getValueType().getSuperclass();
        if (superclass.getAnnotation(Form.class) != null) {
            result.addAll(readAnnotatedFields(ClassDescriptor.from(superclass)));
        }
        Set<String> excludedProperties = getExcludedProperties(current);
        excludeProperties(excludedProperties, result);
        Parameter param;
        for (Field field : current.getValueType().getDeclaredFields()) {
            if ((param = field.getAnnotation(Parameter.class)) != null) {
                checkParameterName(param.name());
                FieldProperty property = new FieldProperty(field, param.name(), param.order());
                result.add(PropertyDescriptor.createFrom(property, current));
            }
        }
        return result;
    }

    private static Collection<PropertyDescriptor> readAnnotatedMethods(ClassDescriptor<?> current) {
        List<PropertyDescriptor> result = new LinkedList<>();
        Class<?> superclass = current.getValueType().getSuperclass();
        if (superclass.getAnnotation(Form.class) != null) {
            result.addAll(readAnnotatedMethods(ClassDescriptor.from(superclass)));
        }
        Set<String> excludedProperties = getExcludedProperties(current);
        excludeProperties(excludedProperties, result);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(current.getValueType());
            Parameter param;
            for (java.beans.PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
                if (p.getReadMethod() != null
                        && (param = p.getReadMethod().getAnnotation(Parameter.class)) != null
                        && current.getValueType() == p.getReadMethod().getDeclaringClass()) {
                    checkParameterName(param.name());
                    MethodProperty methodProperty = new MethodProperty(p, param.name(), param.order());
                    result.add(PropertyDescriptor.createFrom(methodProperty, current));
                }
            }

        } catch (IntrospectionException e) {
            LOGGER.warn("Error while read properties for " + current + " " + e.getMessage());
            LOGGER.debug(e.getMessage(), e);
        }
        return result;
    }

    private static void excludeProperties(Set<String> excludedProperties, List<? extends ParameterDescriptor> result) {
        Iterator<? extends ParameterDescriptor> iterator = result.iterator();
        while (iterator.hasNext()) {
            ParameterDescriptor p = iterator.next();
            if (excludedProperties.contains(p.getPropertyName())) {
                LOGGER.debug("property " + p.getPropertyName() + " excluded");
                iterator.remove();
            }
        }
    }

    private static Set<String> getExcludedProperties(ClassDescriptor<?> current) {
        Set<String> excludedPoperties = new HashSet<>();
        ExcludeInherited e;
        if ((e = current.getValueType().getAnnotation(ExcludeInherited.class)) != null) {
            excludedPoperties.addAll(Arrays.asList(e.value()));
        }
        return excludedPoperties;
    }

    private static void checkParameterName(String name) {
        if (name.contains(ParameterDescriptor.DELIMITER)) {
            throw new IllegalArgumentException("Separator is not allowed in property name: " + name);
        }
    }

    private static void checkUniquenessInDeclaredClass(Collection<? extends PropertyDescriptor> fields,
                                                       Collection<? extends PropertyDescriptor> methods) {
        Set<PropertyDescriptor> unique = new HashSet<>();
        unique.addAll(fields);
        for (PropertyDescriptor method : methods) {
            if (!unique.add(method)) {
                throw new IllegalArgumentException("Both field and method " + method.getPropertyName()
                        + " in " + method.getDeclaredClass().getPropertyName() + " marked as @" + Parameter.class.getName());
            }
        }
    }

    private static void checkUniquenessInClass(Collection<? extends ParameterDescriptor> descriptors) {
        Map<String, ParameterDescriptor> unique = new HashMap<>();
        for (ParameterDescriptor descriptor : descriptors) {
            if (unique.containsKey(descriptor.getPropertyName())) {
                ParameterDescriptor first = unique.get(descriptor.getPropertyName());
                throw new IllegalArgumentException(String.format("Property is not unique: %s.%s and %s.%s",
                        descriptor.getDeclaredClass().getValueType().getName(), descriptor.getPropertyName(),
                        first.getDeclaredClass().getValueType().getName(), first.getPropertyName()));
            }
            unique.put(descriptor.getPropertyName(), descriptor);
        }

    }

    static List<ParameterDescriptor> readChildDescriptors(Class<?> clazz) {
        if (clazz.getAnnotation(Form.class) != null) {
            return readChildDescriptors(ClassDescriptor.from(clazz));
        } else {
            return new LinkedList<>();
        }
    }

    static void readLogicalGroup(ClassDescriptor<?> classDescriptor,
                                 Collection<List<ParameterDescriptor>> singleSelection,
                                 Collection<List<ParameterDescriptor>> manySelection) {
        final List<LogicalGroup> allGroups = readLogicalGroups(classDescriptor.getValueType());
        final Map<String, ParameterDescriptor> children = mapLogicalChildren(classDescriptor);
        SelectionType defaultSelectionType = null;
        List<ParameterDescriptor> defaultGroup = new LinkedList<>();
        Set<String> explicitlyDeclaredFields = new HashSet<>();
        for (LogicalGroup g : allGroups) {
            if (g.fields().length == 0) {
                if (defaultSelectionType != null) {
                    throw new IllegalArgumentException("Duplicate @" + LogicalGroup.class.getName()
                            + " without fields in " + classDescriptor.getValueType().getName());
                }
                defaultSelectionType = g.type();
            } else {
                List<ParameterDescriptor> parameterDescriptors = new ArrayList<>(g.fields().length);
                for (String s : g.fields()) {
                    if (!explicitlyDeclaredFields.add(s)) {
                        throw new IllegalArgumentException("Property " + s + " already defined in @"
                                + LogicalGroup.class.getName() + " in " + classDescriptor.getValueType().getName());
                    }
                    if (children.containsKey(s)) {
                        parameterDescriptors.add(children.get(s));
                    } else {
                        throw new IllegalArgumentException("Property " + s + " not marked as @"
                                + Parameter.class.getName() + " in " + classDescriptor.getValueType().getName());
                    }
                }
                if (SelectionType.SINGLE.equals(g.type())) {
                    singleSelection.add(parameterDescriptors);
                } else {
                    manySelection.add(parameterDescriptors);
                }
            }
        }
        for (Map.Entry<String, ParameterDescriptor> e : children.entrySet()) {
            if (!explicitlyDeclaredFields.contains(e.getKey())) {
                defaultGroup.add(e.getValue());
            }
        }
        if (!defaultGroup.isEmpty()) {
            if (SelectionType.SINGLE.equals(defaultSelectionType)) {
                singleSelection.add(defaultGroup);
            } else {
                manySelection.add(defaultGroup);
            }
        }
        Set<String> excluded = getExcludedProperties(classDescriptor);
        Set<ClassDescriptor<?>> processed = new HashSet<>();
        processed.add(classDescriptor);
        for (ParameterDescriptor p : classDescriptor.getChildren()) {
            if (processed.add(p.getDeclaredClass())) {
                transferInheritLogicalGroup(singleSelection, p.getDeclaredClass().getSingleSelection(), excluded);
                transferInheritLogicalGroup(manySelection, p.getDeclaredClass().getManySelection(), excluded);
            }
        }
    }

    private static void transferInheritLogicalGroup(Collection<List<ParameterDescriptor>> current,
                                                    Collection<List<ParameterDescriptor>> parent,
                                                    Set<String> excluded) {
        if (parent.isEmpty()) {
            return;
        }
        for (List<ParameterDescriptor> parameterDescriptors : parent) {
            List<ParameterDescriptor> params = new LinkedList<>(parameterDescriptors);
            excludeProperties(excluded, params);
            if (!params.isEmpty()) {
                current.add(params);
            }
        }
    }

    private static List<LogicalGroup> readLogicalGroups(Class<?> clazz) {
        List<LogicalGroup> allGroups = new LinkedList<>();
        LogicalGroups groups = clazz.getAnnotation(LogicalGroups.class);
        LogicalGroup group = clazz.getAnnotation(LogicalGroup.class);
        if (group != null) {
            allGroups.add(group);
        }
        if (groups != null) {
            allGroups.addAll(Arrays.asList(groups.value()));
        }
        return allGroups;
    }

    private static Map<String, ParameterDescriptor> mapLogicalChildren(ClassDescriptor<?> classDescriptor) {
        final Map<String, ParameterDescriptor> res = new HashMap<>();
        for (ParameterDescriptor p : classDescriptor.getChildren()) {
            if (DescriptorUtils.isLogical(p) && classDescriptor == p.getDeclaredClass()) {
                res.put(p.getPropertyName(), p);
            }
        }
        return res;
    }

    static List<Converter<?>> readConverters(ParameterDescriptor descriptor,
                                             Class<? extends Converter<?>>[] converters) {
        List<Converter<?>> res = new LinkedList<>();
        if (converters != null) {
            for (Class<? extends Converter<?>> converterClass : converters) {
                Converter<?> converter = processConverter(converterClass, descriptor.getValueType());
                if (converter != null) {
                    res.add(new ConverterWrapper<>(converter, descriptor));
                }
            }
        }
        return res;
    }

    static boolean isLogical(ParameterDescriptor property) {
        return isLogical(property.getValueType());
    }

    static boolean isLogical(Class<?> property) {
        return boolean.class.equals(property)
                || Boolean.class.equals(property);
    }

    private static Converter<?> processConverter(Class<? extends Converter<?>> converterClass,
                                                 Class<?> valueFieldType) {
        Converter<?> result = tryInstantiateWithParameter(converterClass);
        if (result == null) {
            result = tryInstantiateWithParameter(converterClass, valueFieldType);
        }
        return result;
    }

    private static Converter<?> tryInstantiateWithParameter(Class<? extends Converter<?>> converterClass,
                                                            Object... args) {
        Class<?>[] valueFieldTypeClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            valueFieldTypeClasses[i] = args[i].getClass();
        }

        try {
            Constructor<?> constructor = converterClass.getDeclaredConstructor(valueFieldTypeClasses);
            constructor.setAccessible(true);
            return (Converter<?>) constructor.newInstance(args);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            LOGGER.warn("Error while instantiate converter " + converterClass
                    + " with " + Arrays.toString(valueFieldTypeClasses) + " constructor, " + e.getMessage());
            LOGGER.debug(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            LOGGER.debug("Class hasn't constructor with arguments: " + Arrays.toString(valueFieldTypeClasses));
        }
        return null;
    }

    static Collection<List<ParameterDescriptor>> unmodifiable(Collection<List<ParameterDescriptor>> source) {
        Collection<List<ParameterDescriptor>> res = new HashSet<>();
        for (List<ParameterDescriptor> s : source) {
            res.add(Collections.unmodifiableList(s));
        }
        return Collections.unmodifiableCollection(res);
    }

}

package org.mathcuprum.ovm.core.meta;

import org.mathcuprum.ovm.core.Converter;
import org.mathcuprum.ovm.core.ParameterDescriptor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ebuldygin on 13.08.2017.
 */
class PropertyDescriptor implements ParameterDescriptor {

    private final Property property;
    private final String title;
    private final ClassDescriptor declaredClass;
    private final List<ParameterDescriptor> children;
    private final List<Converter<?>> converters = new LinkedList<>();

    private PropertyDescriptor(Property field, ClassDescriptor classDescriptor) {
        Parameter p = field.getAnnotation(Parameter.class);
        if (p == null) {
            throw new IllegalArgumentException("Field " + field.getName()
                    + " not marked as @" + Parameter.class.getName());
        }
        this.declaredClass = classDescriptor;
        this.property = field;
        this.title = p.title();
        this.children = Collections.unmodifiableList(DescriptorUtils.readChildDescriptors(property.getType()));
        this.converters.addAll(DescriptorUtils.readConverters(this, p.converter()));
    }

    @Override
    public void setInstanceValue(Object target, Object value) {
        property.set(target, value);
    }

    @Override
    public Object getInstanceValue(Object source) {
        if (source == null) {
            return null;
        }
        return property.get(source);
    }

    @Override
    public Class<?> getValueType() {
        return property.getType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Converter<T> converterFor(Class<T> widgetClass) {
        Converter<?> alternative = null;
        for (Converter<?> converter : converters) {
            if (widgetClass == converter.getViewType()) {
                return (Converter<T>) converter;
            }
            if (alternative == null && widgetClass.isAssignableFrom(converter.getViewType())) {
                alternative = converter;
            }
        }
        if (alternative != null) {
            return (Converter<T>) alternative;
        } else if (!converters.isEmpty()) {
            return null;
        }
        Converter<T> defaultConverter = (Converter<T>) Converters
                .getDefaultConverter(widgetClass, getValueType());
        if (defaultConverter != null) {
            ConverterWrapper<T> wrappedConverter = new ConverterWrapper<>(defaultConverter, this);
            converters.add(wrappedConverter);
            return wrappedConverter;
        }
        return null;
    }

    @Override
    public List<ParameterDescriptor> getChildren() {
        return children;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getPropertyName() {
        return property.getName();
    }

    @Override
    public ClassDescriptor getDeclaredClass() {
        return declaredClass;
    }

    @Override
    public int getOrder() {
        return property.getOrder();
    }

    @Override
    public String toString() {
        return "PropertyDescriptor{" +
                "property=" + property +
                ", title='" + title + '\'' +
                ", declaredClass=" + declaredClass +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        PropertyDescriptor that = (PropertyDescriptor) obj;
        return Objects.equals(that.getDeclaredClass(), getDeclaredClass())
                && Objects.equals(that.getPropertyName(), getPropertyName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeclaredClass(), getPropertyName());
    }

    static PropertyDescriptor createFrom(Property field, ClassDescriptor classDescriptor) {
        if (!field.getType().isPrimitive()) {
            return new PropertyDescriptor(field, classDescriptor);
        } else {
            if (boolean.class.equals(field.getType())) {
                return createBooleanDescriptor(field, classDescriptor);
            } else if (byte.class.equals(field.getType())) {
                return createByteDescriptor(field, classDescriptor);
            } else if (char.class.equals(field.getType())) {
                return createCharDescriptor(field, classDescriptor);
            } else if (short.class.equals(field.getType())) {
                return createShortDescriptor(field, classDescriptor);
            } else if (int.class.equals(field.getType())) {
                return createIntDescriptor(field, classDescriptor);
            } else if (long.class.equals(field.getType())) {
                return createLongDescriptor(field, classDescriptor);
            } else if (float.class.equals(field.getType())) {
                return createFloatDescriptor(field, classDescriptor);
            } else if (double.class.equals(field.getType())) {
                return createDoubleDescriptor(field, classDescriptor);
            }
        }
        throw new AssertionError("Incorrect type " + field.getType());
    }

    private static PropertyDescriptor createBooleanDescriptor(final Property field,
                                                              final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Boolean.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = false;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createByteDescriptor(final Property field,
                                                           final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Byte.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = (byte) 0;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createCharDescriptor(final Property field,
                                                           final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Character.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = (char) 0;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createShortDescriptor(final Property field,
                                                            final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Short.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = (short) 0;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createIntDescriptor(final Property field,
                                                          final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Integer.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = 0;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createLongDescriptor(final Property field,
                                                           final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Long.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = 0L;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createFloatDescriptor(final Property field,
                                                            final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Float.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = 0.0f;
                }
                super.setInstanceValue(target, value);
            }
        };
    }

    private static PropertyDescriptor createDoubleDescriptor(final Property field,
                                                             final ClassDescriptor classDescriptor) {
        return new PropertyDescriptor(field, classDescriptor) {
            @Override
            public Class<?> getValueType() {
                return Double.class;
            }

            @Override
            public void setInstanceValue(Object target, Object value) {
                if (value == null) {
                    value = 0.0;
                }
                super.setInstanceValue(target, value);
            }
        };
    }
}

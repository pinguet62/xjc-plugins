package fr.pinguet62.jxb.swagger;

import java.lang.reflect.Field;
import java.util.EnumSet;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public final class MatchersUtils {

    // Util
    private MatchersUtils() {}

    private static abstract class TypeSafeMatcherWithoutDescription<T> extends TypeSafeMatcher<T> {
        @Override
        public void describeTo(Description description) {}
    }

    public static Matcher<Class<?>> hasApiModelAnnotation() {
        return new TypeSafeMatcherWithoutDescription<Class<?>>() {
            @Override
            protected boolean matchesSafely(Class<?> type) {
                return type.getDeclaredAnnotation(ApiModel.class) != null;
            }
        };
    };

    public static Matcher<Class<?>> hasDescription(final String description) {
        return new TypeSafeMatcherWithoutDescription<Class<?>>() {
            @Override
            protected boolean matchesSafely(Class<?> type) {
                return type.getDeclaredAnnotation(ApiModel.class).description().equals(description);
            }
        };
    };

    public static <T extends Enum<T>> Matcher<Class<T>> enumValue(final String fieldName, final Matcher<T> fieldMatcher) {
        return new TypeSafeMatcherWithoutDescription<Class<T>>() {
            @Override
            protected boolean matchesSafely(Class<T> type) {
                for (T enumValue : EnumSet.allOf(type)) {
                    if (enumValue.name().equals(fieldName))
                        return fieldMatcher.matches(enumValue);
                }
                throw new RuntimeException("Value not found on enum " + type + ": " + fieldName);
            }
        };
    };

    public static Matcher<Class<?>> field(final String fieldName, final Matcher<Field> fieldMatcher) {
        return new TypeSafeMatcherWithoutDescription<Class<?>>() {
            @Override
            protected boolean matchesSafely(Class<?> type) {
                try {
                    Field field = type.getDeclaredField(fieldName);
                    return fieldMatcher.matches(field);
                } catch (NoSuchFieldException | SecurityException e) {
                    throw new RuntimeException();
                }
            }
        };
    };

    public static Matcher<Field> hasApiModelPropertyAnnotation() {
        return new TypeSafeMatcherWithoutDescription<Field>() {
            @Override
            protected boolean matchesSafely(Field field) {
                return field.getDeclaredAnnotation(ApiModelProperty.class) != null;
            }
        };
    };

    public static Matcher<Field> hasValue(final String apiModelPropertyValue) {
        return new TypeSafeMatcherWithoutDescription<Field>() {
            @Override
            protected boolean matchesSafely(Field field) {
                return field.getDeclaredAnnotation(ApiModelProperty.class).value().equals(apiModelPropertyValue);
            }
        };
    };

    public static Matcher<Field> isRequired(final boolean required) {
        return new TypeSafeMatcherWithoutDescription<Field>() {
            @Override
            protected boolean matchesSafely(Field field) {
                return field.getDeclaredAnnotation(ApiModelProperty.class).required() == required;
            }
        };
    };

    public static Matcher<Field> hasDataType(final String apiModelPropertyDataType) {
        return new TypeSafeMatcherWithoutDescription<Field>() {
            @Override
            protected boolean matchesSafely(Field field) {
                return field.getDeclaredAnnotation(ApiModelProperty.class).dataType().equals(apiModelPropertyDataType);
            }
        };
    };

}
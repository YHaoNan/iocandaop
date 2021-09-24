package io.lilpig.iocaop.framework.utils;

import io.lilpig.iocaop.framework.exceptions.NoThisAnnotationException;

import java.lang.annotation.Annotation;

public class AnnotationUtils {
    public static <T extends Annotation> T require(Class<?> clz, Class<T> annotationType) throws NoThisAnnotationException {
        T t = clz.getAnnotation(annotationType);
        if (t==null) throw new NoThisAnnotationException(clz.getName(), annotationType.getName());
        return t;
    }
}

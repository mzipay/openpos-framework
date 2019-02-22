package org.jumpmind.pos.util;

import java.lang.annotation.Annotation;

import org.springframework.aop.support.AopUtils;

public class ClassUtils {

    /**
     * This method first attempts to check the given targetObject's class for an 
     * annotation of the given type.  If that fails, then it uses a Spring AOP
     * Utility to attempt to locate the annotation.  This is useful for CGLIB
     * proxies who don't actually have the annotation of the proxied bean
     * on them, and therefore the actual class being proxied needs to be checked
     * for the annotation.
     * @param annotationClass The annotation type to search for.
     * @param targetObj The object whose class should be searched for the given 
     * annotation type.
     * @return Will return null if the annotation could not found. Otherwise,
     * if the annotation exists on the class of the given targetObj, it will be
     * returned.
     */
    public static <A extends Annotation> A resolveAnnotation(Class<A> annotationClass, Object targetObj) {
        A annotation = targetObj.getClass().getAnnotation(annotationClass);
        if (annotation == null) {
            Class<?> targetClass = AopUtils.getTargetClass(targetObj);
            annotation = targetClass.getAnnotation(annotationClass);
        }

        return annotation;
    }
}

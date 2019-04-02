package org.jumpmind.pos.service.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import org.jumpmind.pos.service.instrumentation.ServiceSample;
import org.jumpmind.pos.service.model.ModuleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClassUtils extends org.jumpmind.pos.util.ClassUtils {
    
    private ClassUtils() {
    }
    
    protected static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    public static List<Class<?>> getClassesForPackageAndAnnotation(String packageName, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = Arrays.asList(new Class[] {ModuleModel.class, ServiceSample.class});

        return org.jumpmind.pos.util.ClassUtils.getClassesForPackageAndAnnotation(packageName, annotation, classes, null);
    }
}

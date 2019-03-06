package org.jumpmind.pos.util.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * As long as the {@link SpringBootEnvironmentPostProcessor} is enabled, 
 * and the annotation is defined on a class in the {@code org.jumpmind.pos} package hierarchy,
 * the properties defined in yaml resource(s) specified on the annotation will be 
 * loaded into the Spring environment. The properties loaded
 * will be loaded last (meaning they will have the lowest precedence) after all 
 * other Spring environment resources have been processed.  For example, environment
 * properties defined in the application.yml will take precedence over those
 * specified through a {@link YamlPropertySource}.  This allows for overriding
 * of environment properties defined in a YamlPropertySource by those defined in 
 * property sources that have higher precedence, such as the application.yaml.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface YamlPropertySource {
    public String[] value();
}

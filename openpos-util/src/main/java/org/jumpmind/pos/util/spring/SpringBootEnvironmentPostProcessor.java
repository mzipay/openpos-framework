package org.jumpmind.pos.util.spring;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jumpmind.pos.util.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * OpenPOS hook into the Spring boot environment loading mechanism that
 * allows for augmentation of the Spring environment.  Current functionality
 * scans for all classes under package {@code org.jumpmind.pos} that have a
 * {@link YamlPropertySource} annotation on them and attempts to load the properties
 * defined in yaml resource(s) into the Spring environment. The properties loaded
 * will be loaded last (meaning they will have the lowest precedence) after all 
 * other Spring environment resources have been processed.  For example, environment
 * properties defined in the application.yml will take precedence over those
 * specified through a {@link YamlPropertySource}.  This allows for overriding
 * of environment properties defined in a YamlPropertySource by those defined in 
 * property sources that have higher precedence, such as the application.yaml.
 * Processing of all YamlPropertySources can be disabled by setting 
 * {@code openpos.yamlPropertySourceLoadingEnabled=false}.
 * <br/>
 * <br/>
 * Note: In order for this EnvironmentPostProcessor to run, it must be specified
 * in the {@code META-INF/spring.factories} file:<br/><br/>
 * {@code org.springframework.boot.env.EnvironmentPostProcessor=org.jumpmind.pos.util.spring.SpringBootEnvironmentPostProcessor}
 *
 */
public class SpringBootEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        boolean yamlPropertySourceLoadingEnabled = environment
            .getProperty("openpos.yamlPropertySourceLoadingEnabled", 
            Boolean.class, true
        );
        if (yamlPropertySourceLoadingEnabled) {
            loadYamlPropertySources(environment);
        }
    }
    
    protected void loadYamlPropertySources(ConfigurableEnvironment environment) {
        List<Class<?>> yamlPropertySourceClasses = ClassUtils.getClassesForPackageAndAnnotation("org.jumpmind.pos", YamlPropertySource.class);
        for (Class<?> c : yamlPropertySourceClasses) {
            String[] annotationValue = c.getAnnotation(YamlPropertySource.class).value();
            System.out.println(String.format("[%s] Found YamlPropertySource on %s. value=%s", this.getClass().getSimpleName(), c.getSimpleName(), Arrays.toString(annotationValue)));
            
            for (String resourceLoc: c.getAnnotation(YamlPropertySource.class).value()) {
                try {
                    Resource resource = resourceLoader.getResource(resourceLoc);
                    List<PropertySource<?>> propertySources = loadYaml(resource);
                    for (PropertySource<?> propertySource : propertySources) {
                        environment.getPropertySources().addLast(propertySource);    
                    }
                    
                } catch (Exception ex) {
                    System.out.println(String.format("[%s] WARN: The YamlPropertySource at '%s' could not be loaded. Reason: %s", this.getClass().getSimpleName(), resourceLoc, ex.getMessage()));
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<PropertySource<?>> loadYaml(Resource res) throws IOException {
        if (!res.exists()) {
            throw new IllegalArgumentException("Resource '" + res + "' does not exist");
        }
        
        try {
            // spring boot 2.x version
            Method method = loader.getClass().getMethod("load", String.class, Resource.class);
            return (List<PropertySource<?>>)method.invoke(loader, "custom-resource", res);
        } catch (Exception ex) {
            try {
                // spring boot 1.5 version
                Method method = loader.getClass().getMethod("load", String.class, Resource.class, String.class);
                List<PropertySource<?>> list = new ArrayList<>();
                list.add((PropertySource<?>)method.invoke(loader, "custom-resource", res, null));
                return list;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}

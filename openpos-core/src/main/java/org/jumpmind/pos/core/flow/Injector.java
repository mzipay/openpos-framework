package org.jumpmind.pos.core.flow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class Injector {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;
    
    public void performInjections(Object target, Scope scope,  Map<String, ScopeValue> extraScope) {
        performInjectionsImpl(target, scope, extraScope);
        performPostContruct(target);
    }
    
    protected void performInjectionsImpl(Object target, Scope scope,  Map<String, ScopeValue> extraScope) {
        Class<?> targetClass = target.getClass();
        while (targetClass != null) {
            performInjectionsImpl(targetClass, target, scope, extraScope);
            targetClass = targetClass.getSuperclass();
            if (targetClass == Object.class) {
                targetClass = null;
            }
        }
    }

    protected void performInjectionsImpl(Class<?> targetClass, Object target, Scope scope,  Map<String, ScopeValue> extraScope) {
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                String name = field.getName();
                ScopeValue value = scope.resolve(name);
                if (value == null && extraScope != null) {
                    value = extraScope.get(name);
                }

                if (value == null) {
                    if (applicationContext.containsBean(name)) {
                        value = new ScopeValue(applicationContext.getBean(name));
                    } else {
                        try {                            
                            Object beanByClass = applicationContext.getBean(field.getType());
                            if (beanByClass != null) {
                                value = new ScopeValue(beanByClass);
                            }
                        } catch (NoSuchBeanDefinitionException ex) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("No bean found", ex);
                            }
                        }
                    }
                }

                if (value != null) {
                    try {
                        field.set(target, value.getValue());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (autowired.required()) {
                    throw new FlowException("Failed to resolve required injection: " + name + " for " + target);
                }
            }
        }
    }
    
    protected void performPostContruct(Object target) {
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PostConstruct postConstructAnnotation = method.getAnnotation(PostConstruct.class);
            if (postConstructAnnotation != null) {
                method.setAccessible(true);
                try {
                    method.invoke(target);
                } catch (Exception ex) {
                    throw new FlowException("Failed to invoke @PostConstruct method " + method, ex);
                }
            }
        }
    }
}

package org.jumpmind.pos.core.flow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class Injector {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AutowireCapableBeanFactory applicationContext;
    
    public void performInjections(Object target, Scope scope,  StateContext currentContext, Map<String, ScopeValue> extraScope) {
        performInjectionsImpl(target, scope, currentContext, extraScope);
        performPostContruct(target);
    }
    
    protected void performInjectionsImpl(Object target, Scope scope, StateContext currentContext, Map<String, ScopeValue> extraScope) {
        Class<?> targetClass = target.getClass();
        applicationContext.autowireBean(target);
        while (targetClass != null) {
            performInjectionsImpl(targetClass, target, scope, currentContext, extraScope);
            targetClass = targetClass.getSuperclass();
            if (targetClass == Object.class) {
                targetClass = null;
            }
        }
    }

    protected void performInjectionsImpl(Class<?> targetClass, Object target, Scope scope, 
            StateContext currentContext, Map<String, ScopeValue> extraScope) {
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                String name = field.getName();
                
                ScopeValue value = currentContext.resolveScope(name); // flow scope
                if (value == null) {                    
                    value = scope.resolve(name); // conversation / session / node scope
                }
                if (value == null) {
                    Object configScopeValue = currentContext.getFlowConfig().getConfigScope() != null ?
                            currentContext.getFlowConfig().getConfigScope().get(name) : null; // config scope
                    if (configScopeValue != null) {
                        value = new ScopeValue(configScopeValue);
                    }
                }                
                if (value == null && extraScope != null) { // special injections such as the StateManager itself.
                    value = extraScope.get(name);
                }

                if (value != null) {
                    try {
                        field.set(target, value.getValue());
                    } catch (Exception ex) {
                        logger.error("", ex);
                    }
                } 
//                else if (autowired.required()) {
//                    throw failedToResolveInjection(field, name, targetClass, target, scope, currentContext, extraScope);
//                }
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
    
    private FlowException failedToResolveInjection(Field field, 
            String name, Class<?> targetClass, Object target, Scope scope,
            StateContext currentContext, Map<String, ScopeValue> extraScope) {
        
        StringBuilder buff = new StringBuilder();
        buff.append(String.format("Failed to resolve required injection '%s' for field %s\n", name, field));
        buff.append("Tried the following contexts:\n");
        buff.append(printScopeValues(scope, currentContext, extraScope));
        
        throw new FlowException(buff.toString());
    }  
    
    public String printScopeValues(Scope scope, StateContext currentContext, Map<String, ScopeValue> extraScope) {
        
        StringBuilder buff = new StringBuilder();
        
        buff.append(reportScope("FLOW SCOPE", currentContext.getFlowScope()));
        buff.append(reportScope("CONVERSATION SCOPE", scope.getConversationScope()));
        buff.append(reportScope("SESSION SCOPE", scope.getSessionScope()));
        buff.append(reportScope("NODE SCOPE", scope.getNodeScope()));
        buff.append(reportScope("SYSTEM SCOPE", extraScope));
        
        return buff.toString();
    }
    
    protected String reportScope(String scopeName, Map<String, ScopeValue> scope) {
        
        final int MAX_VALUE_WIDTH = 64;
        
        StringBuilder buff = new StringBuilder();
        
        buff.append(scopeName).append(":\n");
        if (scope != null) {
            if (scope.isEmpty()) {
                buff.append("\t<empty>\n");
            } else {
                for (Map.Entry<String, ScopeValue> entry : scope.entrySet()) {                    
                    buff.append("\t").append(entry.getKey()).append("=").append(StringUtils.abbreviate(entry.getValue().getValue().toString(), MAX_VALUE_WIDTH)).append("\n");
                }
            }
        }        
        
        return buff.toString();
    }
}

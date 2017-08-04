package org.jumpmind.jumppos.core.flow;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// TODO should be called just ActionHandler, need to repackage annotation of the same name.
@Component
@org.springframework.context.annotation.Scope("prototype")
public class ActionHandlerImpl {

//    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String METHOD_ON_ANY = "onAnyAction";

    public boolean handleAction(Object state, Action action) {
        Method[] methods = state.getClass().getDeclaredMethods();

        Method anyMethod = null;

        String actionName = action.getName();
        for (Method method : methods) {
            ActionHandler actionHandlerAnnotation = method.getAnnotation(ActionHandler.class);
            String matchingMethodName = "on" + actionName;
            if (actionHandlerAnnotation != null) {
                method.setAccessible(true);
                if (matchingMethodName.equals(method.getName())) {
                    invokeHandleAction(state, action, method);
                } else if (METHOD_ON_ANY.equals(method.getName())) {
                    anyMethod = method;
                }
            }
        }

        if (anyMethod != null) {            
            invokeHandleAction(state, action, anyMethod);
            return true;
        }

        return false;

    }

    protected void invokeHandleAction(Object state, Action action, Method method) {
        try {
            method.invoke(state, action); // TODO allow for some flexibility in args.
        } catch (Exception ex) {
            throw new FlowException("Failed to invoke method " + method, ex);
        }      
    }

}

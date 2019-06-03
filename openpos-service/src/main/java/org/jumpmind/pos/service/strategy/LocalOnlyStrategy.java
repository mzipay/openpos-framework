package org.jumpmind.pos.service.strategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.EndpointOverride;
import org.jumpmind.pos.service.InjectionContext;
import org.jumpmind.pos.service.PosServerException;
import org.jumpmind.pos.service.ServiceSpecificConfig;
import org.jumpmind.pos.util.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component(LocalOnlyStrategy.LOCAL_ONLY_STRATEGY)
public class LocalOnlyStrategy extends AbstractInvocationStrategy implements IInvocationStrategy {

    static final String LOCAL_ONLY_STRATEGY = "LOCAL_ONLY";

    @Autowired
    Environment env;

    Map<String, Object> endPointsByPath;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (endPointsByPath == null) {
            endPointsByPath = new HashMap<>();
            Collection<Object> beans = applicationContext.getBeansWithAnnotation(RestController.class).values();
            if (beans != null) {
                Collection<Object> endpointOverrides = applicationContext.getBeansWithAnnotation(EndpointOverride.class).values();
                Collection<Object> endpoints = applicationContext.getBeansWithAnnotation(Endpoint.class).values();
                for (Object object : beans) {
                    Class<?>[] interfaces = object.getClass().getInterfaces();
                    for (Class<?> i : interfaces) {
                        RestController controller = i.getAnnotation(RestController.class);
                        if (controller != null) {
                            String serviceName = controller.value();
                            String implementation = env
                                    .getProperty(String.format("openpos.services.specificConfig.%s.implementation", serviceName), "default");
                            log.info("Loading endpoints for the '{}' implementation of {}({})", implementation, i.getSimpleName(),
                                    serviceName);
                            Method[] methods = i.getMethods();
                            for (Method method : methods) {
                                String path = buildPath(method);
                                for (Object overridenBean : endpointOverrides) {
                                    EndpointOverride override = ClassUtils.resolveAnnotation(EndpointOverride.class, overridenBean);
                                    if (override.path().equals(path) && override.implementation().equals(implementation)) {
                                        endPointsByPath.put(path, overridenBean);
                                        break;
                                    }
                                }

                                if (!endPointsByPath.containsKey(path)) {
                                    Object endpointBean = findMatch(path, endpoints, implementation);
                                    if (endpointBean == null) {
                                        endpointBean = findMatch(path, endpoints, "default");
                                    }
                                    
                                    if (endpointBean != null) {
                                            endPointsByPath.put(path, endpointBean);
                                    } else {
                                        log.warn(String.format(
                                                "No endpoint defined for path '%s' in '%s' in the service Please define a Spring-discoverable @Endpoint class, "
                                                        + "with a method annotated like  @Endpoint(\"%s\")",
                                                path, i.getSimpleName(), path));                                        
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected Object findMatch(String path, Collection<Object> endpoints, String implementation ) {
        for (Object endpointBean : endpoints) {
            Endpoint endPoint = ClassUtils.resolveAnnotation(Endpoint.class, endpointBean);
            if (endPoint.path().equals(path) && endPoint.implementation().equals(implementation)) {
                return endpointBean;
            }
        }
        return null;
    }

    public String getStrategyName() {
        return LOCAL_ONLY_STRATEGY;
    }

    @Override
    public Object invoke(ServiceSpecificConfig config, Object proxy, Method method, Object[] args) throws Throwable {
        String path = buildPath(method);
        Object obj = endPointsByPath.get(path);
        if (obj != null) {
            endpointInjector.performInjections(obj, new InjectionContext(args));
            Method targetMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (targetMethod != null) {
                try {
                    return targetMethod.invoke(obj, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        }

        throw new PosServerException(String.format("No endpoint found for path '%s' Please define a Spring-discoverable @Component class, "
                + "with a method annotated like  @Endpoint(\"%s\")", path, path));
    }

}

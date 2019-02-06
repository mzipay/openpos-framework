package org.jumpmind.pos.service.strategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.service.ServiceSpecificConfig;
import org.jumpmind.pos.util.web.ConfiguredRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component(RemoteOnlyStrategy.REMOTE_ONLY_STRATEGY)
public class RemoteOnlyStrategy extends AbstractInvocationStrategy implements IInvocationStrategy {
    
    static final String REMOTE_ONLY_STRATEGY = "REMOTE_ONLY";
    
    public String getStrategyName() {
        return REMOTE_ONLY_STRATEGY;
    }

    @Override
    public Object invoke(ServiceSpecificConfig config, Object proxy, Method method, Object[] args) throws Throwable {
        int httpTimeoutInSecond = config.getHttpTimeout();
        ConfiguredRestTemplate template = new ConfiguredRestTemplate(httpTimeoutInSecond);
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods != null && requestMethods.length > 0) {
            HttpMethod requestMethod = translate(requestMethods[0]);
            String url = buildUrl(config, proxy, method, args);
            Object requestBody = findRequestBody(method, args);
            Object[] newArgs = findArgs(method, args);
            if (method.getReturnType().equals(Void.TYPE)) {
                template.execute(url, requestBody, requestMethod, newArgs);
            } else {
                return template.execute(url, requestBody, method.getReturnType(), requestMethod, newArgs);
            }
        } else {
            throw new IllegalStateException("A method must be specified on the @RequestMapping");
        }
        return null;
    }
    
    protected String buildUrl(ServiceSpecificConfig config, Object proxy, Method method, Object[] args) {
        String url = config.getUrl();
        String path = buildPath(method);
        url = String.format("%s%s", url, path);
        return url;
    }
    
    protected Object[] findArgs(Method method, Object[] args) {
        List<Object> newArgs = new ArrayList<>();
        Annotation[][] types = method.getParameterAnnotations();
        for (int i = 0; i < types.length; i++) {
            Annotation[] argAnnotations = types[i];
            for (Annotation annotation : argAnnotations) {
                if (PathVariable.class.equals(annotation.annotationType()) || RequestParam.class.equals(annotation.annotationType())) {
                    newArgs.add(args[i]);
                }
                
            }
        }
        return newArgs.toArray();
    }
    
    protected  Object findRequestBody(Method method, Object[] args) {
        Annotation[][] types = method.getParameterAnnotations();
        for (int i = 0; i < types.length; i++) {
            Annotation[] argAnnotations = types[i];
            for (Annotation annotation : argAnnotations) {
                if (RequestBody.class.equals(annotation.annotationType())) {
                    return args[i];
                }
                
            }
        }
           
        return null;
    }
    
    protected HttpMethod translate(RequestMethod method) {
        return HttpMethod.valueOf(method.name());
    }

}

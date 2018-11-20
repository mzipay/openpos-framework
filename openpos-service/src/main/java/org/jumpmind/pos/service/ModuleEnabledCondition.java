package org.jumpmind.pos.service;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ModuleEnabledCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean include = true;
        Environment env = context.getEnvironment();
        if (env != null) {
            String includedModules = env.getProperty("include.modules");
            if (isNotBlank(includedModules)) {
                Map<String, Object> attrs = metadata.getAnnotationAttributes(Configuration.class.getName());
                include = includedModules.contains((String) attrs.get("value"));
            }

        }
        return include;
    }

}
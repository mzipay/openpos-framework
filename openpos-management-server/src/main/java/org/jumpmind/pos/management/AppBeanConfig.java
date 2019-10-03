package org.jumpmind.pos.management;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeanConfig {

    @Bean
    public ScriptEngine groovyScriptEngine() {
        return new ScriptEngineManager().getEngineByName("groovy"); 
    }

}

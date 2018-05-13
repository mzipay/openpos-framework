package org.jumpmind.pos.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // provide access to static resources in /src/main/resources/static, such as svg files.
        // SVG files can't be accessed when running the client app standalone as
        // javascript app (not served), so we serve them up from the spring-boot server for now
        registry.addResourceHandler("/**").addResourceLocations("/", "classpath:/static/");
    }
}

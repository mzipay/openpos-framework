package org.jumpmind.pos.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(
        basePackages = { "org.jumpmind.pos.test" })
@PropertySource(value = { "classpath:test.properties" })
public class TestConfig {

}

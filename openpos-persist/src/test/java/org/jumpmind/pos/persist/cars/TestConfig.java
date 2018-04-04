package org.jumpmind.pos.persist.cars;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = { "org.jumpmind.pos", "org.jumpmind.pos.core", "org.jumpmind.pos.app" })
public class TestConfig {

}

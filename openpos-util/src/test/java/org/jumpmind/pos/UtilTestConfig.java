package org.jumpmind.pos;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(
    basePackages = { "org.jumpmind.pos.test" }
)
@EnableConfigurationProperties
public class UtilTestConfig {

}

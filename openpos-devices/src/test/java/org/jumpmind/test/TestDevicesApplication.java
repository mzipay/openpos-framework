package org.jumpmind.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = { "org.jumpmind.pos"})
@PropertySource(value = { "classpath:test-devices.properties"})
public class TestDevicesApplication {

}

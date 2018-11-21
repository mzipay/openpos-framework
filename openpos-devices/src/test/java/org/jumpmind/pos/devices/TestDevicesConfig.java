package org.jumpmind.pos.devices;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = { "org.jumpmind.pos.devices", "org.jumpmind.pos.service", "org.jumpmind.pos.persist" })
@PropertySource(value = { "classpath:test-devices.properties"})
public class TestDevicesConfig {

}

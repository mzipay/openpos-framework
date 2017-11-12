package org.jumpmind.jumppos.core.config;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MutableBoolean initialized() {
        return new MutableBoolean(false);
    }
}

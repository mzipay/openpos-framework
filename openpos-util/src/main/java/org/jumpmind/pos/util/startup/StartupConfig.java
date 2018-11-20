package org.jumpmind.pos.util.startup;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

    @Bean
    public MutableBoolean initialized() {
        return new MutableBoolean(false);
    }
}

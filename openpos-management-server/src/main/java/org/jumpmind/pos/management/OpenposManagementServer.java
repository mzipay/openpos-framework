package org.jumpmind.pos.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class OpenposManagementServer {
    public static void main(String[] args) throws Exception {
        
        // TODO: write PID to file.  See https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-process-monitoring.html
        SpringApplication.run(OpenposManagementServer.class, args);
    }
}

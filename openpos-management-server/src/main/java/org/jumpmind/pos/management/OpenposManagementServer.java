package org.jumpmind.pos.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class OpenposManagementServer {
    public static void main(String[] args) {
        // TODO: write PID to file.  See https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-process-monitoring.html
        SpringApplication.run(OpenposManagementServer.class, args);
    }
}

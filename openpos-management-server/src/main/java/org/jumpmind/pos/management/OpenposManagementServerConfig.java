package org.jumpmind.pos.management;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="openpos.management-server")
@Data
public class OpenposManagementServerConfig {

    @NotNull
    private String mainWorkDirPath;
    private ClientConnect clientConnect;
    private DeviceProcess deviceProcess;

    @Data
    public static class ClientConnect {
        private String hostname;
        private String webServiceBaseUrlTemplate;
        private String secureWebServiceBaseUrlTemplate;
        private String webSocketBaseUrlTemplate;
        private String secureWebSocketBaseUrlTemplate;
    }
    
    @Data
    public static class DeviceProcess {
        public static final String AUTO_PORT_ALLOCATION = "AUTO";
        public static final String DEFAULT_PROCESS_PORT_ARG_TEMPLATE = "-Dserver.port=%d";
        public static final String DEFAULT_JAVA_REMOTE_DEBUG_ARG_TEMPLATE = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=%d";
        public static final String DEFAULT_PROCESS_LOG_FILENAME = "process.log";

        private String initializationScript;
        private long startMaxWaitMillis = 60000;
        @NotNull 
        private String statusUrlTemplate;
        private long statusMaxWaitMillis = 1000;
        private long statusCheckPeriodMillis = 7500;
        @NotNull @Value("${java.home}")
        private String javaExecutablePath;

        private String[] classpathEntries = new String[0];
        private String[] additionalJavaArguments = new String[0];
        private String executableJarPath;
        private String mainClass;
        private String processPort = AUTO_PORT_ALLOCATION;
        private String processPortArgTemplate = DEFAULT_PROCESS_PORT_ARG_TEMPLATE;
        private String[] processArguments = new String[0];
        private String processLogFilePath = DEFAULT_PROCESS_LOG_FILENAME;
        private String javaRemoteDebugPort;
        private String javaRemoteDebugArgTemplate = DEFAULT_JAVA_REMOTE_DEBUG_ARG_TEMPLATE;
    }
    
}

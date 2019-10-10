package org.jumpmind.pos.management;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.management.OpenposManagementServerConfig.JavaExecutableConfig;
import org.jumpmind.pos.util.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SocketUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JavaProcessCommandBuilderImpl implements ProcessCommandBuilder {
    @Autowired
    OpenposManagementServerConfig config;

    @Override
    public List<String> constructProcessCommandParts(DeviceProcessInfo pi) {
        JavaExecutableConfig javaProcessCfg = config.getDeviceProcessConfig(pi).getJavaExecutableConfig();
        String resolvedJavaPath = resolveJavaExecutablePath(javaProcessCfg);
        log.debug("Java path resolved: {}", resolvedJavaPath);
        
        String classpath = constructClasspath(javaProcessCfg);
        log.debug("Classpath is: [{}]", classpath);

        Integer port = allocateProcessPort(javaProcessCfg);
        if (null == port) {
            throw new DeviceProcessLaunchException(
                String.format("Failed to allocate a port for Device Process '%s', " +
                    "can't launch.", pi.getDeviceId())
            );
        }
        log.info("Assigning port {} to Device Process '{}'", port, pi.getDeviceId());

        List<String> generatedAdditionalArgs = new ArrayList<>();
        generatedAdditionalArgs.add(String.format(javaProcessCfg.getProcessPortArgTemplate(), port));
        pi.setPort(port);
        
        if (StringUtils.isNotBlank(javaProcessCfg.getJavaRemoteDebugPort())) {
           Integer debugPort = allocateDebugPort(javaProcessCfg);
           if (debugPort != null) {
               generatedAdditionalArgs.add(String.format(javaProcessCfg.getJavaRemoteDebugArgTemplate(), debugPort));
           } else {
               log.warn("Failed to allocate a debug port for Device Process '%s', " +
                   "remote debugging won't be available.", pi.getDeviceId());
           }
        }
        
        String[] additionalArgs = constructAdditionalJavaArguments(
            javaProcessCfg,
            generatedAdditionalArgs.toArray(new String[0])
        );
        
        log.debug("Additional Java arguments: [{}]", String.join(" ", additionalArgs));
        
        String[] processArgs = constructProcessArguments(javaProcessCfg);
        log.debug("Process arguments: [{}]", String.join(" ", processArgs));

        List<String> commandLineArgs = new ArrayList<>();
        commandLineArgs.add(resolvedJavaPath);
        if (StringUtils.isNotBlank(classpath)) {
            commandLineArgs.add("-cp");
            commandLineArgs.add(classpath);
        }
        if (ArrayUtils.isNotEmpty(additionalArgs)) {
            commandLineArgs.addAll(Arrays.asList(additionalArgs));
        }
        if (StringUtils.isNotBlank(javaProcessCfg.getExecutableJarPath())) {
            commandLineArgs.add("-jar");
            commandLineArgs.add(javaProcessCfg.getExecutableJarPath());
        } else {
            if (StringUtils.isNotBlank(javaProcessCfg.getMainClass())) {
                commandLineArgs.add(javaProcessCfg.getMainClass());
            }
        }
        if (ArrayUtils.isNotEmpty(processArgs)) {
            commandLineArgs.addAll(Arrays.asList(processArgs));
        }
        
        return commandLineArgs;
    }

    protected String[] constructProcessArguments(JavaExecutableConfig javaProcessCfg) {
        return javaProcessCfg.getProcessArguments();
    }
    
    protected String resolveJavaExecutablePath(JavaExecutableConfig javaProcessCfg) {
        String javaExecutablePath = javaProcessCfg.getJavaExecutablePath();
        File javaPath = new File(javaExecutablePath);
        if (javaPath.exists() && javaPath.isFile()) {
            if (! javaPath.getPath().toLowerCase().endsWith("java") && 
                ! javaPath.getPath().toLowerCase().endsWith("java.exe")) {
                throw new DeviceProcessLaunchException("When the value of javaExecutablePath is a file, it is "
                        + "expected to be an executable named 'java'.");
            }
        } else {  // it's a directory, check for java exe in current dir or bin subdir
            javaPath = new File(javaExecutablePath, "java");
            
            if (! javaPath.exists() || ! javaPath.isFile()) {
                javaPath = new File(new File(javaExecutablePath, "bin"), "java");
                if (! javaPath.exists() || ! javaPath.isFile()) {
                    throw new DeviceProcessLaunchException(
                        String.format("Could not find a java exectuable at '%s/java' or '%s/bin/java'.", 
                            javaExecutablePath, javaExecutablePath)
                    ); 
                }
            }
        }
        
        if (! javaPath.canExecute()) {
            log.warn("Resolved java executable at '{}' does not "
                    + "appear to be executable.", javaPath.getAbsolutePath());
        }
        
        return javaPath.getAbsolutePath();
    }

    protected String constructClasspath(JavaExecutableConfig javaProcessCfg) {
        String cp = String.join(File.pathSeparator, javaProcessCfg.getClasspathEntries());
        return cp;
    }
    
    protected String[] constructAdditionalJavaArguments(JavaExecutableConfig javaProcessCfg, String...moreArgs) {
        return ArrayUtils.addAll(moreArgs, javaProcessCfg.getAdditionalJavaArguments());
    }
    
    protected Integer allocateDebugPort(JavaExecutableConfig javaProcessCfg) {
        return allocatePort("processRemoteDebugPort", javaProcessCfg.getJavaRemoteDebugPort());
    }
    
    protected Integer allocateProcessPort(JavaExecutableConfig javaProcessCfg) {
        return allocatePort("processPort", javaProcessCfg.getProcessPort());
    }
    
    protected Integer allocatePort(String portParameterName, String portParameterValue) {
        if (StringUtils.isEmpty(portParameterValue)) {
            return null; 
         }
         
         Integer allocatedPort = null;
         if (portParameterValue.contains("-") || portParameterValue.contains(",")) {
             // We have a range or multiple ports/ranges
             if (portParameterValue.contains(",")) {
                 // Iterate over the ports until a free one is found
                 String[] valuesOrRanges = portParameterValue.split(",");
                 for(String valueOrRange : valuesOrRanges) {
                     allocatedPort = findAvailablePort(valueOrRange.trim(), portParameterName);
                     if (allocatedPort != null) {
                         break;
                     }
                 }
             } else {
                 allocatedPort = findAvailablePort(portParameterValue, portParameterName);
             }
         } else if (portParameterValue.equalsIgnoreCase(OpenposManagementServerConfig.JavaExecutableConfig.AUTO_PORT_ALLOCATION)) {
             allocatedPort = SocketUtils.findAvailableTcpPort();
         } else {
             try {
                 allocatedPort = Integer.parseInt(portParameterValue);
             } catch (Exception ex) {
                 throw new DeviceProcessLaunchException(
                     String.format("Cannot convert %s value of '%s' to an integer. Reason: %s", 
                         portParameterName, portParameterValue, ex.getMessage())
                 );
             }
             
             // Now check if it the port is in use
             if (! NetworkUtils.isTcpPortAvailable(allocatedPort)) {
                 throw new DeviceProcessLaunchException(
                     String.format("Port %d specified by the %s parameter is already in use.", 
                             allocatedPort, portParameterName));
             }
         }
         
         if (allocatedPort == null) {
             throw new DeviceProcessLaunchException(
                 String.format("Failed to find an available port in the values/ranges "
                    + "specified by the %s parameter: [%s].", portParameterName, portParameterValue));
         }
         return allocatedPort;
        
    }
    
    protected Integer findAvailablePort(String valueOrRange, String portParameterName) {
        if (null == valueOrRange) {
            log.warn("Given port valueOrRange is null");
            return null;
        }
        
        Integer availablePort = null;
        if (valueOrRange.contains("-")) {
            String[] split = valueOrRange.split("-");
            if (split != null && split.length > 1) {
                try {
                    Integer minPort = Integer.parseInt(split[0].trim());
                    Integer maxPort = Integer.parseInt(split[1].trim());
                    availablePort = SocketUtils.findAvailableTcpPort(minPort, maxPort);
                } catch (Exception ex) {
                    log.warn("Could not find a port in the range of '{}' from the {} parameter. Reason: {}", valueOrRange, portParameterName, ex.getMessage());
                }
            } else {
                log.warn("Could not convert port range of '{}' from the {} parameter to a range.", valueOrRange, portParameterName);
            }
        } else {
            Integer portToCheck = null;
            try { 
                portToCheck = Integer.parseInt(valueOrRange); 
                if (NetworkUtils.isTcpPortAvailable(portToCheck)) {
                    availablePort = portToCheck;
                }
            } catch (Exception ex) {
                log.warn("Could not convert port value of '{}' from the {} parameter to an integer", valueOrRange, portParameterName);
            }
        }
        
        return availablePort;
        
    }
    
}

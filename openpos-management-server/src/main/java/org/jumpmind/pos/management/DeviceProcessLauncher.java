package org.jumpmind.pos.management;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.util.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceProcessLauncher {
    
    @Autowired
    OpenposManagementServerConfig config;
    
    public DeviceProcessInfo launch(DeviceProcessInfo pi, long maxWaitMillis) throws DeviceProcessLaunchException {
        File processWorkingDir = createWorkingDirectory(pi.getDeviceId());
        log.info("Device Process '{}' working dir: {}", pi.getDeviceId(), processWorkingDir.getAbsolutePath());
        
        List<String> commandLine = constructProcessCommandParts(pi);
        log.debug("Device Process '{}' command line: {}", pi.getDeviceId(), String.join(" ", commandLine));
        
        ProcessBuilder processBuilder = constructProcessBuilder(pi, commandLine, processWorkingDir);
        
        Process process = startProcess(pi, processBuilder);
        pi.setProcess(process);
        
        return pi;
    }

    protected Process startProcess(DeviceProcessInfo pi, ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            if (process.isAlive()) {
                log.info("Device Process '{}' started", pi.getDeviceId());
                return process;
            } else {
                log.error("Launch failure for Device Process '{}'. Command line: {}", pi.getDeviceId(), String.join(" ", processBuilder.command()));
                throw new DeviceProcessLaunchException(
                        String.format("Device Process '%s' died unexpectedly.  See above for command line used.", pi.getDeviceId()));
            }
        } catch (IOException ex) {
            log.error("Launch failure for Device Process '{}'. Command line: {}", pi.getDeviceId(), String.join(" ", processBuilder.command()));
            throw new DeviceProcessLaunchException(
                String.format("Failed to launch Device Process for '%s'.  See above for command line used.", pi.getDeviceId()),
                ex
            );
        }
    }
    
    
    protected ProcessBuilder constructProcessBuilder(DeviceProcessInfo pi, List<String> commandLineParts, File workingDir) {
        ProcessBuilder builder = new ProcessBuilder(commandLineParts);
        builder.directory(workingDir).redirectErrorStream(true)
            .redirectInput(Redirect.INHERIT)
            .redirectOutput(new File(workingDir, config.getDeviceProcess().getProcessLogFilename()));
        return builder;
    }
    
    protected List<String> constructProcessCommandParts(DeviceProcessInfo pi) {
        String resolvedJavaPath = resolveJavaExecutablePath();
        log.debug("Java path resolved: {}", resolvedJavaPath);
        
        String classpath = constructClasspath();
        log.debug("Classpath is: [{}]", classpath);

        Integer port = allocateProcessPort();
        if (null == port) {
            throw new DeviceProcessLaunchException(
                String.format("Failed to allocate a port for Device Process '%s', " +
                    "can't launch.", pi.getDeviceId())
            );
        }
        log.info("Assigning port {} to Device Process '{}'", port, pi.getDeviceId());

        List<String> generatedAdditionalArgs = new ArrayList<>();
        generatedAdditionalArgs.add(String.format(config.getDeviceProcess().getProcessPortArgTemplate(), port));
        pi.setPort(port);
        
        if (StringUtils.isNotBlank(config.getDeviceProcess().getJavaRemoteDebugPort())) {
           Integer debugPort = allocateDebugPort();
           if (debugPort != null) {
               generatedAdditionalArgs.add(String.format(config.getDeviceProcess().getJavaRemoteDebugArgTemplate(), debugPort));
           } else {
               log.warn("Failed to allocate a debug port for Device Process '%s', " +
                   "remote debugging won't be available.", pi.getDeviceId());
           }
        }
        
        String additionalArgs = constructAdditionalJavaArguments(
            generatedAdditionalArgs.toArray(new String[0])
        );
        
        
        log.debug("Additional Java arguments: [{}]", additionalArgs);
        
        String processArgs = constructProcessArguments();
        log.debug("Process arguments: [{}]", processArgs);

        List<String> commandLineArgs = new ArrayList<>();
        commandLineArgs.add(resolvedJavaPath);
        if (StringUtils.isNotBlank(classpath)) {
            commandLineArgs.add("-cp");
            commandLineArgs.add(classpath);
        }
        if (StringUtils.isNotBlank(additionalArgs)) {
            commandLineArgs.add(additionalArgs);
        }
        if (StringUtils.isNotBlank(config.getDeviceProcess().getExecutableJarPath())) {
            commandLineArgs.add("-jar");
            commandLineArgs.add(config.getDeviceProcess().getExecutableJarPath());
        } else {
            if (StringUtils.isNotBlank(config.getDeviceProcess().getMainClass())) {
                commandLineArgs.add(config.getDeviceProcess().getMainClass());
            }
        }
        if (StringUtils.isNotBlank(processArgs)) {
            commandLineArgs.add(processArgs);
        }
        
        return commandLineArgs;
        
    }
    
    protected File createWorkingDirectory(String deviceId) {
        File mainWorkDir = new File(config.getMainWorkDirPath());
        File deviceProcessWorkDir = new File(mainWorkDir, deviceId);
        
        if (! deviceProcessWorkDir.exists()) {
            boolean success = false;
            Exception createDirEx = null;
            try {
                success = deviceProcessWorkDir.mkdirs();
            } catch (Exception ex) {
                success = false;
                createDirEx = ex;
            }
            if (! success) {
                throw new DeviceProcessLaunchException(
                    String.format("Failed to create Device Process working directory for device '%s' at location '%s'", 
                        deviceId, deviceProcessWorkDir.getAbsolutePath()
                    ), 
                    createDirEx
                );
            }
        }
        
        return deviceProcessWorkDir;
    }
    
    protected Integer allocateDebugPort() {
        return allocatePort("processRemoteDebugPort", config.getDeviceProcess().getJavaRemoteDebugPort());
    }
    
    protected Integer allocateProcessPort() {
        return allocatePort("processPort", config.getDeviceProcess().getProcessPort());
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
         } else if (portParameterValue.equalsIgnoreCase(OpenposManagementServerConfig.DeviceProcess.AUTO_PORT_ALLOCATION)) {
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
    protected String constructClasspath() {
        String cp = String.join(File.pathSeparator, config.getDeviceProcess().getClasspathEntries());
        return cp;
    }
    
    protected String constructAdditionalJavaArguments(String...moreArgs) {
        String additionalArgs = String.join(" ", ArrayUtils.addAll(moreArgs, config.getDeviceProcess().getAdditionalJavaArguments()));
        return additionalArgs;
    }

    protected String constructProcessArguments() {
        String processArgs = String.join(" ", config.getDeviceProcess().getProcessArguments());
        return processArgs;
    }
    
    protected String resolveJavaExecutablePath() {
        String javaExecutablePath = config.getDeviceProcess().getJavaExecutablePath();
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
}

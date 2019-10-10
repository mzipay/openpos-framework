package org.jumpmind.pos.management;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import javax.script.ScriptEngine;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.management.OpenposManagementServerConfig.DeviceProcessConfig;
import org.jumpmind.pos.util.StreamCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceProcessLauncher {
    private static final String END_OF_PROCESS_LOG_FOOTER = "\n\n" +
        "======================================================================\n" +
        "   END OF OUTPUT, see Device instance logs for remainder of logging\n" +
        "======================================================================";
    
    @Autowired
    OpenposManagementServerConfig config;
    
    @Autowired
    ScriptEngine groovyScriptEngine;
    
    @Autowired 
    private ApplicationContext appContext;
    
    @Autowired
    ProcessCommandBuilderFactory processCmdFactory;

    
    public DeviceProcessInfo launch(DeviceProcessInfo pi, long maxWaitMillis) throws DeviceProcessSetupException, DeviceProcessLaunchException {
        initializeWorkingDir(pi);
        File workingDir = getWorkingDir(pi.getDeviceId());
        ProcessCommandBuilder commandBuilder = processCmdFactory.make(config.getDeviceProcessConfig(pi));
        
        List<String> commandLine = commandBuilder.constructProcessCommandParts(pi);
        log.debug("Device Process '{}' command line: {}", pi.getDeviceId(), String.join(" ", commandLine));
        
        ProcessBuilder processBuilder = constructProcessBuilder(pi, commandLine, workingDir);
        
        Process process = startProcess(pi, workingDir, processBuilder);
        pi.setProcess(process);
        
        return pi;
    }

    protected void initializeWorkingDir(DeviceProcessInfo pi) throws DeviceProcessSetupException {
        if (isWorkingDirPresent(pi.getDeviceId())) {
            return;
        }

        File processWorkingDir = createWorkingDirectory(pi.getDeviceId());
        DeviceProcessConfig deviceProcessCfg = config.getDeviceProcessConfig(pi);
        
        if (StringUtils.isNotBlank(deviceProcessCfg.getInitializationScript())) {
            if (groovyScriptEngine == null) {
                processWorkingDir.delete();
                throw new DeviceProcessSetupException(
                    String.format("Failed to create groovy script engine when " +
                        "attempting to run initialization script '%s'. Is groovy on the classpath?", 
                        deviceProcessCfg.getInitializationScript()
                    )
                );
            }
 
            boolean executeFinished = false;
            Resource scriptRes = appContext.getResource(deviceProcessCfg.getInitializationScript());
            if (! scriptRes.exists()) {
                throw new DeviceProcessSetupException(
                    String.format("The Device Process '%s' initialization script '%s' was not found at the given location.", 
                        pi.getDeviceId(),
                        deviceProcessCfg.getInitializationScript()
                    )
                );
            }
            
            try (
                InputStream scriptStream = scriptRes.getInputStream();
                Reader scriptReader = new InputStreamReader(scriptStream);
            ) {
                try {
                    groovyScriptEngine.put("deviceId", pi.getDeviceId());
                    groovyScriptEngine.put("workingDir", processWorkingDir);
                    groovyScriptEngine.put("config", config);
                    groovyScriptEngine.put("log", log);
                    log.info("Executing Device Process '{}' initialization script '{}'...", 
                        pi.getDeviceId(), deviceProcessCfg.getInitializationScript());
                    Object scriptResult = groovyScriptEngine.eval(scriptReader);
                    executeFinished = true;
                    log.info("Device Process '{}' initialization script completed with result: {}", 
                            pi.getDeviceId(), scriptResult);

                } catch (Exception ex) {
                    throw new DeviceProcessSetupException(
                        String.format("A failure occurred when executing Device Process '%s' initialization script '%s'.", 
                            pi.getDeviceId(),
                            deviceProcessCfg.getInitializationScript()
                        ),
                        ex
                    );
                }
            } catch (DeviceProcessSetupException dpex) {
                throw dpex;
            } catch (IOException e) {
                throw new DeviceProcessSetupException(
                    String.format("A failure occurred when loading Device Process '%s' initialization script '%s'.", 
                        pi.getDeviceId(),
                        deviceProcessCfg.getInitializationScript()
                    ),
                    e
                );
            } finally {
                if (!executeFinished) {
                    processWorkingDir.delete();
                }
            }
        }
    }

    protected Process startProcess(DeviceProcessInfo pi, File workingDir, ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            StreamCopier streamCopier = new StreamCopier(
                pi.getDeviceId(),
                process.getInputStream(),
                new FileOutputStream(new File(workingDir, config.getDeviceProcessConfig(pi).getProcessLogFilePath())),
                true,
                os -> {try {os.write(END_OF_PROCESS_LOG_FOOTER.getBytes());} catch (Exception ex) {log.warn("", ex);}}
            );
            streamCopier.start();
            pi.setStreamCopier(streamCopier);
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
        File processLogfile = new File(workingDir, config.getDeviceProcessConfig(pi).getProcessLogFilePath());
        if (! processLogfile.exists()) {
            if (! processLogfile.getParentFile().exists()) {
                if (processLogfile.getParentFile().mkdirs()) {
                    log.info("Created directory '{}'", processLogfile.getParentFile().getAbsolutePath());
                }
            }
        }
        
        ProcessBuilder builder = new ProcessBuilder(commandLineParts);
        builder.directory(workingDir).redirectErrorStream(true)
            .redirectInput(Redirect.INHERIT);
        return builder;
    }
    
    
    public File getWorkingDir(String deviceId) {
        File mainWorkDir = new File(config.getMainWorkDirPath());
        File deviceProcessWorkDir = new File(mainWorkDir, deviceId);
        
        return deviceProcessWorkDir;
    }
    
    public boolean isWorkingDirPresent(String deviceId) {
        return getWorkingDir(deviceId).exists();
    }

    public boolean isNotWorkingDirPresent(String deviceId) {
        return ! isWorkingDirPresent(deviceId);
    }
    
    protected File createWorkingDirectory(String deviceId) {
        File deviceProcessWorkDir = getWorkingDir(deviceId);
        
        if (! deviceProcessWorkDir.exists()) {
            boolean success = false;
            Exception createDirEx = null;
            try {
                success = deviceProcessWorkDir.mkdirs();
            } catch (Exception ex) {
                success = false;
                createDirEx = ex;
            }
            if (success) {
                log.info("Device Process '{}' working dir created at: {}", deviceId, deviceProcessWorkDir.getAbsolutePath());
            } else {
                throw new DeviceProcessSetupException(
                    String.format("Failed to create Device Process working directory for device '%s' at location '%s'", 
                        deviceId, deviceProcessWorkDir.getAbsolutePath()
                    ), 
                    createDirEx
                );
            }
        }
        
        return deviceProcessWorkDir;
    }
    
    
}

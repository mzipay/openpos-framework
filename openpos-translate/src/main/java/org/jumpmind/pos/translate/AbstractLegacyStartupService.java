package org.jumpmind.pos.translate;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.util.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

public abstract class AbstractLegacyStartupService implements ILegacyStartupService {
    private final static Logger logger = LoggerFactory.getLogger(AbstractLegacyStartupService.class);

    @Value("${external.process.enabled}")
    private boolean externalProcessEnabled;

    @Value("${orpos.headless.working.dir}")
    private String appWorkingDir;

    @Value("${rmi.registry.port}")
    private int rmiRegistryPort;

    /** Comma separated list of port numbers to use for remote debugging */
    @Value("${external.process.debug.ports:}")
    protected String remoteDebugPortValues;

    private String[] remoteDebugPorts = {};

    @Value("${prefix.classpath}")
    private String prefixClassPath;
    
    @Value("${postfix.classpath}")
    private String postfixClasspath;

    @Value("${library.path}")
    private String libraryPath;

    
    private int externalProcessCount = 0;

    private Registry sharedRegistry;

    private Map<String, ITranslationManager> translationManagers = new HashMap<>();
    
    public static String getDefaultServiceName(String storeId, String workstationId) {
        return String.format("%s/%s-%s", ITranslationManager.class.getSimpleName(), storeId, workstationId);
    }

    @PostConstruct
    protected void init() {
        if (this.isExternalProcessEnabled()) {
            try {
                this.setSharedRegistry(LocateRegistry.createRegistry(this.getRmiRegistryPort()));
            } catch (RemoteException e) {
                logger.error("Failed to create rmi regsistry", e);
            }

            if (StringUtils.isNotBlank(remoteDebugPortValues)) {
                this.setRemoteDebugPorts(remoteDebugPortValues.split(","));
            }
        }
    }

    @Override
    public void startPreviouslyStarted() {
        File workingDir = new File(this.getAppWorkingDir());
        if (workingDir.exists()) {
            File[] files = workingDir.listFiles();
            for (File file : files) {
                if (file.isDirectory() && Pattern.matches("\\d{5}-\\d{3}", file.getName())) {
                    String storeId = file.getName().substring(0, 5);
                    String workstationId = file.getName().substring(6);
                    if (this.isExternalProcessEnabled()) {
                        startExternal(file, storeId, workstationId);
                    } else {
                        startInternal(file, storeId, workstationId);
                    }
                }
            }
        }
    }

    @Override
    public void start(String nodeId) {
        String storeId = NodeUtils.parseStoreId(nodeId);
        String workstationId = NodeUtils.parseWorkstationId(nodeId);
        File file = new File(this.getAppWorkingDir(), nodeId);
        if (this.isExternalProcessEnabled()) {
            startExternal(file, storeId, workstationId);
        } else {
            startInternal(file, storeId, workstationId);
        }

    }

    protected abstract void startHeadless();
    protected abstract ITranslationManager createTranslationManagerServer();
    protected abstract Class<?> getHeadlessWorkstationProcessClass();
    protected abstract String getExternalHeadlessWorkstationProcessServiceName(String storeId, String workstationId);
    protected abstract void generateStoreProperties(String directory, String storeId, String workstationId);
    protected abstract Optional<String> getNodeWorkingSubdirectoryName();
    /**
     * Invoked after {@link AbstractLegacyStartupService#generateStoreProperties(String, String, String)} to allow for initialization
     * of other configuration files and data.
     */
    protected void initOtherHeadlessConfiguration(String directory, String storeId, String workstationId) {
    }
    
    protected void startExternal(File file, String storeId, String workstationId) {
        try {
            file.mkdirs();
            logger.info("Starting external process for store: {}, workstation: {}", storeId, workstationId);
            Optional<String> workingSubdir = this.getNodeWorkingSubdirectoryName();
            String workingDir = workingSubdir.isPresent() ? new File(file, workingSubdir.get()).getAbsolutePath() : file.getAbsolutePath();
            generateStoreProperties(workingDir, storeId, workstationId);
            initOtherHeadlessConfiguration(workingDir, storeId, workstationId);
            List<String> cmdLine = new ArrayList<>();
            String javaHome = System.getProperty("java.home");
            String os = System.getProperty("os.name");
            String classpath = System.getProperty("java.class.path");
            // add working dir to path
            
            classpath = workingDir  + java.io.File.pathSeparatorChar 
                            + (StringUtils.isNotEmpty(this.getPrefixClassPath()) ? (this.getPrefixClassPath()  + java.io.File.pathSeparatorChar):"")
                            + classpath
                            + (StringUtils.isNotEmpty(this.getPostfixClasspath()) ? (java.io.File.pathSeparatorChar) + this.getPostfixClasspath() :"");
                            
            logger.info("start POS with classpath: " + classpath );
            
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            if (os.toLowerCase().contains("win")) {
                javaBin += ".exe";
            }

            cmdLine.add(javaBin);
            
            String[] remoteDebugPorts = this.getRemoteDebugPorts();
            if (remoteDebugPorts.length > 0) {
                if (this.getExternalProcessCount() < remoteDebugPorts.length) {
                    String port = remoteDebugPorts[this.getExternalProcessCount()];
                    cmdLine.add(String.format("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=%s",port));
                    logger.debug("Assigned debug port {} to store/wkstn {}/{}", port, storeId, workstationId);
                } else {
                    logger.warn("Out of ports on the given list of ports via external.process.debug.ports, " +
                        "won't configure remote debugging for store/wkstn {}/{}", storeId, workstationId);
                }
            }
            
            cmdLine.add("-cp");
            if (classpath.toLowerCase().contains("100-openpos-server.jar")) {
                // one jar
                String[] paths = classpath.split(System.getProperty("path.separator"));
                StringBuilder newPath = new StringBuilder();
                for (String path : paths) {
                    newPath.append(new File(path).getAbsolutePath()).append(System.getProperty("path.separator"));
                }
                cmdLine.add(newPath.toString());
                cmdLine.add("org.springframework.boot.loader.JarLauncher");                
            } else {
                // running in IDE or development environment
                cmdLine.add(classpath);
            }
            
            if(StringUtils.isNotEmpty(libraryPath))
            {
            		cmdLine.add("-Djava.library.path=" + libraryPath);
            }
            
            cmdLine.add(this.getHeadlessWorkstationProcessClass().getName());

            ProcessBuilder pb = new ProcessBuilder(cmdLine);
            pb.redirectErrorStream(true);
            pb.directory(new File(workingDir));
            File redirectLogFile = new File(workingDir, "logs/process.log");
            redirectLogFile.getParentFile().mkdirs();
            pb.redirectOutput(redirectLogFile);
            logger.info("Starting process: {}", cmdLine);
            final Process process = pb.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (process.isAlive()) {
                    logger.info("Destroying child process {}" + file.getName());
                    process.destroy();
                }
            }));
            logger.info("Started process for {}", file.getName());
            if (process.isAlive()) {
                logger.info("Getting rmi interface for {}", file.getName());
                for (int i = 0; i < 15; i++) {
                    try {
                        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
                        factory.setServiceUrl(String.format("rmi://localhost:%d/%s", this.getRmiRegistryPort(),
                                this.getExternalHeadlessWorkstationProcessServiceName(storeId, workstationId)));
                        factory.setServiceInterface(ITranslationManager.class);
                        factory.afterPropertiesSet();
                        ITranslationManager remote = (ITranslationManager) factory.getObject();
                        remote.ping();
                        this.getTranslationManagers().put(file.getName(), remote);
                    } catch (RemoteAccessException e) {
                        this.getTranslationManagers().remove(file.getName());
                        logger.info("The remote interface was not available.  Trying again in a second");
                        Thread.sleep(1000);
                    }
                }
                this.incrementExternalProcessCount();
            } else {
                logger.warn("The launched process died unexpectly for {}", file.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to start process", e);
        }
    }

    protected void startInternal(File file, String storeId, String workstationId) {
        file.mkdirs();
        String userDir = System.getProperty("user.dir");
        String currentNodeDirParentName = file.getParentFile().getName();
        File appWorkingDir = new File(this.getAppWorkingDir());
        String appDirName = appWorkingDir.getName();
        try {
            currentNodeDirParentName = file.getParentFile().getCanonicalFile().getName();
            appDirName = appWorkingDir.getCanonicalFile().getName();
        } catch (IOException ex) {
            logger.warn("", ex);
        }
        if (currentNodeDirParentName.equals(appDirName)) {
            logger.info("Starting internal process for store: {}, workstation: {} in working dir: {}", storeId, workstationId, userDir);
            generateStoreProperties(userDir, storeId, workstationId);
            initOtherHeadlessConfiguration(userDir, storeId, workstationId);
            this.startHeadless();
            translationManagers.put(file.getName(), this.createTranslationManagerServer());
        } else {
            logger.warn("Could not start the internal process for store: {}, workstation: {} because the node working directory needs to be: {}",
                    storeId, workstationId, this.getAppWorkingDir() + "/" + file.getName() );
        }
    }
    
    @Override
    public ITranslationManager getTranslationManagerRef(String nodeId) {
        ITranslationManager translationManager = translationManagers.get(nodeId);
        logger.debug("Returning this translationManager for nodeId {}: {}", nodeId, translationManager);
        return translationManager;
    }

    public boolean isExternalProcessEnabled() {
        return externalProcessEnabled;
    }

    public void setExternalProcessEnabled(boolean externalProcessEnabled) {
        this.externalProcessEnabled = externalProcessEnabled;
    }

    protected Registry getSharedRegistry() {
        return sharedRegistry;
    }

    protected void setSharedRegistry(Registry sharedRegistry) {
        this.sharedRegistry = sharedRegistry;
    }

    protected int getRmiRegistryPort() {
        return rmiRegistryPort;
    }

    protected void setRmiRegistryPort(int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }

    protected String[] getRemoteDebugPorts() {
        return remoteDebugPorts;
    }

    protected void setRemoteDebugPorts(String[] remoteDebugPorts) {
        this.remoteDebugPorts = remoteDebugPorts;
    }

    protected String getAppWorkingDir() {
        return appWorkingDir;
    }

    protected void setAppWorkingDir(String appWorkingDir) {
        this.appWorkingDir = appWorkingDir;
    }

    protected int getExternalProcessCount() {
        return externalProcessCount;
    }

    protected void setExternalProcessCount(int externalProcessCount) {
        this.externalProcessCount = externalProcessCount;
    }
    
    protected void incrementExternalProcessCount() {
        this.externalProcessCount++;
    }

    protected Map<String, ITranslationManager> getTranslationManagers() {
        return translationManagers;
    }

    protected void setTranslationManagers(Map<String, ITranslationManager> translationManagers) {
        this.translationManagers = translationManagers;
    }

    protected String getPrefixClassPath() {
        return prefixClassPath;
    }

    protected void setPrefixClassPath(String prefixClassPath) {
        this.prefixClassPath = prefixClassPath;
    }

    protected String getPostfixClasspath() {
        return postfixClasspath;
    }

    protected void setPostfixClasspath(String postfixClasspath) {
        this.postfixClasspath = postfixClasspath;
    }

	protected String getLibraryPath() {
		return libraryPath;
	}

	protected void setLibraryPath(String libraryPath) {
		this.libraryPath = libraryPath;
	}

}

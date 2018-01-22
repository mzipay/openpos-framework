package org.jumpmind.pos.wrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.Scanner;

import org.jumpmind.symmetric.wrapper.WrapperHelper;

public class Wrapper {

    protected static final String SYS_CONFIG_DIR = "org.jumpmind.pos.config.dir";
    protected static final String OPENPOS_HOME = "OPENPOS_HOME";

    public static void runServiceWrapper(String[] args) throws Exception {

        String configFileName = "openpos_service.conf";
        ProtectionDomain protectionDomain = Wrapper.class.getProtectionDomain();
        String jarFileName = protectionDomain.getCodeSource().getLocation().getFile();

        String appHomeDir = getConfigDir(args, true);
        createConfigFileIfNeeded(appHomeDir, configFileName, jarFileName);
        createLogDirIfNeeded(appHomeDir);

        WrapperHelper.run(args, appHomeDir, appHomeDir + File.separator + configFileName, jarFileName);
    }

    protected static void createLogDirIfNeeded(String appHomeDir) {

        Path logFilePath = Paths.get(appHomeDir + "/logs");
        if (!Files.exists(logFilePath)) {
            try {
                Files.createDirectories(logFilePath);
            } catch (IOException e) {
                System.out.println("Unable to create log file directory " + logFilePath + "Error =" + e.getMessage());
                System.exit(-1);
            }
        }
    }

    protected static void createConfigFileIfNeeded(String appHomeDir, String configFileName, String jarFileName) {

        File configFile = new File(appHomeDir, configFileName);
        if (!configFile.exists()) {
            try {
                String propContent = convertStreamToString(Wrapper.class.getClassLoader().getResourceAsStream(configFileName));
                propContent = propContent.replace("$(openpos.jar)", jarFileName);
                propContent = propContent.replace("$(java.io.tmpdir)", appHomeDir + File.separator + "tmp");
                propContent = propContent.replace("$(openpos.home.dir)", appHomeDir);
                FileOutputStream output = new FileOutputStream(configFile);
                try {
                    output.write(propContent.getBytes("utf-8"));
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                System.out.println("Unable to write config file for service wrapper." + e.getMessage());
                System.exit(-1);
            }
        }
    }

    static String convertStreamToString(java.io.InputStream is) throws IOException {
        Scanner scanner = new Scanner(is);
        java.util.Scanner s = scanner.useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        s.close();
        scanner.close();
        is.close();
        return result;
    }

    protected static boolean isBlank(String value) {
        return value == null || value.trim().equals("");
    }

    protected static String getConfigDir(String[] args, boolean printInstructions) {

        String configDir = "";

        if (args != null && args.length > 1 && !args[1].equalsIgnoreCase("INSTALL")) {
            int index = args[1].lastIndexOf(File.separator);
            if (index != -1) {
                configDir = args[1].substring(0, index + 1);
            }
        } else {
            configDir = System.getProperty(SYS_CONFIG_DIR);
            if (isBlank(configDir)) {
                configDir = System.getenv(OPENPOS_HOME);
                if (isBlank(configDir)) {
                    /*
                     * If METL_HOME is not set, fall back to SYM_HOME for
                     * backwards compatibility
                     */
                    configDir = System.getenv("SYM_HOME");
                }
                if (isBlank(configDir)) {
                    configDir = System.getProperty("user.dir");
                    if (printInstructions) {
                        System.out.println("You can configure the following system property to point to a working directory "
                                + "where configuration files can be found: -D" + SYS_CONFIG_DIR + "=/some/config/dir");
                    }
                }
            }
            if (printInstructions) {
                System.out.println("The current config directory is " + configDir);
                System.out.println("The current working directory is " + System.getProperty("user.dir"));
                System.out.println("");
                System.out.println("");
            }
        }
        if (isBlank(System.getProperty("h2.baseDir"))) {
            System.setProperty("h2.baseDir", configDir);
        }

        return configDir;
    }
    
    public static void main(String[] args) throws Exception {
        runServiceWrapper(args);
    }
}

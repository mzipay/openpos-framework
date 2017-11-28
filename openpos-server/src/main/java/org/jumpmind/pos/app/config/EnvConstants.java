package org.jumpmind.pos.app.config;

/**
 * These are environment properties definitions that can be set in properties at startup
 */
public final class EnvConstants {

    private EnvConstants() {
    }
    
    public final static String LOG_FILE = "log.file";
    
    public final static String LOG_TO_CONSOLE_ENABLED = "log.to.console.enabled";
    
    public final static String LOG_TO_FILE_ENABLED = "log.to.file.enabled";
    
    public final static String TABLE_PREFIX = "table.prefix";
    
    public final static String CONFIG_DIR = "config.dir";
    
}
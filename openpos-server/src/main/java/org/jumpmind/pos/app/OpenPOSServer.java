package org.jumpmind.pos.app;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.jumpmind.pos.persist.driver.Driver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(
        basePackages = { "org.jumpmind.pos" })
@PropertySource(value = { "classpath:openpos-defaults.properties", "file:./config/openpos.properties" }, ignoreResourceNotFound = true)
public class OpenPOSServer {
    
    private static Logger log;

    public static void main(String[] args) throws Exception {
        initLogging();
        loadJumpMindDriver();
        
        boolean handled = false;
        
        if (args != null && args.length > 0 && !args[0].startsWith("-")) {
            Class<?> clazz = Class.forName(args[0]);
            Method method = clazz.getMethod("main", String[].class);
            String[] param1 = new String[0];
            method.invoke(clazz, new Object[] { param1});
            handled = true;
        }
        
        if (!handled) {            
            SpringApplication.run(OpenPOSServer.class, args);
        }
    }

    protected static void loadJumpMindDriver() {
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    protected static void initLogging() {
        try {
            log = Logger.getLogger(OpenPOSServer.class);
            final String msg = "OpenPOS logging enabled.";
            log.info(msg);
            System.out.println(msg);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

}

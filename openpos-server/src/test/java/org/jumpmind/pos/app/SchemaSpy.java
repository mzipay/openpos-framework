package org.jumpmind.pos.app;
import java.io.File;

import org.apache.commons.io.FileUtils;

import net.sourceforge.schemaspy.Main;

public class SchemaSpy {

    public static void main(String[] args) throws Exception {
        FileUtils.deleteDirectory(new File("build/schemaspy"));
        Main.main(new String[] { "-t","src/test/resources/schemaspy.properties","-u","","-p","","-o","build/schemaspy","-s","PUBLIC"});
    }

}
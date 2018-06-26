package org.jumpmind.pos.core.javapos;

import java.io.InputStream;

import jpos.config.simple.xml.XercesRegPopulator;
import jpos.loader.JposServiceLoader;
import jpos.util.JposProperties;

public class ClasspathXercesRegPopulator extends XercesRegPopulator {

    @Override
    protected InputStream getPopulatorFileIS() throws Exception {
        
        JposProperties jposProperties = JposServiceLoader.getManager().getProperties();
        if (jposProperties.isPropertyDefined("jpos.config.populatorFile")) {
            String jposXml = jposProperties.getPropertyString("jpos.config.populatorFile");
            return getClass().getResourceAsStream(jposXml);
        } else {
            throw new IllegalStateException("Please define the path to the jpos.xml in jpos.config.populatorFile in jpos.properties");
        }
    }
}

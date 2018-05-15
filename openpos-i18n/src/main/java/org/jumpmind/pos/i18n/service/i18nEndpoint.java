package org.jumpmind.pos.i18n.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jumpmind.pos.service.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class i18nEndpoint {

	final Logger log = LoggerFactory.getLogger(getClass());
	
	final static String OVERRIDE = "override";
	
    @Endpoint("/getString")
    public String getString(String base, String key, Locale locale, String brand, Object... args) {

    	final String[] properties = {
    		base + "_" + brand + "_" + OVERRIDE,
   			base + "_" + brand,
   			base + "_" + OVERRIDE,
   			base,
   		};
    	for (String string : properties) {
    		try {
    			ResourceBundle bundle = ResourceBundle.getBundle(string, locale);
    			if (bundle.containsKey(key)) {
    				MessageFormat formatter = new MessageFormat("", locale);
    				try {
    					formatter.applyPattern(bundle.getString(key));
    					return formatter.format(args);
    				} catch (IllegalArgumentException e) {
    					log.warn("Bad message format or arguments in \"" +
    							string + ".properties\" key: \"" + key + "\"", e);
    					return "<UNABLE TO APPLY PATTERN>";
    				}
        		}
    		} catch (MissingResourceException e) {
    			continue;
    		}
		}
    	return "<MISSING RESOURCE>";
    }
}

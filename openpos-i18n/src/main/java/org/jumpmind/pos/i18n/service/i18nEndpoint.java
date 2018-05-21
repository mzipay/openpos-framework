package org.jumpmind.pos.i18n.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.i18n.model.i18nRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


@Component
public class i18nEndpoint {

    final Logger log = LoggerFactory.getLogger(getClass());

    final static String OVERRIDE = "override";
    
    @Autowired
    private i18nRepository i18nRepository;

    @Endpoint("/getString")
    public String getString(@RequestParam(value="base", defaultValue="") String base,
            @RequestParam(value="key", defaultValue="") String key,
            @RequestParam(value="locale", defaultValue="") Locale locale,
            @RequestParam(value="brand", defaultValue="") String brand,
            @RequestParam(value="args", defaultValue="") Object... args) {

        final String[] properties = { base + "_" + brand + "_" + OVERRIDE, base + "_" + brand, base + "_" + OVERRIDE, base, };
       
        String dbquery = i18nRepository.getString(base, key, locale, brand);
        if (dbquery == null) {
        	for (String property : properties) {
        		try {
        			ResourceBundle bundle = ResourceBundle.getBundle(String.format("i18n/%s", property), locale);
        			if (bundle.containsKey(key)) {
        				String pattern = bundle.getString(key);
        				 return format(property, pattern, locale, args);
        			}
            	} catch (MissingResourceException e) {
            		continue;
            	}
        	}
        } else {
        	return format("DATABASE", dbquery, locale, args);
        }
    	return "<MISSING RESOURCE>";
    }
    
    private String format(String location, String pattern, Locale locale, Object... args) {
		try {
			MessageFormat formatter = new MessageFormat(pattern, locale);
			return formatter.format(args);
		} catch (IllegalArgumentException e) {
			log.warn("Bad message format or arguments in resource \"" + location + "_" + locale + "\"", e);
			return "<UNABLE TO APPLY PATTERN>";
		}
	}
}

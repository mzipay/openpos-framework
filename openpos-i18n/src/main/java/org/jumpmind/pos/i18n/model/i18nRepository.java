package org.jumpmind.pos.i18n.model;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.Locale;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.jumpmind.pos.i18n.model.i18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "i18nModule" })
public class i18nRepository {
      
    private Query<i18n> keyLookup = new Query<i18n>()
            .named("keyLookup")
            .result(i18n.class);
    
    @Autowired
    @Qualifier("i18nSession")
    @Lazy
    private DBSession dbSession;    
    
    public String getString(String base, String key, Locale locale, String brand) {
    	
    	//TODO Figure out how to query with multiple primary keys
    	
    	List<i18n> phrases = dbSession.query(keyLookup, key);
    	
    	for (i18n phrase : phrases) {
    		if (phrase.getLocale().equals(locale)) {
    			if (phrase.getBaseName().equals(base)) {
    				if (phrase.getBrand().equals(brand)) {
    					return phrase.getPattern();
    				} else if (phrase.getBrand().equals("*")) {
    					return phrase.getPattern();
    				}
    			}
    		}
    	}
    	return "";
    }
    
    public void save(i18n resource) {
        dbSession.save(resource);
    }
}
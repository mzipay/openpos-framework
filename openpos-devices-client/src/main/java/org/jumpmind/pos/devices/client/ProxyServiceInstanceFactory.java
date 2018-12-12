package org.jumpmind.pos.devices.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jpos.JposConst;
import jpos.JposException;
import jpos.config.JposEntry;
import jpos.loader.JposServiceInstance;
import jpos.loader.JposServiceInstanceFactory;

public class ProxyServiceInstanceFactory implements JposServiceInstanceFactory {

    final String[] PROPS_TO_IGNORE = { "productURL", "serviceClass", "vendorName", "productDescription", "deviceCategory", "productName",
            "vendorURL", "logicalName", "jposVersion", "serviceInstanceFactoryClass" };

    final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JposServiceInstance createInstance(String s, JposEntry jposentry) throws JposException {
        if (!jposentry.hasPropertyWithName("serviceClass")) {
            throw new JposException(JposConst.JPOS_E_NOSERVICE, "The JposEntry does not contain the 'serviceClass' property");
        }
        try {
            String s1 = (String) jposentry.getPropertyValue("serviceClass");
            Class class1 = Class.forName(s1);
            Constructor constructor = class1.getConstructor(new Class[0]);
            JposServiceInstance instance = (JposServiceInstance) constructor.newInstance(new Object[0]);
            Iterator i = jposentry.getProps();
            while (i.hasNext()) {
                JposEntry.Prop prop = (JposEntry.Prop) i.next();
                if (!ignoreProperty(prop.getName())) {
                    Class<?> clazz = class1;
                    boolean setField = false;
                    while (clazz != null && clazz != Object.class && !setField) {
                        try {
                            Field field = clazz.getDeclaredField(prop.getName());
                            Class<?> type = field.getType();
                            Object value = prop.getValue();
                            if (type == int.class) {
                                value = new Integer(prop.getValueAsString());
                            }
                            field.set(instance, value);
                            setField = true;                            
                        } catch (NoSuchFieldException e) {
                            clazz = clazz.getSuperclass();
                        } catch (Exception e) {
                            logger.error("", e);
                            break;
                        }
                    }
                    if (!setField) {
                        logger.debug("No such property: " + prop.getName() + " exists on " + class1.getSimpleName());
                    }
                }
            }
            return instance;
        } catch (Exception exception) {
            throw new JposException(JposConst.JPOS_E_NOSERVICE, "Could not create the service instance!", exception);
        }
    }

    protected boolean ignoreProperty(String name) {
        for (String string : PROPS_TO_IGNORE) {
            if (string.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
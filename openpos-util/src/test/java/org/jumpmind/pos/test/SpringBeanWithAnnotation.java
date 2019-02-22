package org.jumpmind.pos.test;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(proxyMode=ScopedProxyMode.INTERFACES)  // Forces spring to make a proxy
public class SpringBeanWithAnnotation implements SpringBeanIfc {

    @SuppressWarnings("unused")
    private int unusedVariable;
}

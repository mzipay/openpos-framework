package org.jumpmind.pos.util;

import static org.junit.Assert.assertNotNull;

import org.jumpmind.pos.UtilTestConfig;
import org.jumpmind.pos.test.JavaBeanWithAnnotation;
import org.jumpmind.pos.test.SpringBeanIfc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {UtilTestConfig.class})
public class ClassUtilsTest {
    
    @Autowired
    SpringBeanIfc springBean;
    
    @Test
    public void testResolveAnnotationOnSpringBean() {
        assertNotNull(springBean);
        Component supWarnAnnot = ClassUtils.resolveAnnotation(Component.class, springBean);
        assertNotNull(supWarnAnnot);
    }

    @Test
    public void testResolveAnnotationOnPojo() {
        JavaBeanWithAnnotation javaBean = new JavaBeanWithAnnotation();
        
        Deprecated supWarnAnnot = ClassUtils.resolveAnnotation(Deprecated.class, javaBean);
        assertNotNull(supWarnAnnot);
    }
    

}

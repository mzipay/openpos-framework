package org.jumpmind.pos.persist.cars;

import java.util.Arrays;

import org.jumpmind.pos.persist.DBSessionFactory;
import org.junit.Before;
import org.junit.Test;

public class DatabaseSchemaTest {
    
    private DBSessionFactory sessionFactory = new DBSessionFactory();

    @Before
    public void setup() {
        sessionFactory.init(
                PersistTestUtil.testDbPlatform(), 
                PersistTestUtil.getSessionContext(), 
                Arrays.asList(CarModel.class),
                DBSessionFactory.getQueryTemplates("persist-test"),
                DBSessionFactory.getDmlTemplates("persist-test"));   
    }
    
    @Test
    public void someTest() {
        
    }
    
}

package org.jumpmind.pos.persist.cars;

import java.util.Arrays;

import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.impl.DatabaseSchema;
import org.junit.Before;
import org.junit.Test;

public class DatabaseSchemaTest {
    
    private DBSessionFactory sessionFactory = new DBSessionFactory();

    @Before
    public void setup() {
        sessionFactory.setDatabaseSchema(new DatabaseSchema());
        sessionFactory.init(
                PeristTestUtil.getH2TestProperties(), 
                PeristTestUtil.getSessionContext(), 
                Arrays.asList(CarEntity.class),
                PeristTestUtil.getQueryTempaltes());
    }
    
    @Test
    public void someTest() {
        
    }
    
}

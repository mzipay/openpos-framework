package org.jumpmind.pos.persist.cars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.impl.DatabaseSchema;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class DBSessionTest {
    
    @Autowired
    private DBSessionFactory sessionFactory;

    @Before
    public void setup() {
        sessionFactory.setDatabaseSchema(new DatabaseSchema());
        sessionFactory.init(
                PeristTestUtil.getH2TestProperties(), 
                PeristTestUtil.getSessionContext(), 
                Arrays.asList(CarEntity.class));
    }    

    @Test
    public void testBasicCrud() {
        final String VIN = "KMHCN46C58U242743";
        String rowId = null;
        
        {            
            DBSession db = sessionFactory.createDbSession();
            CarEntity someHyundai = new CarEntity();
            someHyundai.setVin(VIN);
            someHyundai.setMake("Hyundai");
            someHyundai.setModel("Accent");
            someHyundai.setModelYear("2005");
            rowId = db.save(someHyundai);
            assertNotNull(rowId);
        }
        {            
            DBSession db = sessionFactory.createDbSession();
            CarEntity someHyundai = new CarEntity();
            someHyundai.setVin(VIN + "2342");
            someHyundai.setMake("Hyundai");
            someHyundai.setModel("Elantra");
            someHyundai.setModelYear("2005");
            String secondRowId = db.save(someHyundai);
            assertNotNull(rowId);
        }        

        {   
            DBSession db = sessionFactory.createDbSession();
            CarEntity hyundaiLookupedUp = db.findByRowId(CarEntity.class, rowId);
            assertNotNull(hyundaiLookupedUp);
            assertEquals(VIN, hyundaiLookupedUp.getVin());
            assertEquals("Hyundai", hyundaiLookupedUp.getMake());
            assertEquals("Accent", hyundaiLookupedUp.getModel());
            assertEquals("2005", hyundaiLookupedUp.getModelYear());
            hyundaiLookupedUp.setModelYear("2006");
            db.save(hyundaiLookupedUp);
        }
        
        {            
            DBSession db = sessionFactory.createDbSession();
            CarEntity hyundaiLookupedUp = db.findByNaturalId(CarEntity.class, VIN);
            assertNotNull(hyundaiLookupedUp);
            assertEquals(VIN, hyundaiLookupedUp.getVin());
            assertEquals("Hyundai", hyundaiLookupedUp.getMake());
            assertEquals("Accent", hyundaiLookupedUp.getModel());
            assertEquals("2006", hyundaiLookupedUp.getModelYear());            
        }
    }
}

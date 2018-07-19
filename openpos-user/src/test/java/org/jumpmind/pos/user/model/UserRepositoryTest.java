package org.jumpmind.pos.user.model;

import static org.junit.Assert.*;

import java.util.List;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.cars.TestPersistCarsConfig;
import org.junit.Before;
import org.junit.Test;
import org.jumpmind.pos.user.model.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestPersistCarsConfig.class})
public class UserRepositoryTest {	
	
    @Autowired
	UserRepository userRepository;	
    
    @Autowired
    @Qualifier("userSession")
    private DBSession dbSession;
    
    @Before
	public void generateTestData () {
		
    	/* Workgroup Mappings */
    	
		WorkgroupModel workgroup = new WorkgroupModel();
		workgroup.setDescription("Retail");
		workgroup.setWorkgroupId("1");
		dbSession.save(workgroup);
		{
			WorkgroupPermissionModel wp = new WorkgroupPermissionModel();
			wp.setWorkgroupId("1");
			wp.setPermissionId("take_inventory");
		
			WorkgroupPermissionModel wp1 = new WorkgroupPermissionModel();
			wp1.setWorkgroupId("1");
			wp1.setPermissionId("use_till");
		
			WorkgroupPermissionModel wp2 = new WorkgroupPermissionModel();
			wp2.setWorkgroupId("1");
			wp2.setPermissionId("clock_out");
			
			dbSession.save(wp);
        	dbSession.save(wp1);
        	dbSession.save(wp2);
		}
	
		WorkgroupModel workgroup1 = new WorkgroupModel();
        workgroup1.setDescription("Management");
        workgroup1.setWorkgroupId("2");
        dbSession.save(workgroup1);
        {   	
        	WorkgroupPermissionModel wp3 = new WorkgroupPermissionModel();
        	wp3.setWorkgroupId("2");
        	wp3.setPermissionId("use_till");
		
        	WorkgroupPermissionModel wp4 = new WorkgroupPermissionModel();
        	wp4.setWorkgroupId("2");
        	wp4.setPermissionId("apply_discounts");
		
        	WorkgroupPermissionModel wp5 = new WorkgroupPermissionModel();
        	wp5.setWorkgroupId("2");
        	wp5.setPermissionId("return_items");
        	
        	dbSession.save(wp3);
        	dbSession.save(wp4);
        	dbSession.save(wp5);
        }
        
        WorkgroupModel workgroup2 = new WorkgroupModel();
        workgroup2.setDescription("This workgroup has no permissions.");
        workgroup2.setWorkgroupId("No perm");
        dbSession.save(workgroup2);
        {
        	
        }
        
        /* Permissions */
        {
        	PermissionModel permission = new PermissionModel();
        	permission.setPermissionId("take_inventory");
        	dbSession.save(permission);
        
        	PermissionModel permission1 = new PermissionModel();
        	permission1.setPermissionId("use_till");
        	dbSession.save(permission1);
        
        	PermissionModel permission2 = new PermissionModel();
        	permission2.setPermissionId("clock_out");
        	dbSession.save(permission2);
        
        	PermissionModel permission3 = new PermissionModel();
        	permission3.setPermissionId("apply_discounts");
        	dbSession.save(permission3);
        
        	PermissionModel permission4 = new PermissionModel();
        	permission4.setPermissionId("return_items");
        	dbSession.save(permission4);
        }
        
        
        /* Users */
        {
        	UserModel user = new UserModel();
        	user.setUsername("Clerk");
        	user.setWorkgroupId("1");
        	dbSession.save(user);
        	
        	UserModel user1 = new UserModel();
        	user1.setUsername("Salesman");
        	user1.setWorkgroupId("1");
        	dbSession.save(user1);
        
        	UserModel user2 = new UserModel();
        	user2.setUsername("Store Manager");
        	user2.setWorkgroupId("2");
        	dbSession.save(user2);
        	
        	UserModel user3 = new UserModel();
        	user3.setUsername("General Manager");
        	user3.setWorkgroupId("2");
        	dbSession.save(user3);   
        	
        	UserModel user4 = new UserModel();
        	user4.setUsername("No Workplace");
        	user4.setWorkgroupId("none");
        	dbSession.save(user4);  
        	
        	UserModel user5 = new UserModel();
        	user5.setUsername("No Permissions");
        	user5.setWorkgroupId("No perm");
        	dbSession.save(user5);
        }	
    }
    
    @Test
    public void findUserRetailTest () {
    	UserModel salesman = userRepository.findUser("Salesman");
    	assertEquals(salesman.getUsername(), "Salesman");
    	assertEquals(salesman.getWorkgroup().getDescription(), "Retail");
    	List<PermissionModel> permissions = salesman.getWorkgroup().getPermissions();
    	for (PermissionModel permission : permissions) {
			assertTrue(permission.getPermissionId().matches("take_inventory|use_till|clock_out"));
		}
    }
    
    @Test
    public void findUserManagementTest () {
    	UserModel manager = userRepository.findUser("General Manager");
    	assertEquals(manager.getUsername(), "General Manager");
    	assertEquals(manager.getWorkgroup().getDescription(), "Management");
    	List<PermissionModel> permissions = manager.getWorkgroup().getPermissions();
    	for (PermissionModel permission : permissions) {
			assertTrue(permission.getPermissionId().matches("return_items|apply_discounts|use_till"));
		}
    }
    
    @Test
    public void findUserNoUserTest () {
    	UserModel salesman = userRepository.findUser("invalid");
    	assertTrue(salesman == null);
    }
    
    @Test
    public void findUserNoWorkplaceTest () {
    	UserModel salesman = userRepository.findUser("No Workplace");
    	assertEquals(salesman.getUsername(), "No Workplace");
    	assertTrue(salesman.getWorkgroup().getDescription() == null);
    }
    
    @Test
    public void findUserNoPermissionsTest () {
    	UserModel salesman = userRepository.findUser("No Permissions");
    	assertEquals(salesman.getUsername(), "No Permissions");
    	assertEquals(salesman.getWorkgroup().getDescription(), "This workgroup has no permissions.");
    	assertTrue(salesman.getWorkgroup().getPermissions() == null);
    }
}
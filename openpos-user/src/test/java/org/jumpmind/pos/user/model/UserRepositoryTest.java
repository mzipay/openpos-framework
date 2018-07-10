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
    	
		Workgroup workgroup = new Workgroup();
		workgroup.setDescription("Retail");
		workgroup.setWorkgroupId("1");
		dbSession.save(workgroup);
		{
			WorkgroupPermission wp = new WorkgroupPermission();
			wp.setWorkgroupId("1");
			wp.setPermissionId("take_inventory");
		
			WorkgroupPermission wp1 = new WorkgroupPermission();
			wp1.setWorkgroupId("1");
			wp1.setPermissionId("use_till");
		
			WorkgroupPermission wp2 = new WorkgroupPermission();
			wp2.setWorkgroupId("1");
			wp2.setPermissionId("clock_out");
			
			dbSession.save(wp);
        	dbSession.save(wp1);
        	dbSession.save(wp2);
		}
	
		Workgroup workgroup1 = new Workgroup();
        workgroup1.setDescription("Management");
        workgroup1.setWorkgroupId("2");
        dbSession.save(workgroup1);
        {   	
        	WorkgroupPermission wp3 = new WorkgroupPermission();
        	wp3.setWorkgroupId("2");
        	wp3.setPermissionId("use_till");
		
        	WorkgroupPermission wp4 = new WorkgroupPermission();
        	wp4.setWorkgroupId("2");
        	wp4.setPermissionId("apply_discounts");
		
        	WorkgroupPermission wp5 = new WorkgroupPermission();
        	wp5.setWorkgroupId("2");
        	wp5.setPermissionId("return_items");
        	
        	dbSession.save(wp3);
        	dbSession.save(wp4);
        	dbSession.save(wp5);
        }
        
        Workgroup workgroup2 = new Workgroup();
        workgroup2.setDescription("This workgroup has no permissions.");
        workgroup2.setWorkgroupId("No perm");
        dbSession.save(workgroup2);
        {
        	
        }
        
        /* Permissions */
        {
        	Permission permission = new Permission();
        	permission.setPermissionId("take_inventory");
        	dbSession.save(permission);
        
        	Permission permission1 = new Permission();
        	permission1.setPermissionId("use_till");
        	dbSession.save(permission1);
        
        	Permission permission2 = new Permission();
        	permission2.setPermissionId("clock_out");
        	dbSession.save(permission2);
        
        	Permission permission3 = new Permission();
        	permission3.setPermissionId("apply_discounts");
        	dbSession.save(permission3);
        
        	Permission permission4 = new Permission();
        	permission4.setPermissionId("return_items");
        	dbSession.save(permission4);
        }
        
        
        /* Users */
        {
        	User user = new User();
        	user.setUsername("Clerk");
        	user.setWorkgroupId("1");
        	dbSession.save(user);
        	
        	User user1 = new User();
        	user1.setUsername("Salesman");
        	user1.setWorkgroupId("1");
        	dbSession.save(user1);
        
        	User user2 = new User();
        	user2.setUsername("Store Manager");
        	user2.setWorkgroupId("2");
        	dbSession.save(user2);
        	
        	User user3 = new User();
        	user3.setUsername("General Manager");
        	user3.setWorkgroupId("2");
        	dbSession.save(user3);   
        	
        	User user4 = new User();
        	user4.setUsername("No Workplace");
        	user4.setWorkgroupId("none");
        	dbSession.save(user4);  
        	
        	User user5 = new User();
        	user5.setUsername("No Permissions");
        	user5.setWorkgroupId("No perm");
        	dbSession.save(user5);
        }	
    }
    
    @Test
    public void findUserRetailTest () {
    	User salesman = userRepository.findUser("Salesman");
    	assertEquals(salesman.getUsername(), "Salesman");
    	assertEquals(salesman.getWorkgroup().getDescription(), "Retail");
    	List<Permission> permissions = salesman.getWorkgroup().getPermissions();
    	for (Permission permission : permissions) {
			assertTrue(permission.getPermissionId().matches("take_inventory|use_till|clock_out"));
		}
    }
    
    @Test
    public void findUserManagementTest () {
    	User manager = userRepository.findUser("General Manager");
    	assertEquals(manager.getUsername(), "General Manager");
    	assertEquals(manager.getWorkgroup().getDescription(), "Management");
    	List<Permission> permissions = manager.getWorkgroup().getPermissions();
    	for (Permission permission : permissions) {
			assertTrue(permission.getPermissionId().matches("return_items|apply_discounts|use_till"));
		}
    }
    
    @Test
    public void findUserNoUserTest () {
    	User salesman = userRepository.findUser("invalid");
    	assertTrue(salesman == null);
    }
    
    @Test
    public void findUserNoWorkplaceTest () {
    	User salesman = userRepository.findUser("No Workplace");
    	assertEquals(salesman.getUsername(), "No Workplace");
    	assertTrue(salesman.getWorkgroup().getDescription() == null);
    }
    
    @Test
    public void findUserNoPermissionsTest () {
    	User salesman = userRepository.findUser("No Permissions");
    	assertEquals(salesman.getUsername(), "No Permissions");
    	assertEquals(salesman.getWorkgroup().getDescription(), "This workgroup has no permissions.");
    	assertTrue(salesman.getWorkgroup().getPermissions() == null);
    }
}
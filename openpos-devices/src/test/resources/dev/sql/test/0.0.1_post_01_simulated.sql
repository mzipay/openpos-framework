
DELETE FROM dev_device_prop;
DELETE FROM dev_device;

INSERT INTO dev_device (profile, device_name, description, service_class, factory_class, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('dev', 'Scanner', null, 'org.jumpmind.pos.core.javapos.SimulatedScannerService', 'org.jumpmind.pos.core.javapos.SimulatedJposServiceInstanceFactory', 'me', current_timestamp, 'you', current_timestamp);
  
INSERT INTO dev_device (profile, device_name, description, service_class, factory_class, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('dev', 'Printer', null, 'org.jumpmind.pos.core.javapos.SimulatedPOSPrinterService', 'org.jumpmind.pos.core.javapos.SimulatedJposServiceInstanceFactory', 'me', current_timestamp, 'you', current_timestamp);
  
  
INSERT INTO dev_device_prop (profile, device_name, property_name, property_value, property_type, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('dev', 'Scanner', 'testProp', 'testValue', 'String', 'me', current_timestamp, 'you', current_timestamp);  
  
  
INSERT INTO dev_device_prop (profile, device_name, property_name, property_value, property_type, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('dev', 'Scanner', 'testProp2', '2', 'Integer', 'me', current_timestamp, 'you', current_timestamp);  


INSERT INTO dev_device (profile, device_name, description, service_class, factory_class, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('other', 'Scanner', null, 'org.jumpmind.pos.core.javapos.SimulatedScannerService', 'org.jumpmind.pos.core.javapos.SimulatedJposServiceInstanceFactory', 'me', current_timestamp, 'you', current_timestamp);
  
  
INSERT INTO dev_device_prop (profile, device_name, property_name, property_value, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('other', 'Scanner', 'testProp2', 'testValue2', 'me', current_timestamp, 'you', current_timestamp);    
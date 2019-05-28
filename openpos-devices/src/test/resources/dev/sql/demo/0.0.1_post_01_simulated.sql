
DELETE FROM dev_config_prop;
DELETE FROM dev_config;
  
INSERT INTO dev_config (profile, device_name, description, service_class, factory_class, last_update_by, last_update_time, create_by, create_time) 
  VALUES ('dev', 'Printer', null, 'org.jumpmind.pos.core.javapos.SimulatedPOSPrinterService', 'org.jumpmind.pos.core.javapos.SimulatedJposServiceInstanceFactory', 'me', current_timestamp, 'you', current_timestamp);

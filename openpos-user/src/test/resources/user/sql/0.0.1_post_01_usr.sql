INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('1', 'Retail', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('2', 'Management', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('3', 'No Permissions', current_timestamp, 'test', 'test');

INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('take_inventory', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('use_till', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('clock_out', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('apply_discounts', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('return_items', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('sell', current_timestamp, 'test', 'test');

INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('1', 'take_inventory', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('1', 'use_till', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('1', 'clock_out', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('1', 'sell', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'take_inventory', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'use_till', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'clock_out', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'apply_discounts', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'return_items', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'sell', current_timestamp, 'test', 'test');

INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('tstark', 'Tony', 'Stark', 'Iron Man', '1', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('cmax', 'Crystalia', 'Maximoff', 'Crystal', '2', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('ajones', 'Angelica', 'Jones', 'Firestar', '3', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('jhowlett', 'James', 'Howlett', 'Wolverine', '1', current_timestamp, 'test', 'test');

INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('tstark', '1', 'stark', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('cmax', '1', 'max', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('ajones', '1', 'jones', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('jhowlett', '1', 'howlett', current_timestamp, 'test', 'test');

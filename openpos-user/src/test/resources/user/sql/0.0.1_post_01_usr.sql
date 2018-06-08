INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('1', 'Management', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('2', 'Retail', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('3', 'Bad Permissions', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('4', 'No Permissions', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('5', 'All Sell Permissions', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP (workgroup_id, description, create_time, create_by, last_update_by) VALUES ('6', 'All Permissions', current_timestamp, 'test', 'test');

INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('sell.*', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('sell.main', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('sell.none', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('sellxmain', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('sell.customer_search', current_timestamp, 'test', 'test');
INSERT INTO USR_PERMISSION (permission_id, create_time, create_by, last_update_by) VALUES ('*', current_timestamp, 'test', 'test');

INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('1', 'sell.main', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('1', 'sell.customer_search', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('2', 'sell.main', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('3', 'sell.none', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('3', 'sellxmain', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('5', 'sell.*', current_timestamp, 'test', 'test');
INSERT INTO USR_WORKGROUP_PERMISSION (workgroup_id, permission_id, create_time, create_by, last_update_by) VALUES ('6', '*', current_timestamp, 'test', 'test');

INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('tstark', 'Tony', 'Stark', 'Iron Man', '1', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('cmax', 'Crystalia', 'Maximoff', 'Crystal', '2', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('ajones', 'Angelica', 'Jones', 'Firestar', '3', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('jhow', 'James', 'Howlett', 'Wolverine', '4', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('sell', 'All', 'Sell', 'Seller', '5', current_timestamp, 'test', 'test');
INSERT INTO USR_USER (username, first_name, last_name, nickname, workgroup_id, create_time, create_by, last_update_by) VALUES ('all', 'All', 'All', 'All', '6', current_timestamp, 'test', 'test');

INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('tstark', '1', 'stark', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('cmax', '1', 'max', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('ajones', '1', 'jones', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('jhow', '1', 'howlett', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('sell', '1', 'sell', current_timestamp, 'test', 'test');
INSERT INTO USR_PASSWORD_HISTORY (username, password_sequence, hashed_password, create_time, create_by, last_update_by) VALUES ('all', '1', 'all', current_timestamp, 'test', 'test');

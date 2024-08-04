-- drop user
drop user if exists 'base_root'@'%';
-- create user
create user 'base_root'@'%' identified by '';
-- grants privileges
grant all on `base\_db`.* to 'base_root'@'%';
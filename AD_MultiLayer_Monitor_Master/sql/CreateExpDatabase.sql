drop database if exists experiment;
create database experiment;

use experiment;

drop table if exists service_indicator_stat;
drop table if exists service_method_stat;
drop table if exists service_stat;
drop table if exists service_method_invocation;
drop table if exists indicator_observation;
drop table if exists indicator;
drop table if exists observation;
drop table if exists workload_service;
drop table if exists service_method;
drop table if exists performance_type;
drop table if exists service;
drop table if exists method;
drop table if exists probe_type;
drop table if exists failure;
drop table if exists failure_type;
drop table if exists run;
drop table if exists run_type;
drop table if exists workload;

create table workload (workload_id int primary key auto_increment, wl_name varchar(100), wl_description 	varchar(300));

create table run_type (run_type_id int primary key auto_increment, rt_description varchar(50));

create table run (run_id int primary key auto_increment, run_type_id int, workload_id int,
	foreign key (run_type_id) references run_type(run_type_id) on delete cascade, 
	foreign key (workload_id) references workload(workload_id) on delete cascade);

create table failure_type (failure_type_id int primary key auto_increment, ft_description varchar(30));

create table failure (failure_id int primary key auto_increment, failure_type_id int, run_id int, fa_description varchar(100), fa_time datetime, 
    foreign key (failure_type_id) references failure_type(failure_type_id) on delete cascade,
    foreign key (run_id) references run(run_id) on delete cascade);

create table probe_type (probe_type_id int primary key auto_increment, pt_description varchar(50));

create table method (method_id int primary key auto_increment, me_name varchar(50), me_portlet varchar(50));

create table service (service_id int primary key auto_increment, se_name varchar(40), se_description varchar(300));

create table performance_type (performance_type_id int primary key auto_increment, pet_description varchar(50));

create table service_method (service_method_id int primary key auto_increment, service_id int, method_id int, me_index int not null, 
	foreign key (service_id) references service(service_id) on delete cascade,
    foreign key (method_id) references method(method_id) on delete cascade);

create table workload_service (workload_service_id int primary key auto_increment, workload_id int, 		service_id int, ws_index int, 
	foreign key (workload_id) references workload(workload_id) on delete cascade, 
	foreign key (service_id) references service(service_id) on delete cascade);

create table indicator (indicator_id int primary key auto_increment, probe_type_id int, in_tag varchar(50), in_description varchar(200), 
    foreign key (probe_type_id) references probe_type(probe_type_id) on delete cascade);

create table observation (observation_id int primary key auto_increment, run_id int, ob_time datetime, 
    foreign key (run_id) references run(run_id) on delete cascade); 

create table value_category (value_category_id int primary key auto_increment, vc_description varchar(50));
    
create table indicator_observation (indicator_observation_id int primary key auto_increment, indicator_id int, observation_id int,
    foreign key (indicator_id) references indicator(indicator_id) on delete cascade, 
    foreign key (observation_id) references observation(observation_id) on delete cascade);
    
create table indicator_observation_category (indicator_observation_id int, value_category_id int, ioc_value varchar(50), 
	foreign key (indicator_observation_id) references indicator_observation(indicator_observation_id) on delete cascade, 
	foreign key (value_category_id) references value_category(value_category_id) on delete cascade,
	primary key(indicator_observation_id, value_category_id));

create table service_method_invocation (service_method_invocation_id int primary key auto_increment, run_id int, service_method_id int, start_time datetime, end_time datetime, response varchar(50),
	foreign key (service_method_id) references service_method(service_method_id) on delete cascade,
    foreign key (run_id) references run(run_id) on delete cascade);
    
create table service_stat (service_stat_id int primary key auto_increment, service_id int, serv_dur_avg double, serv_dur_std double, serv_dur_std_perc double, serv_obs_avg double, serv_obs_std double, serv_obs_std_perc double,
    foreign key (service_id) references service(service_id) on delete cascade);
    
create table service_method_stat (service_method_stat_id int primary key auto_increment, service_method_id int, sm_dur_avg double, sm_dur_std double, sm_dur_std_perc double, sm_obs_avg double, sm_obs_std double, sm_obs_std_perc double, 
    foreign key (service_method_id) references service_method(service_method_id) on delete cascade);
    
create table service_indicator_stat (service_stat_id int, indicator_id int, value_category_id int, si_avg_first double, si_std_first double, si_med_first double, si_mod_first double, si_avg_last double, si_std_last double, si_med_last double, si_mod_last double, si_all_avg double, si_all_std double, si_all_med double, si_all_mod double,
    foreign key (indicator_id) references indicator(indicator_id) on delete cascade, 
    foreign key (service_stat_id) references service_stat(service_stat_id) on delete cascade,
    foreign key (value_category_id) references value_category(value_category_id) on delete cascade,
    primary key (indicator_id, value_category_id, service_stat_id));

create table performance (performance_id int primary key auto_increment, run_id int, 
    performance_type_id int, probe_type_id int, ind_number int, perf_time int,
    foreign key (run_id) references run(run_id) on delete cascade,
    foreign key (performance_type_id) references performance_type(performance_type_id) on delete cascade,
    foreign key (probe_type_id) references probe_type(probe_type_id) on delete cascade);

insert into value_category (vc_description) values ('Plain'), ('Difference');

insert into performance_type (pet_description) values ('ot'), ('pmtt'), ('dat'); 
    
insert into failure_type (ft_description) values 
	('Catastrophic'), ('Restart'), ('Abort'), ('Silent'), ('Hindering'), ('TEST'), ('LIFERAY');
    
insert into run_type (rt_description) values
	('Golden'), ('Faulty'), ('Test');
    
insert into probe_type (pt_description) values
	('CENTOS'), ('UBUNTU'), ('WINDOWS'), ('JVM'), ('MYSQL'), 
    ('SQLSERVER'), ('ORACLE'), ('MONGODB'), ('TOMCAT'), ('JBOSS'), ('UNIX_NETWORK');

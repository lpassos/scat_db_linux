-- . . . . . . . . . . . . . . . . . . . .

drop table if exists release cascade ;

create table release (
  id   serial      primary key,
  name varchar(50) unique not null
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists subsystem cascade ;

create table subsystem (
  id   serial      primary key,
  name varchar(50) unique not null
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists file cascade ;

create table file (
  id           bigserial primary key,
  fk_file_info bigserial not null,
  fk_release   serial    not null,
  unique (fk_file_info, fk_release)
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists kconfig_file cascade ;

create table kconfig_file (
  fk_file bigserial primary key
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists build_file cascade ;

create table build_file (
  fk_file bigserial primary key
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists c_source_file cascade ;

create table c_source_file (
  fk_file bigserial primary key,
  sloc     bigint   not null
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists other_file cascade ;

create table other_file (
  fk_file bigserial primary key
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists file_info ;

create table file_info (
  id           bigserial        primary key,
  path         varchar(1000000) not null,
  fk_subsystem serial           not null,  
  unique(path) 
) ;

-- . . . . . . . . . . . . . . . . . . . .
-- Note that the name of a feature is 
-- not unique; a feature may be 
-- defined multiple times in distinct
-- subsystems. 

drop table if exists feature cascade ;

create table feature (
  id                   bigserial     primary key,
  name                 varchar(1000) not null,
  fk_kconfig_file      bigserial     not null,
  fk_feature_type      char(2)       not null,
  fk_feature_data_type char(2)       not null,
  unique(name, fk_kconfig_file, fk_feature_type, fk_feature_data_type)  
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists feature_type ;

create table feature_type (
  id   char(2)      primary key check (id in ('CH', 'MC', 'CO')),
  name varchar(20)  unique not null
) ;

insert into feature_type values 
  ('CH', 'choice'),
  ('MC', 'menuconfig'),
  ('CO', 'config') ;


-- . . . . . . . . . . . . . . . . . . . .

drop table if exists feature_data_type ;

create table feature_data_type (
  id   char(2)      primary key check (id in ('BO', 'TR', 'ST', 'IN', 'HE')),
  name varchar(20)  unique not null
) ;

insert into feature_data_type values
  ('BO', 'boolean'),
  ('TR', 'tristate'),
  ('ST', 'string'),
  ('IN', 'int'),
  ('HE', 'hex') ;

-- . . . . . . . . . . . . . . . . . . . .
-- As feature names are not unique,
-- a feature reference cannot be 
-- directly linked with the referred
-- feature. Instead, we record the 
-- name of the referred feature
-- only.

drop table if exists feature_ref cascade ;

create table feature_ref (
  id      bigserial        primary key,
  name    varchar(1000)    not null,
  context varchar(1000000) not null,
  line    integer          not null
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists kconfig_ref cascade ;

create table kconfig_ref (
  fk_feature_ref  bigserial primary key,
  fk_kconfig_file bigserial not null
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists build_ref cascade ;

create table build_ref (
  fk_feature_ref bigserial primary key,
  fk_build_file  bigserial not null
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists c_source_ref cascade ;

create table c_source_ref (
  fk_feature_ref bigserial primary key,
  fk_c_source_file bigserial not null  
) ;

-- . . . . . . . . . . . . . . . . . . . .

drop table if exists ifdef_ref cascade ;

create table ifdef_ref (
  fk_c_source_ref bigserial primary key
) ;

-- . . . . . . . . . . . . . . . . . . . .

alter table file_info drop constraint 
  if exists file_info_to_subsystem ;

alter table file_info
  add constraint file_info_to_subsystem foreign key
  (fk_subsystem) references subsystem(id) on delete cascade ;
  
-- . . . . . . . . . . . . . . . . . . . . 

alter table file drop constraint
  if exists file_to_file_info ;
  
alter table file
  add constraint file_to_file_info foreign key
  (fk_file_info) references file_info(id) on delete cascade ;

alter table file drop constraint
  if exists file_to_release ;

alter table file
  add constraint file_to_release foreign key
  (fk_release) references release(id) on delete cascade ;
  
-- . . . . . . . . . . . . . . . . . . . . 

alter table feature drop constraint
  if exists feature_to_kconfig_file ;
  
alter table feature
  add constraint feature_to_kconfig_file foreign key
  (fk_kconfig_file) references kconfig_file(fk_file) on delete cascade ;

alter table feature
  add constraint feature_to_feature_data_type foreign key
  (fk_feature_data_type) references feature_data_type(id) on delete cascade ;

alter table feature
  add constraint feature_to_feature_type foreign key
  (fk_feature_type) references feature_type(id) on delete cascade ;
  
-- . . . . . . . . . . . . . . . . . . . . 

alter table kconfig_ref drop constraint
  if exists kconfig_ref_to_kconfig_file ;
  
alter table kconfig_ref
  add constraint kconfig_ref_to_kconfig_file foreign key
  (fk_kconfig_file) references kconfig_file(fk_file) on delete cascade ;


alter table kconfig_ref drop constraint
  if exists kconfig_ref_to_feature_ref ;
  
alter table kconfig_ref
  add constraint kconfig_ref_to_feature_ref foreign key
  (fk_feature_ref) references feature_ref(id) on delete cascade ;


-- . . . . . . . . . . . . . . . . . . . . 

alter table build_ref drop constraint
  if exists build_ref_to_build_file ;
  
alter table build_ref
  add constraint build_ref_to_build_file foreign key
  (fk_build_file) references build_file(fk_file) on delete cascade ;

alter table build_ref drop constraint
  if exists build_ref_to_feature_ref ;
  
alter table build_ref
  add constraint build_ref_to_feature_ref foreign key
  (fk_feature_ref) references feature_ref(id) on delete cascade ;

  
-- . . . . . . . . . . . . . . . . . . . . 

alter table c_source_ref drop constraint
  if exists c_source_ref_to_c_source_file ;
  
alter table c_source_ref
  add constraint c_source_ref_to_c_source_file foreign key
  (fk_c_source_file) references c_source_file(fk_file) on delete cascade ;  
  
alter table c_source_ref drop constraint
  if exists c_source_ref_to_feature_ref ;
  
alter table c_source_ref
  add constraint c_source_ref_to_feature_ref foreign key
  (fk_feature_ref) references feature_ref(id) on delete cascade ;
  
-- . . . . . . . . . . . . . . . . . . . . 

alter table ifdef_ref drop constraint
  if exists ifdef_ref_to_c_source_ref ;
  
alter table ifdef_ref 
  add constraint ifdef_ref_to_c_source_ref foreign key
  (fk_c_source_ref) references c_source_ref(fk_feature_ref) on delete cascade ;

-- . . . . . . . . . . . . . . . . . . . . 

alter table kconfig_file drop constraint
  if exists kconfig_file_to_file ; 
  
alter table kconfig_file 
  add constraint kconfig_file_to_file foreign key
  (fk_file) references file(id) on delete cascade ;

-- . . . . . . . . . . . . . . . . . . . . 

alter table build_file drop constraint
  if exists build_file_to_file ; 
  
alter table build_file 
  add constraint build_file_to_file foreign key
  (fk_file) references file(id) on delete cascade ;

-- . . . . . . . . . . . . . . . . . . . . 

alter table c_source_file drop constraint
  if exists c_source_file_to_file ; 
  
alter table c_source_file 
  add constraint c_source_file_to_file foreign key
  (fk_file) references file(id) on delete cascade ;

-- . . . . . . . . . . . . . . . . . . . . 

alter table other_file drop constraint
  if exists other_file_to_file ; 
  
alter table other_file 
  add constraint other_file_to_file foreign key
  (fk_file) references file(id) on delete cascade ;


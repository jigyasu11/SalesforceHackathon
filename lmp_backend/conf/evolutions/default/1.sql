
# --- !Ups
create table location (
     _id bigint(20) NOT NULL AUTO_INCREMENT,
     user_id varchar(30) NULL,
     latitude decimal(14, 6) NULL,
     longitude decimal(14, 6) NULL,
     device_id varchar(30) NULL,
     device_name varchar(30) NULL,
     battery int(11) NULL,
     ping_time bigint(20) NULL,
     PRIMARY KEY (_id)
);

# --- !Downs
 
DROP TABLE location;

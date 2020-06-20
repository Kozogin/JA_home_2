drop database if exists magazine;
create database magazine char set utf8;
use magazine;

create table users(
	id int not null primary key auto_increment,
    username varchar(120) not null unique,
    password varchar(30) not null,
    first_name varchar(45) not null,
    last_name varchar(45) not null,
    details text  
);

create table magazine(	
    id int not null primary key auto_increment,
    name varchar(60) not null,
    description text,
    price decimal(6,2) not null,
    isbn varchar(12) not null unique
);

create table magazine_users(
	magazine_id int not null,
    users_id int not null,
    primary key (magazine_id, users_id)
);
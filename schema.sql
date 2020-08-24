create database form default charset 'utf8';
create user siriraj identified with mysql_native_password by '@siriraj';
grant all on form.* to siriraj;

use form;
create table users(
    id        int unique not null auto_increment,
    email     varchar(200) unique not null,
    password  varchar(200) not null,
    full_name varchar(200) not null
);

create table forms(
    id        int unique not null auto_increment,
    user_id   int not null,
    title     varchar(200) not null default ''
);

create table elements(
    id          int unique not null auto_increment,
    form_id     int not null,
    title       varchar(200) not null default '',
    type        varchar(200) not null,
    placeholder varchar(200),
    min         int default  0,
    max         int default 10,
    value       int,
    step        int,
    detail      varchar(200),
    status      varchar(200) default 'active'
);

insert into users(email,password,full_name)
    values('user@email.com', sha2('user', 512), 'Administrator');

create table form_values(
    id         int unique not null auto_increment,
    element_id int not null,
    value      varchar(200),
    time       timestamp
);

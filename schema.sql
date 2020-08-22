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
    id        int unique not null auto_increment,
    form_id   int not null,
    title     varchar(200) not null default '',
    type      varchar(200) not null
);
alter table elements add placeholder varchar(200);
alter table elements add min int default 0;
alter table elements add max int default 10;
alter table elements add value int;
alter table elements add step int;

insert into users(email,password,full_name)
    values('user@email.com', sha2('user', 512), 'Administrator');
insert into forms(user_id, title)
    values(1, 'Form 1');

insert into elements(form_id, title, type)
    values(1, 'Element 1', 'text');
insert into elements(form_id, title, type)
    values(1, 'Element 2', 'range');
insert into elements(form_id, title, type)
    values(1, 'Element 3', 'text');
insert into elements(form_id, title, type)
    values(1, 'Overall', 'range');


insert into users(email,password,full_name)
values('james@bond.com', sha2('james123', 512), 'James Bond');

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

insert into users(email,password,full_name)
	values('user@email.com', sha2('user', 512), 'Administrator');
insert into forms(user_id, title)
	values(1, 'Form 1');

insert into elements(form_id, title, type)
	values(1, 'Element 1', 'slider');
insert into elements(form_id, title, type)
	values(1, 'Element 2', 'slider');
insert into elements(form_id, title, type)
	values(1, 'Element 3', 'slider');
insert into elements(form_id, title, type)
	values(1, 'Overall', 'slider');
	
	
insert into users(email,password,full_name)
values('james@bond.com', sha2('james123', 512), 'James Bond');

	
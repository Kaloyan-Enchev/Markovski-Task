create schema if not exists markovski;

create table users
(
    user_id      int auto_increment
        primary key,
    first_name   varchar(20) not null,
    last_name    varchar(20) not null,
    birth_date   date        null,
    phone_number varchar(10) not null,
    email        varchar(55) not null,
    password     varchar(50) not null
);
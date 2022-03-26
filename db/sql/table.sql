create table users
(
    id       varchar(10) not null
        primary key,
    name     varchar(20) not null,
    password varchar(10) not null
# Chapter 5
#     ,level     tinyint     not null,
#     login     int         not null,
#     recommend int         not null
);

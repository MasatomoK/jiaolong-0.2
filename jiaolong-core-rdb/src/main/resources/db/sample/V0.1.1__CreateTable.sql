create table "user"
(
    ID       serial primary key,
    name     varchar(100) not null,
    password varchar(100) not null
)

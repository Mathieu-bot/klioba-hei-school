create table if not exists "user"
(
    id         varchar
        constraint user_pk primary key,

    email      varchar unique not null,
    first_name varchar        not null,
    last_name  varchar        not null
);

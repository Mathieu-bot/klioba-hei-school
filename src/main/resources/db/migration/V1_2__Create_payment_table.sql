create table if not exists payment
(
    id               varchar
        constraint payment_pk primary key,

    amount           integer                  not null,
    creation_instant timestamp with time zone not null
);

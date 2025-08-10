create table if not exists payment
(
    id                            varchar
        constraint payment_pk primary key,

    amount                        integer                  null,
    status                        varchar                  not null,

    psp_id                        varchar                  not null,
    psp_last_verification_instant timestamp with time zone null
);

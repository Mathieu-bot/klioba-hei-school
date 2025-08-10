create table if not exists event
(
    id      varchar
        constraint event_pk primary key,

    user_id varchar not null
        constraint user_id_fk
            references "user" (id),

    payment_id varchar not null
        constraint payment_id_fk
            references payment (id),

    creation_instant timestamp with time zone not null
);

create index if not exists user_id_index on event (user_id);
create index if not exists payment_id_index on event (payment_id);

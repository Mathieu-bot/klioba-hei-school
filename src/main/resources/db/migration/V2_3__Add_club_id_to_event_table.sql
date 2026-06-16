alter table event
    add column club_id varchar
        constraint club_id_fk references club (id);

create index if not exists club_id_index on event (club_id);

insert into club (id, name)
values ('foot', 'Club Foot'),
       ('basket', 'Club Basket')
on conflict (id) do nothing;

-- Users
insert into "user" (id, email, first_name, last_name)
values ('user_jean', 'jean@hei.school', 'Jean', 'Dupont'),
       ('user_marie', 'marie@hei.school', 'Marie', 'Claire')
on conflict (id) do nothing;

-- Club memberships
insert into club_membership (club_id, user_id)
select 'foot', 'user_jean'
where not exists (select 1 from club_membership where club_id = 'foot' and user_id = 'user_jean');
insert into club_membership (club_id, user_id)
select 'foot', 'user_marie'
where not exists (select 1 from club_membership where club_id = 'foot' and user_id = 'user_marie');
insert into club_membership (club_id, user_id)
select 'basket', 'user_jean'
where not exists (select 1 from club_membership where club_id = 'basket' and user_id = 'user_jean');
insert into club_membership (club_id, user_id)
select 'basket', 'user_marie'
where not exists (select 1 from club_membership where club_id = 'basket' and user_id = 'user_marie');

-- Payments for Club Foot
insert into payment (id, amount, psp_type, psp_id, status, psp_last_verification_instant, creation_instant)
values ('pay_foot_cotisation_1', 5000, 'ORANGE_MONEY', 'MP260601.1430.A00001', 'CONFIRMED', '2026-06-01 14:35:00+03', '2026-06-01 14:30:00+03'),
       ('pay_foot_cotisation_2', 5000, 'ORANGE_MONEY', 'MP260605.1030.A00002', 'CONFIRMED', '2026-06-05 10:35:00+03', '2026-06-05 10:30:00+03'),
       ('pay_foot_withdrawal_1', -3000, 'ORANGE_MONEY', 'MP260610.0900.A00003', 'CONFIRMED', '2026-06-10 09:05:00+03', '2026-06-10 09:00:00+03')
on conflict (id) do nothing;

-- Events for Club Foot
insert into event (id, user_id, payment_id, club_id, creation_instant, comment)
values ('event_foot_cotisation_1', 'user_jean', 'pay_foot_cotisation_1', 'foot', '2026-06-01 14:30:00+03', null),
       ('event_foot_cotisation_2', 'user_marie', 'pay_foot_cotisation_2', 'foot', '2026-06-05 10:30:00+03', null),
       ('event_foot_withdrawal_1', 'user_jean', 'pay_foot_withdrawal_1', 'foot', '2026-06-10 09:00:00+03', 'Achat équipements')
on conflict (id) do nothing;

-- Payments for Club Basket
insert into payment (id, amount, psp_type, psp_id, status, psp_last_verification_instant, creation_instant)
values ('pay_basket_cotisation_1', 5000, 'ORANGE_MONEY', 'MP260602.1530.A00004', 'CONFIRMED', '2026-06-02 15:35:00+03', '2026-06-02 15:30:00+03'),
       ('pay_basket_cotisation_2', 5000, 'ORANGE_MONEY', 'MP260606.1130.A00005', 'VERIFYING', null, '2026-06-06 11:30:00+03'),
       ('pay_basket_withdrawal_1', -2000, 'ORANGE_MONEY', 'MP260612.1000.A00006', 'CONFIRMED', '2026-06-12 10:05:00+03', '2026-06-12 10:00:00+03')
on conflict (id) do nothing;

-- Events for Club Basket
insert into event (id, user_id, payment_id, club_id, creation_instant, comment)
values ('event_basket_cotisation_1', 'user_jean', 'pay_basket_cotisation_1', 'basket', '2026-06-02 15:30:00+03', null),
       ('event_basket_cotisation_2', 'user_marie', 'pay_basket_cotisation_2', 'basket', '2026-06-06 11:30:00+03', null),
       ('event_basket_withdrawal_1', 'user_marie', 'pay_basket_withdrawal_1', 'basket', '2026-06-12 10:00:00+03', 'Achat ballons')
on conflict (id) do nothing;

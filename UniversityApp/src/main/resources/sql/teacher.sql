CREATE TABLE IF NOT EXISTS public.teacher
(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL
);

insert into public.teacher (id, first_name, last_name) values (1, 'Orel', 'Wollen');
insert into public.teacher (id, first_name, last_name) values (2, 'Valencia', 'Purvess');
insert into public.teacher (id, first_name, last_name) values (3, 'Afton', 'Shortland');
insert into public.teacher (id, first_name, last_name) values (4, 'Brok', 'Forsyde');
insert into public.teacher (id, first_name, last_name) values (5, 'Boony', 'Foran');
insert into public.teacher (id, first_name, last_name) values (6, 'Darcy', 'Morigan');
insert into public.teacher (id, first_name, last_name) values (7, 'Felicle', 'Carnoghan');
insert into public.teacher (id, first_name, last_name) values (8, 'Leanora', 'McGinney');
insert into public.teacher (id, first_name, last_name) values (9, 'Sile', 'Cockley');
insert into public.teacher (id, first_name, last_name) values (10, 'Stanley', 'Westgate');
insert into public.teacher (id, first_name, last_name) values (11, 'Niel', 'Willshaw');
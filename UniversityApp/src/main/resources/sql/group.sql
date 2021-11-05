CREATE TABLE IF NOT EXISTS public.ugroup
(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(50) NOT NULL,
	faculty_id INTEGER REFERENCES public.faculty (id)
);

INSERT INTO public.ugroup (id, name, faculty_id) VALUES (1, 'Economic1', 1);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (2, 'Economic2', 1);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (3, 'Economic3', 1);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (4, 'Juristical1', 2);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (5, 'Juristical2', 2);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (6, 'Juristical3', 2);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (7, 'Historical1', 3);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (8, 'Historical2', 3);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (9, 'Historical3', 3);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (10, 'Biological1', 4);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (11, 'Biological2', 4);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (12, 'Biological3', 4);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (13, 'Geographical1', 5);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (14, 'Geographical2', 5);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (15, 'Geographical3', 5);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (16, 'Information technologies1', 6);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (17, 'Information technologies2', 6);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (18, 'Information technologies3', 6);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (19, 'The international economy1', 7);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (20, 'The international economy2', 7);
INSERT INTO public.ugroup (id, name, faculty_id) VALUES (21, 'The international economy3', 7);
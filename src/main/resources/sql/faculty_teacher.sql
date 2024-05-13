CREATE TABLE IF NOT EXISTS public.faculty_teacher
(
	faculty_id INTEGER REFERENCES faculty (id) NOT NULL,
	teacher_id INTEGER REFERENCES teacher (id) NOT NULL,
	PRIMARY KEY (faculty_id, teacher_id)
);

insert into public.faculty_teacher (faculty_id, teacher_id) values (1, 1);
insert into public.faculty_teacher (faculty_id, teacher_id) values (2, 2);
insert into public.faculty_teacher (faculty_id, teacher_id) values (3, 3);
insert into public.faculty_teacher (faculty_id, teacher_id) values (4, 4);
insert into public.faculty_teacher (faculty_id, teacher_id) values (5, 5);
insert into public.faculty_teacher (faculty_id, teacher_id) values (6, 6);
insert into public.faculty_teacher (faculty_id, teacher_id) values (7, 7);
insert into public.faculty_teacher (faculty_id, teacher_id) values (1, 8);
insert into public.faculty_teacher (faculty_id, teacher_id) values (2, 9);
insert into public.faculty_teacher (faculty_id, teacher_id) values (3, 10);
insert into public.faculty_teacher (faculty_id, teacher_id) values (4, 11);
insert into public.faculty_teacher (faculty_id, teacher_id) values (5, 1);
insert into public.faculty_teacher (faculty_id, teacher_id) values (6, 2);
insert into public.faculty_teacher (faculty_id, teacher_id) values (7, 3);
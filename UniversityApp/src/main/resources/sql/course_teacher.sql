CREATE TABLE IF NOT EXISTS public.course_teacher
(
	course_id INTEGER REFERENCES course (id) NOT NULL,
	teacher_id INTEGER REFERENCES teacher (id) NOT NULL,
	PRIMARY KEY (course_id, teacher_id)
);

insert into public.course_teacher (course_id, teacher_id) values (1, 1);
insert into public.course_teacher (course_id, teacher_id) values (2, 2);
insert into public.course_teacher (course_id, teacher_id) values (3, 3);
insert into public.course_teacher (course_id, teacher_id) values (4, 4);
insert into public.course_teacher (course_id, teacher_id) values (5, 5);
insert into public.course_teacher (course_id, teacher_id) values (6, 6);
insert into public.course_teacher (course_id, teacher_id) values (7, 7);
insert into public.course_teacher (course_id, teacher_id) values (8, 8);
insert into public.course_teacher (course_id, teacher_id) values (9, 9);
insert into public.course_teacher (course_id, teacher_id) values (10, 10);
insert into public.course_teacher (course_id, teacher_id) values (1, 11);
insert into public.course_teacher (course_id, teacher_id) values (3, 2);
insert into public.course_teacher (course_id, teacher_id) values (4, 3);
insert into public.course_teacher (course_id, teacher_id) values (4, 1);
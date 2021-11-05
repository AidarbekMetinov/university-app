CREATE TABLE IF NOT EXISTS public.lecture
(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	audience_id INTEGER REFERENCES audience (id),
	teacher_id INTEGER REFERENCES teacher (id),
	course_id INTEGER REFERENCES course (id),
	date_time TIMESTAMP NOT NULL
);

insert into public.lecture (id, audience_id, teacher_id, course_id, date_time) values (1, 2, 2, 2, '2026-04-07 04:40:20');
insert into public.lecture (id, audience_id, teacher_id, course_id, date_time) values (2, 3, 3, 3, '2025-02-17 04:40:20');
insert into public.lecture (id, audience_id, teacher_id, course_id, date_time) values (3, 4, 4, 4, '2024-11-30 04:40:20');
insert into public.lecture (id, audience_id, teacher_id, course_id, date_time) values (4, 5, 5, 5, '2025-10-04 04:40:20');

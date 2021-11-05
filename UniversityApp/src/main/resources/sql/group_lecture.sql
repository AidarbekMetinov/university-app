CREATE TABLE IF NOT EXISTS public.group_lecture
(
	group_id INTEGER REFERENCES ugroup (id) NOT NULL,
	lecture_id INTEGER REFERENCES lecture (id) NOT NULL,
	PRIMARY KEY (group_id, lecture_id)
);

INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (2, 1);
INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (3, 1);
INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (5, 2);
INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (6, 3);
INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (7, 4);
INSERT INTO public.group_lecture (group_id, lecture_id) VALUES (9, 4);
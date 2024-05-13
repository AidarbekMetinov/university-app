CREATE TABLE IF NOT EXISTS public.audience
(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	audience_n INTEGER UNIQUE NOT NULL,
	seat INTEGER NOT NULL,
	has_projector BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (1, 1, 30, true);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (2, 2, 30, false);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (3, 3, 30, false);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (4, 4, 30, true);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (5, 5, 30, true);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (6, 6, 30, false);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (7, 7, 30, false);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (8, 8, 30, false);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (9, 9, 30, true);
INSERT INTO public.audience (id, audience_n, seat, has_projector) VALUES (10, 10, 30, false);
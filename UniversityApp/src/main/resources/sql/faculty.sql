CREATE TABLE IF NOT EXISTS public.faculty
(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(2000) NOT NULL
);

INSERT INTO public.faculty (id, name, description) VALUES (1, 'Economic', 'An economist is a specialist in the field of economics,
	economic expert. Economists are called scientists (that is, specialists in the field of economic science),
	and practitioners who work in the field of research, planning and management of the economic activities of the enterprise.
	By the way, in 2021, the ProfGid vocational guidance center developed an accurate vocational guidance test. He himself will 
	tell you which professions suit you, will give an opinion about your personality type and intelligence.');
INSERT INTO public.faculty (id, name, description) VALUES (2, 'Juristical', 'Juristical education is a body of knowledge about the state,
	management, law, the presence of which gives rise to the professional practice of legal activity. ');
INSERT INTO public.faculty (id, name, description) VALUES (3, 'Historical', 'If you are interested in the past since childhood, you can 
	easily remember dates and like to explore the causes of certain events, you should think about the profession of a historian. 
	These are specialists who can restore the picture of the past of different countries and even of all mankind.');
INSERT INTO public.faculty (id, name, description) VALUES (4, 'Biological', 'The Faculty of Biology trains a wide range of personnel in
	various biological specialties, is the basis for scientific research in biology and related fields.');
INSERT INTO public.faculty (id, name, description) VALUES (5, 'Geographical', 'Geography is a complex of natural and social sciences 
	that study the structure, functioning and evolution of the geographic shell, interaction and distribution in space of natural 
	and natural-social geosystems and their components.');
INSERT INTO public.faculty (id, name, description) VALUES (6, 'Information technologies', 'Information technology is the study, design, 
	development, implementation, support or management of computer-based information systems�particularly software applications 
	and computer hardware.');
INSERT INTO public.faculty (id, name, description) VALUES (7, 'The international economy', 'International economics is a field of study 
	that assesses the implications of international trade, international investment, and international borrowing and lending. 
	There are two broad subfields within the discipline: international trade and international finance.');
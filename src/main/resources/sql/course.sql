CREATE TABLE IF NOT EXISTS public.course
(
	id BIGSERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(2000) NOT NULL
);

INSERT INTO public.course (id, name, description) VALUES (1, 'Mathematics', 'Mathematics (knowledge, study, learning) 
	includes the study of such topics as quantity (number theory), structure (algebra), 
	space (geometry), and change (analysis). It has no generally accepted definition.');
INSERT INTO public.course (id, name, description) VALUES (2, 'History', 'History (inquiry, knowledge acquired by investigation) 
	is the study of the past. Events occurring before the invention of writing systems are considered prehistory. 
	"History" is an umbrella term that relates to past events as well as the memory, discovery, collection, organization, 
	presentation, and interpretation of information about these events. Historians place the past in context using historical 
	sources such as written documents, oral accounts, ecological markers, and material objects including art and artifacts.');
INSERT INTO public.course (id, name, description) VALUES (3, 'Geography', 'Geography ("earth description") is a field of science devoted 
	to the study of the lands, features, inhabitants, and phenomena of the Earth and planets. The first person to use the word 
	geography was Eratosthenes. Geography is an all-encompassing discipline that seeks an understanding of Earth and its human 
	and natural complexities not merely where objects are, but also how they have changed and come to be.');
INSERT INTO public.course (id, name, description) VALUES (4, 'Economy', 'An economy ("household" and "manage") is an area of the 
	production, distribution and trade, as well as consumption of goods and services by different agents. In general, it is 
	defined "as a social domain that emphasize the practices, discourses, and material expressions associated with the production, 
	use, and management of resources".');
INSERT INTO public.course (id, name, description) VALUES (5, 'Natural history', 'Natural history is a domain of inquiry involving 
	organisms, including animals, fungi, and plants, in their natural environment, leaning more towards observational than 
	experimental methods of study. A person who studies natural history is called a naturalist or natural historian.');
INSERT INTO public.course (id, name, description) VALUES (6, 'Biology', 'Biology is the natural science that studies life and living 
	organisms, including their physical structure, chemical processes, molecular interactions, physiological mechanisms, 
	development and evolution. Despite the complexity of the science, certain unifying concepts consolidate it into a single, 
	coherent field. Biology recognizes the cell as the basic unit of life, genes as the basic unit of heredity, and evolution as 
	the engine that propels the creation and extinction of species. Living organisms are open systems that survive by transforming 
	energy and decreasing their local entropy to maintain a stable and vital condition defined as homeostasis.');
INSERT INTO public.course (id, name, description) VALUES (7, 'Physics', 'Physics ("knowledge of nature", "nature") is the natural science 
	that studies matter, its motion and behavior through space and time, and the related entities of energy and force. Physics is 
	one of the most fundamental scientific disciplines, and its main goal is to understand how the universe behaves.');
INSERT INTO public.course (id, name, description) VALUES (8, 'Chemistry', 'Chemistry is the scientific discipline involved with elements 
	and compounds composed of atoms, molecules and ions: their composition, structure, properties, behavior and the changes they 
	undergo during a reaction with other substances.');
INSERT INTO public.course (id, name, description) VALUES (9, 'Ecology', 'Ecology ("house" and "study of") is a branch of biology 
	concerning the spatial and temporal patterns of the distribution and abundance of organisms, including the causes and 
	consequences.');
INSERT INTO public.course (id, name, description) VALUES (10, 'Astronomy', 'Astronomy (literally meaning the science that studies the laws 
	of the stars) is a natural science that studies celestial objects and phenomena. It uses mathematics, physics, and chemistry 
	in order to explain their origin and evolution. Objects of interest include planets, moons, stars, nebulae, galaxies, and 
	comets. Relevant phenomena include supernova explosions, gamma ray bursts, quasars, blazars, pulsars, and cosmic microwave 
	background radiation. More generally, astronomy studies everything that originates outside Earths atmosphere. Cosmology is a 
	branch of astronomy that studies the universe as a whole.');

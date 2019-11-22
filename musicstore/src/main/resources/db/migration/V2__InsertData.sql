--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1 (Debian 12.1-1.pgdg100+1)
-- Dumped by pg_dump version 12.1 (Debian 12.1-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: albums; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Data for Name: artists; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Data for Name: genres; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Data for Name: roles_perms; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Data for Name: tracks; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: music
--



--
-- Name: albums_id_seq; Type: SEQUENCE SET; Schema: public; Owner: music
--

SELECT pg_catalog.setval('public.albums_id_seq', 1, false);


--
-- Name: artists_id_seq; Type: SEQUENCE SET; Schema: public; Owner: music
--

SELECT pg_catalog.setval('public.artists_id_seq', 1, false);


--
-- Name: genres_id_seq; Type: SEQUENCE SET; Schema: public; Owner: music
--

SELECT pg_catalog.setval('public.genres_id_seq', 1, false);


--
-- Name: tracks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: music
--

SELECT pg_catalog.setval('public.tracks_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--


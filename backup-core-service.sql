--
-- PostgreSQL database dump
--

\restrict 25kzZiaSb9g5CJgrj9yAnsWZRgLtGLgj5pXyPBqYWkWR6Ey0mMOb72IOS1LNLyR

-- Dumped from database version 14.20 (Debian 14.20-1.pgdg13+1)
-- Dumped by pg_dump version 14.20 (Debian 14.20-1.pgdg13+1)

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
-- Name: pg_trgm; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;


--
-- Name: EXTENSION pg_trgm; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';


--
-- Name: func_filter_job_ad_outside(text, boolean, bigint[], bigint[], text, boolean, integer, integer, boolean, text, boolean, bigint, integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_filter_job_ad_outside(p_keyword text DEFAULT NULL::text, p_is_show_expired boolean DEFAULT false, p_career_ids bigint[] DEFAULT NULL::bigint[], p_level_ids bigint[] DEFAULT NULL::bigint[], p_job_ad_location text DEFAULT NULL::text, p_is_remote boolean DEFAULT NULL::boolean, p_salary_from integer DEFAULT NULL::integer, p_salary_to integer DEFAULT NULL::integer, p_negotiable boolean DEFAULT NULL::boolean, p_job_type text DEFAULT NULL::text, p_search_org boolean DEFAULT NULL::boolean, p_org_id bigint DEFAULT NULL::bigint, p_limit integer DEFAULT 10, p_offset integer DEFAULT 0, p_sort_by text DEFAULT 'created_at'::text, p_sort_direction text DEFAULT 'desc'::text) RETURNS TABLE(id bigint, code character varying, title character varying, orgid bigint, positionid bigint, jobtype character varying, duedate timestamp without time zone, quantity integer, salarytype character varying, salaryfrom integer, salaryto integer, currencytype character varying, keyword character varying, description text, requirement text, benefit text, hrcontactid bigint, jobadstatus character varying, ispublic boolean, isautosendemail boolean, emailtemplateid bigint, isremote boolean, isalllevel boolean, keycodeinternal character varying, isactive boolean, isdeleted boolean, createdby character varying, createdat timestamp without time zone, updatedby character varying, updatedat timestamp without time zone, viewcount bigint)
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN QUERY EXECUTE format($f$
        SELECT DISTINCT ja.id,
                        ja.code,
                        ja.title,
                        ja.org_id,
                        ja.position_id,
                        ja.job_type,
                        ja.due_date,
                        ja.quantity,
                        ja.salary_type,
                        ja.salary_from,
                        case when ja.salary_to is null then 0 else ja.salary_to end as salary_to,
                        ja.currency_type,
                        ja.keyword,
                        ja.description,
                        ja.requirement,
                        ja.benefit,
                        ja.hr_contact_id,
                        ja.job_ad_status,
                        ja.is_public,
                        ja.is_auto_send_email,
                        ja.email_template_id,
                        ja.is_remote,
                        ja.is_all_level,
                        ja.key_code_internal,
                        ja.is_active,
                        ja.is_deleted,
                        ja.created_by,
                        ja.created_at,
                        ja.updated_by,
                        ja.updated_at,
                        case when jas.view_count is null then 0 else jas.view_count end as view_count
        FROM job_ad ja
        LEFT JOIN job_ad_career jac ON jac.job_ad_id = ja.id
        LEFT JOIN job_ad_level jal ON jal.job_ad_id = ja.id
        LEFT JOIN job_ad_work_location jawl ON jawl.job_ad_id = ja.id
        LEFT JOIN organization_address oa ON oa.id = jawl.work_location_id
        LEFT JOIN job_ad_statistic jas ON jas.job_ad_id = ja.id
        JOIN organization o ON o.id = ja.org_id AND o.is_active = true
        WHERE ja.is_public = true
          AND (%L = true OR ja.job_ad_status = 'OPEN')
          AND (%L = true OR ja.due_date >= CURRENT_DATE)
          AND (%L IS NULL OR jac.career_id = ANY(%L))
          AND (%L IS NULL OR jal.level_id = ANY(%L))
          AND (%L IS NULL OR (oa.province IS NOT NULL AND lower(oa.province) LIKE lower('%%' || %L || '%%')))
          AND (%L IS NULL OR ja.is_remote = %L)
          AND (%L IS NULL OR (ja.salary_from IS NOT NULL AND ja.salary_from <= %L))
          AND (%L IS NULL OR (ja.salary_to IS NOT NULL AND %L <= ja.salary_to))
          AND (%L IS NULL OR ja.salary_type = 'NEGOTIABLE')
          AND (%L IS NULL OR ja.job_type = %L)
          AND (%L IS NULL OR ja.org_id = %L)
          AND (%L IS NULL
               OR (%L = true AND lower(o.name) LIKE lower('%%' || %L || '%%'))
               OR ts_rank(to_tsvector(ja.title || ' ' || replace(ja.keyword, ';', ' ')), plainto_tsquery(%L)) > 0.05
               OR similarity(ja.title || ' ' || replace(ja.keyword, ';', ' '), %L) > 0.3)
        ORDER BY %I %s, created_at DESC
        LIMIT %s OFFSET %s
        $f$,
        p_is_show_expired,
        p_is_show_expired,
        p_career_ids, p_career_ids,
        p_level_ids, p_level_ids,
        p_job_ad_location, p_job_ad_location,
        p_is_remote, p_is_remote,
        p_salary_from, p_salary_from,
        p_salary_to, p_salary_to,
        p_negotiable,
        p_job_type, p_job_type,
        p_org_id, p_org_id,
        p_keyword,
        p_search_org, p_keyword,
        p_keyword, p_keyword,
        p_sort_by, p_sort_direction,
        p_limit, p_offset
    );
END;
$_$;


ALTER FUNCTION public.func_filter_job_ad_outside(p_keyword text, p_is_show_expired boolean, p_career_ids bigint[], p_level_ids bigint[], p_job_ad_location text, p_is_remote boolean, p_salary_from integer, p_salary_to integer, p_negotiable boolean, p_job_type text, p_search_org boolean, p_org_id bigint, p_limit integer, p_offset integer, p_sort_by text, p_sort_direction text) OWNER TO postgres;

--
-- Name: func_working_location_outside(text, boolean, bigint[], bigint[], text, boolean, integer, integer, boolean, text, boolean, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.func_working_location_outside(p_keyword text DEFAULT NULL::text, p_is_show_expired boolean DEFAULT false, p_career_ids bigint[] DEFAULT NULL::bigint[], p_level_ids bigint[] DEFAULT NULL::bigint[], p_job_ad_location text DEFAULT NULL::text, p_is_remote boolean DEFAULT NULL::boolean, p_salary_from integer DEFAULT NULL::integer, p_salary_to integer DEFAULT NULL::integer, p_negotiable boolean DEFAULT NULL::boolean, p_job_type text DEFAULT NULL::text, p_search_org boolean DEFAULT NULL::boolean, p_org_id bigint DEFAULT NULL::bigint) RETURNS TABLE(id bigint, isremote boolean, province character varying)
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN QUERY EXECUTE format($f$
        SELECT DISTINCT ja.id,
                        ja.is_remote,
                        oa.province
        FROM job_ad ja
        LEFT JOIN job_ad_career jac ON jac.job_ad_id = ja.id
        LEFT JOIN job_ad_level jal ON jal.job_ad_id = ja.id
        LEFT JOIN job_ad_work_location jawl ON jawl.job_ad_id = ja.id
        LEFT JOIN organization_address oa ON oa.id = jawl.work_location_id
        JOIN organization o ON o.id = ja.org_id AND o.is_active = true
        WHERE ja.is_public = true
          AND ja.job_ad_status = 'OPEN'
          AND (%L = true OR ja.due_date >= CURRENT_DATE)
          AND (%L IS NULL OR jac.career_id = ANY(%L))
          AND (%L IS NULL OR jal.level_id = ANY(%L))
          AND (%L IS NULL OR (oa.province IS NOT NULL AND lower(oa.province) LIKE lower('%%' || %L || '%%')))
          AND (%L IS NULL OR ja.is_remote = %L)
          AND (%L IS NULL OR (ja.salary_from IS NOT NULL AND ja.salary_from <= %L))
          AND (%L IS NULL OR (ja.salary_to IS NOT NULL AND %L <= ja.salary_to))
          AND (%L IS NULL OR ja.salary_type = 'NEGOTIABLE')
          AND (%L IS NULL OR ja.job_type = %L)
          AND (%L IS NULL OR ja.org_id = %L)
          AND (%L IS NULL
               OR (%L = true AND lower(o.name) LIKE lower('%%' || %L || '%%'))
               OR ts_rank(to_tsvector(ja.title || ' ' || replace(ja.keyword, ';', ' ')), plainto_tsquery(%L)) > 0.05
               OR similarity(ja.title || ' ' || replace(ja.keyword, ';', ' '), %L) > 0.3)
        $f$,
        p_is_show_expired,
        p_career_ids, p_career_ids,
        p_level_ids, p_level_ids,
        p_job_ad_location, p_job_ad_location,
        p_is_remote, p_is_remote,
        p_salary_from, p_salary_from,
        p_salary_to, p_salary_to,
        p_negotiable,
        p_job_type, p_job_type,
        p_org_id, p_org_id,
        p_keyword,
        p_search_org, p_keyword,
        p_keyword, p_keyword
    );
END;
$_$;


ALTER FUNCTION public.func_working_location_outside(p_keyword text, p_is_show_expired boolean, p_career_ids bigint[], p_level_ids bigint[], p_job_ad_location text, p_is_remote boolean, p_salary_from integer, p_salary_to integer, p_negotiable boolean, p_job_type text, p_search_org boolean, p_org_id bigint) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: attach_file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attach_file (
    id bigint NOT NULL,
    original_filename character varying(255) NOT NULL,
    base_filename character varying(255) NOT NULL,
    extension character varying(50) NOT NULL,
    filename character varying(255) NOT NULL,
    format character varying(100),
    resource_type character varying(100) NOT NULL,
    secure_url character varying(500) NOT NULL,
    type character varying(100) NOT NULL,
    url character varying(500) NOT NULL,
    public_id character varying(255) NOT NULL,
    folder character varying(255),
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.attach_file OWNER TO postgres;

--
-- Name: attach_file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.attach_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attach_file_id_seq OWNER TO postgres;

--
-- Name: attach_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.attach_file_id_seq OWNED BY public.attach_file.id;


--
-- Name: calendar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.calendar (
    id bigint NOT NULL,
    job_ad_process_id bigint NOT NULL,
    calendar_type character varying(100) NOT NULL,
    join_same_time boolean DEFAULT false,
    date date NOT NULL,
    time_from time without time zone NOT NULL,
    duration_minutes integer NOT NULL,
    org_address_id bigint,
    meeting_link character varying(500),
    note text,
    creator_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.calendar OWNER TO postgres;

--
-- Name: calendar_candidate_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.calendar_candidate_info (
    id bigint NOT NULL,
    calendar_id bigint NOT NULL,
    candidate_info_id bigint NOT NULL,
    time_from time without time zone NOT NULL,
    time_to time without time zone NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100),
    date date NOT NULL
);


ALTER TABLE public.calendar_candidate_info OWNER TO postgres;

--
-- Name: calendar_candidate_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.calendar_candidate_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.calendar_candidate_info_id_seq OWNER TO postgres;

--
-- Name: calendar_candidate_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.calendar_candidate_info_id_seq OWNED BY public.calendar_candidate_info.id;


--
-- Name: calendar_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.calendar_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.calendar_id_seq OWNER TO postgres;

--
-- Name: calendar_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.calendar_id_seq OWNED BY public.calendar.id;


--
-- Name: candidate_evaluation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.candidate_evaluation (
    id bigint NOT NULL,
    job_ad_process_candidate_id bigint NOT NULL,
    evaluator_id bigint NOT NULL,
    comments text NOT NULL,
    score numeric,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.candidate_evaluation OWNER TO postgres;

--
-- Name: candidate_evaluation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.candidate_evaluation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.candidate_evaluation_id_seq OWNER TO postgres;

--
-- Name: candidate_evaluation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.candidate_evaluation_id_seq OWNED BY public.candidate_evaluation.id;


--
-- Name: candidate_info_apply; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.candidate_info_apply (
    id bigint NOT NULL,
    candidate_id bigint NOT NULL,
    full_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(50),
    cv_file_id bigint NOT NULL,
    cover_letter text,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.candidate_info_apply OWNER TO postgres;

--
-- Name: candidate_info_apply_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.candidate_info_apply_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.candidate_info_apply_id_seq OWNER TO postgres;

--
-- Name: candidate_info_apply_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.candidate_info_apply_id_seq OWNED BY public.candidate_info_apply.id;


--
-- Name: candidate_summary_org; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.candidate_summary_org (
    id bigint NOT NULL,
    skill text,
    level_id bigint NOT NULL,
    org_id bigint NOT NULL,
    candidate_info_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.candidate_summary_org OWNER TO postgres;

--
-- Name: candidate_summary_org_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.candidate_summary_org_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.candidate_summary_org_id_seq OWNER TO postgres;

--
-- Name: candidate_summary_org_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.candidate_summary_org_id_seq OWNED BY public.candidate_summary_org.id;


--
-- Name: careers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.careers (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.careers OWNER TO postgres;

--
-- Name: department; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.department (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    org_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.department OWNER TO postgres;

--
-- Name: department_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.department_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.department_id_seq OWNER TO postgres;

--
-- Name: department_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.department_id_seq OWNED BY public.department.id;


--
-- Name: failed_rollback; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.failed_rollback (
    id bigint NOT NULL,
    type character varying(100) NOT NULL,
    payload text NOT NULL,
    error_message text,
    status boolean DEFAULT false,
    retry_count integer DEFAULT 0,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.failed_rollback OWNER TO postgres;

--
-- Name: failed_rollback_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.failed_rollback_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.failed_rollback_id_seq OWNER TO postgres;

--
-- Name: failed_rollback_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.failed_rollback_id_seq OWNED BY public.failed_rollback.id;


--
-- Name: industry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.industry (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.industry OWNER TO postgres;

--
-- Name: industry_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.industry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.industry_id_seq OWNER TO postgres;

--
-- Name: industry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.industry_id_seq OWNED BY public.industry.id;


--
-- Name: industry_sub_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.industry_sub_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.industry_sub_id_seq OWNER TO postgres;

--
-- Name: industry_sub_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.industry_sub_id_seq OWNED BY public.careers.id;


--
-- Name: interview_panel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.interview_panel (
    id bigint NOT NULL,
    calendar_id bigint NOT NULL,
    interviewer_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.interview_panel OWNER TO postgres;

--
-- Name: interview_panel_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.interview_panel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.interview_panel_id_seq OWNER TO postgres;

--
-- Name: interview_panel_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.interview_panel_id_seq OWNED BY public.interview_panel.id;


--
-- Name: job_ad; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    title character varying(255) NOT NULL,
    org_id bigint NOT NULL,
    position_id bigint NOT NULL,
    job_type character varying(100) NOT NULL,
    due_date timestamp without time zone NOT NULL,
    quantity integer DEFAULT 1,
    salary_type character varying(100) NOT NULL,
    salary_from integer,
    salary_to integer,
    currency_type character varying(50) NOT NULL,
    keyword character varying(255),
    description text,
    requirement text,
    benefit text,
    hr_contact_id bigint NOT NULL,
    job_ad_status character varying(100) NOT NULL,
    is_public boolean DEFAULT true,
    is_auto_send_email boolean DEFAULT false,
    email_template_id bigint,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100),
    is_remote boolean DEFAULT false,
    is_all_level boolean DEFAULT false,
    key_code_internal character varying(100)
);


ALTER TABLE public.job_ad OWNER TO postgres;

--
-- Name: job_ad_candidate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_candidate (
    id bigint NOT NULL,
    job_ad_id bigint NOT NULL,
    candidate_info_id bigint NOT NULL,
    apply_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    candidate_status character varying(100) NOT NULL,
    eliminate_reason_type text,
    eliminate_reason_detail text,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100),
    onboard_date timestamp without time zone,
    eliminate_date timestamp without time zone
);


ALTER TABLE public.job_ad_candidate OWNER TO postgres;

--
-- Name: job_ad_candidate_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_candidate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_candidate_id_seq OWNER TO postgres;

--
-- Name: job_ad_candidate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_candidate_id_seq OWNED BY public.job_ad_candidate.id;


--
-- Name: job_ad_career; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_career (
    id bigint NOT NULL,
    career_id bigint NOT NULL,
    job_ad_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_ad_career OWNER TO postgres;

--
-- Name: job_ad_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_id_seq OWNER TO postgres;

--
-- Name: job_ad_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_id_seq OWNED BY public.job_ad.id;


--
-- Name: job_ad_industry_sub_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_industry_sub_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_industry_sub_id_seq OWNER TO postgres;

--
-- Name: job_ad_industry_sub_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_industry_sub_id_seq OWNED BY public.job_ad_career.id;


--
-- Name: job_ad_level; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_level (
    id bigint NOT NULL,
    job_ad_id bigint NOT NULL,
    level_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_ad_level OWNER TO postgres;

--
-- Name: job_ad_level_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_level_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_level_id_seq OWNER TO postgres;

--
-- Name: job_ad_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_level_id_seq OWNED BY public.job_ad_level.id;


--
-- Name: job_ad_process; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_process (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    sort_order integer NOT NULL,
    job_ad_id bigint NOT NULL,
    process_type_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_ad_process OWNER TO postgres;

--
-- Name: job_ad_process_candidate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_process_candidate (
    id bigint NOT NULL,
    job_ad_process_id bigint NOT NULL,
    job_ad_candidate_id bigint NOT NULL,
    action_date timestamp without time zone,
    is_current_process boolean DEFAULT false,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_ad_process_candidate OWNER TO postgres;

--
-- Name: job_ad_process_candidate_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_process_candidate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_process_candidate_id_seq OWNER TO postgres;

--
-- Name: job_ad_process_candidate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_process_candidate_id_seq OWNED BY public.job_ad_process_candidate.id;


--
-- Name: job_ad_process_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_process_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_process_id_seq OWNER TO postgres;

--
-- Name: job_ad_process_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_process_id_seq OWNED BY public.job_ad_process.id;


--
-- Name: job_ad_statistic; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_statistic (
    id bigint NOT NULL,
    job_ad_id bigint NOT NULL,
    view_count bigint DEFAULT 0,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_ad_statistic OWNER TO postgres;

--
-- Name: job_ad_statistic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_statistic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_statistic_id_seq OWNER TO postgres;

--
-- Name: job_ad_statistic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_statistic_id_seq OWNED BY public.job_ad_statistic.id;


--
-- Name: job_ad_work_location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_ad_work_location (
    id bigint NOT NULL,
    job_ad_id bigint NOT NULL,
    work_location_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_ad_work_location OWNER TO postgres;

--
-- Name: job_ad_work_location_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_ad_work_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_ad_work_location_id_seq OWNER TO postgres;

--
-- Name: job_ad_work_location_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_ad_work_location_id_seq OWNED BY public.job_ad_work_location.id;


--
-- Name: job_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_config (
    id bigint NOT NULL,
    job_name character varying(100) NOT NULL,
    schedule_type character varying(50) NOT NULL,
    expression character varying(100) NOT NULL,
    description character varying(500),
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.job_config OWNER TO postgres;

--
-- Name: job_config_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.job_config_id_seq OWNER TO postgres;

--
-- Name: job_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.job_config_id_seq OWNED BY public.job_config.id;


--
-- Name: level; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.level (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    is_default boolean DEFAULT false,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.level OWNER TO postgres;

--
-- Name: level_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.level_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.level_id_seq OWNER TO postgres;

--
-- Name: level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.level_id_seq OWNED BY public.level.id;


--
-- Name: organization; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    logo_id bigint,
    cover_photo_id bigint,
    website character varying(255),
    staff_count_from integer,
    staff_count_to integer,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.organization OWNER TO postgres;

--
-- Name: organization_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_address (
    id bigint NOT NULL,
    org_id bigint NOT NULL,
    is_headquarter boolean DEFAULT false,
    province character varying(150) NOT NULL,
    district character varying(150),
    ward character varying(150),
    detail_address character varying(255) NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.organization_address OWNER TO postgres;

--
-- Name: organization_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.organization_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.organization_address_id_seq OWNER TO postgres;

--
-- Name: organization_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.organization_address_id_seq OWNED BY public.organization_address.id;


--
-- Name: organization_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.organization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.organization_id_seq OWNER TO postgres;

--
-- Name: organization_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.organization_id_seq OWNED BY public.organization.id;


--
-- Name: organization_industry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_industry (
    id bigint NOT NULL,
    org_id integer NOT NULL,
    industry_id integer NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.organization_industry OWNER TO postgres;

--
-- Name: organization_industry_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.organization_industry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.organization_industry_id_seq OWNER TO postgres;

--
-- Name: organization_industry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.organization_industry_id_seq OWNED BY public.organization_industry.id;


--
-- Name: position; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."position" (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    department_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public."position" OWNER TO postgres;

--
-- Name: position_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.position_id_seq OWNER TO postgres;

--
-- Name: position_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.position_id_seq OWNED BY public."position".id;


--
-- Name: position_process; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.position_process (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    position_id bigint NOT NULL,
    process_type_id bigint NOT NULL,
    sort_order integer NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.position_process OWNER TO postgres;

--
-- Name: position_process_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.position_process_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.position_process_id_seq OWNER TO postgres;

--
-- Name: position_process_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.position_process_id_seq OWNED BY public.position_process.id;


--
-- Name: process_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.process_type (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    sort_order integer DEFAULT 0,
    is_default boolean DEFAULT false,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.process_type OWNER TO postgres;

--
-- Name: process_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.process_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.process_type_id_seq OWNER TO postgres;

--
-- Name: process_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.process_type_id_seq OWNED BY public.process_type.id;


--
-- Name: search_history_outside; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.search_history_outside (
    id bigint NOT NULL,
    keyword character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    is_active boolean DEFAULT true,
    is_deleted boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    created_by character varying(100),
    updated_by character varying(100)
);


ALTER TABLE public.search_history_outside OWNER TO postgres;

--
-- Name: search_history_outside_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.search_history_outside_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.search_history_outside_id_seq OWNER TO postgres;

--
-- Name: search_history_outside_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.search_history_outside_id_seq OWNED BY public.search_history_outside.id;


--
-- Name: shedlock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shedlock (
    name character varying(64) NOT NULL,
    lock_until time without time zone NOT NULL,
    locked_at time without time zone NOT NULL,
    locked_by character varying(255) NOT NULL
);


ALTER TABLE public.shedlock OWNER TO postgres;

--
-- Name: attach_file id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attach_file ALTER COLUMN id SET DEFAULT nextval('public.attach_file_id_seq'::regclass);


--
-- Name: calendar id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar ALTER COLUMN id SET DEFAULT nextval('public.calendar_id_seq'::regclass);


--
-- Name: calendar_candidate_info id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar_candidate_info ALTER COLUMN id SET DEFAULT nextval('public.calendar_candidate_info_id_seq'::regclass);


--
-- Name: candidate_evaluation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_evaluation ALTER COLUMN id SET DEFAULT nextval('public.candidate_evaluation_id_seq'::regclass);


--
-- Name: candidate_info_apply id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_info_apply ALTER COLUMN id SET DEFAULT nextval('public.candidate_info_apply_id_seq'::regclass);


--
-- Name: candidate_summary_org id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_summary_org ALTER COLUMN id SET DEFAULT nextval('public.candidate_summary_org_id_seq'::regclass);


--
-- Name: careers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.careers ALTER COLUMN id SET DEFAULT nextval('public.industry_sub_id_seq'::regclass);


--
-- Name: department id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.department ALTER COLUMN id SET DEFAULT nextval('public.department_id_seq'::regclass);


--
-- Name: failed_rollback id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.failed_rollback ALTER COLUMN id SET DEFAULT nextval('public.failed_rollback_id_seq'::regclass);


--
-- Name: industry id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.industry ALTER COLUMN id SET DEFAULT nextval('public.industry_id_seq'::regclass);


--
-- Name: interview_panel id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interview_panel ALTER COLUMN id SET DEFAULT nextval('public.interview_panel_id_seq'::regclass);


--
-- Name: job_ad id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad ALTER COLUMN id SET DEFAULT nextval('public.job_ad_id_seq'::regclass);


--
-- Name: job_ad_candidate id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_candidate ALTER COLUMN id SET DEFAULT nextval('public.job_ad_candidate_id_seq'::regclass);


--
-- Name: job_ad_career id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_career ALTER COLUMN id SET DEFAULT nextval('public.job_ad_industry_sub_id_seq'::regclass);


--
-- Name: job_ad_level id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_level ALTER COLUMN id SET DEFAULT nextval('public.job_ad_level_id_seq'::regclass);


--
-- Name: job_ad_process id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process ALTER COLUMN id SET DEFAULT nextval('public.job_ad_process_id_seq'::regclass);


--
-- Name: job_ad_process_candidate id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process_candidate ALTER COLUMN id SET DEFAULT nextval('public.job_ad_process_candidate_id_seq'::regclass);


--
-- Name: job_ad_statistic id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_statistic ALTER COLUMN id SET DEFAULT nextval('public.job_ad_statistic_id_seq'::regclass);


--
-- Name: job_ad_work_location id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_work_location ALTER COLUMN id SET DEFAULT nextval('public.job_ad_work_location_id_seq'::regclass);


--
-- Name: job_config id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_config ALTER COLUMN id SET DEFAULT nextval('public.job_config_id_seq'::regclass);


--
-- Name: level id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.level ALTER COLUMN id SET DEFAULT nextval('public.level_id_seq'::regclass);


--
-- Name: organization id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization ALTER COLUMN id SET DEFAULT nextval('public.organization_id_seq'::regclass);


--
-- Name: organization_address id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_address ALTER COLUMN id SET DEFAULT nextval('public.organization_address_id_seq'::regclass);


--
-- Name: organization_industry id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_industry ALTER COLUMN id SET DEFAULT nextval('public.organization_industry_id_seq'::regclass);


--
-- Name: position id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."position" ALTER COLUMN id SET DEFAULT nextval('public.position_id_seq'::regclass);


--
-- Name: position_process id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.position_process ALTER COLUMN id SET DEFAULT nextval('public.position_process_id_seq'::regclass);


--
-- Name: process_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.process_type ALTER COLUMN id SET DEFAULT nextval('public.process_type_id_seq'::regclass);


--
-- Name: search_history_outside id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.search_history_outside ALTER COLUMN id SET DEFAULT nextval('public.search_history_outside_id_seq'::regclass);


--
-- Data for Name: attach_file; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attach_file (id, original_filename, base_filename, extension, filename, format, resource_type, secure_url, type, url, public_id, folder, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
2	cv-long.pdf	cv-long	pdf	cv-long_1764563715649	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764563717/cv-connect/vclong2003/cv-long_1764563715649.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764563717/cv-connect/vclong2003/cv-long_1764563715649.pdf	cv-connect/vclong2003/cv-long_1764563715649	cv-connect/vclong2003	t	f	2025-12-01 04:35:18.459318	\N	vclong2003	\N
3	Tran_Anh_Vu_1761901836479.pdf	Tran_Anh_Vu_1761901836479	pdf	Tran_Anh_Vu_1761901836479_1764564320542	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764564323/cv-connect/iamdha0706/Tran_Anh_Vu_1761901836479_1764564320542.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764564323/cv-connect/iamdha0706/Tran_Anh_Vu_1761901836479_1764564320542.pdf	cv-connect/iamdha0706/Tran_Anh_Vu_1761901836479_1764564320542	cv-connect/iamdha0706	t	f	2025-12-01 04:45:23.655598	\N	iamdha0706	\N
4	2.2 BÀI GIẢNG PPLNCKH (F_2024)-1.pdf	2.2 BÀI GIẢNG PPLNCKH (F_2024)-1	pdf	2.2 BÀI GIẢNG PPLNCKH (F_2024)-1_1764578290341	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764578293/cv-connect/admin/2.2%20B%C3%80I%20GI%E1%BA%A2NG%20PPLNCKH%20%28F_2024%29-1_1764578290341.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764578293/cv-connect/admin/2.2%20B%C3%80I%20GI%E1%BA%A2NG%20PPLNCKH%20%28F_2024%29-1_1764578290341.pdf	cv-connect/admin/2.2 BÀI GIẢNG PPLNCKH (F_2024)-1_1764578290341	cv-connect/admin	t	f	2025-12-01 08:38:15.225651	\N	admin	\N
5	d9ae2f47-bb3d-4176-a610-819e1f7544d1.jpg	d9ae2f47-bb3d-4176-a610-819e1f7544d1	jpg	d9ae2f47-bb3d-4176-a610-819e1f7544d1_1764606224635	jpg	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764606227/cv-connect/cvconnect/d9ae2f47-bb3d-4176-a610-819e1f7544d1_1764606224635.jpg	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764606227/cv-connect/cvconnect/d9ae2f47-bb3d-4176-a610-819e1f7544d1_1764606224635.jpg	cv-connect/cvconnect/d9ae2f47-bb3d-4176-a610-819e1f7544d1_1764606224635	cv-connect/cvconnect	t	f	2025-12-01 16:23:47.610672	\N	cvconnect	\N
6	6923c4b76d1171763951799.jpg	6923c4b76d1171763951799	jpg	6923c4b76d1171763951799_1764663816430	webp	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764663817/cv-connect/internal/6923c4b76d1171763951799_1764663816430.webp	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764663817/cv-connect/internal/6923c4b76d1171763951799_1764663816430.webp	cv-connect/internal/6923c4b76d1171763951799_1764663816430	cv-connect/internal	t	f	2025-12-02 08:23:38.486606	\N	internal	\N
7	woori-bank-vietnam-5ffd2222c019e.png	woori-bank-vietnam-5ffd2222c019e	png	woori-bank-vietnam-5ffd2222c019e_1764664249973	png	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764664251/cv-connect/internal/woori-bank-vietnam-5ffd2222c019e_1764664249973.png	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764664251/cv-connect/internal/woori-bank-vietnam-5ffd2222c019e_1764664249973.png	cv-connect/internal/woori-bank-vietnam-5ffd2222c019e_1764664249973	cv-connect/internal	t	f	2025-12-02 08:30:51.61459	\N	internal	\N
8	Angular_Trần-Anh-Tú_1761186085824.pdf	Angular_Trần-Anh-Tú_1761186085824	pdf	Angular_Trần-Anh-Tú_1761186085824_1764734283169	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764734285/cv-connect/quannm32/Angular_Tr%E1%BA%A7n-Anh-T%C3%BA_1761186085824_1764734283169.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764734285/cv-connect/quannm32/Angular_Tr%E1%BA%A7n-Anh-T%C3%BA_1761186085824_1764734283169.pdf	cv-connect/quannm32/Angular_Trần-Anh-Tú_1761186085824_1764734283169	cv-connect/quannm32	t	f	2025-12-03 03:58:05.974136	\N	quannm32	\N
9	cv-template.pdf	cv-template	pdf	cv-template_1764747083042	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764747084/cv-connect/duong2003nb/cv-template_1764747083042.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764747084/cv-connect/duong2003nb/cv-template_1764747083042.pdf	cv-connect/duong2003nb/cv-template_1764747083042	cv-connect/duong2003nb	t	f	2025-12-03 07:31:25.114649	\N	duong2003nb	\N
11	download.png	download	png	download_1764751697315	png	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1764751698/cv-connect/duong2003nb/download_1764751697315.png	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1764751698/cv-connect/duong2003nb/download_1764751697315.png	cv-connect/duong2003nb/download_1764751697315	cv-connect/duong2003nb	t	f	2025-12-03 08:48:18.853065	\N	duong2003nb	\N
12	Do_Thi_Hong_Tester.pdf	Do_Thi_Hong_Tester	pdf	Do_Thi_Hong_Tester_1765445610315	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1765445612/cv-connect/anhnt746/Do_Thi_Hong_Tester_1765445610315.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1765445612/cv-connect/anhnt746/Do_Thi_Hong_Tester_1765445610315.pdf	cv-connect/anhnt746/Do_Thi_Hong_Tester_1765445610315	cv-connect/anhnt746	t	f	2025-12-11 09:33:33.239368	\N	anhnt746	\N
13	1600w-c3Jw1yOiXJw.jpg	1600w-c3Jw1yOiXJw	jpg	1600w-c3Jw1yOiXJw_1765622344644	webp	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1765622346/cv-connect/internal/1600w-c3Jw1yOiXJw_1765622344644.webp	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1765622346/cv-connect/internal/1600w-c3Jw1yOiXJw_1765622344644.webp	cv-connect/internal/1600w-c3Jw1yOiXJw_1765622344644	cv-connect/internal	t	f	2025-12-13 10:39:07.581585	\N	internal	\N
14	c1394e4f7c5af88b679b49309e0bb6db.jpg	c1394e4f7c5af88b679b49309e0bb6db	jpg	c1394e4f7c5af88b679b49309e0bb6db_1765631443167	jpg	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1765631444/cv-connect/internal/c1394e4f7c5af88b679b49309e0bb6db_1765631443167.jpg	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1765631444/cv-connect/internal/c1394e4f7c5af88b679b49309e0bb6db_1765631443167.jpg	cv-connect/internal/c1394e4f7c5af88b679b49309e0bb6db_1765631443167	cv-connect/internal	t	f	2025-12-13 13:10:44.905616	\N	internal	\N
15	c1394e4f7c5af88b679b49309e0bb6db.jpg	c1394e4f7c5af88b679b49309e0bb6db	jpg	c1394e4f7c5af88b679b49309e0bb6db_1765631617047	jpg	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1765631618/cv-connect/internal/c1394e4f7c5af88b679b49309e0bb6db_1765631617047.jpg	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1765631618/cv-connect/internal/c1394e4f7c5af88b679b49309e0bb6db_1765631617047.jpg	cv-connect/internal/c1394e4f7c5af88b679b49309e0bb6db_1765631617047	cv-connect/internal	t	f	2025-12-13 13:13:38.788092	\N	internal	\N
16	cv-nguyen minh quan-backend.pdf	cv-nguyen minh quan-backend	pdf	cv-nguyen minh quan-backend_1766249715326	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1766249718/cv-connect/huykeo2022%40gmail.com/cv-nguyen%20minh%20quan-backend_1766249715326.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1766249718/cv-connect/huykeo2022%40gmail.com/cv-nguyen%20minh%20quan-backend_1766249715326.pdf	cv-connect/huykeo2022@gmail.com/cv-nguyen minh quan-backend_1766249715326	cv-connect/huykeo2022@gmail.com	t	f	2025-12-20 16:55:18.935026	\N	huykeo2022@gmail.com	\N
17	1600w-c3Jw1yOiXJw.jpg	1600w-c3Jw1yOiXJw	jpg	1600w-c3Jw1yOiXJw_1766391083045	webp	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1766391085/cv-connect/cvconnect/1600w-c3Jw1yOiXJw_1766391083045.webp	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1766391085/cv-connect/cvconnect/1600w-c3Jw1yOiXJw_1766391083045.webp	cv-connect/cvconnect/1600w-c3Jw1yOiXJw_1766391083045	cv-connect/cvconnect	t	f	2025-12-22 08:11:25.564793	\N	cvconnect	\N
18	Vy Tran Results.pdf	Vy Tran Results	pdf	Vy Tran Results_1766547083330	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1766547085/cv-connect/tranvy10122000%40gmail.com/Vy%20Tran%20Results_1766547083330.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1766547085/cv-connect/tranvy10122000%40gmail.com/Vy%20Tran%20Results_1766547083330.pdf	cv-connect/tranvy10122000@gmail.com/Vy Tran Results_1766547083330	cv-connect/tranvy10122000@gmail.com	t	f	2025-12-24 03:31:26.410301	\N	tranvy10122000@gmail.com	\N
19	CV_TRAN THAI BINH DUONG_FRONTEND DEVELOPER INTERN.pdf	CV_TRAN THAI BINH DUONG_FRONTEND DEVELOPER INTERN	pdf	CV_TRAN THAI BINH DUONG_FRONTEND DEVELOPER INTERN_1766933767831	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1766933769/cv-connect/duong2003nb/CV_TRAN%20THAI%20BINH%20DUONG_FRONTEND%20DEVELOPER%20INTERN_1766933767831.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1766933769/cv-connect/duong2003nb/CV_TRAN%20THAI%20BINH%20DUONG_FRONTEND%20DEVELOPER%20INTERN_1766933767831.pdf	cv-connect/duong2003nb/CV_TRAN THAI BINH DUONG_FRONTEND DEVELOPER INTERN_1766933767831	cv-connect/duong2003nb	t	f	2025-12-28 14:56:10.712042	\N	duong2003nb	\N
20	Omelette Logo without Text.png	Omelette Logo without Text	png	Omelette Logo without Text_1766979480889	png	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1766979482/cv-connect/internal/Omelette%20Logo%20without%20Text_1766979480889.png	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1766979482/cv-connect/internal/Omelette%20Logo%20without%20Text_1766979480889.png	cv-connect/internal/Omelette Logo without Text_1766979480889	cv-connect/internal	t	f	2025-12-29 03:38:02.784391	\N	internal	\N
21	Ảnh màn hình 2026-01-02 lúc 15.13.33.png	Ảnh màn hình 2026-01-02 lúc 15.13.33	png	Ảnh màn hình 2026-01-02 lúc 15.13.33_1767341996040	png	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767341999/cv-connect/internal/A%CC%89nh%20ma%CC%80n%20hi%CC%80nh%202026-01-02%20lu%CC%81c%2015.13.33_1767341996040.png	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767341999/cv-connect/internal/A%CC%89nh%20ma%CC%80n%20hi%CC%80nh%202026-01-02%20lu%CC%81c%2015.13.33_1767341996040.png	cv-connect/internal/Ảnh màn hình 2026-01-02 lúc 15.13.33_1767341996040	cv-connect/internal	t	f	2026-01-02 08:20:03.906488	\N	internal	\N
22	Ảnh màn hình 2026-01-02 lúc 15.13.57.png	Ảnh màn hình 2026-01-02 lúc 15.13.57	png	Ảnh màn hình 2026-01-02 lúc 15.13.57_1767342000123	png	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767342002/cv-connect/internal/A%CC%89nh%20ma%CC%80n%20hi%CC%80nh%202026-01-02%20lu%CC%81c%2015.13.57_1767342000123.png	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767342002/cv-connect/internal/A%CC%89nh%20ma%CC%80n%20hi%CC%80nh%202026-01-02%20lu%CC%81c%2015.13.57_1767342000123.png	cv-connect/internal/Ảnh màn hình 2026-01-02 lúc 15.13.57_1767342000123	cv-connect/internal	t	f	2026-01-02 08:20:03.920531	\N	internal	\N
23	dashboard.jpg	dashboard	jpg	dashboard_1767631643349	jpg	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767631645/cv-connect/internal/dashboard_1767631643349.jpg	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767631645/cv-connect/internal/dashboard_1767631643349.jpg	cv-connect/internal/dashboard_1767631643349	cv-connect/internal	t	f	2026-01-05 16:47:26.337789	\N	internal	\N
24	dashboard.jpg	dashboard	jpg	dashboard_1767631746818	jpg	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767631747/cv-connect/morsoftware/dashboard_1767631746818.jpg	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767631747/cv-connect/morsoftware/dashboard_1767631746818.jpg	cv-connect/morsoftware/dashboard_1767631746818	cv-connect/morsoftware	t	f	2026-01-05 16:49:08.373092	\N	morsoftware	\N
25	dashboard.jpg	dashboard	jpg	dashboard_1767631779199	jpg	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767631782/cv-connect/morsoftware/dashboard_1767631779199.jpg	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767631782/cv-connect/morsoftware/dashboard_1767631779199.jpg	cv-connect/morsoftware/dashboard_1767631779199	cv-connect/morsoftware	t	f	2026-01-05 16:49:44.399268	\N	morsoftware	\N
26	Do_Thi_Hong_Tester.pdf	Do_Thi_Hong_Tester	pdf	Do_Thi_Hong_Tester_1767667966780	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767667969/cv-connect/minhnq224/Do_Thi_Hong_Tester_1767667966780.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767667969/cv-connect/minhnq224/Do_Thi_Hong_Tester_1767667966780.pdf	cv-connect/minhnq224/Do_Thi_Hong_Tester_1767667966780	cv-connect/minhnq224	t	f	2026-01-06 02:52:50.563727	\N	minhnq224	\N
27	NGUYEN-THI-THAO-VAN-TopCV.vn-070126.01048.pdf	NGUYEN-THI-THAO-VAN-TopCV.vn-070126.01048	pdf	NGUYEN-THI-THAO-VAN-TopCV.vn-070126.01048_1767719638568	pdf	image	https://res.cloudinary.com/daygzzzwz/image/upload/v1767719644/cv-connect/ntthaovana921dqh%40gmail.com/NGUYEN-THI-THAO-VAN-TopCV.vn-070126.01048_1767719638568.pdf	upload	http://res.cloudinary.com/daygzzzwz/image/upload/v1767719644/cv-connect/ntthaovana921dqh%40gmail.com/NGUYEN-THI-THAO-VAN-TopCV.vn-070126.01048_1767719638568.pdf	cv-connect/ntthaovana921dqh@gmail.com/NGUYEN-THI-THAO-VAN-TopCV.vn-070126.01048_1767719638568	cv-connect/ntthaovana921dqh@gmail.com	t	f	2026-01-06 17:14:05.547289	\N	ntthaovana921dqh@gmail.com	\N
\.


--
-- Data for Name: calendar; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.calendar (id, job_ad_process_id, calendar_type, join_same_time, date, time_from, duration_minutes, org_address_id, meeting_link, note, creator_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	14	TEST_OFFLINE	f	2025-12-05	11:00:00	60	2	\N	\N	7	t	f	2025-12-03 04:03:44.045719	\N	minhnq224	\N
2	2	TEST_OFFLINE	f	2025-12-15	10:00:00	60	1	\N	ứng viên mang theo laptop cá nhân	3	t	f	2025-12-12 10:20:16.036885	\N	cvconnect	\N
3	5	PROBATION	t	2025-12-24	08:40:00	30	1	\N	\N	3	t	f	2025-12-20 10:05:33.891267	\N	cvconnect	\N
4	7	TEST_ONLINE	f	2026-01-23	09:00:00	30	\N	https://meet.google.com/	\N	2	t	f	2025-12-28 14:02:42.743475	\N	quannm32	\N
5	21	INTERVIEW_OFFLINE	f	2025-12-31	11:07:00	30	7	\N	\N	34	t	f	2025-12-29 04:12:22.108375	\N	DIEPADANG	\N
6	8	INTERVIEW_OFFLINE	f	2026-01-08	09:00:00	40	10	\N		2	t	f	2026-01-06 15:10:24.240437	\N	quannm32	\N
\.


--
-- Data for Name: calendar_candidate_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.calendar_candidate_info (id, calendar_id, candidate_info_id, time_from, time_to, is_active, is_deleted, created_at, updated_at, created_by, updated_by, date) FROM stdin;
1	1	4	11:00:00	12:00:00	t	f	2025-12-03 04:03:44.066738	\N	minhnq224	\N	2025-12-05
2	2	6	10:00:00	11:00:00	t	f	2025-12-12 10:20:16.084515	\N	cvconnect	\N	2025-12-15
3	3	6	08:40:00	09:10:00	t	f	2025-12-20 10:05:33.930904	\N	cvconnect	\N	2025-12-24
4	3	5	08:40:00	09:10:00	t	f	2025-12-20 10:05:33.93361	\N	cvconnect	\N	2025-12-24
5	4	1	09:00:00	09:30:00	t	f	2025-12-28 14:02:42.789119	\N	quannm32	\N	2026-01-23
6	5	9	11:07:00	11:37:00	t	f	2025-12-29 04:12:22.158204	\N	DIEPADANG	\N	2025-12-31
7	6	1	09:00:00	09:40:00	t	f	2026-01-06 15:10:24.259709	\N	quannm32	\N	2026-01-08
8	6	8	09:40:00	10:20:00	t	f	2026-01-06 15:10:24.261988	\N	quannm32	\N	2026-01-08
\.


--
-- Data for Name: candidate_evaluation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.candidate_evaluation (id, job_ad_process_candidate_id, evaluator_id, comments, score, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	30	3	ứng viên tiềm năng	10	t	f	2025-12-20 09:02:05.818682	\N	cvconnect	\N
2	38	2	Chuyên môn tốt, nắm chắc nền tảng, học hỏi nhanh	8	t	f	2026-01-06 15:42:43.722116	\N	quannm32	\N
3	38	3	Admin đánh giá: tốt	7	t	f	2026-01-06 15:44:32.989635	\N	cvconnect	\N
\.


--
-- Data for Name: candidate_info_apply; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.candidate_info_apply (id, candidate_id, full_name, email, phone, cv_file_id, cover_letter, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	5	Long Vu Cong	vclong2003@gmail.com	0888827768	2	Dear Mr. Skywalker,\n\nI am writing to recommend Leia Thompson. She worked with me at Acme Inc. as a Senior Product Manager and reported to me in my position as VP of Engineering.\n\nAs an employee, Leia was always reliable and resourceful. During her time in my team, she managed to conduct high-impact user research and make a number of key recommendations that resulted in an improved product (and subsequently increased sales).\n\nI’ve always put a premium on initiative and willingness to learn among my team members and Leia never failed to deliver on both fronts. An example was when she suggested we create a regular internal meetup where more senior employees could answer questions from other employees about their work. She was the first to take advantage of the knowledge these meetups offered and implemented it in her own work.\n\nLeia is a delight to work with – a team player with a positive, can-do attitude all the way. I wouldn’t hesitate to hire her again if the opportunity arose.\n\nShould you have any further questions, feel free to reach me at +10000000.\n\nThanks,\n\nSarah Long\nVP of Engineering, Acme Inc.	t	f	2025-12-01 04:35:18.477969	\N	vclong2003	\N
2	6	tran anh vu	iamdha0706@gmail.com	0987654321	3		t	f	2025-12-01 04:45:23.657506	\N	iamdha0706	\N
3	1	Duong	duong2003nb@gmail.com	01234566	4		t	f	2025-12-01 08:38:15.243663	\N	admin	\N
4	2	Nguyễn Minh Quân	nnmhqn2003@gmail.com	0348930275	8	Tôi là senior Golang. Hãy tuyển tôi!	t	f	2025-12-03 03:58:05.983401	\N	quannm32	\N
5	10	Trần Thái Bình Dương	duong2003nb@gmail.com	0833870414	9	Đơn xin ứng tuyển vị trí intern	t	f	2025-12-03 07:31:25.119053	\N	duong2003nb	\N
6	13	Nguyễn Thế Anh	anhnguyenthe2911@gmail.com	0123456789	12	CEO Mr.Quan	t	f	2025-12-11 09:33:33.258434	\N	anhnt746	\N
7	11	Quan Adaxia	nnmhqn2003@gmail.com	0348930275	16	Tôi là kỹ sư phát triển phần mềm bằng Golang	t	f	2025-12-20 16:55:18.95466	\N	huykeo2022@gmail.com	\N
8	33	Vy	tranvy10122000@gmail.com	03396769876	18	a Quân admin giới thiệu	t	f	2025-12-24 03:31:26.433404	\N	tranvy10122000@gmail.com	\N
9	10	Trần Thái Bình Dương	duong2003nb@gmail.com	0833870414	19	Chào anh chị,\nEm nộp đơn intern Java mong muốn được học hỏi và cống hiến	t	f	2025-12-28 14:56:10.721192	\N	duong2003nb	\N
10	7	Đỗ Thị Hồng	minhnq224@gmail.com	0991289047	26		t	f	2026-01-06 02:52:50.571827	\N	minhnq224	\N
11	41	Nguyên Thị Thảo Vân	ntthaovana921dqh@gmail.com	0987654321	27		t	f	2026-01-06 17:14:05.559274	\N	ntthaovana921dqh@gmail.com	\N
\.


--
-- Data for Name: candidate_summary_org; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.candidate_summary_org (id, skill, level_id, org_id, candidate_info_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	\N	7	2	4	t	f	2025-12-03 04:00:06.711464	\N	minhnq224	\N
2	\N	3	1	3	t	f	2025-12-05 04:41:34.627266	\N	cvconnect	\N
3	\N	4	1	6	t	f	2025-12-20 09:20:22.386733	\N	cvconnect	\N
4	\N	5	1	8	t	f	2026-01-06 15:34:29.931466	\N	quannm32	\N
5	Java	5	7	1	t	f	2026-01-07 06:32:51.166471	2026-01-07 06:36:50.884216	DIEPADANG	DIEPADANG
6	\N	3	1	11	t	f	2026-01-07 06:47:50.215658	\N	cvconnect	\N
\.


--
-- Data for Name: careers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.careers (id, code, name, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
152	IT_Software	Kỹ sư phần mềm	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
153	FE	Frontend Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
154	BE	Backend Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
155	FS	Fullstack Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
156	Mobile	Mobile Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
157	GameDev	Game Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
158	AI	Trí tuệ Nhân tạo (AI)	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
159	ML	Machine Learning Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
160	DL	Deep Learning Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
161	DataEngineer	Data Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
162	DataScientist	Data Scientist	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
163	BigData	Big Data Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
164	DataAnalyst	Data Analyst	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
165	DevOps	DevOps Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
166	Cloud	Cloud Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
167	SRE	Site Reliability Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
168	SysAdmin	System Administrator	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
169	CyberSecurity	Chuyên gia An ninh mạng	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
170	Pentester	Pentester / Kiểm thử xâm nhập	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
171	SecOps	Security Operations	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
172	QA	QA Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
173	QA_Automation	Automation Tester	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
174	QA_Manual	Manual Tester	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
175	Blockchain	Blockchain Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
176	Web3	Web3 Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
177	CryptoResearch	Crypto Researcher	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
178	UIUX	UI/UX Designer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
179	ProductManager	Product Manager	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
180	BA	Business Analyst	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
181	IoT	Kỹ sư IoT	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
182	Embedded	Embedded System Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
183	RPA	RPA Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
184	ARVR	AR/VR Developer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
185	AutomotiveSW	Automotive Software Engineer	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
186	DevRel	Developer Relations / Evangelist	t	f	2025-11-29 15:32:02.32365	\N	admin	\N
\.


--
-- Data for Name: department; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.department (id, code, name, org_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
4	KD101	Phòng Kinh doanh	2	t	f	2025-12-02 08:25:38.407536	\N	minhnq224	\N
6	CNTT1	Kỹ thuật	9	t	f	2026-01-05 16:50:22.306942	\N	morsoftware	\N
3	PDL	Team Dữ liệu	1	t	f	2025-11-29 14:37:04.366544	2026-01-06 03:24:24.415125	cvconnect	cvconnect
2	PAI	Team AI	1	t	f	2025-11-29 14:36:42.626449	2026-01-06 03:24:32.452956	cvconnect	cvconnect
1	PKTCN	Team Phát triển phần mềm	1	t	f	2025-11-29 14:35:56.888408	2026-01-06 03:24:51.910746	cvconnect	cvconnect
7	PQA	Team Kiểm thử phần mềm	1	t	f	2026-01-06 03:25:10.174738	2026-01-06 03:32:07.008852	cvconnect	cvconnect
8	PCNTT	Phòng CNTT	2	t	f	2026-01-06 07:47:43.362367	\N	minhnq224	\N
5	TECH	TECH	7	t	f	2025-12-29 03:47:50.379854	2026-01-06 09:32:50.657418	DIEPADANG	DIEPADANG
9	P_PTPM	Phòng phát triển phần mềm	7	t	f	2026-01-06 09:33:11.07056	\N	DIEPADANG	\N
\.


--
-- Data for Name: failed_rollback; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.failed_rollback (id, type, payload, error_message, status, retry_count, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
\.


--
-- Data for Name: industry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.industry (id, code, name, description, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	IT	Công nghệ thông tin	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
2	EDU	Giáo dục & Đào tạo	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
3	FIN	Tài chính - Ngân hàng	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
4	INS	Bảo hiểm	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
5	HEA	Y tế & Chăm sóc sức khỏe	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
6	REA	Bất động sản	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
7	CON	Xây dựng	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
8	MAN	Sản xuất - Chế biến	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
9	TRA	Thương mại - Bán lẻ	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
10	AGR	Nông nghiệp & Thủy sản	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
11	LOG	Giao thông vận tải & Logistics	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
12	ENE	Năng lượng	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
13	TEL	Viễn thông	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
14	MED	Truyền thông & Giải trí	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
15	TOU	Du lịch & Khách sạn	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
16	LAW	Luật & Dịch vụ pháp lý	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
17	HR	Nhân sự & Tuyển dụng	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
18	FOO	Thực phẩm & Đồ uống	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
19	FAS	Thời trang & Mỹ phẩm	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
20	PUB	Dịch vụ công (Chính phủ, Hành chính)	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
21	AUT	Ô tô & Công nghiệp phụ trợ	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
22	AVI	Hàng không & Vũ trụ	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
23	MAR	Hàng hải & Đóng tàu	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
24	ELE	Điện tử & Cơ điện tử	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
25	MEC	Cơ khí & Chế tạo máy	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
26	MIN	Khai khoáng & Khoáng sản	\N	t	f	2025-11-26 14:54:00.117053	\N	admin	\N
\.


--
-- Data for Name: interview_panel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.interview_panel (id, calendar_id, interviewer_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	1	7	t	f	2025-12-03 04:03:44.05804	\N	minhnq224	\N
2	2	2	t	f	2025-12-12 10:20:16.074939	\N	cvconnect	\N
3	3	11	t	f	2025-12-20 10:05:33.919569	\N	cvconnect	\N
4	4	11	t	f	2025-12-28 14:02:42.776822	\N	quannm32	\N
5	5	34	t	f	2025-12-29 04:12:22.148917	\N	DIEPADANG	\N
6	6	11	t	f	2026-01-06 15:10:24.251264	\N	quannm32	\N
\.


--
-- Data for Name: job_ad; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad (id, code, title, org_id, position_id, job_type, due_date, quantity, salary_type, salary_from, salary_to, currency_type, keyword, description, requirement, benefit, hr_contact_id, job_ad_status, is_public, is_auto_send_email, email_template_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by, is_remote, is_all_level, key_code_internal) FROM stdin;
1	JD-1	Game development internship	1	1	FULL_TIME	2025-12-25 00:00:00	1	NEGOTIABLE	\N	\N	VND	Unity;Intern	<p>Working with Technical Artist to optimize/develop game shader with in-house game engine.</p><p></p>	<ul class="list-disc ml-4"><li><p>Bachelor of CS or equivalent</p></li><li><p>GPA &gt;= 3.8</p></li><li><p>Familiar with low level shader language</p></li><li><p>Familiar with GPU rendering pipeline</p></li><li><p>Familiar with either UE4+ or Unity</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Working with the best of game developers in the industry</p></li></ul><p></p>	2	PAUSE	t	f	\N	t	f	2025-11-30 09:07:09.207311	2026-01-06 02:55:44.015348	cvconnect	cvconnect	t	f	\N
7	JD-1	Backend Developer (Golang/Python) + Signing Bonus Onboard Vào Tháng 12	2	2	PART_TIME	2026-01-31 00:00:00	4	NEGOTIABLE	\N	\N	VND		<ul class="list-disc ml-4"><li><p>Nghiên cứu và phát triển các sản phẩm phần mềm trong lĩnh vực An ninh mạng (thực hiện giám sát và phát hiện tấn công; quản lý ATTT cho tổ chức/doanh nghiệp)</p></li><li><p>Làm việc với team phát triển phần mềm tiếp nhận, phân tích và tạo ra các thiết kế/kiến trúc mới hướng đến quy mô và hiệu suất</p></li><li><p>Thực hiện tối ưu về mặt kiến trúc hiệu năng cho các hệ thống lớn</p></li><li><p>Nghiên cứu, áp dụng các công nghệ mới vào phát triển sản phẩm (microservice, cloud native, CI/CD)</p></li><li><p>Làm việc theo mô hình Agile-Scrum</p></li><li><p>Có cơ hội tham gia xử lý các bài toán mới, khó như: Scalable, BigData, High-performance, Real-time Processing…</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Tốt nghiệp Đại học chuyên ngành: An toàn thông tin; Công nghệ thông tin, Khoa học máy tính, Toán – Tin…</p></li><li><p>Từ 3 năm kinh nghiệm lập trình Backend</p></li><li><p>Thành thạo ít nhất 01 ngôn ngữ Backend Golang hoặc Python</p></li><li><p>Có kiến thức tốt về OOP, Design Pattern, kiến trúc hệ thống (Microservices)</p></li><li><p>Có kinh nghiệm làm việc với Cơ sở dữ liệu SQL/NoSQL</p></li><li><p>Có tư duy viết clean code, dễ bảo trì, có kiểm thử (unit test)</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Sign-on Bonus 1 tháng lương nếu đạt level đánh giá và bắt đầu làm việc trước 01/01/2026.</p></li><li><p>Thu nhập cạnh tranh từ 330-380M Gross/năm, review lương hàng năm (tháng 3).</p></li><li><p>Bảo hiểm sức khỏe MIC (hạn mức đến 600 triệu/năm), khám sức khỏe định kỳ.</p></li><li><p>Hỗ trợ học chứng chỉ quốc tế (50–100%), tài khoản Udemy và học liệu ATTT cập nhật liên tục.</p></li><li><p>Làm việc tại Landmark 72, môi trường hiện đại, có khu gym, café, giải trí.<br>Các hoạt động teambuilding, happy hours, nghỉ mát, sinh nhật, thể thao nội bộ.</p></li><li><p>12 ngày phép + 3 ngày nghỉ thêm, nghỉ ½ ngày cho các dịp lễ đặc biệt</p></li></ul><p></p>	7	OPEN	t	f	\N	t	f	2025-12-03 03:54:25.996322	\N	minhnq224	\N	f	f	\N
10	JD-1	Tuyển TTS Frontend	9	5	FULL_TIME	2026-01-24 00:00:00	2	NEGOTIABLE	\N	\N	VND	vuejs;mor;morsoftware;frontend	<p>Phối hợp với đội ngũ thiết kế và đội ngũ backend, phát triển, triển khai và bảo trì tính năng cho hệ thống quản lý nhân sự hàng đầu Nhật Bản</p>	<ul class="list-disc ml-4"><li><p>Có hiểu biết cơ bản về HTML/CSS/JS</p></li><li><p>Có kinh nghiệp làm việc với ReactJs/VueJs</p></li><li><p>Giao tiếp Tiếng Anh cơ bản (Nghe/Đọc)</p></li></ul><p></p><p></p>	<ul class="list-disc ml-4"><li><p>Đóng bảo hiểm theo quy định</p></li><li><p>Thưởng các dịp nghỉ lễ</p></li><li><p>Thưởng dự án</p></li><li><p>Teambuilding</p></li><li><p>Làm việc và phát triển trong môi trường chuyên nghiệp</p></li></ul><p></p>	40	OPEN	t	f	\N	t	f	2026-01-05 16:57:24.776765	\N	morsoftware	\N	f	f	\N
8	JD-3	Thực tập sinh Java	1	1	FULL_TIME	2026-01-14 00:00:00	2	NEGOTIABLE	\N	\N	VND	java;fresher;internship;intern;paid	<p>LG CNS is looking for full stack developers for cloud domain project</p><ul class="list-disc ml-4"><li><p>Develop and maintain fullstack web applications using Java (Spring Boot) on backend and React.js on frontend.</p></li><li><p>Participate in the entire software development lifecycle, from requirements gathering, analysis, design to deployment and maintenance.</p></li><li><p>Conduct unit testing and support integration testing.</p></li><li><p>Ensure code quality, performance, and scalability.</p></li><li><p>Maintain technical documentation and follow coding standards</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Bachelor's degree of Information Technology or higher</p></li><li><p>Have working experience and excellent knowledge at software developing using Java, Spring boot</p></li><li><p>Have working experience and excellent knowledge at software developing using React, HTML, JavaScript</p></li><li><p>Good knowledge about AI (Azure OpenAI and GenAI)</p></li><li><p>Good knowledge about public cloud (Azure, AWS,)</p></li><li><p>Database: MariaDB (or MySQL).</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Attractive salary and bonus will be discussed after going through CV &amp; Interview</p></li><li><p>Review capacity annually and adjust salary increases according to work performance.</p></li><li><p>Health care: Premium health insurance, Annual health check-up</p></li><li><p>Young working environment</p></li><li><p>Good career development opportunities with interesting and challenging projects.</p></li><li><p>English, Korean, technical, soft skills training courses.</p></li><li><p>Opportunity to learn special courses from LG CNS, new technology and security.</p></li><li><p>Gifts on holidays (April 30th - May 1st, September 2nd, Tet, etc.)</p></li><li><p>Outdoor activities with company support: sports clubs, team building, happy hour parties, birthdays, travel, employee and family events, etc.</p></li><li><p>Working hours: 8 hours from Monday - Friday (8 hours/day)</p></li></ul><p></p>	4	OPEN	t	f	\N	t	f	2025-12-28 14:48:10.827834	2026-01-06 07:39:46.772715	cvconnect	cvconnect	f	f	\N
9	JD-1	CVCC/CG Quản Trị Rủi Ro Công Nghệ Thông Tin (Digital Channel Risk Management)	7	4	FULL_TIME	2026-03-20 00:00:00	12	NEGOTIABLE	\N	\N	VND		<p>Tóm tắt : Chịu trách nhiệm nhận diện, đánh giá, kiểm soát và giám sát các rủi ro hoạt động phát sinh từ các hoạt động và sản phẩm số của Ngân hàng, bao gồm Internet Banking, Mobile Banking, eKYC, hệ thống thanh toán số, v.v.<br>- Đảm bảo các quy trình số hoạt động an toàn và hiệu quả, tuân thủ các chính sách nội bộ, quy định pháp luật và các thông lệ quản lý rủi ro tốt nhất.</p><p>Chi tiết :</p><p>1.1. Quản lý Rủi ro Hoạt động Kênh Kỹ thuật số<br>- Thực hiện Tự Đánh giá Rủi ro và Kiểm soát (RCSA) định kỳ và đột xuất để ứng phó với những thay đổi đáng kể trong các sản phẩm/ứng dụng kỹ thuật số.<br>- Phối hợp phát triển và cập nhật thường xuyên danh mục rủi ro chính cho từng sản phẩm/dịch vụ trên các kênh kỹ thuật số.<br>- Tham gia đánh giá rủi ro cho các quy trình tiếp nhận khách hàng kỹ thuật số, xác thực giao dịch, tích hợp với bên thứ ba (ngân hàng mở), v.v.<br>1.2. Kiểm soát và Giám sát<br>- Phát triển và giám sát hiệu quả của các biện pháp kiểm soát chính cho hoạt động kênh kỹ thuật số.<br>- Đưa ra cảnh báo sớm về các điểm yếu về quy trình hoặc kỹ thuật có thể dẫn đến rủi ro hoặc sự cố.<br>1.3. Quản lý Sự kiện và Sự cố Rủi ro<br>- Ghi nhận, phân tích và điều tra nguyên nhân gốc rễ của các sự cố liên quan đến các kênh kỹ thuật số.<br>- Phối hợp với các bên liên quan để xây dựng kế hoạch khắc phục, theo dõi tiến độ giải quyết và báo cáo theo yêu cầu.<br>- Duy trì và cập nhật cơ sở dữ liệu sự kiện rủi ro (cơ sở dữ liệu sự kiện tổn thất).<br>1.4. Quản lý Rủi ro trong Dự án và Sản phẩm Mới<br>- Tham gia đánh giá rủi ro cho các dự án công nghệ và triển khai các sản phẩm và dịch vụ kỹ thuật số mới.<br>- Xem xét các tài liệu thiết kế, sơ đồ quy trình nghiệp vụ và kiến ​​trúc hệ thống để đưa ra khuyến nghị từ góc độ kiểm soát rủi ro.</p><p>Hỗ trợ xây dựng tài liệu quy trình liên quan đến hoạt động ngân hàng số.<br>- Tham gia các buổi truyền thông nâng cao nhận thức rủi ro cho nhân viên.<br>- Thực hiện các nhiệm vụ khác theo phân công của ban quản lý.</p>	<ul class="list-disc ml-4"><li><p>Trình độ chuyên môn: Cử nhân Quản trị Rủi ro, Tài chính - Ngân hàng, Công nghệ Thông tin, Hệ thống Thông tin, Quản trị Kinh doanh hoặc lĩnh vực liên quan.</p></li><li><p>Kinh nghiệm: Tối thiểu 2 năm kinh nghiệm trong quản trị rủi ro hoạt động, kiểm soát nội bộ, kiểm toán nội bộ hoặc phát triển sản phẩm số.</p></li><li><p>Kỹ năng:<br>- Tư duy phân tích mạnh mẽ, lập luận logic và tư duy phản biện.<br>- Khả năng giải quyết vấn đề và ra quyết định.<br>- Kỹ năng giao tiếp và làm việc nhóm hiệu quả; khả năng hợp tác giữa các phòng ban.<br>- Kỹ năng giao tiếp tiếng Anh cơ bản.<br>- Hiểu biết sâu sắc về các quy định, chính sách, quy trình và hướng dẫn của chính phủ và ngành ngân hàng liên quan đến quản trị rủi ro.<br>- Kinh nghiệm xử lý sự cố và các tình huống bất ngờ trong hoạt động quản trị rủi ro.<br>- Khả năng lập kế hoạch và triển khai tốt; ưu tiên ứng viên có kinh nghiệm hoặc kiến ​​thức về các dự án phát triển ứng dụng ngân hàng số.</p></li></ul><p></p>	<p>- Mức lương cạnh tranh theo năng lực.</p><p>- Cơ hội tham gia các dự án chuyển đổi số quy mô lớn.</p><p>- Được đào tạo và cập nhật kiến thức về công nghệ, an toàn thông tin và quản trị rủi ro.</p><p>- Môi trường làm việc chuyên nghiệp, năng động, nhiều cơ hội thăng tiến.</p>	34	OPEN	t	t	6	t	f	2025-12-29 04:03:37.019925	2026-01-06 09:32:24.574735	DIEPADANG	DIEPADANG	f	f	\N
2	JD-2	Lập trình viên Java Backend	1	1	FULL_TIME	2025-12-31 00:00:00	12	NEGOTIABLE	\N	\N	VND	java; backend; remote	<p>CVConnect is a leading global IT service provider headquartered in Vietnam. With 33,000+ employees in 88 offices across 30 countries, we serve 1,100+ clients, including 96 Fortune 500 companies.</p><p>We believe diversity fuels innovation and strive to create an inclusive workplace where talents of all backgrounds thrive. We welcome expatriates and international professionals to bring fresh perspectives and help shape the future of technology.</p><p>JOB OVERVIEW<br>We are looking for a passionate and detail-oriented Middle Java developer to join our team for a high-impact project with global client. This is your opportunity to work on cutting-edge financial technologies and contribute to the stability, scalability, and performance of mission-critical systems.</p><p>RESPONSIBILITIES</p><ul class="list-disc ml-4"><li><p>Work with Client’s designers and product owners to deliver production-ready source code based upon assigned features and requirements.</p></li><li><p>Test and remediate issues found during development in accordance with accepted quality control practices.</p></li><li><p>When needed or requested, provide technical expertise and insight related to engineering and design efforts.</p></li><li><p>Lead technical engagement &amp; collaborate with project teams on business requirement, analysis, design, development, and testing of the applications in support of business solutions.</p></li><li><p>Strong ability to analyze functional needs and drive the design for positive business outcomes; recommends solutions that are aligned with business</p></li><li><p>Join Business application development, lead/coach technical project team and review development solution/code</p></li><li><p>Maintenance and support business applications</p></li><li><p>Review code &amp; control code quality</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Bachelor’s degree in Computer Science/IT or related field</p></li><li><p>Well-communicate in English.</p></li><li><p>Minimum 2 years of professional experience as a Java Developer.</p></li><li><p>Knowledge of Spring/Spring Boot essential.</p></li><li><p>Database technologies: Postgres and Redis desirable.</p></li><li><p>Experience building ReSTful APIs essential – SOAP not required.</p></li><li><p>Experience in Database: Oracle, MS SQL Server, PostgreSQL</p></li><li><p>Knowledge of Microservice</p></li><li><p>Hand-on experience with Cloud Computing (Azure/AWS)</p></li><li><p>Familiarity with Agile development methodologies and tools.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Attractive annual summer vacation allowance</p></li><li><p>Sponsored training courses for personal growth and up to 100% coverage for certification costs</p></li><li><p>Global and inclusive workplace with monthly cross-cultural events</p></li><li><p>International exposure and career growth across global locations</p></li><li><p>Work-life balance benefits with a flexible leave policy and annual health check-ups to support employee well-being</p></li><li><p>Work on impactful global banking projects with U.S. clients.</p></li><li><p>Join a supportive, international working environment with open culture.</p></li><li><p>Attractive salary package and annual performance bonuses.</p></li><li><p>Flexible working hours &amp; remote/hybrid options.</p></li><li><p>Premium health insurance, company trips, training budget, and more.</p></li></ul><p></p>	2	PAUSE	t	f	\N	t	f	2025-11-30 14:42:24.953001	2026-01-06 02:55:42.164375	cvconnect	cvconnect	t	f	252e9ae7-9863-419f-aa94-58c818971890
11	JD-4	QA Engineer (Automotive)	1	9	FULL_TIME	2026-02-28 00:00:00	3	RANGE	20000000	40000000	VND	QA	<p><strong>42dot Vietnam</strong> functions as a global QA center, overseeing global quality to ensure the stability and performance of vehicle and software systems throughout the entire SDV development process. At 42dot Vietnam, you will comprehensively verify the hardware, software, and performance quality of vehicles and infotainment systems. Based on your background and strengths, the specific role may focus on one or more areas such as ECU (Electronic Control Units), IVI (In-Vehicle Infotainment), or embedded platforms. Cross-domain collaboration may also be required when necessary.</p><p style="text-align: justify;">&nbsp;<br><strong>[Important Information for Applicants]</strong><br>We are hiring for positions at 42dot Vietnam and it operates independently from 42dot Headquarters. So, please note that the employment terms and benefits may not be identical to those of the headquarters. All employees will officially belong to 42dot Vietnam, and the detailed working conditions and benefits will be shared transparently during the contract process with selected candidates.</p>	<ul class="list-disc ml-4"><li><p>Test planning &amp; scenario design: Develop test plans and scenarios based on requirements for&nbsp;hardware interfaces, software features, and system performance.&nbsp;</p></li><li><p>Test environment setup: Configure ECU/HIL/IVI equipment, network simulations, measurement&nbsp;devices, and verification tools.&nbsp;</p></li><li><p>Validation execution:&nbsp;</p><ul class="list-disc ml-4"><li><p>Hardware: Verify power/boot sequences, I/O devices, and communication interfaces.&nbsp;</p></li><li><p>Software: Test IVI apps and vehicle functions, validate UI responsiveness and network connectivity.&nbsp;</p></li><li><p>Performance: Conduct CPU/GPU/memory bottleneck analysis, profiling, and optimization.&nbsp;</p></li></ul></li><li><p>Log analysis &amp; reporting: Create test reports, identify root causes of issues, and propose&nbsp;improvements.&nbsp;</p></li><li><p>Test automation &amp; optimization: Build automation environments using Appium, adb scripts, and&nbsp;CI/CD pipelines.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Premium office in District 1, free parking, free tea and coffee, full-loaded pantry</p></li><li><p>High-performance laptops, professional software and testing tools</p></li><li><p>13th-month salary, performance-based bonus</p></li><li><p>Language training, advanced technical courses for QA, knowledge sharing workshops</p></li><li><p>Annual health check-up, social insurance</p></li><li><p>Company parties, team-building activities, in-house clubs, workshops &amp; seminars</p></li></ul><p></p>	2	OPEN	t	t	1	t	f	2026-01-06 04:03:17.281383	2026-01-06 04:57:03.39271	cvconnect	cvconnect	t	f	\N
12	JD-5	Frontend developer (PA project)	1	8	PART_TIME	2026-01-31 00:00:00	4	NEGOTIABLE	\N	\N	VND		<p>We’re looking for a<strong> front end developer</strong> to join our team supporting our production application used by over a thousand people. Your work will have a real impact on users' everyday jobs and the company’s activities.&nbsp;</p><p style="text-align: justify;">&nbsp;</p><p style="text-align: justify;">Aside from the technical skills listed below we need someone with some interest in UI and UX to be able to spend time with users on a regular basis to capture their feedback.</p><p style="text-align: justify;">You would start with updating and upgrading our UI while you learn about the app and then do more complex tasks and feature development.&nbsp;</p><p style="text-align: justify;">We expect you to be proactive, propose new ideas and suggest improvements and truly be part of the project.</p><p style="text-align: justify;">As part of the team you will also have to do code review for your peers. This will also allow you to learn more and grow with us.</p><p style="text-align: justify;">&nbsp;</p><p style="text-align: justify;">Our teams are in Vietnam, Finland, and the USA therefore we need someone who is fluent in English</p>	<ul class="list-disc ml-4"><li><p>Experience in front end development with HTML, CSS, Sass, Typescript&nbsp;</p></li><li><p>Knowledge of modern JS frameworks, preferably Vue 3 and Vite with state management (Pinia or Vuex)</p></li><li><p>Understanding of RESTful APIs and asynchronous programming patterns (async/await, Promises)</p></li><li><p>Usage of GIT and Github</p></li><li><p>Experience with Visual Code</p></li><li><p>Fluent in English, both spoken and written</p></li></ul><p>&nbsp;</p><p><strong>Nice-to-Have Skills</strong></p><ul class="list-disc ml-4"><li><p>Experience with Jira</p></li><li><p>Experience with API tools like Postman</p></li><li><p>Experience with Figma</p></li><li><p>Experience with AI assisted coding tools like Cursor</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p><strong>Growth:</strong> A supportive environment where you can level up your skills in both engineering and product design.</p></li><li><p><strong>Impact:</strong> Your work will directly improve the experience of thousands of users in the proptech space.</p></li><li><p><strong>Compensation:</strong> Competitive salary and the tools you need to do your best work.</p></li></ul><p></p>	4	OPEN	t	t	1	t	f	2026-01-06 07:33:01.192737	\N	cvconnect	\N	t	f	\N
14	JD-2	Senior IOS Mobile Apps Developer (Remote, Swift)	2	12	FULL_TIME	2026-01-28 00:00:00	5	RANGE	40000000	70000000	VND	Mobile; Swift	<p>The IOS Developer will be working with our IT Department. This is a key function for the group. You will be guided and supported by our dev group leader.</p><ul class="list-disc ml-4"><li><p>Manage in Swift and SwiftUI.</p></li><li><p>Manage the full technical life-cycle of iOS applications during the development phase.</p></li><li><p>Collaborate with team members and provide technical insight as needed.</p></li><li><p>Perform individual project components within the entire development life cycle, including implementation, testing, deployment, and maintenance.</p></li><li><p>Other duties assigned by the Immediate Supervisor.</p></li></ul><p></p>	<p>Qualifications:</p><ul class="list-disc ml-4"><li><p>Has good knowledge of Swift and <strong>SwiftUI </strong>( +3 years of Experience)</p></li><li><p>7+ years of Experience working with native iOS Development and with different iOS variations</p></li><li><p>Deep understanding of Apple design principles and interface guidelines</p></li><li><p>Deep understanding of iOS/Swift memory management</p></li><li><p>Strong knowledge of iOS UI design principles, patterns, and best practices</p></li><li><p>Understanding of SOLID principles</p></li><li><p>Clean coding style with smokie tests</p></li><li><p>Understanding of RESTful APIs and how to connect iOS applications to back-end services</p></li><li><p>Understanding of different security aspects (encryption, API protection, SSH, etc.)</p></li><li><p>Experience with offline storage, threading, and performance tuning</p></li><li><p>Familiarity with Crashlitics and other Firebase services</p></li><li><p>Familiarity with cloud message APIs and push notifications</p></li><li><p>Familiarity with continuous integration</p></li><li><p>Proficient understanding of code versioning tools. Understanding of Git branching models such as Git Flow and comfortable doing code reviews. Xcode Cloud CI/CD</p></li><li><p>Strong problem-solving skills, creativity, and appetite to learn.</p></li><li><p>Able to work and deliver under pressure and within deadlines.</p></li><li><p>Enjoy and thrive in a fast-moving start-up environment.</p></li></ul><p>Other requirements:</p><ul class="list-disc ml-4"><li><p>to see the code of your pet-projects in git</p></li><li><p>to see app(s) you worked on in past jobs in the App Store</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Flexible working hour</p></li><li><p>Competitive salary</p></li><li><p>Medical insurance</p></li></ul><p></p>	7	OPEN	t	f	\N	t	f	2026-01-06 07:51:35.469147	\N	minhnq224	\N	t	t	\N
15	JD-3	Android Developer (Kotlin / MVVM)	2	12	FULL_TIME	2026-02-28 00:00:00	3	NEGOTIABLE	\N	\N	VND	Kotlin; Up to 2000$	<ul class="list-disc ml-4"><li><p>Design and develop Android applications following clean architecture principles (e.g. MVVM, MVI).</p></li><li><p>Implement dependencies using DI frameworks such as Hilt or Dagger.</p></li><li><p>Handle asynchronous programming and multithreading using Kotlin Coroutines and Flow.</p></li><li><p>Work with Jetpack libraries, including Navigation and Jetpack Compose, to build scalable and modern UI.</p></li><li><p>Analyze logs, identify root causes, and reproduce bugs for debugging and issue resolution.</p></li><li><p>Collaborate with designers, product managers, and other developers to deliver high-quality features.</p></li><li><p>Continuously learn and apply new technologies and best practices.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Strong understanding of Android application architecture (MVVM, MVI, etc.).</p></li><li><p>Experience with dependency injection using Hilt or Dagger.</p></li><li><p>Proficient in Kotlin, especially with Coroutines and Flow.</p></li><li><p>Familiarity with Android Jetpack components: Navigation and Compose.</p></li><li><p>Good debugging skills: Able to read logs, trace and reproduce bugs effectively.</p></li><li><p>Self-driven, hardworking, and eager to learn.</p></li></ul><p></p>	<p>***[BENEFITS] - What differentiate Absolute from other software companies in Vietnam:</p><ul class="list-disc ml-4"><li><p>Product company with global reputation specialized in end-point security</p></li><li><p><strong>Very attractive salary (Up to 2000$)</strong></p></li><li><p>Global standard working environment</p></li><li><p>100% salary during probation period</p></li><li><p>13th month salary</p></li><li><p>Birthday Bonus, New Year Bonus&nbsp;</p></li><li><p>Social Insurance, health insurance, unemployment insurance on total salary after probation period</p></li><li><p>Bao Viet Healthcare Insurance</p></li><li><p>Health Check&nbsp;</p></li><li><p>Team Building&nbsp;</p></li><li><p>Flexible working hours</p></li><li><p>Training &amp; development opportunities</p></li><li><p>Laptop and PC for everyone</p></li></ul><p></p>	7	OPEN	t	f	\N	t	f	2026-01-06 07:53:29.910976	\N	minhnq224	\N	t	f	\N
16	JD-4	Data & AI Scientist (Python, SQL, GenAI)	2	13	FULL_TIME	2026-01-31 00:00:00	4	RANGE	20000000	35000000	VND	Food and Beverage; Software Products and Web Services	<p><strong>Overview</strong></p><p>Momos is a rapidly growing startup headquartered in San Diego and Singapore, building AI for customers at every location. Momos works with leading enterprise multi-location brands and QSRs globally, such as Firehouse Subs, Shake Shack, and Baskins Robbins, and powers the entire customer stack with AI for 20,000 locations globally.</p><p>At Momos, our core principle is putting restaurants at the forefront of everything we do. Since day one, we have embedded the valuable feedback and input from our restaurant partners into our company culture and product design. This collaborative approach has been instrumental in creating a solution that truly meets the needs of the F&amp;B industry. Today, we are proud to be trusted by over 2000 restaurants globally. If you have a passion for food and want to work for a mission-driven company that is actively shaping the future of the F&amp;B industry, we would be thrilled to have you join our team.</p><p>&nbsp;</p><p><strong>The Role</strong></p><p>Momos is seeking a highly motivated individual to join our Data team, playing a pivotal role in empowering our customers to optimize operations through insightful data-driven recommendations. Utilizing data science and Artificial Intelligence (AI), you will be responsible for designing and implementing data analytics and agentic AI tools, focusing on insights and recommendations for our customers. Working closely with the Head of Data and cross-functional teams, you will contribute to building the next-generation data platform for our fast-paced, early-stage startup.</p><p>&nbsp;</p><p><strong>Primary Roles &amp; Responsibilities:</strong></p><ul class="list-disc ml-4"><li><p>Collaborate with Operations, Product, and Data Engineering teams to design and implement scalable analytics and reporting solutions.</p></li><li><p>Partner with Data Engineering to develop core datasets for operational and exploratory analysis.</p></li><li><p>Query, transform, and analyse large datasets.</p></li><li><p>Construct rich and dynamic dashboards using in-house tools and third-party solutions like Tableau.</p></li><li><p>R&amp;D and launch end-to-end AI Products for customers</p></li><li><p>Leverage Generative AI models to develop data products for all internal and external purposes</p></li><li><p>Automate solutions where appropriate.</p></li></ul><p><strong>Additional Responsibilities:</strong></p><ul class="list-disc ml-4"><li><p>Leverage extensive volumes of structured and unstructured data to enhance customer business performance.</p></li><li><p>Manage end-to-end deployment of data projects across various functions and stakeholders.</p></li><li><p>Develop a profound understanding of data models, business domains, and support data quality for allocated areas of ownership.</p></li><li><p>Build dynamic dashboards using in-house and third-party tools, aligning with business requirements.</p></li></ul><p></p>	<p><strong>Ideal Background:</strong></p><ul class="list-disc ml-4"><li><p>Bachelor's or Master's degree in a quantitative discipline (e.g., Mathematics/Statistics, Actuarial Sciences, Computer Science, Engineering, or other data science disciplines).</p></li><li><p>2-4 years of experience in a highly quantitative role.</p></li><li><p>Self-driven team player with the ability to quickly learn and apply new tools and techniques.</p></li><li><p>High proficiency in Python, SQL, and data visualization tools such as Tableau.</p></li><li><p>Excellent communication skills in both technical and non-scientific language for effective collaboration with business stakeholders.</p></li><li><p>Strong working knowledge of Generative AI and have designed and deployed LLM models in production</p></li></ul><p><strong>Nice to Haves:</strong></p><ul class="list-disc ml-4"><li><p>Experience in developing internal or external data products.</p></li><li><p>Alignment with executive-level stakeholders.</p></li><li><p>Working experience in public cloud infrastructure &amp; tools such as AWS (e.g. S3, Lambda, and EC2) and/or GCP (e.g. BigQuery, Compute Engine)</p></li><li><p>Domain expertise in the Adtech industry.</p></li><li><p>Domain expertise in F&amp;B technology.</p></li></ul><p></p>	<p><strong>Benefits</strong></p><ul class="list-disc ml-4"><li><p>Private medical insurance&nbsp;</p></li><li><p>Paid time off and flexible working culture&nbsp;</p></li><li><p>A dynamic and inclusive company culture</p></li><li><p>Access to the latest technology and tools for personal development</p></li><li><p>Comprehensive onboarding program for new employees</p></li><li><p>Employee recognition programs for outstanding performance</p></li><li><p>A supportive environment that encourages innovation and creativity</p></li></ul><p><strong>Location</strong></p><ul class="list-disc ml-4"><li><p>Ho Chi Minh City, Vietnam</p></li></ul><p><strong>Cultural Values</strong></p><ul class="list-disc ml-4"><li><p>Mission-driven and fast-paced, entrepreneurial environment.</p></li><li><p>A collaborative and flat company culture.</p></li><li><p>Comprehensive private health insurance.</p></li><li><p>Discretionary trips to visit teams/ offices in the region.</p></li><li><p>Cross-cultural team bonding/networking.</p></li><li><p>Love Food? Join our Team!</p></li></ul><p></p>	7	OPEN	t	f	\N	t	f	2026-01-06 08:01:37.21243	\N	minhnq224	\N	f	f	\N
17	JD-5	Data Scientist (Customer profile)	2	13	PART_TIME	2026-01-31 00:00:00	5	NEGOTIABLE	\N	\N	VND	Data Science; Python; NLP; AI	<p><strong>Vai trò thiết yếu:</strong></p><ul class="list-disc ml-4"><li><p>Thực hiện phân tích, xử lý và chuẩn bị dữ liệu phục vụ huấn luyện/tinh chỉnh các mô hình LLM.</p></li><li><p>Áp dụng các kỹ thuật NLP để làm sạch, chuẩn hóa và biến dữ liệu thô thành dữ liệu chất lượng cao.</p></li><li><p>Đảm bảo dữ liệu phù hợp với yêu cầu kỹ thuật, đáp ứng tiêu chuẩn chất lượng và tuân thủ quy định pháp lý.</p></li><li><p>Phối hợp với Data Engineers, Data Stewards và AI Engineers để bảo đảm dữ liệu huấn luyện được sẵn sàng và tối ưu cho các pipeline pre-training/fine-tuning LLM.</p></li></ul><p><strong>Trách nhiệm thiết yếu:</strong></p><ul class="list-disc ml-4"><li><p>Thu thập, tiền xử lý và làm sạch dữ liệu văn bản tiếng Việt từ nhiều nguồn (tài chính, pháp luật, kế toán, quản trị doanh nghiệp…).</p></li><li><p>Áp dụng các kỹ thuật NLP như tokenization, sentence segmentation, deduplication, normalization để chuẩn hóa dữ liệu.</p></li><li><p>Thực hiện đánh giá chất lượng dữ liệu (Data Quality Score, tính đa dạng, tính cân bằng corpus).</p></li><li><p>Xây dựng và duy trì các pipeline xử lý dữ liệu NLP bán tự động phục vụ huấn luyện mô hình.</p></li><li><p>Gắn nhãn dữ liệu ở mức cơ bản hoặc phối hợp với Data Labeling Specialists để đảm bảo tính chính xác và tính nhất quán.</p></li><li><p>Làm việc cùng AI Engineers để kiểm thử dữ liệu đầu vào cho pre-training/fine-tuning, đánh giá tính phù hợp và hiệu quả.</p></li><li><p>Đề xuất cải tiến kỹ thuật nhằm tăng hiệu quả xử lý dữ liệu và tối ưu chi phí.</p></li></ul><p></p>	<p><strong>Học vấn:</strong></p><ul class="list-disc ml-4"><li><p>Cử nhân hoặc Thạc sĩ chuyên ngành Khoa học dữ liệu, Trí tuệ nhân tạo, Công nghệ Thông tin hoặc các lĩnh vực liên quan.</p></li></ul><p><strong>Kinh nghiệm:</strong></p><ul class="list-disc ml-4"><li><p>3–5 năm kinh nghiệm trong xử lý dữ liệu văn bản hoặc NLP.</p></li><li><p>Trải nghiệm thực tế trong các dự án chuẩn bị dữ liệu cho huấn luyện mô hình NLP/LLM.</p></li></ul><p><strong>Kiến thức và kỹ năng:</strong></p><ul class="list-disc ml-4"><li><p>Thành thạo Python và các thư viện NLP (Hugging Face, SpaCy, NLTK, OpenNMT).</p></li><li><p>Kinh nghiệm trong làm sạch dữ liệu, text augmentation, deduplication và corpus building.</p></li><li><p>Hiểu biết về các yêu cầu dữ liệu cho pre-training/fine-tuning LLM (khối lượng, tính đa dạng, domain-specific corpora).</p></li><li><p>Kỹ năng phân tích dữ liệu, đánh giá chất lượng và trực quan hóa kết quả.</p></li><li><p>Khả năng làm việc nhóm, giao tiếp hiệu quả với các nhóm AI/ML và kỹ thuật dữ liệu.</p></li></ul><p><strong>Ưu tiên:</strong></p><ul class="list-disc ml-4"><li><p>Kinh nghiệm làm việc với dữ liệu tiếng Việt hoặc ngôn ngữ có tính đa dạng cú pháp cao.</p></li><li><p>Hiểu biết về các xu hướng mới trong chuẩn bị dữ liệu cho LLM: synthetic data generation, data augmentation pipelines.</p></li><li><p>Nắm vững quy định pháp lý quốc tế và trong nước về dữ liệu (GDPR, Decree 13/2023/NĐ-CP).</p></li></ul><p></p>	<p><strong>Tiên phong công nghệ, uy tín</strong></p><ul class="list-disc ml-4"><li><p>MISA là doanh nghiệp CNTT xuất sắc nhất khu vực Châu Á - Châu Đại Dương. Tiên phong xuất khẩu giải pháp SaaS</p></li><li><p>TOP đầu doanh nghiệp CNTT tăng trưởng liên tục với quy mô nhân sự tăng 20%/năm, doanh thu tăng 15%/năm</p></li><li><p>Hội tụ 3000 nhân tài cùng khát vọng đưa sản phẩm công nghệ “Make In Vietnam” vươn tầm quốc tế</p></li><li><p>Xây dựng niềm tin với 270.000 khách hàng là đơn vị HCSN, doanh nghiệp, 2.5 triệu khách hàng cá nhân tại Việt Nam và 20 quốc gia</p></li><li><p>Hơn 100 giải thưởng trong ngành CNTT trong nước và quốc tế</p></li></ul><p><strong>Nền tảng vững chắc cho phát triển sự nghiệp, thăng tiến, quyền lợi</strong></p><ul class="list-disc ml-4"><li><p>Lương cứng cạnh tranh. Thưởng năng suất dựa trên kết quả công việc từ 2 tháng lương.</p></li><li><p>Đánh giá review lương 2 lần/năm, thưởng sáng kiến...</p></li><li><p>Huấn luyện “Hổ tướng”: chương trình đào tạo quản lý tài năng, bệ phóng trở thành Chiến tướng tinh nhuệ</p></li><li><p>Giải thưởng “Gấu vàng": nơi tôn vinh những tài năng xuất sắc nhất</p></li><li><p>Gói chăm sóc sức khỏe toàn diện tại Medlatec, cháy hết mình tại các CLB theo sở thích, chương trình teambuilding, du lịch định kỳ</p></li></ul><p><strong>Môi trường thân thiện, chia sẻ, đồng hành</strong></p><ul class="list-disc ml-4"><li><p>Kết nối tài năng: tập trung phát triển những con người có chung lý tưởng, mục tiêu, cùng trao giá trị và nhận thành công</p></li><li><p>Tư duy đột phá: môi trường tôn trọng sự khác biệt và đề cao sáng tạo, MISA-er được tự do phát triển các ý tưởng tiến bộ, cải tiến công việc</p></li><li><p>Công nghệ cao: trang bị máy tính làm việc, tối ưu hiệu suất công việc bằng ứng dụng công nghệ, phần mềm tự động (AMIS, Jira, Power BI, AI Marketing,...)</p></li><li><p>Nơi làm việc hạnh phúc: MISA mong muốn tạo một môi trường làm việc để bạn luôn cảm thấy hạnh phúc</p></li></ul><p></p>	7	OPEN	t	f	\N	t	f	2026-01-06 08:09:19.114012	2026-01-06 08:10:18.467569	minhnq224	minhnq224	t	f	91b8e658-3121-4041-8efd-4710b89d1b0b
18	JD-2	Senior Backend Developer (PHP, Laravel, MySQL, Kafka)	7	14	FULL_TIME	2026-01-31 00:00:00	2	RANGE	30000000	50000000	VND	PHP; Microservices; Laravel; AWS; Financial	<p><strong>Introduction:</strong></p><p>We are looking for a highly skilled Senior Software Developer to join our talented engineering team. In this role, you will have the opportunity to work on complex systems that support large- scale applications. You will be responsible for backend development, architecture, system performance optimization, and driving the integration of tools such as Laravel, Kafka, MySQL, Graylog, and AWS.</p><p><strong>Responsibilities:</strong></p><ul class="list-disc ml-4"><li><p><strong>Backend Development:</strong></p><ul class="list-disc ml-4"><li><p>Lead the design and implementation of scalable, efficient, and secure applications using Laravel.</p></li><li><p>Architect and build RESTful APIs and services with a focus on scalability and maintainability.</p></li><li><p>Work on system components with high traffic and large data volumes, ensuring that they are optimized for performance and reliability.</p></li></ul></li><li><p><strong>Database Management:</strong></p><ul class="list-disc ml-4"><li><p>Build, optimize, and maintain high-performance MySQL databases.</p></li><li><p>Ensure data integrity and high availability with a focus on query optimization, schema design, and indexing strategies.</p></li><li><p>Implement data migrations and transformations as required by the application</p></li></ul></li><li><p><strong>Graylog for Monitoring &amp; Logging:</strong></p><ul class="list-disc ml-4"><li><p>Set up and manage Graylog to aggregate logs and monitor application health.</p></li><li><p>Work on log processing and ensure log data is being captured correctly for debugging, analytics, and alerting purposes.</p></li></ul></li><li><p><strong>Performance Optimization:</strong></p><ul class="list-disc ml-4"><li><p>Continuously analyze system performance and implement improvements to enhance speed, scalability, and reliability.</p></li><li><p>Optimize code and database queries to ensure high efficiency and low latency.</p></li><li><p>Address bottlenecks in both the backend code and infrastructure.</p></li></ul></li><li><p><strong>Mentorship &amp; Leadership:</strong></p><ul class="list-disc ml-4"><li><p>Provide mentorship and technical guidance to junior and mid-level developers, helping to develop their skills.</p></li><li><p>Lead by example, contributing to coding standards, code reviews, and best practices.</p></li><li><p>Foster a culture of learning and growth within the engineering team.</p></li></ul></li><li><p><strong>Collaboration &amp; Communication:</strong></p><ul class="list-disc ml-4"><li><p>Collaborate closely with cross-functional teams, including product, design, and operations, to deliver features on time and in scope.</p></li><li><p>Communicate technical concepts to both technical and non- technical stakeholders effectively.</p></li><li><p>Lead or participate in architecture reviews, sprint planning, and other agile ceremonies.</p></li></ul></li></ul><p></p>	<p><strong>Requirements:</strong></p><ul class="list-disc ml-4"><li><p><strong>Technical Skills:</strong></p><ul class="list-disc ml-4"><li><p>Expert-level experience with Laravel framework and modern PHP development practices.</p></li><li><p>Strong experience working with Kafka for message brokering and real-time data streaming.</p></li><li><p>Solid understanding of MySQL, including performance tuning, indexing, and query optimization.</p></li><li><p>Proficiency in AWS cloud services (EC2, S3, RDS, CloudWatch, Lambda, etc.).</p></li><li><p>Experience with Graylog or similar log management/monitoring tools.</p></li><li><p>Expertise in performance optimization techniques for both backend and database systems.</p></li></ul></li><li><p><strong>Experience:</strong></p><ul class="list-disc ml-4"><li><p>5+ years of professional experience in software development, with a focus on backend systems.</p></li><li><p>Proven track record in designing, building, and maintaining scalable web applications.</p></li><li><p>Experience in building and maintaining production-level applications with high traffic and low latency requirements.</p></li><li><p>Experience in architecting and developing microservices or distributed systems.</p></li></ul></li><li><p><strong>Soft Skills:</strong></p><ul class="list-disc ml-4"><li><p>Strong leadership and mentoring skills, with the ability to guide a team through complex technical challenges.</p></li><li><p>Excellent problem-solving skills and the ability to debug complex issues under pressure.</p></li><li><p>Strong communication skills, with the ability to effectively communicate complex technical ideas to both technical and non-technical<br>stakeholders.</p></li><li><p>Self-motivated, proactive, and able to work independently or as part of a team in a fast-paced environment.</p></li></ul></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>&nbsp; Getting 100% salary paid for work trial.</p></li><li><p>&nbsp; Attractive salary schemes including 13th month salary</p></li><li><p>&nbsp; Equipment provided includes Macbook and widescreen monitor</p></li><li><p>&nbsp; Premium healthcare package</p></li><li><p>&nbsp; Monthly team dinners and annual company trips</p></li><li><p>&nbsp; Opportunity to greatly improve English skills with CubicStack’s international client</p></li><li><p>&nbsp; Embracing multiple, new technical stacks to accelerate business development</p></li><li><p>&nbsp; Allowed flexible working hours with annual leaves package</p></li></ul><p></p>	34	OPEN	t	f	\N	t	f	2026-01-06 09:39:27.348394	\N	DIEPADANG	\N	t	f	\N
19	JD-3	Mid-Senior Java Backend Developer (Spring, English)	7	14	CONTRACT	2026-01-28 00:00:00	4	NEGOTIABLE	\N	\N	VND	Java; SOLID; Backend	<p>We're looking for an experienced&nbsp;<strong>Mid</strong>-<strong>Senior Backend Java Engineer</strong> to join our team. In this role, you'll be a key player in designing, implementing, and optimizing our backend systems and services. You'll work on complex distributed systems, contribute to architectural decisions, and help mentor junior team members, all while driving high-quality code and project outcomes.</p><p><strong>What You'll Be Doing</strong></p><ul class="list-disc ml-4"><li><p><strong>System Design &amp; Development:</strong> Lead the analysis and design of complex systems and API services. You'll be responsible for the full development lifecycle, from concept to deployment.</p></li><li><p><strong>Code Quality &amp; Maintenance:</strong> Write robust&nbsp;<strong>unit and integration tests</strong> to ensure the accuracy and reliability of our services. You'll also participate in&nbsp;<strong>code reviews</strong> to maintain high standards and help colleagues grow.</p></li><li><p><strong>Architectural Leadership:</strong> Design and implement distributed computing services and propose improvements to existing system architecture. Your insights will be crucial in areas like index strategies, query optimization, and&nbsp;<strong>Redis</strong> data structures.</p></li><li><p><strong>Project Ownership:</strong> Take full ownership of project requirements, from clarifying ambiguities with stakeholders to decomposing projects into actionable tasks and coordinating with other teams.</p></li><li><p><strong>Team &amp; Process Improvement:</strong> Actively identify opportunities for system optimization and process improvements. You will contribute to refining team culture, development workflows, and engineering best practices.</p></li><li><p><strong>Mentorship &amp; Collaboration:</strong> Collaborate with cross-functional teams (Product, Frontend, QA, IT) and provide mentorship and technical guidance to more junior team members.</p></li></ul><p><br></p>	<p><strong>Required Skills &amp; Experience</strong></p><ul class="list-disc ml-4"><li><p><strong>Experience:</strong> A minimum of&nbsp;<strong>3 years</strong> of professional experience in software development.</p></li><li><p><strong>Proficiency:</strong> Deep expertise in&nbsp;<strong>Java</strong>, including the&nbsp;<strong>Spring Framework</strong>, and proficiency in&nbsp;<strong>Shell scripting</strong>.</p></li><li><p><strong>System Design:</strong> Proven experience in designing and implementing&nbsp;<strong>distributed services</strong> and applying&nbsp;<strong>design patterns</strong>.</p></li><li><p><strong>Database Skills:</strong> Strong command of&nbsp;<strong>SQL syntax</strong> and an understanding of database normalization.</p></li><li><p><strong>Tools &amp; Technologies:</strong> Hands-on experience with&nbsp;<strong>Redis</strong> and familiarity with the&nbsp;<strong>ELK stack</strong> (Elasticsearch, Logstash, Kibana).</p></li><li><p><strong>Testing:</strong> Experience in writing effective&nbsp;<strong>unit tests</strong> is essential.</p></li><li><p><strong>Soft Skills:</strong> You should be a proactive problem-solver with a tenacious attitude, excellent communication skills (<strong>English or Chinese</strong>) for both written and speaking, and the ability to articulate technical decisions clearly. Willingness to engage in pair programming is also required.</p></li></ul><p><strong>What We Offer</strong></p><p>This role offers a clear path for growth and increasing impact within our team.</p><ul class="list-disc ml-4"><li><p><strong>First 3-6 Months:</strong> You will onboard, get familiar with our tech stack and workflows, and begin taking responsibility for product optimization and routine maintenance. You'll start tackling more complex technical requirements.</p></li><li><p><strong>Beyond 6 Months:</strong> You'll transition into leading core development tasks, contributing significantly to system architecture, and mentoring newer team members. You'll have the opportunity to influence engineering best practices and play a key role in technical decision-making.</p></li></ul><p><strong>The Details</strong></p><ul class="list-disc ml-4"><li><p><strong>Work Type</strong>: On-Site</p></li><li><p><strong>Work Hours</strong>: Day shift, 09:00–18:00, Monday to Friday.</p></li><li><p><strong>Management</strong>: This is an individual contributor role with no direct management responsibilities.</p></li><li><p><strong>Leave</strong>: Two days off per week (Saturday and Sunday).</p></li><li><p><strong>Education</strong>: Bachelor’s degree or above (no specific major required).</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p><strong>Full Salary Insurance Contribution:</strong> Social, Health, and Unemployment Insurance paid on 90<strong>% of your official gross salary</strong>.</p></li><li><p><strong>Annual Bonuses:</strong> <strong>13th-Month, 14th-Month Salary and KPIs</strong> <strong>Bonus</strong></p></li><li><p><strong>Healthcare:</strong> Comprehensive <strong>Annual Health Check-up</strong> package provided by the company.</p></li><li><p><strong>Work Equipment:</strong> Provision of necessary <strong>computers, and modern office equipment</strong> required for your role.</p></li><li><p><strong>Team Building Budget:</strong> A dedicated <strong>quarterly budget</strong> for team meals or staff gatherings.</p></li><li><p><strong>Company Trip:</strong> An annual company vacation (<strong>local and/or international</strong> destinations).</p></li><li><p><strong>Special Occasion Gifts:</strong> Thoughtful gifts provided for major holidays (e.g., <strong>Tet, Mid-Autumn Festival, Christmas</strong>, etc.).</p></li></ul><p></p>	34	OPEN	t	f	\N	t	f	2026-01-06 09:48:36.70653	\N	DIEPADANG	\N	t	t	\N
20	JD-4	Fullstack Dev - AI products	7	14	FULL_TIME	2026-02-25 00:00:00	2	NEGOTIABLE	\N	\N	VND	AI; Fullstack	<h2><strong>Main Tasks</strong></h2><ul class="list-disc ml-4"><li><p>Integrate models with a focus on product features and prioritization.</p></li><li><p>Build and maintain software solutions and data processing pipelines</p></li><li><p>Ensure correct integration of LLMs in terms of quality and costs effectifness</p></li><li><p>Deploy models in production environments using cloud platforms (AWS) and containerization (Docker)</p></li><li><p>Integrate CI/CD pipelines using Git and automated testing</p></li><li><p>Collaborate with product stakeholders to define and prioritize requirements</p></li><li><p>Document and communicate technical content clearly to non-technical stakeholders.</p></li></ul><p></p>	<h2><strong>Job Qualification</strong></h2><ul class="list-disc ml-4"><li><p>Degree in Computer Science, Data Science, Mathematics, or equivalent</p></li><li><p>Minimum 3 years of professional experience in Software Development with focus on AI Products</p></li><li><p>Experience in product-oriented software development (not research-focused)</p></li><li><p>Ability to independently prioritize and implement requirements</p></li><li><p>Willingness to participate in in-person meetings with local team members</p></li></ul><p><strong>Hard Skills (must have)</strong></p><ul class="list-disc ml-4"><li><p>Ability to explain complex technical content in a clear and understandable way</p></li><li><p>Proactive problem-solving and independent prioritization of tasks</p></li><li><p>Product and feature mindset: focus on implementation and value, not research.</p></li><li><p>Python Ecosystem: Pandas, NumPy</p></li><li><p>Data Processing: SQL, database design, ETL processes</p></li><li><p>Cloud &amp; Deployment: AWS, Docker</p></li><li><p>Versioning &amp; CI/CD: Git, automated pipelines</p></li><li><p>Security: Basic knowledge of cloud security (IAM, access control).</p></li></ul><p></p>	<p><strong>Why TecAlliance Vietnam?&nbsp;</strong></p><p><strong>Growth opportunities:</strong></p><ul class="list-disc ml-4"><li><p>Top modern technology stacks</p></li><li><p>Working on challenging products</p></li><li><p>Work directly with software development colleagues across Europe and Asia</p></li><li><p>Travelling across Europe and Asia</p></li><li><p>Competitive salary with regular reviews, growth paths</p></li></ul><p><strong>Professional working environment:&nbsp;</strong></p><ul class="list-disc ml-4"><li><p>Innovating together</p></li><li><p>A real global team which works agile</p></li><li><p>Culture of active support for learning and development</p></li><li><p>Onboarding and training process to get part of the global team</p></li><li><p>Flexibility on work times with Hybrid (default)</p></li><li><p>Central office location in Ho Chi Minh City</p></li></ul><p><strong>Challenging products:</strong></p><ul class="list-disc ml-4"><li><p>We help to shape digital change</p></li><li><p>35 languages</p></li><li><p>7.5 million articles from 700+ brands</p></li><li><p>4 million repair instructions</p></li><li><p>E-mobility</p></li><li><p>Connectivity &amp; Telematics</p></li></ul><p><strong>High collaborative colleagues:</strong></p><ul class="list-disc ml-4"><li><p>Fun and Young team members</p></li><li><p>Team boding events</p></li><li><p>Social contribution</p></li></ul><p><strong>We offer&nbsp;</strong></p><p><strong>Health and wellness:</strong></p><ul class="list-disc ml-4"><li><p>Medical, dental insurance for employees, including packages for family members (maximum 02) after probation period (PIT will be calculated)</p></li><li><p>Annual health check</p></li><li><p>Annual company outing trip, year-end party and monthly social activities</p></li><li><p>Pantry snacks and drinks</p></li><li><p>Free lunch at office</p></li><li><p>Ergonomic chair &amp; lift table.</p></li></ul><p><strong>Financial wellbeing</strong></p><ul class="list-disc ml-4"><li><p>Competitive compensation</p></li><li><p>13th month salary</p></li><li><p>Phone and travelling allowance</p></li></ul><p><strong>Flexibility and time off</strong></p><ul class="list-disc ml-4"><li><p>Pay time off, 15 days annual leaves and 10 days sick leaves</p></li><li><p>Hybrid work model (default)</p></li></ul><p></p>	34	OPEN	t	f	\N	t	f	2026-01-06 09:57:44.139196	\N	DIEPADANG	\N	f	f	\N
21	JD-5	Full Stack Developer (NodeJS, ReactJS, AI)	7	14	FULL_TIME	2026-02-10 00:00:00	4	RANGE	30000000	50000000	VND	Fullstack; CI/CD	<p>We're looking for a Senior Full Stack Developer who excels at building polished, production-ready user interfaces at speed. You'll leverage AI-powered development tools to rapidly create UI screens and integrate them with our backend infrastructure. This role is ideal for someone who combines strong frontend craft with full-stack capabilities and embraces AI to maximize productivity.</p><p><strong>Your Tasks</strong></p><ul class="list-disc ml-4"><li><p>Build responsive, professional user interfaces using React—rapidly translating designs and requirements into functional screens</p></li><li><p>Leverage AI development tools (Claude, GitHub Copilot, Cursor, v0) to accelerate prototyping and production development</p></li><li><p>Design and implement RESTful APIs using Node.js (Express/Nest.js) to connect frontend with backend services</p></li><li><p>Create and maintain a scalable component library enabling consistent, rapid UI development</p></li><li><p>Design efficient database schemas (PostgreSQL, MySQL) and ensure data model integration</p></li><li><p>Collaborate closely with product team to iterate quickly based on user feedback</p></li><li><p>Implement CI/CD pipelines and support deployment processes</p></li><li><p>Ensure application security through proper authentication (OAuth, JWT) and best practices</p></li><li><p>Participate in code reviews, testing, and troubleshooting</p></li></ul><p></p>	<p><strong>Must Have</strong></p><ul class="list-disc ml-4"><li><p>4+ years in software development, with 2+ years in full stack development</p></li><li><p>Strong proficiency in React.js with demonstrated ability to create polished, pixel- perfect interfaces</p></li><li><p>Solid experience with Node.js (Express.js or Nest.js) for backend development</p></li><li><p>Experience using AI coding assistants to accelerate development workflows</p></li><li><p>Strong understanding of JavaScript, HTML, CSS, and responsive design principles</p></li><li><p>Experience with RESTful API design and implementation</p></li><li><p>Familiarity with CI/CD tools (Jenkins, GitLab CI, or similar)</p></li><li><p>Knowledge of authentication protocols and security best practices</p></li><li><p>Strong problem-solving skills and attention to visual detail</p></li><li><p>Good English communication skills (written and verbal)</p></li><li><p>Can-do attitude and desire for a long-term position in a growing team</p></li></ul><p><strong>Nice to Have</strong></p><ul class="list-disc ml-4"><li><p>Experience with AI-powered UI generation tools (Claude Code, v0, Bolt, or similar)</p></li><li><p>Familiarity with design tools (Figma) and design-to-code workflows</p></li><li><p>Experience building data-heavy dashboards and visualization components</p></li><li><p>Proficiency in SQL and relational databases (PostgreSQL, MySQL)</p></li><li><p>Python experience</p></li><li><p>Cloud platform experience (AWS, Azure, Google Cloud)</p></li><li><p>Understanding of logistics/freight domain or B2B SaaS</p></li></ul><p><strong>Language skills:</strong></p><ul class="list-disc ml-4"><li><p>Written and spoken English</p></li><li><p>German communication would be a plus.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Social security systems such as health insurance, social security, unemployment insurance according to the Labour Code (based on your full gross salary).</p></li><li><p>Health Check-Up• Team Building / Excursions</p></li><li><p>Annual salary review</p></li><li><p>Opportunity to build an innovative product with disruption potential based on 20 years of domain experience and large amounts of data – strong backing by a well-established German company and highly dynamic team behind.</p></li><li><p>Professional, enthusiastic, dynamic environment with learning opportunities in a new location with long-term commitment.</p></li><li><p>You will have the opportunity to contribute your own strengths to a new young team and help build up the location.</p></li><li><p>13th salary included, with additional performance bonuses based on individual performance and the company's revenue.</p></li><li><p>MacBook M3 pro and all other Apple devices (mouse, keyboard), headset provided by company</p></li></ul><p></p>	34	OPEN	t	f	\N	t	f	2026-01-06 09:59:09.568517	\N	DIEPADANG	\N	f	f	\N
22	JD-2	Tuyển TTS Backend - PHP	9	6	FULL_TIME	2026-01-24 00:00:00	2	NEGOTIABLE	\N	\N	VND	php	<p>Phối hợp triển khai chức năng cho hệ thống sử dụng PHP. Nghỉ T7 CN, được đào tạo bài bản bởi các Mentor dày dặn kinh nghiệm.</p>	<ul class="list-disc ml-4"><li><p>Có kiến thức cơ bản về PHP</p></li><li><p>Có kiến thức cơ bản về cấu trúc dữ liệu và giải thuật</p></li><li><p>Giao tiếp tiếng Anh cơ bản</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Thử việc 100% lương</p></li><li><p>Thưởng Tết, thưởng dự án</p></li><li><p>Teambuilding</p></li><li><p>Review lương 2 lần 1 năm</p></li></ul><p></p>	40	OPEN	t	f	\N	t	f	2026-01-06 13:23:09.067217	\N	morsoftware	\N	f	f	\N
23	JD-3	Lập Trình Viên PHP (Có Thể Đi Làm Sau Tết)	9	6	FULL_TIME	2026-01-31 00:00:00	1	NEGOTIABLE	\N	\N	VND	php	<ul class="list-disc ml-4"><li><p>Tham gia phát triển và bảo trì các dự án web sử dụng PHP/Laravel Framework</p></li><li><p>Phân tích yêu cầu hệ thống, nắm vững tính năng sản phẩm và đưa ra giải pháp kỹ thuật</p></li><li><p>Viết mã, xử lý logic nghiệp vụ và sửa lỗi theo phân công của trưởng nhóm</p></li><li><p>Nghiên cứu, áp dụng các công nghệ mới để cải tiến hoặc phát triển sản phẩm mới</p></li><li><p>Thời gian làm việc: giờ hành chính từ thứ 2 - thứ 6, nghỉ thứ 7, chủ nhật</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p><strong>Có kinh nghiệm từ 1 năm trong lập trình web với PHP/Laravel</strong></p></li><li><p><strong>Có kinh nghiệm chuyên sâu về HTML, CSS, JavaScript, JQuery, Ajax, Vue...</strong></p></li><li><p>Thành thạo PHP và framework Laravel, có kinh nghiệm thực tế trong phát triển web application</p></li><li><p>Có kinh nghiệm làm việc với cơ sở dữ liệu quan hệ (MySQL) và viết truy vấn SQL hiệu quả</p></li><li><p>Hiểu biết về RESTful API và kinh nghiệm xây dựng, tích hợp API</p></li><li><p>Thành thạo sử dụng Git hoặc hệ thống quản lý phiên bản khác</p></li><li><p>Biết thêm Python là một lợi thế</p></li><li><p>Có khả năng viết unit test và sử dụng các nền tảng kiểm thử tự động</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p><strong>Mức lương: từ 14 triệu - 16 triệu/tháng (hoặc có thể thỏa thuận theo kinh nghiệm và năng lực thực tế).</strong></p></li><li><p>Môi trường làm việc trẻ trung, thân thiện, chuyên nghiệp, năng động, tiếp xúc với nhiều công nghệ tiên tiến.</p></li><li><p>Được hưởng chế độ bảo hiểm sức khoẻ do công ty cung cấp.</p></li><li><p>Phúc lợi liên hoan phòng ban: 300,000/cá nhân/tháng.</p></li><li><p>Được tham gia các hoạt động của công ty như: Team building, gala dinner, annual trips, barbecues, ...</p></li><li><p>Được hưởng đầy đủ các quyền lợi theo luật lao động: nghỉ phép, BHXH,...</p></li><li><p>Chế độ nâng lương linh hoạt; thưởng và các chế độ phúc lợi khác theo quy định của Công ty.</p></li><li><p>Được đào tạo, nâng cao về chuyên môn, kỹ năng nghiệp vụ và các kỹ năng mềm khác.</p></li></ul><p></p>	40	OPEN	t	f	\N	t	f	2026-01-06 13:40:03.31712	\N	morsoftware	\N	f	f	\N
25	JD-5	Senior Frontend Developer (React.Js/Next.Js) - Mức Lương Upto 30M	9	5	FULL_TIME	2026-01-31 00:00:00	1	RANGE	20000000	30000000	VND	frontend;react;tailwind	<ul class="list-disc ml-4"><li><p>Phát triển giao diện web hoặc ứng dụng đa nền tảng sử dụng ReactJS / NextJS, đảm bảo hiệu suất và trải nghiệm người dùng tốt nhất.</p></li><li><p>Triển khai UI/UX theo thiết kế, xây dựng các hiệu ứng và hoạt ảnh hiện đại, mượt mà.</p></li><li><p>Tích hợp với API backend và các API của bên thứ ba.</p></li><li><p>Tối ưu hóa hiệu năng, SEO (đặc biệt với Next.js) và khả năng tương thích trình duyệt.</p></li><li><p>Tham gia review code, viết unit test / integration test để đảm bảo chất lượng sản phẩm.</p></li><li><p>Phối hợp chặt chẽ với designer, backend, QA trong quá trình phát triển tính năng.</p></li><li><p>Nghiên cứu, đề xuất và ứng dụng các công nghệ front-end mới nhằm nâng cao chất lượng sản phẩm và quy trình làm việc.</p></li><li><p>(Nếu có nhu cầu) Hỗ trợ build &amp; publish ứng dụng lên App Store / Google Play khi làm việc với dự án hybrid hoặc PWA.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Tốt nghiệp đại học chuyên ngành CNTT hoặc các ngành liên quan;</p></li><li><p><strong>Có 2 - 3+ năm kinh nghiệm phát triển ứng dụng web (hoặc mobile nếu có);</strong></p></li><li><p><strong>Có ít nhất 1 năm kinh nghiệm làm việc trong các dự án AI;</strong></p></li><li><p>Thành thạo ReactJS hoặc Next.js;</p></li><li><p>Có kinh nghiệm làm việc với HTML, SCSS, ưu tiên biết thêm Tailwind;</p></li><li><p>Hiểu biết về RESTful APIs;</p></li><li><p>Nắm vững quy trình tiêu chuẩn trong việc xây dựng và triển khai phần mềm;</p></li><li><p>Có kinh nghiệm làm việc nhóm sử dụng Git;</p></li><li><p>Hiểu sâu về lập trình hàm (Functional programming);</p></li><li><p>Có trách nhiệm cao trong công việc;</p></li><li><p>Có khả năng làm việc độc lập hoặc làm việc nhóm hiệu quả.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Mức lương hấp dẫn tùy thuộc vào năng lực</p></li><li><p>Tham gia BHXH, teambuilding, phúc lợi hàng năm,...</p></li><li><p>Lễ, tết, tháng 13, thưởng khác theo năng lực</p></li><li><p>Làm việc tại môi trường startup chuyên nghiệp, năng động, trẻ trung.</p></li><li><p>Cơ hội tiếp cận với những dự án khủng, những xu hướng công nghệ/nền tảng mới nhất, thỏa sức sáng tạo phát triển bản thân.</p></li></ul><p></p>	40	OPEN	t	f	\N	t	f	2026-01-06 13:44:19.73005	\N	morsoftware	\N	f	f	\N
24	JD-4	Frontend Developer (Vue/Nuxt)	9	5	FULL_TIME	2026-01-31 00:00:00	1	RANGE	25000000	40000000	VND	nuxt;vue	<ul class="list-disc ml-4"><li><p>Chuyển đổi thiết kế Figma thành giao diện HTML/CSS/JS chuẩn, responsive và dễ bảo trì.</p></li><li><p>Xây dựng webapp bằng Vue.js và Nuxt.js (SPA/SSR/SSG), đảm bảo hiệu năng và khả năng mở rộng.</p></li><li><p>Tối ưu tốc độ tải trang (LCP, FID, CLS), bundle size, caching, hydration và SEO cho các trang SSR.</p></li><li><p>Triển khai logic frontend tương tác API (REST/GraphQL) và tích hợp module nội bộ.</p></li><li><p>Phối hợp với backend, product, marketing và design để đảm bảo chất lượng output.</p></li><li><p>Hiểu eCommerce funnel: Landing → PDP → Cart → Checkout → Post-purchase để cải thiện flow mua hàng.</p></li><li><p>Phân tích hành vi người dùng và làm việc với marketing/data để triển khai A/B test, cải thiện UI/UX và conversion.</p></li><li><p>Nắm các chỉ số cơ bản: CTR, ATC, Checkout Rate, Conversion Rate, AOV, Revenue.</p></li><li><p>Đánh giá &amp; phản biện thiết kế dựa trên khả thi kỹ thuật và trải nghiệm người dùng.</p></li><li><p>Đề xuất cải tiến UI/UX dựa trên dữ liệu: heatmap, session replay, analytics hoặc feedback từ team growth.</p></li><li><p>Đảm bảo consistency theo design system: layout, component, spacing, typography.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Từ 2 năm kinh nghiệm với Vue.js, Nuxt.js và các mô hình SPA + SSR/SSG.</p></li><li><p>Thành thạo HTML5, CSS3 (SCSS/Tailwind), JavaScript ES6+.</p></li><li><p>Kinh nghiệm làm việc với API và Git.</p></li><li><p>Hiểu UI/UX cơ bản, đọc hiểu thiết kế tốt và nắm spacing logic.</p></li><li><p>Tư duy logic, khả năng phân rã vấn đề và tối ưu trải nghiệm người dùng.</p></li><li><p>Ưu tiên có kinh nghiệm storefront eCommerce: Shopify headless, Woo headless, custom.</p></li><li><p>Ưu tiên kinh nghiệm về tối ưu hiệu năng (lazy load, prefetch, CDN, caching).</p></li><li><p>Ưu tiên có tư duy product/business và hiểu customer journey.</p></li><li><p>Biết sử dụng analytics tools (GA4, TikTok Pixel, Meta Pixel, Hotjar, Clarity…) là một lợi thế.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>Thu nhập cạnh tranh nhất thị trường, tương xứng với năng lực và hiệu quả công việc</p></li><li><p>Thưởng tháng 13 và thưởng kinh doanh cuối năm rõ ràng, minh bạch</p></li><li><p>Chính sách làm việc linh hoạt (Hybrid / Work From Home), đảm bảo cân bằng giữa công việc và cuộc sống</p></li><li><p>Môi trường làm việc hạnh phúc &amp; hiện đại. Ghế công thái học 100% nhân viên</p></li><li><p>Pantry luôn đầy đồ ăn</p></li><li><p>Tiệc sinh nhật hàng tháng, du lịch nước ngoài.</p></li><li><p>Bàn bi-a, không gian giải trí và văn phòng view tàu điện trên cao cực chill</p></li><li><p>Cơ hội tham gia xây dựng storefront eCommerce đa brand – high-traffic – tốc độ cao.</p></li><li><p>Lộ trình phát triển lên Senior Frontend / Frontend Architect trong 12–24 tháng.</p></li><li><p>Môi trường đề cao ownership, tốc độ và sự chủ động, luôn ưu tiên hiệu suất và kết quả</p></li></ul><p></p>	40	OPEN	t	f	\N	t	f	2026-01-06 13:41:59.042688	\N	morsoftware	\N	f	f	\N
26	JD-6	Frontend Team Lead (ReactJS/NextJS/React Native)	9	5	FULL_TIME	2026-01-31 00:00:00	1	NEGOTIABLE	\N	\N	VND	leader;frontend	<p><strong>1. Functional Duties</strong></p><p>● Build and maintain modern web and mobile applications using HTML, CSS, JavaScript, and contemporary frontend frameworks.</p><p>● Translate UX/UI designs into high-quality, maintainable, and scalable front-end code.</p><p>● Integrate front-end components with backend APIs and data services.</p><p>● Ensure performance, scalability, stability, and maintainability of front-end systems.</p><p>● Maintain documentation for front-end components, coding standards, and technical decisions.</p><p></p><p><strong>2. Team Leadership &amp; Architecture</strong></p><p>● Lead front-end architecture design and enforce coding standards across the FE team.</p><p>● Conduct code reviews and ensure best practices are followed consistently.</p><p>● Mentor and coach junior and mid-level developers to accelerate their growth.</p><p>● Assign tasks based on team strengths, workload, and project priorities.</p><p>● Identify front-end technical risks and propose mitigation strategies.</p><p>● Participate in the hiring process, including interviews and technical assessments.</p><p></p><p><strong>3. Reporting &amp; Monitoring</strong></p><p>● Report team progress, blockers, and technical risks to the Tech Lead and PDM.</p><p>● Monitor workloads, delivery timelines, and quality of front-end deliverables.</p><p>● Oversee FE team operations in the absence of the Tech Lead or PDM.</p><p>● Proactively track and resolve performance, stability, and UX issues. Cross-functional Collaboration</p><p>● Collaborate with the Tech Lead to align front-end solutions with overall system architecture.</p><p>● Work with the PDM on estimation, planning, refinement, and feasibility checks.</p><p>● Coordinate with backend, QA, and design teams to ensure seamless cross-team workflows and high-quality delivery.</p>	<p><strong>Qualifications Education</strong>: Bachelor’s or Master’s degree in Computer Science, Software Engineering, or related fields.</p><p></p><p><strong>Technical Knowledge &amp; Expertise</strong></p><p>● Good English proficiency.</p><p>● Advanced knowledge of JavaScript frameworks: ReactJS, NextJS, React Native, Redux.</p><p>● Strong command of HTML, CSS, JavaScript, React, and TypeScript for building modern, responsive applications.</p><p>● Experience with Git-based version control platforms such as GitHub or Bitbucket.</p><p>● Knowledge of publishing applications on both iOS and Android.</p><p>● Familiarity with MS Office, Jira, and Slack is a plus.</p><p>● Solid understanding of full application development lifecycle: ideation, planning, documentation, and leading large-scale projects.</p><p>● Ability to write clean, maintainable, and well-structured code.</p><p></p><p><strong>Experience</strong></p><p>● 7+ years of software development experience, and;</p><p>● At least 2 years of experience in a FE technical lead or supervisory role</p><p></p><p><strong>Soft Skills</strong></p><p>● Strong communication and interpersonal skills.</p><p>● Excellent problem-solving and analytical thinking.</p><p>● Effective time management and ability to prioritize.</p><p>● Positive, proactive “can-do” mindset.</p><p>● Ability to explain design and technical decisions clearly and confidently.</p><p>● High sense of responsibility and ownership over team deliverables.</p><p>● Adaptability and openness to new technologies, tools, and methodologies.</p><p>● Strong teamwork and collaboration capabilities.</p>	<ul class="list-disc ml-4"><li><p>Attractive salary package (negotiable in the interview)</p></li><li><p>100% salary from probation period</p></li><li><p>24 paid day-offs (effective from probation period)</p></li><li><p>Social insurance in high salary</p></li><li><p>Premium Health Insurance package</p></li><li><p>Lunch allowance:<strong> 2.000.000vnd</strong>/person/month</p></li><li><p>Annual health check; Company trip &amp; Occasional gifts</p></li><li><p>Yearly learning budget: 300$/person/year</p></li><li><p>Yearly Position Level Assessments</p></li><li><p>Pantry Offering</p></li><li><p>High-end MacBook or laptop, monitor, and other devices as required</p></li></ul><p></p>	40	OPEN	t	f	\N	t	f	2026-01-06 13:51:01.212168	\N	morsoftware	\N	f	f	\N
27	JD-7	Middle/Senior Backend Engineer (Java, Fullstack)	9	6	FULL_TIME	2026-01-31 00:00:00	1	NEGOTIABLE	\N	\N	VND	senior;backend	<p><strong>Job brief</strong></p><p>We are looking for a Middle/ Senior Backend Developer to manage the software development life cycle, from planning and prioritizing to testing and release.</p><p>Middle/ Senior Developer responsibilities include gathering system and user requirements, building modules and testing features before release. If you have hands-on experience developing software with agile methodologies and are ready to lead our junior developers, we’d like to meet you.</p><p>Ultimately, you will deploy and maintain functional, secure and high-quality applications.</p><p></p><p><strong>Your responsibilities</strong></p><p>Identify and analyze user requirements.</p><p>Implement our applications, services and products.</p><p>Write well-designed, efficient code.</p><p>Review, test and debug team members’ code.</p><p>Release and maintain our products/ services in production environments.</p><p>Ensure our applications are secure and up to date.</p>	<p><strong>Your skills and experience</strong></p><p>Work experience as a Middle/ Senior Backend Developer or similar role.</p><p>Experience in designing, implementing, and testing applications using technologies such as Java.</p><p>In-depth knowledge of popular Java frameworks like Spring MVC, Spring Boot, Microservice, JPA, Rest API.</p><p>Proficiency in one of the following: Java.</p><p>Strong knowledge of SQL, MySQL or similar databases.</p><p>Worked on Java build &amp; dependency management using tools like Maven, Gradle.</p><p>Experience with Object-Oriented Design (OOD).</p><p>Experience with Test-Driven Development (TDD).</p><p>Strong delegation and time management skills, problem-solving abilities, teamwork skills.</p><p>English: Intermediate to Upper intermediate level.</p><p>Bachelor’s degree in Computer Science, Information Technology, Engineering or relevant field.</p><p><strong>Nice to have</strong><br>Experience with Fullstack, Docker, Jenkins, AWS, Cloud and SaaS will be a plus.</p><p><strong>Proficiency in Japanese will be a significant advantage.</strong></p>	<p><strong>Why you'll love working here</strong></p><p>Providing market-competitive salary, benefits, can work remotely in future.</p><p>As you contribute to the development of in-house products and acquire business skills in the HR tech field, you will have the opportunity to lead product strategy for business expansion, including planning and development of new business ventures.</p><p>Parking Allowances.</p><p>Full salary for probation.</p><p>Social Insurance in full gross salary.</p><p>Salary review (Twice a year).</p><p>Bonus at the end of the year based on performance evaluation results and company revenue.</p><p>Annual leave, <strong>5 Special leave days</strong>.</p><p>Annual health check-up.</p><p>Company trip and other internal events.</p>	40	OPEN	f	f	\N	t	f	2026-01-06 13:57:40.255531	\N	morsoftware	\N	f	f	57fbc40d-58a4-40de-a997-334c50dc1fab
28	JD-7	Middle/Senior Developer (Java, Microservices)	1	1	FULL_TIME	2026-01-23 00:00:00	3	NEGOTIABLE	\N	\N	VND	Java; Microservices	<p>We are seeking a skilled developer to work on a centralized and optimized customer interaction, sales process, and customer service management platform. This role involves building a scalable and secure system that seamlessly integrates with existing business applications to enhance customer engagement and drive business growth.</p><p>1) Application Development &amp; Maintenance</p><p>Design, develop, and maintain Java applications using Spring Boot and Spring Framework.<br>Implement RESTful APIs and integrate with frontend applications.<br>Write efficient, scalable, and maintainable code.</p><p>2) Database Management</p><p>Develop and optimize database queries for MySQL and MongoDB.<br>Work with JPA/Hibernate for ORM (Object Relational Mapping) and ensure efficient database interactions.</p><p>3) Spring Framework Expertise</p><p>Utilize Spring Core, Spring MVC, Spring Security, Spring Data JPA, and Spring Boot for application development.</p><p>Implement Spring Security for authentication and authorization.<br>4) Testing &amp; Debugging</p><p>Write and maintain unit tests (JUnit, Mockito).</p><p>5) Version Control &amp; CI/CD</p><p>Manage source code using Git and repositories like GitHub/GitLab/Bitbucket.</p><p>6) Continuous Learning &amp; Improvement</p><p>Stay updated with the latest trends in Java, Spring, MySQL, MongoDB, and emerging technologies.<br>Improve existing features and propose innovative solutions.<br>Make quality and usability your primary goals.<br>Problem investigation, system troubleshooting and bug fixing.</p>	<ul class="list-disc ml-4"><li><p>Minimum of 4 years of experience in Java backend development using Spring Boot or similar frameworks.</p></li><li><p>Proficiency in Java 8+ with strong knowledge of OOP (Object-Oriented Programming) concepts.</p></li><li><p>Experience with Spring Framework, including Spring Boot (Microservices development), Spring MVC (Web applications), Spring Data JPA (ORM with Hibernate), Spring Security (Authentication &amp; Authorization)</p></li><li><p>Experience with JPA/Hibernate for ORM and database interactions.</p></li><li><p>Proficiency in developing RESTful APIs and working with API documentation tools like Swagger/OpenAPI.</p></li><li><p>Good knowledge of version control systems, preferably Git (GitHub, GitLab, or Bitbucket).</p></li><li><p>Familiarity with CI/CD pipelines (Jenkins, GitHub Actions, GitLab CI, or equivalent).</p></li><li><p>Basic knowledge of containerization using Docker (Kubernetes is a plus).</p></li><li><p>Understanding of caching mechanisms (Redis, Ehcache) and messaging systems (RabbitMQ, Kafka) is a plus.</p></li><li><p>Experience with testing frameworks such as JUnit, Mockito for unit and integration testing.</p></li><li><p>Experience with Web Layout Design and Development: HTML5, JavaScript, CSS, JQuery, AJAX is a plus.</p></li><li><p>Good to have knowledge in eCommerce systems, Hybris SAP.</p></li><li><p>Good willingness to learn new technologies required for work.</p></li></ul><p></p>	<ul class="list-disc ml-4"><li><p>13th-month salary bonus.</p></li><li><p>One additional day for Annual leave after each year of service.</p></li><li><p>Full base insurance contribution (Social, Medical, and Unemployment insurance).</p></li><li><p>Attractive healthcare insurance.</p></li><li><p>Lunch &amp; parking allowance.</p></li><li><p>Gift in special events.</p></li><li><p>Sponsorship for training courses, professional certificates.</p></li><li><p>Monthly Company’s meeting for employees’ understanding about company’s strategy and development plan; Happy Hour with snack and fresh fruit.</p></li><li><p>Various team building, sports activities, company trips, parties, and Trade Union activities.</p></li><li><p>Weekly social activities for Cycling club, football club,etc.</p></li><li><p>English speaking, professional, dynamic work environment with multi-national team members.</p></li><li><p>Opportunity to work on-site abroad.</p></li><li><p>Various charity activities to create opportunities for employees to help the poor and unfortunate people in Vietnam community.</p></li><li><p>Above benefits are designed for Vietnamese employees. Expat's package will be communicated in offer letter, including work permit support term and condition.</p></li></ul><p></p>	4	OPEN	t	t	1	t	f	2026-01-06 14:01:01.845132	2026-01-06 14:01:05.859306	cvconnect	cvconnect	f	f	3b1850c1-9409-457a-82c4-a22fe9100f59
13	JD-6	AI Expert - Khối Công nghệ thông tin	1	10	CONTRACT	2026-02-28 00:00:00	2	NEGOTIABLE	\N	\N	VND	AI; Banking	<p>Chịu trách nhiệm định hướng, giám sát, điều phối và phát triển các dự án, sản phẩm hoặc dịch vụ AI trong ngân hàng.</p><p>Tham gia xây dựng và triển khai định hướng cho các dự án AI</p><p>Lập kế hoạch chi tiết và quản lý các dự án phát triển và ứng dụng AI.</p><p>Giám sát và hỗ trợ đội ngũ phát triển trong việc phát triển và triển khai các giải pháp AI, đảm bảo chất lượng và hiệu suất cao nhất.</p><p>Làm việc chặt chẽ với các phòng ban liên quan tại Trung tâm AI (LLM, NLP, Computer Vision, Chatbot, R&amp;D.v.v. và các đơn vị khác) để đảm bảo sự phối hợp nhịp nhàng và tối ưu hóa nguồn lực.</p><p>Cập nhật và nghiên cứu các công nghệ AI mới nhất, áp dụng vào các dự án thực tế.</p><p>Đảm bảo chất lượng và hiệu suất cho các dự án AI, thực hiện kiểm tra và đánh giá định kỳ.</p><p>Xác định và đánh giá các rủi ro trong quá trình triển khai dự án.</p><p>Tham gia tuyển dụng nhân tài, huấn luyện và phát triển đội ngũ</p>	<p>Tối thiểu 3 năm kinh nghiệm trong lĩnh vực AI/ML.</p><p>Hiểu biết sâu về các công nghệ AI/ML/Computer Vision/NLP/LLM...</p><p>Có chuyên môn sâu về quản trị dữ liệu</p><p>Kỹ năng lập kế hoạch, tổ chức và quản lý công việc</p><p>Kỹ năng ra quyết định, giải quyết vấn đề và quản lý xung đột</p><p>Khả năng truyền cảm hứng cho đội ngũ</p><p>Hiểu biết về các quy định pháp lý, đạo đức liên quan đến AI</p><p>Kiến thức về quản trị rủi ro và các tiêu chuẩn ngành</p><p>MB Bank yêu cầu ứng viên ứng tuyển cần cung cấp chi tiết các thông tin sau:</p><p>Thông tin cá nhân: Họ và tên, Ngày tháng năm sinh, Giới tính</p><p>Số điện thoại và Email liên hệ</p><p>Trình độ học vấn, Trường đã tốt nghiệp</p><p>Kinh nghiệm làm việc</p><p>Từ 3 - 5 kỹ năng nổi bật</p><p>Nguồn Tuyển dụng</p>	<p>Thưởng tháng lương 13; Thưởng thành tích 06 tháng, 1 năm ; Thưởng các dịp lễ tết trong năm ; Thưởng theo danh hiệu cá nhân và tập thể…</p><p>Du lịch nghỉ dưỡng hàng năm, Khám sức khỏe định kì; Gói bảo hiểm sức khỏe cá nhân và người thân (MIC);</p><p>Quà tặng và ngày nghỉ sinh nhật hưởng nguyên lương</p><p>Được thử sức với các nền tảng công nghệ mới, tham gia vào những dự án chuyển đổi lớn của ngân hàng</p><p>Có cơ hội học hỏi từ các Chuyên gia, lãnh đạo nội bộ hàng đầu tại MB trong lĩnh vực IT, Tài chính ngân hàng</p><p>Được trải nghiệm các phương pháp học tập mới và phát triển năng lực theo lộ trình công danh.</p><p>Hưởng các chính sách hỗ trợ, khuyến khích học tập, nâng cao trình độ và phát triển bản thân (chứng chỉ nghề quốc tế...)</p><p>Những phương pháp làm việc mới đầy sự linh hoạt, sáng tạo và hiệu quả</p><p>Những người cộng sự thân thiện và tài năng</p><p>Cơ sở vật chất, không gian làm việc xanh và hiện đại.</p>	2	OPEN	t	t	1	t	f	2026-01-06 07:38:11.708723	2026-01-06 17:06:05.511729	cvconnect	quannm32	f	f	\N
\.


--
-- Data for Name: job_ad_candidate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_candidate (id, job_ad_id, candidate_info_id, apply_date, candidate_status, eliminate_reason_type, eliminate_reason_detail, is_active, is_deleted, created_at, updated_at, created_by, updated_by, onboard_date, eliminate_date) FROM stdin;
1	1	1	2025-12-01 04:35:18.480877	WAITING_ONBOARDING	\N	\N	t	f	2025-12-01 04:35:18.485156	2025-12-01 10:17:53.820461	vclong2003	cvconnect	2025-12-25 10:17:00	\N
4	7	4	2025-12-03 03:58:05.985643	IN_PROGRESS	\N	\N	t	f	2025-12-03 03:58:05.989603	2025-12-03 03:59:37.487653	quannm32	minhnq224	\N	\N
5	1	5	2025-12-03 07:31:25.122146	REJECTED	BACKGROUND_CHECK_FAILED	Lý lịch 3 đời ko trong sạch	t	f	2025-12-03 07:31:25.122592	2025-12-05 02:55:06.896768	duong2003nb	cvconnect	2025-12-31 07:32:00	2025-12-05 02:55:06.857706
3	2	3	2025-12-01 08:38:15.248084	ONBOARDED	\N	\N	t	f	2025-12-01 08:38:15.248733	2025-12-05 02:56:07.078825	admin	cvconnect	2025-12-24 02:55:00	\N
6	1	6	2025-12-11 09:33:33.261006	ONBOARDED	\N	\N	t	f	2025-12-11 09:33:33.264815	2025-12-20 09:20:34.841948	anhnt746	cvconnect	2026-01-01 01:30:00	\N
7	7	7	2025-12-20 16:55:18.958246	APPLIED	\N	\N	t	f	2025-12-20 16:55:18.962269	\N	huykeo2022@gmail.com	\N	\N	\N
2	1	2	2025-12-01 04:45:23.65884	ONBOARDED	\N	\N	t	f	2025-12-01 04:45:23.65908	2025-12-21 03:28:15.792031	iamdha0706	cvconnect	2026-01-14 17:58:00	\N
9	7	8	2025-12-24 03:39:51.872489	APPLIED	\N	\N	t	f	2025-12-24 03:39:51.873087	\N	tranvy10122000@gmail.com	\N	\N	\N
10	2	1	2025-12-26 04:05:59.760263	IN_PROGRESS	\N	\N	t	f	2025-12-26 04:05:59.76609	2025-12-28 13:58:59.115407	vclong2003	quannm32	\N	\N
11	8	9	2025-12-28 14:56:10.723549	VIEWED_CV	\N	\N	t	f	2025-12-28 14:56:10.727164	\N	duong2003nb	\N	\N	\N
12	9	9	2025-12-29 04:04:14.913917	IN_PROGRESS	\N	\N	t	f	2025-12-29 04:04:14.915468	2025-12-29 04:13:33.031152	duong2003nb	DIEPADANG	\N	\N
14	7	1	2025-12-31 16:02:11.097446	APPLIED	\N	\N	t	f	2025-12-31 16:02:11.11132	\N	vclong2003	\N	\N	\N
15	8	10	2026-01-06 02:52:50.574152	IN_PROGRESS	\N	\N	t	f	2026-01-06 02:52:50.578341	2026-01-06 02:53:32.714147	minhnq224	cvconnect	\N	\N
8	2	8	2025-12-24 03:31:26.437603	IN_PROGRESS	\N	\N	t	f	2025-12-24 03:31:26.443808	2026-01-06 14:57:51.393952	tranvy10122000@gmail.com	quannm32	\N	\N
16	13	11	2026-01-06 17:14:05.563377	VIEWED_CV	\N	\N	t	f	2026-01-06 17:14:05.567226	\N	ntthaovana921dqh@gmail.com	\N	\N	\N
13	9	1	2025-12-29 06:59:43.585969	VIEWED_CV	\N	\N	t	f	2025-12-29 06:59:43.586954	\N	vclong2003	\N	\N	\N
\.


--
-- Data for Name: job_ad_career; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_career (id, career_id, job_ad_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
2	152	2	t	f	2025-11-30 14:42:24.989139	\N	cvconnect	\N
3	154	2	t	f	2025-11-30 14:42:24.994074	\N	cvconnect	\N
4	152	7	t	f	2025-12-03 03:54:26.008375	\N	minhnq224	\N
5	154	7	t	f	2025-12-03 03:54:26.011224	\N	minhnq224	\N
6	161	7	t	f	2025-12-03 03:54:26.012813	\N	minhnq224	\N
7	152	8	t	f	2025-12-28 14:48:10.856515	\N	cvconnect	\N
8	154	9	t	f	2025-12-29 04:03:37.023508	\N	DIEPADANG	\N
9	153	10	t	f	2026-01-05 16:57:24.785836	\N	morsoftware	\N
10	172	11	t	f	2026-01-06 04:03:17.322511	\N	cvconnect	\N
11	153	12	t	f	2026-01-06 07:33:01.283533	\N	cvconnect	\N
12	152	12	t	f	2026-01-06 07:33:01.289296	\N	cvconnect	\N
13	158	13	t	f	2026-01-06 07:38:11.712622	\N	cvconnect	\N
14	156	14	t	f	2026-01-06 07:51:35.47169	\N	minhnq224	\N
15	156	15	t	f	2026-01-06 07:53:29.914572	\N	minhnq224	\N
16	162	16	t	f	2026-01-06 08:01:37.21477	\N	minhnq224	\N
17	158	16	t	f	2026-01-06 08:01:37.216395	\N	minhnq224	\N
18	162	17	t	f	2026-01-06 08:09:19.116438	\N	minhnq224	\N
19	163	17	t	f	2026-01-06 08:09:19.117684	\N	minhnq224	\N
20	152	18	t	f	2026-01-06 09:39:27.350517	\N	DIEPADANG	\N
21	154	18	t	f	2026-01-06 09:39:27.352227	\N	DIEPADANG	\N
22	152	19	t	f	2026-01-06 09:48:36.708372	\N	DIEPADANG	\N
23	154	19	t	f	2026-01-06 09:48:36.709744	\N	DIEPADANG	\N
24	152	20	t	f	2026-01-06 09:57:44.142037	\N	DIEPADANG	\N
25	155	20	t	f	2026-01-06 09:57:44.143599	\N	DIEPADANG	\N
26	158	20	t	f	2026-01-06 09:57:44.144643	\N	DIEPADANG	\N
27	152	21	t	f	2026-01-06 09:59:09.570419	\N	DIEPADANG	\N
28	155	21	t	f	2026-01-06 09:59:09.571698	\N	DIEPADANG	\N
29	154	22	t	f	2026-01-06 13:23:09.072293	\N	morsoftware	\N
30	154	23	t	f	2026-01-06 13:40:03.319833	\N	morsoftware	\N
31	153	24	t	f	2026-01-06 13:41:59.044854	\N	morsoftware	\N
32	155	24	t	f	2026-01-06 13:41:59.046031	\N	morsoftware	\N
33	153	25	t	f	2026-01-06 13:44:19.73208	\N	morsoftware	\N
34	152	26	t	f	2026-01-06 13:51:01.214418	\N	morsoftware	\N
35	153	26	t	f	2026-01-06 13:51:01.215873	\N	morsoftware	\N
36	152	27	t	f	2026-01-06 13:57:40.257537	\N	morsoftware	\N
37	154	27	t	f	2026-01-06 13:57:40.259067	\N	morsoftware	\N
38	152	28	t	f	2026-01-06 14:01:01.84707	\N	cvconnect	\N
39	154	28	t	f	2026-01-06 14:01:01.848327	\N	cvconnect	\N
\.


--
-- Data for Name: job_ad_level; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_level (id, job_ad_id, level_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	1	1	t	f	2025-11-30 09:07:09.275569	\N	cvconnect	\N
2	2	4	t	f	2025-11-30 14:42:25.030397	\N	cvconnect	\N
3	2	5	t	f	2025-11-30 14:42:25.032784	\N	cvconnect	\N
4	7	5	t	f	2025-12-03 03:54:26.039899	\N	minhnq224	\N
5	7	4	t	f	2025-12-03 03:54:26.042677	\N	minhnq224	\N
6	8	2	t	f	2025-12-28 14:48:10.894185	\N	cvconnect	\N
7	8	1	t	f	2025-12-28 14:48:10.896927	\N	cvconnect	\N
8	9	3	t	f	2025-12-29 04:03:37.03514	\N	DIEPADANG	\N
9	9	4	t	f	2025-12-29 04:03:37.036766	\N	DIEPADANG	\N
10	10	1	t	f	2026-01-05 16:57:24.816021	\N	morsoftware	\N
11	11	5	t	f	2026-01-06 04:03:17.364737	\N	cvconnect	\N
12	11	6	t	f	2026-01-06 04:03:17.367311	\N	cvconnect	\N
13	11	7	t	f	2026-01-06 04:03:17.369568	\N	cvconnect	\N
14	12	2	t	f	2026-01-06 07:33:01.34429	\N	cvconnect	\N
15	13	5	t	f	2026-01-06 07:38:11.72496	\N	cvconnect	\N
16	13	6	t	f	2026-01-06 07:38:11.726418	\N	cvconnect	\N
17	15	7	t	f	2026-01-06 07:53:29.926123	\N	minhnq224	\N
18	15	6	t	f	2026-01-06 07:53:29.92822	\N	minhnq224	\N
19	16	5	t	f	2026-01-06 08:01:37.225048	\N	minhnq224	\N
20	16	6	t	f	2026-01-06 08:01:37.226535	\N	minhnq224	\N
21	17	7	t	f	2026-01-06 08:09:19.125248	\N	minhnq224	\N
22	17	5	t	f	2026-01-06 08:09:19.126642	\N	minhnq224	\N
23	18	7	t	f	2026-01-06 09:39:27.360046	\N	DIEPADANG	\N
24	20	5	t	f	2026-01-06 09:57:44.151961	\N	DIEPADANG	\N
25	21	6	t	f	2026-01-06 09:59:09.579877	\N	DIEPADANG	\N
26	21	7	t	f	2026-01-06 09:59:09.581088	\N	DIEPADANG	\N
27	22	1	t	f	2026-01-06 13:23:09.084679	\N	morsoftware	\N
28	23	3	t	f	2026-01-06 13:40:03.330136	\N	morsoftware	\N
29	24	4	t	f	2026-01-06 13:41:59.055343	\N	morsoftware	\N
30	25	7	t	f	2026-01-06 13:44:19.740141	\N	morsoftware	\N
31	26	8	t	f	2026-01-06 13:51:01.223695	\N	morsoftware	\N
32	27	7	t	f	2026-01-06 13:57:40.266976	\N	morsoftware	\N
33	28	6	t	f	2026-01-06 14:01:01.85599	\N	cvconnect	\N
34	28	7	t	f	2026-01-06 14:01:01.85712	\N	cvconnect	\N
\.


--
-- Data for Name: job_ad_process; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_process (id, name, sort_order, job_ad_id, process_type_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	Ứng tuyển	1	1	1	t	f	2025-11-30 09:07:09.26004	\N	cvconnect	\N
2	Thi tuyển	2	1	3	t	f	2025-11-30 09:07:09.262731	\N	cvconnect	\N
3	Phỏng vấn chuyên môn	3	1	4	t	f	2025-11-30 09:07:09.26432	\N	cvconnect	\N
4	Phỏng vấn khách hàng	4	1	4	t	f	2025-11-30 09:07:09.265888	\N	cvconnect	\N
5	Onboard	5	1	6	t	f	2025-11-30 09:07:09.267779	\N	cvconnect	\N
6	Ứng tuyển	1	2	1	t	f	2025-11-30 14:42:25.013252	\N	cvconnect	\N
7	Thi tuyển	2	2	3	t	f	2025-11-30 14:42:25.015936	\N	cvconnect	\N
8	Phỏng vấn chuyên môn	3	2	4	t	f	2025-11-30 14:42:25.017705	\N	cvconnect	\N
9	Phỏng vấn khách hàng	4	2	4	t	f	2025-11-30 14:42:25.019586	\N	cvconnect	\N
10	Onboard	5	2	6	t	f	2025-11-30 14:42:25.021464	\N	cvconnect	\N
11	Ứng tuyển	1	7	1	t	f	2025-12-03 03:54:26.027072	\N	minhnq224	\N
12	Đề nghị làm việc	2	7	5	t	f	2025-12-03 03:54:26.029398	\N	minhnq224	\N
13	Lọc hồ sơ	3	7	2	t	f	2025-12-03 03:54:26.030616	\N	minhnq224	\N
14	Thi tuyển	4	7	3	t	f	2025-12-03 03:54:26.031919	\N	minhnq224	\N
15	Onboard	5	7	6	t	f	2025-12-03 03:54:26.033692	\N	minhnq224	\N
16	Ứng tuyển	1	8	1	t	f	2025-12-28 14:48:10.875693	\N	cvconnect	\N
17	Thi tuyển	2	8	3	t	f	2025-12-28 14:48:10.878627	\N	cvconnect	\N
18	Phỏng vấn chuyên môn	3	8	4	t	f	2025-12-28 14:48:10.880902	\N	cvconnect	\N
19	Phỏng vấn khách hàng	4	8	4	t	f	2025-12-28 14:48:10.883589	\N	cvconnect	\N
20	Onboard	5	8	6	t	f	2025-12-28 14:48:10.88624	\N	cvconnect	\N
21	Ứng tuyển	1	9	1	t	f	2025-12-29 04:03:37.029028	\N	DIEPADANG	\N
22	Phỏng vấn	2	9	4	t	f	2025-12-29 04:03:37.030848	\N	DIEPADANG	\N
23	Đề nghị làm việc	3	9	5	t	f	2025-12-29 04:03:37.032107	\N	DIEPADANG	\N
24	Onboard	4	9	6	t	f	2025-12-29 04:03:37.033269	\N	DIEPADANG	\N
25	Ứng tuyển	1	10	1	t	f	2026-01-05 16:57:24.802955	\N	morsoftware	\N
26	Phỏng vấn giao tiếp	2	10	4	t	f	2026-01-05 16:57:24.805494	\N	morsoftware	\N
27	Phỏng vấn technical	3	10	4	t	f	2026-01-05 16:57:24.80726	\N	morsoftware	\N
28	Onboard	4	10	6	t	f	2026-01-05 16:57:24.808845	\N	morsoftware	\N
29	Ứng tuyển	1	11	1	t	f	2026-01-06 04:03:17.343911	\N	cvconnect	\N
30	Thi tuyển	2	11	3	t	f	2026-01-06 04:03:17.347279	\N	cvconnect	\N
31	Phỏng vấn	3	11	4	t	f	2026-01-06 04:03:17.350095	\N	cvconnect	\N
32	Onboard	4	11	6	t	f	2026-01-06 04:03:17.353633	\N	cvconnect	\N
33	Ứng tuyển	1	12	1	t	f	2026-01-06 07:33:01.319535	\N	cvconnect	\N
34	Phỏng vấn chuyên môn	2	12	4	t	f	2026-01-06 07:33:01.324941	\N	cvconnect	\N
35	Phỏng vấn khách hàng	3	12	4	t	f	2026-01-06 07:33:01.328631	\N	cvconnect	\N
36	Onboard	4	12	6	t	f	2026-01-06 07:33:01.333618	\N	cvconnect	\N
37	Ứng tuyển	1	13	1	t	f	2026-01-06 07:38:11.71603	\N	cvconnect	\N
38	Thi tuyển	2	13	3	t	f	2026-01-06 07:38:11.718086	\N	cvconnect	\N
39	Phỏng vấn	3	13	4	t	f	2026-01-06 07:38:11.719788	\N	cvconnect	\N
40	Đề nghị làm việc	4	13	5	t	f	2026-01-06 07:38:11.721726	\N	cvconnect	\N
41	Onboard	5	13	6	t	f	2026-01-06 07:38:11.723399	\N	cvconnect	\N
42	Ứng tuyển	1	14	1	t	f	2026-01-06 07:51:35.475859	\N	minhnq224	\N
43	Lọc hồ sơ	2	14	2	t	f	2026-01-06 07:51:35.477579	\N	minhnq224	\N
44	Phỏng vấn	3	14	4	t	f	2026-01-06 07:51:35.479032	\N	minhnq224	\N
45	Onboard	4	14	6	t	f	2026-01-06 07:51:35.480451	\N	minhnq224	\N
46	Ứng tuyển	1	15	1	t	f	2026-01-06 07:53:29.919729	\N	minhnq224	\N
47	Lọc hồ sơ	2	15	2	t	f	2026-01-06 07:53:29.921571	\N	minhnq224	\N
48	Phỏng vấn	3	15	4	t	f	2026-01-06 07:53:29.923009	\N	minhnq224	\N
49	Onboard	4	15	6	t	f	2026-01-06 07:53:29.924503	\N	minhnq224	\N
50	Ứng tuyển	1	16	1	t	f	2026-01-06 08:01:37.219358	\N	minhnq224	\N
51	Thi tuyển	2	16	3	t	f	2026-01-06 08:01:37.220888	\N	minhnq224	\N
52	Phỏng vấn	3	16	4	t	f	2026-01-06 08:01:37.222438	\N	minhnq224	\N
53	Onboard	4	16	6	t	f	2026-01-06 08:01:37.223741	\N	minhnq224	\N
54	Ứng tuyển	1	17	1	t	f	2026-01-06 08:09:19.120351	\N	minhnq224	\N
55	Thi tuyển	2	17	3	t	f	2026-01-06 08:09:19.121557	\N	minhnq224	\N
56	Phỏng vấn	3	17	4	t	f	2026-01-06 08:09:19.122774	\N	minhnq224	\N
57	Onboard	4	17	6	t	f	2026-01-06 08:09:19.123887	\N	minhnq224	\N
58	Ứng tuyển	1	18	1	t	f	2026-01-06 09:39:27.355135	\N	DIEPADANG	\N
59	Thi tuyển	2	18	3	t	f	2026-01-06 09:39:27.356564	\N	DIEPADANG	\N
60	Phỏng vấn	3	18	4	t	f	2026-01-06 09:39:27.35774	\N	DIEPADANG	\N
61	Onboard	4	18	6	t	f	2026-01-06 09:39:27.358826	\N	DIEPADANG	\N
62	Ứng tuyển	1	19	1	t	f	2026-01-06 09:48:36.712742	\N	DIEPADANG	\N
63	Thi tuyển	2	19	3	t	f	2026-01-06 09:48:36.714163	\N	DIEPADANG	\N
64	Phỏng vấn	3	19	4	t	f	2026-01-06 09:48:36.715455	\N	DIEPADANG	\N
65	Onboard	4	19	6	t	f	2026-01-06 09:48:36.716661	\N	DIEPADANG	\N
66	Ứng tuyển	1	20	1	t	f	2026-01-06 09:57:44.147284	\N	DIEPADANG	\N
67	Thi tuyển	2	20	3	t	f	2026-01-06 09:57:44.148557	\N	DIEPADANG	\N
68	Phỏng vấn	3	20	4	t	f	2026-01-06 09:57:44.14967	\N	DIEPADANG	\N
69	Onboard	4	20	6	t	f	2026-01-06 09:57:44.150792	\N	DIEPADANG	\N
70	Ứng tuyển	1	21	1	t	f	2026-01-06 09:59:09.574239	\N	DIEPADANG	\N
71	Thi tuyển	2	21	3	t	f	2026-01-06 09:59:09.575667	\N	DIEPADANG	\N
72	Phỏng vấn	3	21	4	t	f	2026-01-06 09:59:09.576925	\N	DIEPADANG	\N
73	Onboard	4	21	6	t	f	2026-01-06 09:59:09.578422	\N	DIEPADANG	\N
74	Ứng tuyển	1	22	1	t	f	2026-01-06 13:23:09.07754	\N	morsoftware	\N
75	Phỏng vấn thuật toán	2	22	4	t	f	2026-01-06 13:23:09.079388	\N	morsoftware	\N
76	Phỏng vấn technical	3	22	4	t	f	2026-01-06 13:23:09.081329	\N	morsoftware	\N
77	Onboard	4	22	6	t	f	2026-01-06 13:23:09.082844	\N	morsoftware	\N
78	Ứng tuyển	1	23	1	t	f	2026-01-06 13:40:03.32357	\N	morsoftware	\N
79	Phỏng vấn thuật toán	2	23	4	t	f	2026-01-06 13:40:03.32542	\N	morsoftware	\N
80	Phỏng vấn technical	3	23	4	t	f	2026-01-06 13:40:03.32671	\N	morsoftware	\N
81	Onboard	4	23	6	t	f	2026-01-06 13:40:03.327936	\N	morsoftware	\N
82	Ứng tuyển	1	24	1	t	f	2026-01-06 13:41:59.048812	\N	morsoftware	\N
83	Phỏng vấn giao tiếp	2	24	4	t	f	2026-01-06 13:41:59.050142	\N	morsoftware	\N
84	Phỏng vấn technical	3	24	4	t	f	2026-01-06 13:41:59.051529	\N	morsoftware	\N
85	Onboard	4	24	6	t	f	2026-01-06 13:41:59.052657	\N	morsoftware	\N
86	Ứng tuyển	1	25	1	t	f	2026-01-06 13:44:19.73498	\N	morsoftware	\N
87	Phỏng vấn giao tiếp	2	25	4	t	f	2026-01-06 13:44:19.736346	\N	morsoftware	\N
88	Phỏng vấn technical	3	25	4	t	f	2026-01-06 13:44:19.737736	\N	morsoftware	\N
89	Onboard	4	25	6	t	f	2026-01-06 13:44:19.738824	\N	morsoftware	\N
90	Ứng tuyển	1	26	1	t	f	2026-01-06 13:51:01.218844	\N	morsoftware	\N
91	Phỏng vấn giao tiếp	2	26	4	t	f	2026-01-06 13:51:01.220324	\N	morsoftware	\N
92	Phỏng vấn technical	3	26	4	t	f	2026-01-06 13:51:01.221357	\N	morsoftware	\N
93	Onboard	4	26	6	t	f	2026-01-06 13:51:01.222463	\N	morsoftware	\N
94	Ứng tuyển	1	27	1	t	f	2026-01-06 13:57:40.262522	\N	morsoftware	\N
95	Phỏng vấn thuật toán	2	27	4	t	f	2026-01-06 13:57:40.263913	\N	morsoftware	\N
96	Phỏng vấn technical	3	27	4	t	f	2026-01-06 13:57:40.264941	\N	morsoftware	\N
97	Onboard	4	27	6	t	f	2026-01-06 13:57:40.265907	\N	morsoftware	\N
98	Ứng tuyển	1	28	1	t	f	2026-01-06 14:01:01.850666	\N	cvconnect	\N
99	Thi tuyển	2	28	3	t	f	2026-01-06 14:01:01.851948	\N	cvconnect	\N
100	Phỏng vấn chuyên môn	3	28	4	t	f	2026-01-06 14:01:01.852883	\N	cvconnect	\N
101	Phỏng vấn khách hàng	4	28	4	t	f	2026-01-06 14:01:01.85382	\N	cvconnect	\N
102	Onboard	5	28	6	t	f	2026-01-06 14:01:01.854826	\N	cvconnect	\N
\.


--
-- Data for Name: job_ad_process_candidate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_process_candidate (id, job_ad_process_id, job_ad_candidate_id, action_date, is_current_process, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
2	2	1	\N	f	t	f	2025-12-01 04:35:18.50941	\N	vclong2003	\N
3	3	1	\N	f	t	f	2025-12-01 04:35:18.511076	\N	vclong2003	\N
4	4	1	\N	f	t	f	2025-12-01 04:35:18.512671	\N	vclong2003	\N
7	2	2	\N	f	t	f	2025-12-01 04:45:23.666424	\N	iamdha0706	\N
9	4	2	\N	f	t	f	2025-12-01 04:45:23.668882	\N	iamdha0706	\N
12	7	3	\N	f	t	f	2025-12-01 08:38:15.259478	\N	admin	\N
13	8	3	\N	f	t	f	2025-12-01 08:38:15.260605	\N	admin	\N
14	9	3	\N	f	t	f	2025-12-01 08:38:15.261772	\N	admin	\N
5	5	1	2025-12-01 10:17:53.2507	t	t	f	2025-12-01 04:35:18.514774	2025-12-01 10:17:53.820212	vclong2003	cvconnect
1	1	1	2025-12-01 04:35:18.480877	f	t	f	2025-12-01 04:35:18.506784	2025-12-01 10:17:53.820786	vclong2003	cvconnect
17	12	4	\N	f	t	f	2025-12-03 03:58:06.005549	\N	quannm32	\N
18	13	4	\N	f	t	f	2025-12-03 03:58:06.006944	\N	quannm32	\N
20	15	4	\N	f	t	f	2025-12-03 03:58:06.009682	\N	quannm32	\N
19	14	4	2025-12-03 03:59:37.48518	t	t	f	2025-12-03 03:58:06.008259	2025-12-03 03:59:37.487474	quannm32	minhnq224
16	11	4	2025-12-03 03:58:05.985643	f	t	f	2025-12-03 03:58:06.003628	2025-12-03 03:59:37.4879	quannm32	minhnq224
22	2	5	\N	f	t	f	2025-12-03 07:31:25.138022	\N	duong2003nb	\N
23	3	5	\N	f	t	f	2025-12-03 07:31:25.140613	\N	duong2003nb	\N
24	4	5	\N	f	t	f	2025-12-03 07:31:25.143311	\N	duong2003nb	\N
25	5	5	2025-12-03 07:32:22.741925	t	t	f	2025-12-03 07:31:25.145985	2025-12-03 07:32:22.743193	duong2003nb	cvconnect
21	1	5	2025-12-03 07:31:25.122146	f	t	f	2025-12-03 07:31:25.134626	2025-12-03 07:32:22.743746	duong2003nb	cvconnect
15	10	3	2025-12-05 02:56:02.105276	t	t	f	2025-12-01 08:38:15.262942	2025-12-05 02:56:02.114424	admin	cvconnect
11	6	3	2025-12-01 08:38:15.248084	f	t	f	2025-12-01 08:38:15.256753	2025-12-05 02:56:02.115179	admin	cvconnect
28	3	6	\N	f	t	f	2025-12-11 09:33:33.287735	\N	anhnt746	\N
29	4	6	\N	f	t	f	2025-12-11 09:33:33.289012	\N	anhnt746	\N
26	1	6	2025-12-11 09:33:33.261006	f	t	f	2025-12-11 09:33:33.283889	2025-12-11 09:45:10.007505	anhnt746	quannm32
30	5	6	2025-12-12 10:56:01.142165	t	t	f	2025-12-11 09:33:33.290659	2025-12-12 10:56:01.68249	anhnt746	quannm32
27	2	6	2025-12-11 09:45:09.943547	f	t	f	2025-12-11 09:33:33.286302	2025-12-12 10:56:01.68331	anhnt746	quannm32
31	11	7	2025-12-20 16:55:18.958246	t	t	f	2025-12-20 16:55:18.982684	\N	huykeo2022@gmail.com	\N
32	12	7	\N	f	t	f	2025-12-20 16:55:18.985361	\N	huykeo2022@gmail.com	\N
33	13	7	\N	f	t	f	2025-12-20 16:55:18.987223	\N	huykeo2022@gmail.com	\N
34	14	7	\N	f	t	f	2025-12-20 16:55:18.989207	\N	huykeo2022@gmail.com	\N
35	15	7	\N	f	t	f	2025-12-20 16:55:18.991594	\N	huykeo2022@gmail.com	\N
6	1	2	2025-12-01 04:45:23.65884	f	t	f	2025-12-01 04:45:23.664874	2025-12-20 17:27:56.65882	iamdha0706	quannm32
10	5	2	2025-12-20 17:53:35.050944	t	t	f	2025-12-01 04:45:23.67036	2025-12-20 17:53:35.081966	iamdha0706	cvconnect
8	3	2	2025-12-20 17:27:56.642724	f	t	f	2025-12-01 04:45:23.667615	2025-12-20 17:53:35.082601	iamdha0706	cvconnect
39	9	8	\N	f	t	f	2025-12-24 03:31:26.465154	\N	tranvy10122000@gmail.com	\N
40	10	8	\N	f	t	f	2025-12-24 03:31:26.466663	\N	tranvy10122000@gmail.com	\N
41	11	9	2025-12-24 03:39:51.872489	t	t	f	2025-12-24 03:39:51.881067	\N	tranvy10122000@gmail.com	\N
42	12	9	\N	f	t	f	2025-12-24 03:39:51.884044	\N	tranvy10122000@gmail.com	\N
43	13	9	\N	f	t	f	2025-12-24 03:39:51.885587	\N	tranvy10122000@gmail.com	\N
44	14	9	\N	f	t	f	2025-12-24 03:39:51.887017	\N	tranvy10122000@gmail.com	\N
45	15	9	\N	f	t	f	2025-12-24 03:39:51.888483	\N	tranvy10122000@gmail.com	\N
49	9	10	\N	f	t	f	2025-12-26 04:05:59.80077	\N	vclong2003	\N
50	10	10	\N	f	t	f	2025-12-26 04:05:59.803368	\N	vclong2003	\N
58	23	12	\N	f	t	f	2025-12-29 04:04:14.932492	\N	duong2003nb	\N
46	6	10	2025-12-26 04:05:59.760263	f	t	f	2025-12-26 04:05:59.794412	2025-12-28 13:58:59.115788	vclong2003	quannm32
48	8	10	2025-12-28 14:35:58.074407	t	t	f	2025-12-26 04:05:59.79899	2025-12-28 14:35:58.709841	vclong2003	quannm32
47	7	10	2025-12-28 13:58:58.36902	f	t	f	2025-12-26 04:05:59.796981	2025-12-28 14:35:58.710453	vclong2003	quannm32
51	16	11	2025-12-28 14:56:10.723549	t	t	f	2025-12-28 14:56:10.736508	\N	duong2003nb	\N
52	17	11	\N	f	t	f	2025-12-28 14:56:10.739039	\N	duong2003nb	\N
53	18	11	\N	f	t	f	2025-12-28 14:56:10.74074	\N	duong2003nb	\N
54	19	11	\N	f	t	f	2025-12-28 14:56:10.742308	\N	duong2003nb	\N
55	20	11	\N	f	t	f	2025-12-28 14:56:10.743806	\N	duong2003nb	\N
59	24	12	\N	f	t	f	2025-12-29 04:04:14.93416	\N	duong2003nb	\N
57	22	12	2025-12-29 04:13:33.023567	t	t	f	2025-12-29 04:04:14.930695	2025-12-29 04:13:33.030948	duong2003nb	DIEPADANG
56	21	12	2025-12-29 04:04:14.913917	f	t	f	2025-12-29 04:04:14.9277	2025-12-29 04:13:33.031826	duong2003nb	DIEPADANG
60	21	13	2025-12-29 06:59:43.585969	t	t	f	2025-12-29 06:59:43.607782	\N	vclong2003	\N
61	22	13	\N	f	t	f	2025-12-29 06:59:43.60987	\N	vclong2003	\N
62	23	13	\N	f	t	f	2025-12-29 06:59:43.611293	\N	vclong2003	\N
63	24	13	\N	f	t	f	2025-12-29 06:59:43.612662	\N	vclong2003	\N
64	11	14	2025-12-31 16:02:11.097446	t	t	f	2025-12-31 16:02:11.145443	\N	vclong2003	\N
65	12	14	\N	f	t	f	2025-12-31 16:02:11.148216	\N	vclong2003	\N
66	13	14	\N	f	t	f	2025-12-31 16:02:11.149765	\N	vclong2003	\N
67	14	14	\N	f	t	f	2025-12-31 16:02:11.151159	\N	vclong2003	\N
68	15	14	\N	f	t	f	2025-12-31 16:02:11.152857	\N	vclong2003	\N
71	18	15	\N	f	t	f	2026-01-06 02:52:50.594377	\N	minhnq224	\N
72	19	15	\N	f	t	f	2026-01-06 02:52:50.595985	\N	minhnq224	\N
73	20	15	\N	f	t	f	2026-01-06 02:52:50.597935	\N	minhnq224	\N
70	17	15	2026-01-06 02:53:32.653137	t	t	f	2026-01-06 02:52:50.592778	2026-01-06 02:53:32.712553	minhnq224	cvconnect
69	16	15	2026-01-06 02:52:50.574152	f	t	f	2026-01-06 02:52:50.590238	2026-01-06 02:53:32.71602	minhnq224	cvconnect
74	37	16	2026-01-06 17:14:05.563377	t	t	f	2026-01-06 17:14:05.582886	\N	ntthaovana921dqh@gmail.com	\N
36	6	8	2025-12-24 03:31:26.437603	f	t	f	2025-12-24 03:31:26.459623	2026-01-06 14:57:51.394094	tranvy10122000@gmail.com	quannm32
38	8	8	2026-01-06 15:01:01.232327	t	t	f	2025-12-24 03:31:26.463665	2026-01-06 15:01:01.278349	tranvy10122000@gmail.com	quannm32
37	7	8	2026-01-06 14:57:51.316628	f	t	f	2025-12-24 03:31:26.462345	2026-01-06 15:01:01.278828	tranvy10122000@gmail.com	quannm32
75	38	16	\N	f	t	f	2026-01-06 17:14:05.586428	\N	ntthaovana921dqh@gmail.com	\N
76	39	16	\N	f	t	f	2026-01-06 17:14:05.587893	\N	ntthaovana921dqh@gmail.com	\N
77	40	16	\N	f	t	f	2026-01-06 17:14:05.589177	\N	ntthaovana921dqh@gmail.com	\N
78	41	16	\N	f	t	f	2026-01-06 17:14:05.590739	\N	ntthaovana921dqh@gmail.com	\N
\.


--
-- Data for Name: job_ad_statistic; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_statistic (id, job_ad_id, view_count, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
7	11	1	t	f	2026-01-06 05:02:27.025522	\N	ANONYMOUS	\N
1	1	46	t	f	2025-11-30 16:29:18.73387	2026-01-06 05:35:15.83076	ANONYMOUS	ANONYMOUS
9	16	1	t	f	2026-01-06 08:01:46.100124	\N	ANONYMOUS	\N
5	9	7	t	f	2025-12-29 04:04:08.171533	2026-01-06 10:06:30.433831	ANONYMOUS	ANONYMOUS
4	8	12	t	f	2025-12-28 14:53:38.663257	2026-01-06 10:07:08.398671	ANONYMOUS	ANONYMOUS
11	25	1	t	f	2026-01-06 13:52:32.993526	\N	ANONYMOUS	\N
8	14	3	t	f	2026-01-06 07:56:43.203995	2026-01-06 13:53:55.269993	ANONYMOUS	ANONYMOUS
12	21	1	t	f	2026-01-06 13:54:05.376454	\N	ANONYMOUS	\N
3	7	41	t	f	2025-12-03 03:55:02.571705	2026-01-06 16:50:49.627457	ANONYMOUS	ANONYMOUS
13	28	1	t	f	2026-01-06 16:51:00.714911	\N	ANONYMOUS	\N
10	13	2	t	f	2026-01-06 08:04:29.775419	2026-01-06 16:57:51.647577	ANONYMOUS	ANONYMOUS
6	10	3	t	f	2026-01-05 17:07:05.183727	2026-01-07 02:42:04.193192	ANONYMOUS	ANONYMOUS
2	2	42	t	f	2025-11-30 16:29:26.899565	2025-12-31 16:02:25.567327	ANONYMOUS	ANONYMOUS
\.


--
-- Data for Name: job_ad_work_location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_ad_work_location (id, job_ad_id, work_location_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	1	1	t	f	2025-11-30 09:07:09.251408	\N	cvconnect	\N
2	2	1	t	f	2025-11-30 14:42:25.002367	\N	cvconnect	\N
3	7	2	t	f	2025-12-03 03:54:26.019326	\N	minhnq224	\N
4	8	1	t	f	2025-12-28 14:48:10.865819	\N	cvconnect	\N
5	9	7	t	f	2025-12-29 04:03:37.02615	\N	DIEPADANG	\N
6	10	9	t	f	2026-01-05 16:57:24.794306	\N	morsoftware	\N
7	11	1	t	f	2026-01-06 04:03:17.33375	\N	cvconnect	\N
8	12	1	t	f	2026-01-06 07:33:01.30051	\N	cvconnect	\N
9	13	1	t	f	2026-01-06 07:38:11.714397	\N	cvconnect	\N
10	14	11	t	f	2026-01-06 07:51:35.473983	\N	minhnq224	\N
11	15	11	t	f	2026-01-06 07:53:29.916938	\N	minhnq224	\N
12	16	11	t	f	2026-01-06 08:01:37.217724	\N	minhnq224	\N
13	17	2	t	f	2026-01-06 08:09:19.119028	\N	minhnq224	\N
14	18	7	t	f	2026-01-06 09:39:27.353619	\N	DIEPADANG	\N
15	19	7	t	f	2026-01-06 09:48:36.711244	\N	DIEPADANG	\N
16	20	7	t	f	2026-01-06 09:57:44.145748	\N	DIEPADANG	\N
17	21	7	t	f	2026-01-06 09:59:09.572996	\N	DIEPADANG	\N
18	22	9	t	f	2026-01-06 13:23:09.074633	\N	morsoftware	\N
19	23	9	t	f	2026-01-06 13:40:03.321841	\N	morsoftware	\N
20	24	9	t	f	2026-01-06 13:41:59.047314	\N	morsoftware	\N
21	25	9	t	f	2026-01-06 13:44:19.733521	\N	morsoftware	\N
22	26	9	t	f	2026-01-06 13:51:01.217296	\N	morsoftware	\N
23	27	9	t	f	2026-01-06 13:57:40.260988	\N	morsoftware	\N
24	28	10	t	f	2026-01-06 14:01:01.849383	\N	cvconnect	\N
\.


--
-- Data for Name: job_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_config (id, job_name, schedule_type, expression, description, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	failed_rollback_retry	FIXED_RATE	600	Chạy lại Rollback data	t	f	2025-12-17 07:21:51.986835	\N	admin	\N
\.


--
-- Data for Name: level; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.level (id, code, name, is_default, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	INTERN	Thực tập sinh	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
2	FRESHER	Fresher	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
3	JUNIOR	Junior	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
4	JUNIOR_PLUS	Junior+	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
5	MIDDLE	Middle	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
6	MIDDLE_PLUS	Middle+	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
7	SENIOR	Senior	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
8	LEADER	Leader	t	t	f	2025-11-26 14:54:00.160325	\N	admin	\N
\.


--
-- Data for Name: organization; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization (id, name, description, logo_id, cover_photo_id, website, staff_count_from, staff_count_to, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
8	Công ty Borderz	<p>Border Z Việt Nam là thành viên của Border Z Group (có trụ sở tại Tokyo - Nhật Bản).</p><p>BorderZ đã và đang cung cấp giải pháp công nghệ thông tin toàn diện cho các tập đoàn đa quốc gia, doanh nghiệp vừa và nhỏ cho Việt Nam, Nhật Bản, Hong Kong, Singapore.... Đội ngũ chuyên gia và kỹ sư của chúng tôi sở hữu kiến thức và kinh nghiệm chuyên sâu về công nghệ thông tin (IT), phát triển phần mềm, ứng dụng web/điện thoại...</p><p>Với tầm nhìn trở thành đơn vị tiên phong trong việc ứng dụng các giải pháp công nghệ tiên tiến nhất, quyết tâm đạt mục tiêu trở thành doanh nghiệp uy tín ở Việt Nam, Nhật Bản và khu vực Đông Nam Á trong việc cung cấp dịch vụ CNTT cho mọi ngành nghề với mạng lưới khách hàng toàn cầu. Đội ngũ lãnh đạo là các chuyên gia công nghệ với 20+ năm kinh nghiệm làm việc tại các công ty hàng đầu trong ngành, đến từ Việt Nam, Hoa Kỳ, Nhật Bản, Singapore, Hongkong,...</p><p>Border Z áp dụng hệ thống ngôn ngữ và nền tảng đa dạng để cung cấp dịch vụ CNTT hàng đầu cho các lĩnh vực trọng điểm.</p><p>&nbsp;Chúng tôi tin rằng, dù doanh nghiệp của bạn ở bất cứ nơi đâu, thuộc bất cứ ngành nghề nào, chúng tôi đều sẽ tìm ra giải pháp hoàn hảo nhất cho nhu cầu của bạn.</p><p>Sứ mệnh: Đem lại những dịch vụ CNTT xuất sắc làm hài lòng mọi khách hàng,&nbsp;cống hiến cho&nbsp;công cuộc chuyển đổi số toàn cầu.</p>	21	22	https://www.border-z.co.jp/	50	100	t	f	2026-01-02 08:20:03.927168	\N	borderzvn	\N
2	Công ty TNHH Đại Phát	<p>CÔNG TY CỔ PHẦN TẬP ĐOÀN ĐẠI PHÁT</p><p>Địa chỉ Thuế Tầng 2 Toà nhà Mipec Tower, Số 229 Tây Sơn, Phường Kim Liên, TP Hà Nội, Việt Nam</p>	6	\N	\N	200	500	t	f	2025-12-02 08:23:38.513004	\N	minhnq224	\N
3	Woori Bank	\N	7	\N	\N	500	\N	t	f	2025-12-02 08:30:51.616838	\N	aindreas30	\N
7	Công ty cổ phần đầu tư và công nghệ ABD	<p><em>Test</em></p>	20	\N	\N	500	\N	t	f	2025-12-29 03:38:02.820909	2026-01-06 14:49:42.353769	DIEPADANG	DIEPADANG
4	Công ty Cổ phần Fintech Ads	\N	13	\N	https://fintechads.com	100	200	t	f	2025-12-13 10:39:07.605766	\N	fintechads	\N
5	Anh Nguyen 株式会社	<p>Anh Nguyen 株式会社でございます。</p>	14	\N	ancorp.example	50	100	t	f	2025-12-13 13:10:44.908496	\N	ancorp.recruite	\N
6	Anh Nguyen 株式会社	<p>Anh Nguyen 株式会社でございます。</p>	15	\N	an.ancorp.example	50	100	t	f	2025-12-13 13:13:38.790263	\N	an.ancorp.recruiter	\N
1	Công ty TNHH CVConnect	<p><strong>CVConnect</strong> lấy khách hàng làm trọng tâm, với sứ mệnh cung cấp cho các doanh nghiệp dịch vụ sản xuất gia công phần mềm tiên tiến nhất trên thế giới. Cùng đội ngũ nhân lực công nghệ dồi dào, chi phí thực thi thấp và chất lượng chuyên môn tốt, chúng tôi cam kết sẽ cùng bạn thay đổi và mang lại giá trị trong tương lai.</p>	17	5	https://vclab.tech	500	\N	t	f	2025-11-27 04:34:51.62142	2025-12-22 08:11:26.590753	cvconnect	cvconnect
9	MOR Software JSC	<p>As one of the Top 10 ICT in Vietnam, we confidently bring your software ideas to life by leveraging technologies. We'll be your trusted partners for the following reasons:</p><ol class="list-decimal ml-4"><li><p>Supporting successful billions of dong projects, we know there is more to a successful product than technology. We know how to produce scalable and high-quality products as well as consult technology solutions with a business mindset. We have many success stories with the Top 50 Forbes in Vietnam, top banks in Vietnam as well as listed companies.</p></li><li><p>Our agile approach allows us to prioritize tasks effectively, ensuring that we deliver high-quality products to our clients quickly. We use the scrum framework and MVP to expedite the development process without compromising on quality. Moreover, all our projects adhere to ISO 9001:2015 and ISO 27001:2013, which helps us meet global standards and maintain high-quality standards.</p></li><li><p>At MOR Software, we leverage the advantages offered by the Vietnamese government, such as tax incentives, political stability, and a focus on education and training in the IT field, to deliver cost-effective and impactful solutions to our clients. Our team always keeps your business goals in mind and works diligently to save you costs while delivering the highest quality services.</p></li></ol><p></p>	23	25	https://morsoftware.com/	100	200	t	f	2026-01-05 16:47:26.372095	2026-01-06 14:00:06.427077	morsoftware	morsoftware
\.


--
-- Data for Name: organization_address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_address (id, org_id, is_headquarter, province, district, ward, detail_address, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	1	t	Thành phố Hà Nội		Phường Hà Đông	Km10 Đường Trần Phú	t	f	2025-11-29 14:18:14.631865	\N	cvconnect	\N
2	2	t	Cao Bằng		Xã Đông Khê	Số 10 -  Đường Phùng Thanh	t	f	2025-12-02 08:28:25.97427	\N	minhnq224	\N
3	3	t	Thành phố Hà Nội		Phường Từ Liêm	34F Keangnam Landmark 72, E6 Phạm Hùng	t	f	2025-12-02 09:50:58.344063	\N	aindreas30	\N
4	4	t	Thành phố Hà Nội	\N	Phường Ba Đình	Số 300 Kim Mã	t	f	2025-12-13 10:39:07.642678	\N	internal	\N
5	5	f	Thành phố Hà Nội	\N	Phường Từ Liêm	Đường Phạm Hùng	t	f	2025-12-13 13:10:44.91611	\N	internal	\N
6	6	t	Thành phố Hà Nội	\N	Phường Từ Liêm	Đường Phạm Hùng	t	f	2025-12-13 13:13:38.796364	\N	internal	\N
7	7	t	Phú Thọ	\N	Phường Phong Châu	Đường 1, Phố Ao Sen	t	f	2025-12-29 03:38:02.857707	\N	internal	\N
8	8	t	Thành phố Hà Nội	\N	Phường Cầu Giấy	Phòng 609, Tầng 6, Tòa nhà CIC Tower, số 2, ngõ 219 phố Trun, Phường Yên Hoà, Quận Cầu Giấy	t	f	2026-01-02 08:20:03.959092	\N	internal	\N
9	9	t	Thành phố Hà Nội	\N	Phường Đại Mỗ	Roman Plaza, phường Đại Mỗ	t	f	2026-01-05 16:47:26.414855	\N	internal	\N
10	1	f	Hưng Yên		Xã Phụng Công	Swanlake Onsen - Tòa R1	t	f	2026-01-06 07:41:38.334465	\N	cvconnect	\N
11	2	f	Thành phố Hà Nội		Phường Hoàn Kiếm	Đường Lý Thái Tổ	t	f	2026-01-06 07:47:23.049141	\N	minhnq224	\N
\.


--
-- Data for Name: organization_industry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_industry (id, org_id, industry_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	1	1	t	f	2025-11-27 06:54:01.287959	\N	cvconnect	\N
2	1	17	t	f	2025-11-27 06:54:01.291453	\N	cvconnect	\N
3	4	1	t	f	2025-12-13 10:39:07.63201	\N	internal	\N
4	4	3	t	f	2025-12-13 10:39:07.636053	\N	internal	\N
5	5	1	t	f	2025-12-13 13:10:44.913851	\N	internal	\N
6	6	1	t	f	2025-12-13 13:13:38.794503	\N	internal	\N
7	7	3	t	f	2025-12-29 03:38:02.843226	\N	internal	\N
8	7	20	t	f	2025-12-29 03:38:02.846602	\N	internal	\N
9	7	24	t	f	2025-12-29 03:38:02.847892	\N	internal	\N
10	8	6	t	f	2026-01-02 08:20:03.95029	\N	internal	\N
11	9	1	t	f	2026-01-05 16:47:26.403516	\N	internal	\N
12	9	4	t	f	2026-01-06 13:58:53.258699	\N	morsoftware	\N
\.


--
-- Data for Name: position; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."position" (id, code, name, department_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	JAVA	Lập trình viên Java	1	t	f	2025-11-29 14:38:13.476221	\N	cvconnect	\N
2	BA001	Nhân viên kinh doanh	4	t	f	2025-12-02 08:26:38.062461	\N	minhnq224	\N
5	FE_VUE_01	Lập trình viên Frontend	6	t	f	2026-01-05 16:51:37.355142	\N	morsoftware	\N
6	BE_PHP_01	Lập trình viên Backend	6	t	f	2026-01-05 16:52:06.82527	\N	morsoftware	\N
3	TESTER	Kỹ sư kiểm thử phần mềm	7	t	f	2025-12-20 15:41:46.371223	2026-01-06 03:25:30.125141	cvconnect	cvconnect
7	BACKEND_WEB	Kỹ sư phát triển phần mềm Backend	1	t	f	2026-01-06 03:26:09.07597	\N	cvconnect	\N
8	FRONTEND_WEB	Kỹ sư phát triển phần mềm Frontend	1	t	f	2026-01-06 03:26:53.295141	\N	cvconnect	\N
9	QA	Quality Assurance (QA)	7	t	f	2026-01-06 03:27:41.645134	\N	cvconnect	\N
10	AI_ENGINEER	Kỹ sư AI	2	t	f	2026-01-06 03:28:41.604191	\N	cvconnect	\N
11	DATA_ENGINEER	Kỹ sư dữ liệu	3	t	f	2026-01-06 03:29:05.296264	\N	cvconnect	\N
12	MOBILE_DEV	Mobile Developer	8	t	f	2026-01-06 07:48:33.414516	\N	minhnq224	\N
13	DATA&AI	Data & AI	8	t	f	2026-01-06 07:59:16.295772	\N	minhnq224	\N
4	PROMPT	Prompt Engineering	5	t	f	2025-12-29 03:50:13.64144	2026-01-06 09:33:28.111769	DIEPADANG	DIEPADANG
14	WEB	Kỹ sư phát triền phần mềm Website	9	t	f	2026-01-06 09:36:41.544779	\N	DIEPADANG	\N
\.


--
-- Data for Name: position_process; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.position_process (id, name, position_id, process_type_id, sort_order, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	Ứng tuyển	1	1	1	t	f	2025-11-29 14:38:13.488145	\N	cvconnect	\N
2	Thi tuyển	1	3	2	t	f	2025-11-29 14:38:13.491004	\N	cvconnect	\N
3	Phỏng vấn chuyên môn	1	4	3	t	f	2025-11-29 14:38:13.492792	\N	cvconnect	\N
4	Phỏng vấn khách hàng	1	4	4	t	f	2025-11-29 14:38:13.494256	\N	cvconnect	\N
5	Onboard	1	6	5	t	f	2025-11-29 14:38:13.495894	\N	cvconnect	\N
6	Ứng tuyển	2	1	1	t	f	2025-12-02 08:26:38.075663	\N	minhnq224	\N
7	Đề nghị làm việc	2	5	2	t	f	2025-12-02 08:26:38.081503	\N	minhnq224	\N
8	Lọc hồ sơ	2	2	3	t	f	2025-12-02 08:26:38.082958	\N	minhnq224	\N
9	Thi tuyển	2	3	4	t	f	2025-12-02 08:26:38.084864	\N	minhnq224	\N
10	Onboard	2	6	5	t	f	2025-12-02 08:26:38.086643	\N	minhnq224	\N
11	Ứng tuyển	3	1	1	t	f	2025-12-20 15:41:46.425062	\N	cvconnect	\N
12	Phỏng vấn	3	4	2	t	f	2025-12-20 15:41:46.427826	\N	cvconnect	\N
13	Onboard	3	6	3	t	f	2025-12-20 15:41:46.429557	\N	cvconnect	\N
14	Ứng tuyển	4	1	1	t	f	2025-12-29 03:50:13.653923	\N	DIEPADANG	\N
15	Phỏng vấn	4	4	2	t	f	2025-12-29 03:50:13.65705	\N	DIEPADANG	\N
16	Đề nghị làm việc	4	5	3	t	f	2025-12-29 03:50:13.658536	\N	DIEPADANG	\N
17	Onboard	4	6	4	t	f	2025-12-29 03:50:13.659841	\N	DIEPADANG	\N
18	Ứng tuyển	5	1	1	t	f	2026-01-05 16:51:37.36599	\N	morsoftware	\N
19	Phỏng vấn giao tiếp	5	4	2	t	f	2026-01-05 16:51:37.37009	\N	morsoftware	\N
20	Phỏng vấn technical	5	4	3	t	f	2026-01-05 16:51:37.371848	\N	morsoftware	\N
21	Onboard	5	6	4	t	f	2026-01-05 16:51:37.373526	\N	morsoftware	\N
22	Ứng tuyển	6	1	1	t	f	2026-01-05 16:52:06.829547	\N	morsoftware	\N
23	Phỏng vấn thuật toán	6	4	2	t	f	2026-01-05 16:52:06.832055	\N	morsoftware	\N
24	Phỏng vấn technical	6	4	3	t	f	2026-01-05 16:52:06.834075	\N	morsoftware	\N
25	Onboard	6	6	4	t	f	2026-01-05 16:52:06.836043	\N	morsoftware	\N
26	Ứng tuyển	7	1	1	t	f	2026-01-06 03:26:09.081587	\N	cvconnect	\N
27	Phỏng vấn	7	4	2	t	f	2026-01-06 03:26:09.084846	\N	cvconnect	\N
28	Onboard	7	6	3	t	f	2026-01-06 03:26:09.086255	\N	cvconnect	\N
29	Ứng tuyển	8	1	1	t	f	2026-01-06 03:26:53.297692	\N	cvconnect	\N
30	Phỏng vấn	8	4	2	t	f	2026-01-06 03:26:53.2994	\N	cvconnect	\N
31	Onboard	8	6	3	t	f	2026-01-06 03:26:53.300509	\N	cvconnect	\N
32	Ứng tuyển	9	1	1	t	f	2026-01-06 03:27:41.648748	\N	cvconnect	\N
33	Thi tuyển	9	3	2	t	f	2026-01-06 03:27:41.650274	\N	cvconnect	\N
34	Phỏng vấn	9	4	3	t	f	2026-01-06 03:27:41.651761	\N	cvconnect	\N
35	Onboard	9	6	4	t	f	2026-01-06 03:27:41.653378	\N	cvconnect	\N
36	Ứng tuyển	10	1	1	t	f	2026-01-06 03:28:41.612245	\N	cvconnect	\N
37	Thi tuyển	10	3	2	t	f	2026-01-06 03:28:41.615254	\N	cvconnect	\N
38	Onboard	10	6	3	t	f	2026-01-06 03:28:41.618127	\N	cvconnect	\N
39	Ứng tuyển	11	1	1	t	f	2026-01-06 03:29:05.298731	\N	cvconnect	\N
40	Thi tuyển	11	3	2	t	f	2026-01-06 03:29:05.300068	\N	cvconnect	\N
41	Phỏng vấn	11	4	3	t	f	2026-01-06 03:29:05.301063	\N	cvconnect	\N
42	Onboard	11	6	4	t	f	2026-01-06 03:29:05.301978	\N	cvconnect	\N
43	Ứng tuyển	12	1	1	t	f	2026-01-06 07:48:33.42419	\N	minhnq224	\N
44	Lọc hồ sơ	12	2	2	t	f	2026-01-06 07:48:33.426795	\N	minhnq224	\N
45	Phỏng vấn	12	4	3	t	f	2026-01-06 07:48:33.428533	\N	minhnq224	\N
46	Onboard	12	6	4	t	f	2026-01-06 07:48:33.430111	\N	minhnq224	\N
47	Ứng tuyển	13	1	1	t	f	2026-01-06 07:59:16.299356	\N	minhnq224	\N
48	Thi tuyển	13	3	2	t	f	2026-01-06 07:59:16.30132	\N	minhnq224	\N
49	Phỏng vấn	13	4	3	t	f	2026-01-06 07:59:16.302562	\N	minhnq224	\N
50	Onboard	13	6	4	t	f	2026-01-06 07:59:16.303744	\N	minhnq224	\N
51	Ứng tuyển	14	1	1	t	f	2026-01-06 09:36:41.548315	\N	DIEPADANG	\N
52	Thi tuyển	14	3	2	t	f	2026-01-06 09:36:41.549869	\N	DIEPADANG	\N
53	Phỏng vấn	14	4	3	t	f	2026-01-06 09:36:41.550989	\N	DIEPADANG	\N
54	Onboard	14	6	4	t	f	2026-01-06 09:36:41.552073	\N	DIEPADANG	\N
\.


--
-- Data for Name: process_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.process_type (id, code, name, sort_order, is_default, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	APPLY	Ứng tuyển	1	t	t	f	2025-11-26 14:54:00.14895	\N	admin	\N
2	SCAN_CV	Lọc hồ sơ	2	t	t	f	2025-11-26 14:54:00.14895	\N	admin	\N
3	CONTEST	Thi tuyển	3	t	t	f	2025-11-26 14:54:00.14895	\N	admin	\N
4	INTERVIEW	Phỏng vấn	4	t	t	f	2025-11-26 14:54:00.14895	\N	admin	\N
5	OFFER	Đề nghị làm việc	5	t	t	f	2025-11-26 14:54:00.14895	\N	admin	\N
6	ONBOARD	Onboard	6	t	t	f	2025-11-26 14:54:00.14895	\N	admin	\N
\.


--
-- Data for Name: search_history_outside; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.search_history_outside (id, keyword, user_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by) FROM stdin;
1	back end	2	t	f	2025-12-03 08:44:23.278218	\N	quannm32	\N
2	develop	2	t	f	2025-12-03 08:44:39.06339	\N	quannm32	\N
3	develop	2	t	f	2025-12-03 08:45:02.720087	\N	quannm32	\N
10	t	38	t	f	2026-01-05 10:01:45.468104	\N	ducht2171@gmail.com	\N
11	đ	38	t	f	2026-01-05 10:01:49.13127	\N	ducht2171@gmail.com	\N
12	de	38	t	f	2026-01-05 10:01:53.99823	\N	ducht2171@gmail.com	\N
13	back	38	t	f	2026-01-05 10:02:02.09143	\N	ducht2171@gmail.com	\N
14	tts	3	t	f	2026-01-05 17:07:57.080155	\N	cvconnect	\N
15	soft	3	t	f	2026-01-05 17:08:07.524027	\N	cvconnect	\N
16	soft	3	t	f	2026-01-05 17:08:22.469734	\N	cvconnect	\N
17	soft	3	t	f	2026-01-05 17:08:24.477361	\N	cvconnect	\N
18	thực	36	t	f	2026-01-06 07:04:42.201234	\N	borderzvn	\N
19	thực	36	t	f	2026-01-06 07:04:48.565102	\N	borderzvn	\N
20	thực	36	t	f	2026-01-06 07:05:06.773296	\N	borderzvn	\N
21	android	1	t	f	2026-01-06 07:54:14.101416	\N	admin	\N
23	java dev	1	t	f	2026-01-06 14:10:44.037571	\N	admin	\N
24	java	1	t	f	2026-01-06 14:10:49.620463	\N	admin	\N
25	java	1	t	f	2026-01-06 14:10:56.03999	\N	admin	\N
\.


--
-- Data for Name: shedlock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.shedlock (name, lock_until, locked_at, locked_by) FROM stdin;
failed_rollback_retry	23:59:44.456	23:57:44.457	ed9dc4345635
\.


--
-- Name: attach_file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.attach_file_id_seq', 27, true);


--
-- Name: calendar_candidate_info_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.calendar_candidate_info_id_seq', 8, true);


--
-- Name: calendar_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.calendar_id_seq', 6, true);


--
-- Name: candidate_evaluation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.candidate_evaluation_id_seq', 3, true);


--
-- Name: candidate_info_apply_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.candidate_info_apply_id_seq', 11, true);


--
-- Name: candidate_summary_org_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.candidate_summary_org_id_seq', 6, true);


--
-- Name: department_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.department_id_seq', 9, true);


--
-- Name: failed_rollback_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.failed_rollback_id_seq', 1, false);


--
-- Name: industry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.industry_id_seq', 26, true);


--
-- Name: industry_sub_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.industry_sub_id_seq', 186, true);


--
-- Name: interview_panel_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.interview_panel_id_seq', 6, true);


--
-- Name: job_ad_candidate_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_candidate_id_seq', 16, true);


--
-- Name: job_ad_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_id_seq', 28, true);


--
-- Name: job_ad_industry_sub_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_industry_sub_id_seq', 39, true);


--
-- Name: job_ad_level_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_level_id_seq', 34, true);


--
-- Name: job_ad_process_candidate_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_process_candidate_id_seq', 78, true);


--
-- Name: job_ad_process_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_process_id_seq', 102, true);


--
-- Name: job_ad_statistic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_statistic_id_seq', 13, true);


--
-- Name: job_ad_work_location_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_ad_work_location_id_seq', 24, true);


--
-- Name: job_config_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.job_config_id_seq', 1, true);


--
-- Name: level_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.level_id_seq', 8, true);


--
-- Name: organization_address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.organization_address_id_seq', 11, true);


--
-- Name: organization_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.organization_id_seq', 9, true);


--
-- Name: organization_industry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.organization_industry_id_seq', 12, true);


--
-- Name: position_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.position_id_seq', 14, true);


--
-- Name: position_process_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.position_process_id_seq', 54, true);


--
-- Name: process_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.process_type_id_seq', 6, true);


--
-- Name: search_history_outside_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.search_history_outside_id_seq', 25, true);


--
-- Name: attach_file attach_file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attach_file
    ADD CONSTRAINT attach_file_pkey PRIMARY KEY (id);


--
-- Name: calendar_candidate_info calendar_candidate_info_calendar_id_candidate_info_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar_candidate_info
    ADD CONSTRAINT calendar_candidate_info_calendar_id_candidate_info_id_key UNIQUE (calendar_id, candidate_info_id);


--
-- Name: calendar_candidate_info calendar_candidate_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar_candidate_info
    ADD CONSTRAINT calendar_candidate_info_pkey PRIMARY KEY (id);


--
-- Name: calendar calendar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar
    ADD CONSTRAINT calendar_pkey PRIMARY KEY (id);


--
-- Name: candidate_evaluation candidate_evaluation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_evaluation
    ADD CONSTRAINT candidate_evaluation_pkey PRIMARY KEY (id);


--
-- Name: candidate_info_apply candidate_info_apply_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_info_apply
    ADD CONSTRAINT candidate_info_apply_pkey PRIMARY KEY (id);


--
-- Name: candidate_summary_org candidate_summary_org_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_summary_org
    ADD CONSTRAINT candidate_summary_org_pkey PRIMARY KEY (id);


--
-- Name: department department_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.department
    ADD CONSTRAINT department_pkey PRIMARY KEY (id);


--
-- Name: failed_rollback failed_rollback_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.failed_rollback
    ADD CONSTRAINT failed_rollback_pkey PRIMARY KEY (id);


--
-- Name: industry industry_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.industry
    ADD CONSTRAINT industry_code_key UNIQUE (code);


--
-- Name: industry industry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.industry
    ADD CONSTRAINT industry_pkey PRIMARY KEY (id);


--
-- Name: careers industry_sub_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.careers
    ADD CONSTRAINT industry_sub_code_key UNIQUE (code);


--
-- Name: careers industry_sub_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.careers
    ADD CONSTRAINT industry_sub_pkey PRIMARY KEY (id);


--
-- Name: interview_panel interview_panel_calendar_id_interviewer_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interview_panel
    ADD CONSTRAINT interview_panel_calendar_id_interviewer_id_key UNIQUE (calendar_id, interviewer_id);


--
-- Name: interview_panel interview_panel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interview_panel
    ADD CONSTRAINT interview_panel_pkey PRIMARY KEY (id);


--
-- Name: job_ad_candidate job_ad_candidate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_candidate
    ADD CONSTRAINT job_ad_candidate_pkey PRIMARY KEY (id);


--
-- Name: job_ad job_ad_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad
    ADD CONSTRAINT job_ad_code_key UNIQUE (code, org_id);


--
-- Name: job_ad_career job_ad_industry_sub_industry_sub_id_job_ad_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_career
    ADD CONSTRAINT job_ad_industry_sub_industry_sub_id_job_ad_id_key UNIQUE (career_id, job_ad_id);


--
-- Name: job_ad_career job_ad_industry_sub_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_career
    ADD CONSTRAINT job_ad_industry_sub_pkey PRIMARY KEY (id);


--
-- Name: job_ad_level job_ad_level_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_level
    ADD CONSTRAINT job_ad_level_pkey PRIMARY KEY (id);


--
-- Name: job_ad job_ad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad
    ADD CONSTRAINT job_ad_pkey PRIMARY KEY (id);


--
-- Name: job_ad_process_candidate job_ad_process_candidate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process_candidate
    ADD CONSTRAINT job_ad_process_candidate_pkey PRIMARY KEY (id);


--
-- Name: job_ad_process job_ad_process_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process
    ADD CONSTRAINT job_ad_process_pkey PRIMARY KEY (id);


--
-- Name: job_ad_statistic job_ad_statistic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_statistic
    ADD CONSTRAINT job_ad_statistic_pkey PRIMARY KEY (id);


--
-- Name: job_ad_work_location job_ad_work_location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_work_location
    ADD CONSTRAINT job_ad_work_location_pkey PRIMARY KEY (id);


--
-- Name: job_config job_config_job_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_config
    ADD CONSTRAINT job_config_job_name_key UNIQUE (job_name);


--
-- Name: job_config job_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_config
    ADD CONSTRAINT job_config_pkey PRIMARY KEY (id);


--
-- Name: level level_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.level
    ADD CONSTRAINT level_code_key UNIQUE (code);


--
-- Name: level level_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.level
    ADD CONSTRAINT level_pkey PRIMARY KEY (id);


--
-- Name: organization_address organization_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_address
    ADD CONSTRAINT organization_address_pkey PRIMARY KEY (id);


--
-- Name: organization_industry organization_industry_org_id_industry_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_industry
    ADD CONSTRAINT organization_industry_org_id_industry_id_key UNIQUE (org_id, industry_id);


--
-- Name: organization_industry organization_industry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_industry
    ADD CONSTRAINT organization_industry_pkey PRIMARY KEY (id);


--
-- Name: organization organization_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (id);


--
-- Name: position position_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (id);


--
-- Name: position_process position_process_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.position_process
    ADD CONSTRAINT position_process_pkey PRIMARY KEY (id);


--
-- Name: process_type process_type_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.process_type
    ADD CONSTRAINT process_type_code_key UNIQUE (code);


--
-- Name: process_type process_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.process_type
    ADD CONSTRAINT process_type_pkey PRIMARY KEY (id);


--
-- Name: search_history_outside search_history_outside_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.search_history_outside
    ADD CONSTRAINT search_history_outside_pkey PRIMARY KEY (id);


--
-- Name: shedlock shedlock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shedlock
    ADD CONSTRAINT shedlock_pkey PRIMARY KEY (name);


--
-- Name: candidate_summary_org uq_candidate_summary_org_org_id_candidate_info_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_summary_org
    ADD CONSTRAINT uq_candidate_summary_org_org_id_candidate_info_id UNIQUE (org_id, candidate_info_id);


--
-- Name: department uq_department_org_code; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.department
    ADD CONSTRAINT uq_department_org_code UNIQUE (org_id, code);


--
-- Name: job_ad_level uq_job_ad_level_job_ad_id_level_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_level
    ADD CONSTRAINT uq_job_ad_level_job_ad_id_level_id UNIQUE (job_ad_id, level_id);


--
-- Name: job_ad_work_location uq_job_ad_work_location_job_ad_id_work_location_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_work_location
    ADD CONSTRAINT uq_job_ad_work_location_job_ad_id_work_location_id UNIQUE (job_ad_id, work_location_id);


--
-- Name: position uq_position_department_code; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT uq_position_department_code UNIQUE (department_id, code);


--
-- Name: calendar_candidate_info calendar_candidate_info_calendar_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar_candidate_info
    ADD CONSTRAINT calendar_candidate_info_calendar_id_fkey FOREIGN KEY (calendar_id) REFERENCES public.calendar(id) ON DELETE CASCADE;


--
-- Name: calendar_candidate_info calendar_candidate_info_candidate_info_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar_candidate_info
    ADD CONSTRAINT calendar_candidate_info_candidate_info_id_fkey FOREIGN KEY (candidate_info_id) REFERENCES public.candidate_info_apply(id) ON DELETE CASCADE;


--
-- Name: calendar calendar_job_ad_process_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar
    ADD CONSTRAINT calendar_job_ad_process_id_fkey FOREIGN KEY (job_ad_process_id) REFERENCES public.job_ad_process(id) ON DELETE CASCADE;


--
-- Name: calendar calendar_org_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calendar
    ADD CONSTRAINT calendar_org_address_id_fkey FOREIGN KEY (org_address_id) REFERENCES public.organization_address(id) ON DELETE CASCADE;


--
-- Name: candidate_evaluation candidate_evaluation_job_ad_process_candidate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_evaluation
    ADD CONSTRAINT candidate_evaluation_job_ad_process_candidate_id_fkey FOREIGN KEY (job_ad_process_candidate_id) REFERENCES public.job_ad_process_candidate(id) ON DELETE CASCADE;


--
-- Name: candidate_info_apply candidate_info_apply_cv_file_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_info_apply
    ADD CONSTRAINT candidate_info_apply_cv_file_id_fkey FOREIGN KEY (cv_file_id) REFERENCES public.attach_file(id) ON DELETE CASCADE;


--
-- Name: candidate_summary_org candidate_summary_org_candidate_info_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_summary_org
    ADD CONSTRAINT candidate_summary_org_candidate_info_id_fkey FOREIGN KEY (candidate_info_id) REFERENCES public.candidate_info_apply(id) ON DELETE CASCADE;


--
-- Name: candidate_summary_org candidate_summary_org_level_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_summary_org
    ADD CONSTRAINT candidate_summary_org_level_id_fkey FOREIGN KEY (level_id) REFERENCES public.level(id) ON DELETE CASCADE;


--
-- Name: candidate_summary_org candidate_summary_org_org_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidate_summary_org
    ADD CONSTRAINT candidate_summary_org_org_id_fkey FOREIGN KEY (org_id) REFERENCES public.organization(id) ON DELETE CASCADE;


--
-- Name: department department_org_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.department
    ADD CONSTRAINT department_org_id_fkey FOREIGN KEY (org_id) REFERENCES public.organization(id) ON DELETE CASCADE;


--
-- Name: interview_panel interview_panel_calendar_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interview_panel
    ADD CONSTRAINT interview_panel_calendar_id_fkey FOREIGN KEY (calendar_id) REFERENCES public.calendar(id) ON DELETE CASCADE;


--
-- Name: job_ad_candidate job_ad_candidate_candidate_info_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_candidate
    ADD CONSTRAINT job_ad_candidate_candidate_info_id_fkey FOREIGN KEY (candidate_info_id) REFERENCES public.candidate_info_apply(id) ON DELETE CASCADE;


--
-- Name: job_ad_candidate job_ad_candidate_job_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_candidate
    ADD CONSTRAINT job_ad_candidate_job_ad_id_fkey FOREIGN KEY (job_ad_id) REFERENCES public.job_ad(id) ON DELETE CASCADE;


--
-- Name: job_ad_career job_ad_industry_sub_industry_sub_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_career
    ADD CONSTRAINT job_ad_industry_sub_industry_sub_id_fkey FOREIGN KEY (career_id) REFERENCES public.careers(id) ON DELETE CASCADE;


--
-- Name: job_ad_career job_ad_industry_sub_job_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_career
    ADD CONSTRAINT job_ad_industry_sub_job_ad_id_fkey FOREIGN KEY (job_ad_id) REFERENCES public.job_ad(id) ON DELETE CASCADE;


--
-- Name: job_ad_level job_ad_level_job_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_level
    ADD CONSTRAINT job_ad_level_job_ad_id_fkey FOREIGN KEY (job_ad_id) REFERENCES public.job_ad(id) ON DELETE CASCADE;


--
-- Name: job_ad_level job_ad_level_level_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_level
    ADD CONSTRAINT job_ad_level_level_id_fkey FOREIGN KEY (level_id) REFERENCES public.level(id) ON DELETE CASCADE;


--
-- Name: job_ad job_ad_org_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad
    ADD CONSTRAINT job_ad_org_id_fkey FOREIGN KEY (org_id) REFERENCES public.organization(id) ON DELETE CASCADE;


--
-- Name: job_ad job_ad_position_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad
    ADD CONSTRAINT job_ad_position_id_fkey FOREIGN KEY (position_id) REFERENCES public."position"(id) ON DELETE CASCADE;


--
-- Name: job_ad_process_candidate job_ad_process_candidate_job_ad_candidate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process_candidate
    ADD CONSTRAINT job_ad_process_candidate_job_ad_candidate_id_fkey FOREIGN KEY (job_ad_candidate_id) REFERENCES public.job_ad_candidate(id) ON DELETE CASCADE;


--
-- Name: job_ad_process_candidate job_ad_process_candidate_job_ad_process_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process_candidate
    ADD CONSTRAINT job_ad_process_candidate_job_ad_process_id_fkey FOREIGN KEY (job_ad_process_id) REFERENCES public.job_ad_process(id) ON DELETE CASCADE;


--
-- Name: job_ad_process job_ad_process_job_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process
    ADD CONSTRAINT job_ad_process_job_ad_id_fkey FOREIGN KEY (job_ad_id) REFERENCES public.job_ad(id) ON DELETE CASCADE;


--
-- Name: job_ad_process job_ad_process_process_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_process
    ADD CONSTRAINT job_ad_process_process_type_id_fkey FOREIGN KEY (process_type_id) REFERENCES public.process_type(id) ON DELETE CASCADE;


--
-- Name: job_ad_statistic job_ad_statistic_job_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_statistic
    ADD CONSTRAINT job_ad_statistic_job_ad_id_fkey FOREIGN KEY (job_ad_id) REFERENCES public.job_ad(id) ON DELETE CASCADE;


--
-- Name: job_ad_work_location job_ad_work_location_job_ad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_work_location
    ADD CONSTRAINT job_ad_work_location_job_ad_id_fkey FOREIGN KEY (job_ad_id) REFERENCES public.job_ad(id) ON DELETE CASCADE;


--
-- Name: job_ad_work_location job_ad_work_location_work_location_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_ad_work_location
    ADD CONSTRAINT job_ad_work_location_work_location_id_fkey FOREIGN KEY (work_location_id) REFERENCES public.organization_address(id) ON DELETE CASCADE;


--
-- Name: organization_address organization_address_org_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_address
    ADD CONSTRAINT organization_address_org_id_fkey FOREIGN KEY (org_id) REFERENCES public.organization(id) ON DELETE CASCADE;


--
-- Name: organization organization_cover_photo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization
    ADD CONSTRAINT organization_cover_photo_id_fkey FOREIGN KEY (cover_photo_id) REFERENCES public.attach_file(id);


--
-- Name: organization_industry organization_industry_industry_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_industry
    ADD CONSTRAINT organization_industry_industry_id_fkey FOREIGN KEY (industry_id) REFERENCES public.industry(id) ON DELETE CASCADE;


--
-- Name: organization_industry organization_industry_org_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_industry
    ADD CONSTRAINT organization_industry_org_id_fkey FOREIGN KEY (org_id) REFERENCES public.organization(id) ON DELETE CASCADE;


--
-- Name: organization organization_logo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization
    ADD CONSTRAINT organization_logo_id_fkey FOREIGN KEY (logo_id) REFERENCES public.attach_file(id);


--
-- Name: position position_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."position"
    ADD CONSTRAINT position_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.department(id) ON DELETE CASCADE;


--
-- Name: position_process position_process_position_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.position_process
    ADD CONSTRAINT position_process_position_id_fkey FOREIGN KEY (position_id) REFERENCES public."position"(id) ON DELETE CASCADE;


--
-- Name: position_process position_process_process_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.position_process
    ADD CONSTRAINT position_process_process_type_id_fkey FOREIGN KEY (process_type_id) REFERENCES public.process_type(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict 25kzZiaSb9g5CJgrj9yAnsWZRgLtGLgj5pXyPBqYWkWR6Ey0mMOb72IOS1LNLyR


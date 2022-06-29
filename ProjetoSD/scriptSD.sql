CREATE TABLE public.pessoa
(
    num_cc character varying(8) COLLATE pg_catalog."default" NOT NULL,
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(30) COLLATE pg_catalog."default" NOT NULL,
    funcao character varying(20) COLLATE pg_catalog."default" NOT NULL,
    departamento character varying(15) COLLATE pg_catalog."default" NOT NULL,
    num_telefone numeric(9,0) NOT NULL,
    morada character varying(100) COLLATE pg_catalog."default" NOT NULL,
    data_validade date NOT NULL,
    CONSTRAINT pessoa_pkey1 PRIMARY KEY (num_cc)
)

TABLESPACE pg_default;

ALTER TABLE public.pessoa
    OWNER to postgres;

    CREATE TABLE public.eleicao
    (
        id bigint NOT NULL,
        data_inicio timestamp(0) with time zone NOT NULL,
        data_fim timestamp(0) with time zone NOT NULL,
        titulo character varying(100) COLLATE pg_catalog."default" NOT NULL,
        descricao character varying(512) COLLATE pg_catalog."default" NOT NULL,
        tipo character varying(100) COLLATE pg_catalog."default" NOT NULL,
        resultado integer,
        CONSTRAINT eleicao_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

    ALTER TABLE public.eleicao
        OWNER to postgres;

        CREATE TABLE public.lista_candidatos
      (
          id bigint NOT NULL,
          nomecandidato character varying(50) COLLATE pg_catalog."default" NOT NULL,
          categoria character varying(30) COLLATE pg_catalog."default" NOT NULL,
          eleicao_id bigint NOT NULL,
          num_votos integer DEFAULT 0,
          CONSTRAINT lista_candidatos_pkey PRIMARY KEY (id),
          CONSTRAINT lista_candidatos_fk1 FOREIGN KEY (eleicao_id)
              REFERENCES public.eleicao (id) MATCH SIMPLE
              ON UPDATE NO ACTION
              ON DELETE NO ACTION
      )

      TABLESPACE pg_default;

      ALTER TABLE public.lista_candidatos
          OWNER to postgres;



          CREATE TABLE public.voto
(
    id_voto integer NOT NULL,
    local_voto character varying(20) COLLATE pg_catalog."default" NOT NULL,
    hora_voto timestamp without time zone,
    eleicao_id bigint NOT NULL,
    pessoa_num_cc character varying(8) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT voto_pkey PRIMARY KEY (id_voto),
    CONSTRAINT voto_fk1 FOREIGN KEY (eleicao_id)
        REFERENCES public.eleicao (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT voto_fk2 FOREIGN KEY (pessoa_num_cc)
        REFERENCES public.pessoa (num_cc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.voto
    OWNER to postgres;


    CREATE TABLE public.departamento
  (
      id bigint NOT NULL,
      departamento character varying(20) COLLATE pg_catalog."default",
      eleicao_id bigint NOT NULL,
      CONSTRAINT departamento_pkey PRIMARY KEY (id),
      CONSTRAINT departamento_fk1 FOREIGN KEY (eleicao_id)
          REFERENCES public.eleicao (id) MATCH SIMPLE
          ON UPDATE NO ACTION
          ON DELETE NO ACTION
  )

  TABLESPACE pg_default;

  ALTER TABLE public.departamento
      OWNER to postgres;


      CREATE TABLE public.pessoa_lista_candidatos
(
    pessoa_num_cc character varying(8) COLLATE pg_catalog."default" NOT NULL,
    lista_candidatos_id bigint NOT NULL,
    CONSTRAINT pessoa_lista_candidatos_fk1 FOREIGN KEY (pessoa_num_cc)
        REFERENCES public.pessoa (num_cc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT pessoa_lista_candidatos_fk2 FOREIGN KEY (lista_candidatos_id)
        REFERENCES public.lista_candidatos (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.pessoa_lista_candidatos
    OWNER to postgres;

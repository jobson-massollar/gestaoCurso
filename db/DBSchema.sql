/*----------------------------------------------------------------------------------
 Exclusão das views na ordem correta
 ----------------------------------------------------------------------------------*/
drop view vw_obrigatorias_faltantes;
drop view vw_disciplinas_cursadas;
drop view vw_alunos_ativos;
drop view vw_alunos;
drop view vw_disciplinas;

DROP TABLE departamentos;

CREATE TABLE departamentos (
    id uuid NOT NULL,
    depto varchar(5) NOT NULL,
    nome varchar(100) NOT NULL,
    CONSTRAINT departamentos_pkey PRIMARY KEY (id),
    CONSTRAINT departamentos_depto_unique UNIQUE (depto)
);

/*----------------------------------------------------------------------------------
 Alunos
 ----------------------------------------------------------------------------------*/

DROP TABLE alunos;

CREATE TABLE alunos (
    id uuid NOT NULL,
    matricula varchar(14) NOT NULL,
    nome varchar(100) NOT NULL,
    sexo bpchar(1) NOT NULL,
    dt_nasc date NULL,
    versao varchar(6) NOT NULL,
    logradouro varchar(100) NOT NULL,
    numero varchar(10) NOT NULL,
    complemento varchar(60) NOT NULL,
    bairro varchar(60) NOT NULL,
    cidade varchar(60) NOT NULL,
    cep varchar(10) NOT NULL,
    telefone1 varchar(20) NOT NULL,
    telefone2 varchar(20) NOT NULL,
    email varchar(40) DEFAULT ''::character varying NOT NULL,
    ingresso varchar(100) NOT NULL,
    evasao varchar(100) NOT NULL,
    dt_evasao date NULL,
    CONSTRAINT alunos_pkey PRIMARY KEY (id),
    CONSTRAINT alunos_matricula_unique UNIQUE (matricula)
);

/*----------------------------------------------------------------------------------
 Disciplinas
 ----------------------------------------------------------------------------------*/

DROP TABLE disciplinas;

CREATE TABLE disciplinas (
    id uuid NOT NULL,
    versao varchar(6) NOT NULL,
    codigo varchar(10) NOT NULL,
    nome varchar(100) NOT NULL,
    periodo int4 NOT NULL,
    creditos int4 NOT NULL,
    horas int4 NOT NULL,
    tipo varchar(60) NOT NULL,
    situacao varchar(20) NOT NULL,
    aula varchar(50) NOT NULL,
    CONSTRAINT disciplinas_pkey PRIMARY KEY (id),
    CONSTRAINT disciplina_codigo_versao_aula_unique UNIQUE (versao, codigo, aula)
);

CREATE INDEX disciplinas_versao_codigo_idx ON public.disciplinas USING btree (versao, codigo);

/*----------------------------------------------------------------------------------
 Pré-requisitos de disciplinas
 ----------------------------------------------------------------------------------*/

DROP TABLE pre_requisitos;

CREATE TABLE pre_requisitos (
    id uuid NOT NULL,
    versao varchar(6) NOT NULL,
    codigo varchar(10) NOT NULL,
    codigo_pre_req varchar(10) NOT NULL,
    CONSTRAINT pre_requisitos_pkey PRIMARY KEY (id),
    CONSTRAINT pre_requisitos_versao_codigo_codigo_pre_req_unique UNIQUE (versao, codigo, codigo_pre_req)
);

CREATE INDEX pre_requisitos_versao_codigo_idx ON public.pre_requisitos USING btree (versao, codigo);
CREATE INDEX pre_requisitos_versao_codigo_pre_req_idx ON public.pre_requisitos USING btree (versao, codigo_pre_req);

/*----------------------------------------------------------------------------------
 Itens do histórico
 ----------------------------------------------------------------------------------*/

DROP TABLE itens_historico;

CREATE TABLE public.itens_historico (
    id uuid NOT NULL,
    matricula varchar(14) NOT NULL,
    ano int4 NOT NULL,
    periodo int4 NOT NULL,
    desc_periodo varchar(20) NOT NULL,
    versao varchar(6) NOT NULL,
    codigo varchar(10) NOT NULL,
    nome varchar(200) NOT NULL,
    situacao int4 NOT NULL,
    descricao varchar(50) NOT NULL,
    nota float4 NULL,
    creditos int4 NOT NULL,
    horas int4 NOT NULL,
    CONSTRAINT itens_historico_pkey PRIMARY KEY (id)
);

CREATE INDEX itens_historico_matricula_idx ON public.itens_historico USING btree (matricula);
-- CREATE INDEX itens_historico_versao_codigo_idx ON public.itens_historico USING btree (versao, codigo);

/*----------------------------------------------------------------------------------
 Extensões de prazo (NÃO é importado)
 ----------------------------------------------------------------------------------*/

DROP TABLE extensoes_prazo;

CREATE TABLE public.extensoes_prazo (
    id uuid NOT NULL,
    matricula varchar(14) NOT NULL,
    prazo int4 NOT NULL,
    CONSTRAINT extensoes_prazo_pkey PRIMARY KEY (id),
    CONSTRAINT extensoes_prazo_matricula_unique UNIQUE (matricula)
);

/*----------------------------------------------------------------------------------
 Disciplinas que possuem equivalentes na grade do BSI (não contam na carga horária
 se cursadas pelo aluno) (NÃO é importado)
 ----------------------------------------------------------------------------------*/

DROP TABLE disciplinas_equivalentes;

CREATE TABLE public.disciplinas_equivalentes (
    id uuid NOT NULL,
    versao varchar(6) NOT NULL,
    codigo varchar(10) NOT NULL,
    nome varchar(200) NOT NULL,
    CONSTRAINT disciplinas_equivalentes_pkey PRIMARY KEY (id),
    CONSTRAINT disciplinas_equivalentes_versao_codigo_unique UNIQUE (versao, codigo)
);

insert into disciplinas_equivalentes(id, versao, codigo, nome) values('00b9cfcf-2083-44a5-bced-a9b1afe67db0', '2023/2', 'TMT0001', 'CÁLCULO-I');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('a32335a2-7d4f-4352-8184-3f54ae075bf6', '2023/2', 'TMT0002', 'CÁLCULO II');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('4b360a80-4df2-4173-b656-70e501a81470', '2023/2', 'TMT0005', 'CÁLCULO-0');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('3fd977a1-8a69-4d88-b456-8901fbf56a92', '2023/2', 'TMT0015', 'CÁLCULO-2');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('cbc50c52-91f2-4078-8d68-43fddb2b3d41', '2023/2', 'TIN0202', 'PROGRAMAÇÃO II');

/*----------------------------------------------------------------------------------
 Inscrições em disciplinas (não é usado atualmente)
 ----------------------------------------------------------------------------------*/

DROP TABLE inscricoes;

CREATE TABLE inscricoes (
    id uuid NOT NULL,
    matricula varchar(14) NOT NULL,
    codigo varchar(10) NOT NULL,
    turma varchar(10) NOT NULL,
    situacao int4 NOT NULL,
    descricao varchar(50) NOT NULL,
    ano int4 NOT NULL,
    periodo int4 NOT NULL,
    dt_solicitacao date NOT NULL,
    hora_solicitacao time NOT NULL,
    dt_processamento date NULL,
    CONSTRAINT inscricoes_pkey PRIMARY KEY (id)
);
-- CREATE INDEX inscricoes_codigo ON public.inscricoes USING btree (codigo);
-- CREATE INDEX inscricoes_descricao ON public.inscricoes USING btree (descricao);
-- CREATE INDEX inscricoes_matricula ON public.inscricoes USING btree (matricula);
-- CREATE INDEX inscricoes_situacao ON public.inscricoes USING btree (situacao);

/*----------------------------------------------------------------------------------
 Itens do diário (não é usado atualmente)
 ----------------------------------------------------------------------------------*/

DROP TABLE itens_diario;

CREATE TABLE itens_diario (
    id uuid NOT NULL,
    matricula varchar(14) NOT NULL,
    nome varchar(100) NOT NULL,
    curso int4 NOT NULL,
    depto varchar(5) NOT NULL,
    codigo varchar(10) NOT NULL,
    versao varchar(6) NOT NULL,
    turma varchar(20) NOT NULL,
    CONSTRAINT itens_diario_pkey PRIMARY KEY (id)
);
-- CREATE INDEX itens_diario_codigo_idx ON public.itens_diario USING btree (codigo);
-- CREATE INDEX itens_diario_matricula_idc ON public.itens_diario USING btree (matricula);

/*----------------------------------------------------------------------------------
 Disciplinas únicas com somatório de horas e créditos
 Essa view permite somar as horas de teoria e prática das disciplinas
 ----------------------------------------------------------------------------------*/

drop view vw_disciplinas;

create or replace view vw_disciplinas as
select versao, codigo, nome, periodo, sum(creditos) as creditos, sum(horas) as horas, tipo
from disciplinas
group by versao, codigo, nome, periodo, tipo;

/*----------------------------------------------------------------------------------
 Alunos com prazo de extensão e trancamentos
 ----------------------------------------------------------------------------------*/

drop view vw_alunos;

create or replace view vw_alunos as
select a.*, coalesce(T1.trancamentos, 0) as trancamentos, coalesce(T2.prazo, 0) as prazo_extensao
from alunos a
left join (
    select ih.matricula, count(*) as trancamentos
    from itens_historico ih
    where codigo = 'TRT0001'
    group by ih.matricula
) T1 on T1.matricula = a.matricula
left join extensoes_prazo T2 on T2.matricula = a.matricula;

/*----------------------------------------------------------------------------------
 Alunos com matrícula ativa
 ----------------------------------------------------------------------------------*/

drop view vw_alunos_ativos;

create or replace view vw_alunos_ativos as
select va.*
from vw_alunos va
where va.dt_evasao is null and left(va.evasao, 3) <> 'ABA';

/*----------------------------------------------------------------------------------
 Itens do histórico com disciplinas cursadas (aprovadas) para alunos ativos
 ----------------------------------------------------------------------------------*/

drop view vw_disciplinas_cursadas;

create or replace view vw_disciplinas_cursadas as
select h.*
from itens_historico h
inner join vw_alunos_ativos a on a.matricula = h.matricula
left join vw_disciplinas d on h.versao = d.versao  and h.codigo = d.codigo
where (h.situacao = 1 or h.situacao = 4 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11);

/*----------------------------------------------------------------------------------
 Alunos e disciplinas obrigatórias faltantes
 ----------------------------------------------------------------------------------*/

drop view vw_obrigatorias_faltantes;

create or replace view vw_obrigatorias_faltantes as
select a.matricula, d.*
from vw_disciplinas d
inner join vw_alunos_ativos a on a.versao = d.versao and d.tipo = 'Obrigatória'
left join (
    select h.*
    from itens_historico h
    inner join vw_alunos_ativos a on a.matricula = h.matricula
    left join vw_disciplinas d on h.versao = d.versao  and h.codigo = d.codigo
    where (h.situacao = 1 or h.situacao = 4 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11) and
           h.tipo = 'Obrigatória'
) T on a.matricula = T.matricula and d.versao = T.versao and d.codigo = T.codigo
where T.codigo is null;

/*----------------------------------------------------------------------------------
  Definição das horas de disciplinas complementares
 ----------------------------------------------------------------------------------*/

update itens_historico ih set horas = 90 where ih.codigo = 'ATC0021';
update itens_historico ih set horas = 180 where ih.codigo = 'ATC0010';
update itens_historico ih set horas = 45 where ih.codigo = 'ATC0031';
update itens_historico ih set horas = 60 where ih.codigo = 'ATC0100';

/*----------------------------------------------------------------------------------
  Complementa os dados vindos da importação, calculando novos campos para facilitar
  o processamento e evitar a criação de muitas views
 ----------------------------------------------------------------------------------*/

DROP PROCEDURE public.complementar_dados();

CREATE OR REPLACE PROCEDURE public.complementar_dados()
 LANGUAGE plpgsql
AS $procedure$
BEGIN

update itens_historico ih
set tipo = T.tipo
    from (
	select h.matricula, h.ano, h.periodo, h.codigo,
	case
	  when h.versao = '2023/2' and left(h.codigo, 3) = 'ATC' then 'Complementar'
	  when d1.tipo is not null then d1.tipo
	  when d2.tipo is not null and d2.codigo = 'HTD0058' then 'Eletiva'
	  when d2.tipo is not null then 'Antiga'
	  when de.versao is not null then 'Equivalente'
	  else 'Eletiva'
	end as tipo
	from itens_historico h
	left join vw_disciplinas d1 on h.versao = d1.versao and h.codigo = d1.codigo
	left join vw_disciplinas d2 on d2.versao = case when h.versao = '2023/2' then '2008/1' else null end and h.codigo = d2.codigo
	left join disciplinas_equivalentes de on de.versao = h.versao and de.codigo = h.codigo) T
where ih.matricula = T.matricula and ih.ano = T.ano and ih.periodo = T.periodo and ih.codigo = T.codigo;

END;
$procedure$
;

COMMENT ON PROCEDURE public.complementar_dados() IS 'Complementa os dados vindos da importação, calculando novos campos para facilitar o processamento e evitar a criação de muitas views.';

CALL public.complementar_dados();

/*=============================================================================*/

/*----------------------------------------------------------------------------------
 Total de alunos inscritos nas disciplinas do DIA (alunos do BSI e de outros)
  Usar na consulta do total de alunos ns turmas
 ----------------------------------------------------------------------------------*/

select i.versao, i.codigo, i.turma, d.nome, count(*) as qtd
from itens_diario i
         inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
where i.depto='DIA'
group by i.versao, i.codigo, i.turma, d.nome
order by i.turma asc, i.versao desc;

select i.versao, i.codigo, i.turma, d.nome, A.qtd + coalesce(B.qtd, 0) as total, A.qtd as bsi, coalesce(B.qtd, 0) as outros
from itens_diario i
         inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
         inner join (
    select i.versao, i.codigo, i.turma, count(*) as qtd
    from itens_diario i
             inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
    where i.depto='DIA' and substring(i.matricula, 6, 3) = '210'
    group by i.versao, i.codigo, i.turma
) A on i.versao = A.versao and i.codigo = A.codigo and i.turma = A.turma
         left join (
    select i.versao, i.codigo, i.turma, count(*) as qtd
    from itens_diario i
             inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
    where i.depto='DIA' and substring(i.matricula, 6, 3) <> '210'
    group by i.versao, i.codigo, i.turma
) B on i.versao = B.versao and i.codigo = B.codigo and i.turma = B.turma
where i.depto='DIA'
group by i.versao, i.codigo, i.turma, d.nome, A.qtd, B.qtd
order by i.turma asc, i.versao desc;
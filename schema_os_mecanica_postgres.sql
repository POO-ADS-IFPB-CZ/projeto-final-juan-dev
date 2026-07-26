-- =========================================================
-- Schema: Sistema de Ordem de Serviço - Oficina Mecânica
-- Disciplina: Programação Orientada a Objetos
-- SGBD: PostgreSQL (Supabase)
-- =========================================================

-- No Supabase, o banco já existe (criado pela plataforma).
-- Rode este script diretamente no SQL Editor do seu projeto.

-- Caso queira recriar as tabelas do zero, descomente as linhas abaixo:
-- DROP TABLE IF EXISTS item_peca CASCADE;
-- DROP TABLE IF EXISTS item_servico CASCADE;
-- DROP TABLE IF EXISTS ordem_servico CASCADE;
-- DROP TABLE IF EXISTS peca CASCADE;
-- DROP TABLE IF EXISTS servico CASCADE;
-- DROP TABLE IF EXISTS mecanico CASCADE;
-- DROP TABLE IF EXISTS equipe CASCADE;
-- DROP TABLE IF EXISTS veiculo CASCADE;
-- DROP TABLE IF EXISTS cliente CASCADE;

-- ---------------------------------------------------------
-- Tabela: cliente
-- ---------------------------------------------------------
CREATE TABLE cliente (
                         id_cliente   SERIAL PRIMARY KEY,
                         nome         VARCHAR(100)        NOT NULL,
                         endereco     VARCHAR(150),
                         telefone     VARCHAR(20)         NOT NULL,
                         cpf          VARCHAR(14)         NOT NULL UNIQUE
);

-- ---------------------------------------------------------
-- Tabela: veiculo
-- ---------------------------------------------------------
CREATE TABLE veiculo (
                         id_veiculo   SERIAL PRIMARY KEY,
                         placa        VARCHAR(8)          NOT NULL UNIQUE,
                         modelo       VARCHAR(60)         NOT NULL,
                         ano          INT                 NOT NULL,
                         cor          VARCHAR(30),
                         id_cliente   INT                 NOT NULL,
                         CONSTRAINT fk_veiculo_cliente
                             FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
                                 ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ---------------------------------------------------------
-- Tabela: equipe
-- ---------------------------------------------------------
CREATE TABLE equipe (
                        id_equipe    SERIAL PRIMARY KEY,
                        nome_equipe  VARCHAR(60)         NOT NULL
);

-- ---------------------------------------------------------
-- Tabela: mecanico
-- ---------------------------------------------------------
CREATE TABLE mecanico (
                          id_mecanico   SERIAL PRIMARY KEY,
                          nome          VARCHAR(100)       NOT NULL,
                          endereco      VARCHAR(150),
                          especialidade VARCHAR(60),
                          id_equipe     INT                NOT NULL,
                          CONSTRAINT fk_mecanico_equipe
                              FOREIGN KEY (id_equipe) REFERENCES equipe(id_equipe)
                                  ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ---------------------------------------------------------
-- Tabela: servico (catálogo de serviços oferecidos)
-- ---------------------------------------------------------
CREATE TABLE servico (
                         id_servico         SERIAL PRIMARY KEY,
                         descricao           VARCHAR(100)       NOT NULL,
                         valor_mao_de_obra   DECIMAL(10,2)      NOT NULL
);

-- ---------------------------------------------------------
-- Tabela: peca (catálogo de peças)
-- ---------------------------------------------------------
CREATE TABLE peca (
                      id_peca         SERIAL PRIMARY KEY,
                      descricao        VARCHAR(100)        NOT NULL,
                      valor_unitario   DECIMAL(10,2)       NOT NULL,
                      estoque          INT                 NOT NULL DEFAULT 0
);

-- ---------------------------------------------------------
-- Tabela: ordem_servico
-- status: ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA
-- ---------------------------------------------------------
CREATE TABLE ordem_servico (
                               id_ordem_servico         SERIAL PRIMARY KEY,
                               data_emissao             DATE                NOT NULL,
                               data_prevista_conclusao  DATE,
                               data_conclusao           DATE,
                               valor_total              DECIMAL(10,2)       NOT NULL DEFAULT 0,
                               status                   VARCHAR(20)         NOT NULL DEFAULT 'ABERTA',
                               id_veiculo               INT                 NOT NULL,
                               id_equipe                INT                 NOT NULL,
                               CONSTRAINT fk_os_veiculo
                                   FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo)
                                       ON DELETE RESTRICT ON UPDATE CASCADE,
                               CONSTRAINT fk_os_equipe
                                   FOREIGN KEY (id_equipe) REFERENCES equipe(id_equipe)
                                       ON DELETE RESTRICT ON UPDATE CASCADE,
                               CONSTRAINT chk_status
                                   CHECK (status IN ('ABERTA','EM_ANDAMENTO','CONCLUIDA','CANCELADA'))
);

-- ---------------------------------------------------------
-- Tabela associativa: item_servico (N:N entre OS e Servico)
-- ---------------------------------------------------------
CREATE TABLE item_servico (
                              id_item_servico    SERIAL PRIMARY KEY,
                              id_ordem_servico    INT                NOT NULL,
                              id_servico          INT                NOT NULL,
                              quantidade          INT                NOT NULL DEFAULT 1,
                              valor               DECIMAL(10,2)      NOT NULL,
                              CONSTRAINT fk_itemserv_os
                                  FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico(id_ordem_servico)
                                      ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT fk_itemserv_servico
                                  FOREIGN KEY (id_servico) REFERENCES servico(id_servico)
                                      ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ---------------------------------------------------------
-- Tabela associativa: item_peca (N:N entre OS e Peca)
-- ---------------------------------------------------------
CREATE TABLE item_peca (
                           id_item_peca       SERIAL PRIMARY KEY,
                           id_ordem_servico    INT                NOT NULL,
                           id_peca             INT                NOT NULL,
                           quantidade          INT                NOT NULL DEFAULT 1,
                           valor               DECIMAL(10,2)      NOT NULL,
                           CONSTRAINT fk_itempeca_os
                               FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico(id_ordem_servico)
                                   ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT fk_itempeca_peca
                               FOREIGN KEY (id_peca) REFERENCES peca(id_peca)
                                   ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =========================================================
-- Índices auxiliares (melhoram performance de busca/join)
-- =========================================================
CREATE INDEX idx_veiculo_cliente   ON veiculo(id_cliente);
CREATE INDEX idx_mecanico_equipe   ON mecanico(id_equipe);
CREATE INDEX idx_os_veiculo        ON ordem_servico(id_veiculo);
CREATE INDEX idx_os_equipe         ON ordem_servico(id_equipe);
CREATE INDEX idx_os_status         ON ordem_servico(status);
CREATE INDEX idx_itemserv_os       ON item_servico(id_ordem_servico);
CREATE INDEX idx_itempeca_os       ON item_peca(id_ordem_servico);

-- =========================================================
-- Dados de exemplo (seed) - opcional, útil para testar
-- =========================================================
INSERT INTO cliente (nome, endereco, telefone, cpf) VALUES
                                                        ('João Silva', 'Rua das Flores, 123', '(83) 99999-1111', '111.111.111-11'),
                                                        ('Maria Souza', 'Av. Brasil, 456', '(83) 99999-2222', '222.222.222-22');

INSERT INTO veiculo (placa, modelo, ano, cor, id_cliente) VALUES
                                                              ('ABC1D23', 'Gol', 2018, 'Prata', 1),
                                                              ('XYZ9K88', 'Onix', 2021, 'Branco', 2);

INSERT INTO equipe (nome_equipe) VALUES
                                     ('Equipe A'),
                                     ('Equipe B');

INSERT INTO mecanico (nome, endereco, especialidade, id_equipe) VALUES
                                                                    ('Carlos Pereira', 'Rua 1, 10', 'Motor', 1),
                                                                    ('Ana Lima', 'Rua 2, 20', 'Elétrica', 2);

INSERT INTO servico (descricao, valor_mao_de_obra) VALUES
                                                       ('Troca de óleo', 80.00),
                                                       ('Alinhamento e balanceamento', 120.00),
                                                       ('Revisão elétrica', 150.00);

INSERT INTO peca (descricao, valor_unitario, estoque) VALUES
                                                          ('Filtro de óleo', 35.00, 50),
                                                          ('Pastilha de freio', 90.00, 30),
                                                          ('Bateria 60Ah', 350.00, 10);

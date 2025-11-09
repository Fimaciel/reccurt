CREATE TABLE unidade_consumidora (
    id VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL CHECK (tipo IN ('residencial', 'comercial', 'industrial', 'essencial', 'emergencia')),
    regiao VARCHAR(100) NOT NULL
);

CREATE TABLE feriado (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('nacional', 'regional')),
    regiao VARCHAR(100)
);

CREATE TABLE comando (
    id SERIAL PRIMARY KEY,
    uc_id VARCHAR(20) NOT NULL,
    tipo_comando VARCHAR(20) NOT NULL CHECK (tipo_comando IN ('corte', 'religacao')),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    solicitante VARCHAR(100) NOT NULL,
    aprovado BOOLEAN NOT NULL,
    motivo TEXT,
    prazo_execucao TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (uc_id) REFERENCES unidade_consumidora (id)
);

CREATE TABLE log_validacao (
    id SERIAL PRIMARY KEY,
    comando_id INT NOT NULL REFERENCES comando(id) ON DELETE CASCADE,
    data_validacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    aprovado VARCHAR(20) NOT NULL,
    motivo TEXT NOT NULL,
    usuario VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS producao (
                                      id INTEGER PRIMARY KEY,
                                      pedido_id INTEGER,
                                      observacao VARCHAR(100),
                                      situacao_producao_id INTEGER,
                                      data_inicio_preparo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      data_fim_preparo TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

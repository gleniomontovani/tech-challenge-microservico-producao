INSERT INTO situacao_producao (id, nome) VALUES (1, 'Recebido') ON CONFLICT DO NOTHING;
INSERT INTO situacao_producao (id, nome) VALUES (2, 'Em preparação') ON CONFLICT DO NOTHING;
INSERT INTO situacao_producao (id, nome) VALUES (3, 'Pronto') ON CONFLICT DO NOTHING;
INSERT INTO situacao_producao (id, nome) VALUES (4, 'Cancelado') ON CONFLICT DO NOTHING;

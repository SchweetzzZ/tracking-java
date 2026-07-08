-- Remove a tabela duplicada criada em V1.
-- A tabela canônica de veículos é 'vehicles' (criada em V2),
-- mapeada pela entidade Vehicle com @Table(name = "vehicles").
DROP TABLE IF EXISTS tb_vehicles;

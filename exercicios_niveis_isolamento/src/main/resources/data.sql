-- Limpar tabelas existentes (opcional)
DELETE FROM tb_item_pedido;
DELETE FROM tb_pedido;
DELETE FROM tb_produto;
DELETE FROM tb_conta_otimista;
DELETE FROM tb_conta_pessimista;

-- Produtos
INSERT INTO tb_produto (nom_produto, vir_produto, qtd_estoque) VALUES ('PS5', 3250.0, 100);
INSERT INTO tb_produto (nom_produto, vir_produto, qtd_estoque) VALUES ('Xbox Series X', 3000.0, 50);
INSERT INTO tb_produto (nom_produto, vir_produto, qtd_estoque) VALUES ('Nintendo Switch', 2000.0, 30);

-- Contas bancárias
INSERT INTO tb_conta_otimista (num_conta, vir_saldo, cod_versao) VALUES ('35451-2', 1000.0, 0);
INSERT INTO tb_conta_otimista (num_conta, vir_saldo, cod_versao) VALUES ('12345-6', 500.0, 0);

INSERT INTO tb_conta_pessimista (num_conta, vir_saldo) VALUES ('35451-2', 1000.0);
INSERT INTO tb_conta_pessimista (num_conta, vir_saldo) VALUES ('12345-6', 500.0);
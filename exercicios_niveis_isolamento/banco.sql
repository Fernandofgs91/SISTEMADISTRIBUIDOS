create database db_exercicios_niveis_isolamento;

use db_exercicios_niveis_isolamento;

create table tb_produto (
    cod_produto integer not null primary key auto_increment,
    nom_produto varchar(255),
    vir_produto double not null,
    qtd_estoque integer not null
);

insert into tb_produto(nom_produto,vir_produto,qtd_estoque) values('PSS',3250,100);
insert into tb_produto(nom_produto,vir_produto,qtd_estoque) values('Xbox Series X',3000,50);

create table tb_pedido (
    num_pedido integer not null primary key auto_increment,
    dat_pedido datetime
);

create table tb_item_pedido (
    cod_item integer not null primary key auto_increment,
    num_pedido integer not null,
    cod_produto integer not null,
    qtd_compra integer not null,
    foreign key(num_pedido) references tb_pedido(num_pedido),
    foreign key(cod_produto) references tb_produto(cod_produto)
);

create table tb_conta_otimista (
    num_conta varchar(255) not null,
    vir_saldo int not null default 0,
    cod_versao int not null default 0,
    primary key(num_conta)
);

create table tb_conta_pessimista (
    num_conta varchar(255) not null,
    vir_saldo int not null default 0,
    primary key(num_conta)
);

insert into tb_conta_otimista (num_conta) values ('35451-2');
insert into tb_conta_pessimista (num_conta) values ('35451-2');
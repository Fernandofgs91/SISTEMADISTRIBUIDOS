create database db_exercicios_niveis_isolamento;

use db_exercicios_niveis_isolamento;

create table tb_produto (

cod_produto integer not null primary key auto_increment,

nom_produto varchar(255),

vlr_produto double not null,

qtd_estoque integer not null

);

insert into tb_produto(nom_produto,vlr_produto,qtd_estoque) values('PS5',3250,100);
insert into tb_produto(nom_produto,vlr_produto,qtd_estoque) values('Xbox Series X',3000,50);


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
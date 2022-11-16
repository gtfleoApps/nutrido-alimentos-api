CREATE DATABASE nutrido_alimento_db;
SHOW DATABASES;
USE nutrido_alimento_db;
CREATE TABLE `grupo_alimento` (
	`id` integer NOT NULL,
	`nome` VARCHAR(50) NOT NULL,
	PRIMARY KEY(`id`)) ENGINE=InnoDB;

CREATE TABLE `unidade_medida` (
	`id` integer NOT NULL,
	`nome` VARCHAR(50) NOT NULL,
	`nome_abreviado` VARCHAR(5) NOT NULL,
	PRIMARY KEY(`id`)) ENGINE=InnoDB;

CREATE TABLE `alimento` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`nome` VARCHAR(20) NOT NULL,
	`kcal` decimal(9,4) NOT NULL,
	`proteina` decimal(9,4) NOT NULL,
	`lipidio` decimal(9,4) NOT NULL,
	`carboidrato` decimal(9,4) NOT NULL,
	`fibra` decimal(9,4) NOT NULL,
	`quantidade` decimal(9,4) NOT NULL,
    `unidade_medida_id` integer NOT NULL,
	`grupo_alimento_id` integer NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`unidade_medida_id`) REFERENCES `unidade_medida` (`id`),
	FOREIGN KEY (`grupo_alimento_id`) REFERENCES `grupo_alimento` (`id`)) ENGINE=InnoDB;

INSERT INTO unidade_medida (id, nome, nome_abreviado) VALUES (01, 'gramas', 'g');

INSERT INTO grupo_alimento (id, nome) VALUES (01, 'Cereais e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (02, 'Verduras, hortalicas e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (03, 'Frutas e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (04, 'Gorduras e oleos');
INSERT INTO grupo_alimento (id, nome) VALUES (05, 'Pescados e frutos do mar');
INSERT INTO grupo_alimento (id, nome) VALUES (06, 'Carnes e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (07, 'Leite e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (08, 'Bebidas (alcoolicas e nao alcoolicas)');
INSERT INTO grupo_alimento (id, nome) VALUES (09, 'Ovos e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (10, 'Produtos acucarados');
INSERT INTO grupo_alimento (id, nome) VALUES (11, 'Miscelaneas');
INSERT INTO grupo_alimento (id, nome) VALUES (12, 'Outros alimentos industrializados');
INSERT INTO grupo_alimento (id, nome) VALUES (13, 'Alimentos preparados');
INSERT INTO grupo_alimento (id, nome) VALUES (14, 'Leguminosas e derivados');
INSERT INTO grupo_alimento (id, nome) VALUES (15, 'Nozes e sementes');
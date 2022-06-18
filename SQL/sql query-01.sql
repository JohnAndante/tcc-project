/*
	CREATE DATABASE TCC01;

	USE TCC01;
*/

	CREATE TABLE usuario (
		codusuario INT NOT NULL AUTO_INCREMENT,
        nome VARCHAR(20),
        sobrenome VARCHAR(40),
        celular VARCHAR(11),
        email VARCHAR(30) NOT NULL,
        senha VARCHAR(40) NOT NULL,
		PRIMARY KEY (codusuario)
	);
    
    -- SELECT * FROM usuario

	CREATE TABLE produto (
		codproduto INT(11) NOT NULL AUTO_INCREMENT,
		descricao VARCHAR(120),
		tamanho VARCHAR(20),
		codInt INT,
		ativo BOOLEAN DEFAULT TRUE,
		codmarca INT NOT NULL,
		codlinha INT NOT NULL,
		codcat INT NOT NULL,
		codsubcat INT NOT NULL,
		codsexo INT NOT NULL,
		PRIMARY KEY (codproduto),
		FOREIGN KEY (codmarca) REFERENCES marca(codmarca),
		FOREIGN KEY (codlinha) REFERENCES linha(codlinha),
		FOREIGN KEY (codcat) REFERENCES categoria(codcat),
		FOREIGN KEY (codsubcat) REFERENCES subcategoria(codsubcat),
		FOREIGN KEY (codsexo) REFERENCES sexo(codsexo)       
	);
    
    -- SELECT * FROM produto


	CREATE TABLE marca (
		codmarca INT NOT NULL AUTO_INCREMENT,
		descricao varchar(20),
		ativo BOOLEAN DEFAULT TRUE,
		PRIMARY KEY (codmarca)
	);
    
    -- SELECT * FROM marca

	CREATE TABLE linha (
		codlinha INT NOT NULL AUTO_INCREMENT,
		descricao VARCHAR(20),
		ativo BOOLEAN DEFAULT TRUE,
		PRIMARY KEY (codlinha)
	);
    
    -- SELECT * FROM linha

	CREATE TABLE categoria (
		codcat INT NOT NULL AUTO_INCREMENT,
		descricao VARCHAR(20),
		ativo BOOLEAN DEFAULT TRUE,
		PRIMARY KEY (codcat)
	);
    
    -- SELECT * FROM categoria

	CREATE TABLE subcategoria (
		codsubcat INT NOT NULL AUTO_INCREMENT,
		descricao VARCHAR(20),
		ativo BOOLEAN DEFAULT TRUE,
		PRIMARY KEY (codsubcat)
	);
    
    -- SELECT * FROM subcategoria

	CREATE TABLE sexo (
		codsexo INT NOT NULL AUTO_INCREMENT,
		descricao VARCHAR(20),
		ativo BOOLEAN DEFAULT TRUE,
		PRIMARY KEY (codsexo)
	);
    
    -- SELECT * FROM sexo

	CREATE TABLE uf (
		coduf INT NOT NULL AUTO_INCREMENT,
        descricao VARCHAR(2) NOT NULL,
        PRIMARY KEY (coduf)
	);
    
    -- SELECT * FROM uf
    
    CREATE TABLE pais (
		codpais INT NOT NULL AUTO_INCREMENT,
        descricao VARCHAR(2) NOT NULL,
        PRIMARY KEY (codpais)
	);
    
    -- SELECT * FROM usuario
    
	CREATE TABLE endereco (
		codendereco INT NOT NULL AUTO_INCREMENT,
        rua VARCHAR(60) NOT NULL,
        numero INT NOT NULL,
        bairro VARCHAR(60) NOT NULL,
        cidade VARCHAR(20) NOT NULL,
        coduf INT NOT NULL,
        codpais INT NOT NULL,
        cep VARCHAR(10),
        PRIMARY KEY (codendereco),
        FOREIGN KEY (coduf) REFERENCES uf(coduf),
        FOREIGN KEY (codpais) REFERENCES pais(codpais)
	);
    
    -- SELECT * FROM endereco
    
    CREATE TABLE cliente (
		codcliente INT NOT NULL AUTO_INCREMENT,
        nome VARCHAR(30) NOT NULL,
        sobrenome VARCHAR(40),
        apelido VARCHAR(30),
        obs VARCHAR(60),
        celular VARCHAR(11),
        codendereco INT NOT NULL,
        PRIMARY KEY (codcliente),
        FOREIGN KEY (codendereco) REFERENCES endereco(codendereco)
    );
    
    -- SELECT * FROM cliente
    
    CREATE TABLE formapgto (
		codformapgto INT NOT NULL AUTO_INCREMENT,
        descricao VARCHAR(20),
        prazo INT,
        PRIMARY KEY (codformapgto)
	);
    
    -- SELECT * FROM formapgto
    
    CREATE TABLE venda (
		codvenda INT NOT NULL AUTO_INCREMENT,
        codusuario INT NOT NULL,
        valortotal DECIMAL(8, 2) NOT NULL,
        codformapgto INT NOT NULL,
        datavenda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (codvenda),
        FOREIGN KEY (codusuario) REFERENCES usuario (codusuario),
        FOREIGN KEY (codformapgto) REFERENCES formapgto(codformapgto)
	);
    
    -- SELECT * FROM venda
    
    CREATE TABLE produtovenda (
		codvenda INT NOT NULL,
        codproduto INT NOT NULL,
        qtd INT NOT NULL,
        valorproduto DECIMAL(8, 2) NOT NULL,
        FOREIGN KEY (codvenda) REFERENCES venda(codvenda),
        FOREIGN KEY (codproduto) REFERENCES produto(codproduto)
	);
    
    -- SELECT * FROM produtovenda
	
    CREATE TABLE pgto (
		codpgto INT NOT NULL AUTO_INCREMENT,
        codcliente INT NOT NULL,
        valorpgto DECIMAL (8,2) NOT NULL,
        datapgto TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (codpgto),
        FOREIGN KEY (codcliente) REFERENCES cliente(codcliente)
	);
    
    -- SELECT * FROM pgto
	
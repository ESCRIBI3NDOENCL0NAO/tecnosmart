CREATE TABLE productos (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       nombre VARCHAR(255) NOT NULL,
       categoria VARCHAR(255) NOT NULL,
       stock INT NOT NULL,
       precio INT NOT NULL,
       descripcion VARCHAR(255)
);
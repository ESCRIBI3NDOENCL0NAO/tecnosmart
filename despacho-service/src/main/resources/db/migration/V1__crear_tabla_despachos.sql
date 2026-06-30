CREATE TABLE despachos (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       pedido_id BIGINT NOT NULL,
       direccion VARCHAR(255) NOT NULL,
       estado VARCHAR(50) NOT NULL
);
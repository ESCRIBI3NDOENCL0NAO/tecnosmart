CREATE TABLE pagos (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       pedido_id BIGINT NOT NULL,
       monto DOUBLE NOT NULL,
       estado VARCHAR(50) NOT NULL
);